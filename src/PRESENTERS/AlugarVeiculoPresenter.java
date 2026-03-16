package PRESENTERS;

import MODELS.*;
import SERVICES.*;
import VIEW.INTERFACE.AlugarVeiculoInterface;
import EXCEPTIONS.RegraNegocioException;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlugarVeiculoPresenter {
    private AlugarVeiculoInterface view;
    private Cliente clienteLogado;
    
    private VeiculoService veiculoService;
    private LocacaoService locacaoService;
    private FilialService filialService;
    private EnderecoService enderecoService;
    private CnhService cnhService;

    private List<Veiculo> listaCompletaVeiculos;
    private List<Filial> listaFiliais;
    private Veiculo veiculoSelecionado;
    private boolean usuarioAptoParaAlugar = false;

    public AlugarVeiculoPresenter(AlugarVeiculoInterface view, Cliente clienteLogado) {
        this.view = view;
        this.clienteLogado = clienteLogado;
        
        this.veiculoService = new VeiculoService();
        this.locacaoService = new LocacaoService();
        this.filialService = new FilialService();
        this.enderecoService = new EnderecoService();
        this.cnhService = new CnhService();
    }

    public void inicializar() {
        verificarAptidaoDoCliente();
        prepararInventarioEFiliais();
    }

    private void verificarAptidaoDoCliente() {
        try{
            Endereco end = enderecoService.buscarEnderecoPorIdCliente(clienteLogado.getId());
            Cnh cnh = cnhService.buscarPorCliente(clienteLogado.getId());
            
            if(end != null && end.getCep() != null && !end.getCep().isEmpty() && cnh != null && !cnh.getDataValidade().isBefore(LocalDate.now())) {
                this.usuarioAptoParaAlugar = true;
            }else{
                this.usuarioAptoParaAlugar = false;
            }
        } catch (Exception e) {
            this.usuarioAptoParaAlugar = false;
        }
        view.exibirAvisoCadastroIncompleto(!this.usuarioAptoParaAlugar);
    }

    private void prepararInventarioEFiliais() {
        listaFiliais = filialService.listarTodas();
        List<String> nomesFiliais = listaFiliais.stream().map(f -> f.getNome() + " (" + f.getEstado() + ")").collect(Collectors.toList());
        view.popularComboFiliais(nomesFiliais);

        Endereco end = enderecoService.buscarEnderecoPorIdCliente(clienteLogado.getId());
        if(end != null && end.getEstado() != null){
            for(int i = 0; i < listaFiliais.size(); i++){
                if(listaFiliais.get(i).getEstado().equalsIgnoreCase(end.getEstado())){
                    view.setFilialRetiradaIndex(i);
                    break;
                }
            }
        }

        listaCompletaVeiculos = veiculoService.listarDisponiveis();
        filtrarCatalogo(); 
    }

    public void filtrarCatalogo() {
        int idxOrigem = view.getFilialRetiradaIndex();
        if(listaCompletaVeiculos == null || listaFiliais == null || idxOrigem < 0) { return; }
        
        this.veiculoSelecionado = null; 
        view.atualizarLabelCarroSelecionado("<html><h3 style='color:gray'>Selecione um veículo...</h3></html>");
        view.travarPainelDireito(false, usuarioAptoParaAlugar);

        int idFilialOrigem = listaFiliais.get(idxOrigem).getId();
        String busca = view.getBuscaRapida().toLowerCase();
        String cat = view.getCategoriaSelecionada();
        String cam = view.getCambioSelecionado();
        int ordem = view.getOrdenacaoSelecionada();

        List<Veiculo> filtrados = listaCompletaVeiculos.stream()
            .filter(v -> v.getFilialAtualId() == idFilialOrigem) 
            .filter(v -> busca.isEmpty() || v.getModelo().toLowerCase().contains(busca) || v.getMarca().toLowerCase().contains(busca) || v.getPlaca().toLowerCase().contains(busca))
            .filter(v -> cat.equals("Todas Categorias") || v.getTipo().name().equals(cat))
            .filter(v -> cam.equals("Qualquer Câmbio") || v.getTransmissao().name().equals(cam))
            .sorted((v1, v2) -> ordem == 0 ? v1.getValorDiaria().compareTo(v2.getValorDiaria()) : v2.getValorDiaria().compareTo(v1.getValorDiaria()))
            .collect(Collectors.toList());

        Object[][] dados = new Object[filtrados.size()][4];
        for(int i = 0; i < filtrados.size(); i++){
            Veiculo v = filtrados.get(i);
            dados[i][0] = v.getId();
            dados[i][1] = "<html><b style='font-size:11px;'>" + v.getMarca() + " " + v.getModelo() + "</b><br><span style='color:gray; font-size:9px;'>Placa: " + v.getPlaca() + "</span></html>";
            dados[i][2] = "<html><span style='color:#0056b3; font-weight:bold;'>" + v.getTipo() + "</span><br><span style='color:gray; font-size:9px;'>" + v.getTransmissao() + "</span></html>";
            dados[i][3] = "<html><b style='color:green; font-size:12px;'>R$ " + String.format("%.2f", v.getValorDiaria()) + "</b></html>";
        }
        
        view.atualizarTabelaVeiculos(dados);
        view.setFilialDevolucaoIndex(idxOrigem);
    }

    public void selecionarVeiculo() {
        int idVeiculo = view.getVeiculoSelecionadoId();
        if (idVeiculo == -1) return;

        veiculoSelecionado = listaCompletaVeiculos.stream().filter(v -> v.getId() == idVeiculo).findFirst().orElse(null);
        
        if(veiculoSelecionado != null){
            String precoFormatado = String.format("%.2f", veiculoSelecionado.getValorDiaria());
            String htmlInfo = "<html><h2 style='color:#0056b3; margin:0px;'>" + veiculoSelecionado.getMarca() + " " + 
            		veiculoSelecionado.getModelo() + "</h2>" + "<i style='font-size:11px; color:gray;'>Diária Base: R$ " + 
            		precoFormatado + " &nbsp;|&nbsp; KM Atual: " + veiculoSelecionado.getKmAtual() + "</i></html>";
            
            view.atualizarLabelCarroSelecionado(htmlInfo);
            
            for(int i = 0; i < listaFiliais.size(); i++){
                if(listaFiliais.get(i).getId() == veiculoSelecionado.getFilialAtualId()) {
                    view.setFilialDevolucaoIndex(i);
                    break;
                }
            }
            
            view.travarPainelDireito(true, usuarioAptoParaAlugar);
            calcularEstimativaLocacao();
        }
    }

    public void exibirFichaTecnica() {
        if(veiculoSelecionado == null) { return; }
        
        String ficha = "<html><body style='width:250px; font-family:sans-serif;'>" +
            "<h2 style='color:#333;'>" + veiculoSelecionado.getMarca() + " " + veiculoSelecionado.getModelo() + "</h2><hr>" +
            "<b>Ano:</b> " + veiculoSelecionado.getAnoFabricacao() + "<br>" +
            "<b>Cor:</b> " + veiculoSelecionado.getCor() + "<br>" +
            "<b>Passageiros:</b> " + veiculoSelecionado.getCapacidadePassageiros() + " pessoas<br>" +
            "<b>Porta-malas:</b> " + veiculoSelecionado.getCapacidadeMalas() + " malas<br>" +
            "<hr><b style='color:red;'>Exige CNH Categoria: " + veiculoSelecionado.getGrupoCnhNecessario() + "</b>" +
            "</body></html>";
            
        view.exibirFichaTecnica(ficha);
    }

    public void calcularEstimativaLocacao() {
        if(veiculoSelecionado == null) { return; }
        
        int idxFilialDev = view.getFilialDevolucaoIndex();
        if(idxFilialDev < 0) { return; }
        
        String dtRetirada = view.getDataRetirada();
        String dtDevolucao = view.getDataDevolucao();
        
        //Verifica se as datas estão completas antes de simular
        if(dtRetirada.contains("_") || dtDevolucao.contains("_")){
            view.atualizarReciboVirtual("<html><i>Preencha as datas para simular...</i></html>");
            return;
        }

        int filialDevId = listaFiliais.get(idxFilialDev).getId();
        
        try{
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime inicio = LocalDate.parse(dtRetirada, fmt).atTime(10, 0);
            LocalDateTime fim = LocalDate.parse(dtDevolucao, fmt).atTime(10, 0);

            Locacao locacaoSimulada = new Locacao(clienteLogado.getId(), veiculoSelecionado.getId(), veiculoSelecionado.getFilialAtualId(), filialDevId, inicio, fim);
            
            Map<String, Object> resumo = locacaoService.simularValores(locacaoSimulada, view.isSeguroSelecionado(), clienteLogado);
            String reciboHtml = gerarTemplateRecibo(resumo);
            view.atualizarReciboVirtual(reciboHtml);
            
        }catch(RegraNegocioException e){
            view.atualizarReciboVirtual("<html><b style='color:red;'>" + e.getMessage() + "</b></html>");
        }catch(Exception e){
            view.atualizarReciboVirtual("<html><i>Formato de data inválido.</i></html>");
        }
    }

    private String gerarTemplateRecibo(Map<String, Object> resumo) {
        StringBuilder sb = new StringBuilder("<html><body style='font-family: sans-serif; width: 300px;'>");
        sb.append("<h3 style='color:#333; margin-bottom: 5px;'>Resumo de Custos</h3><hr>");
        sb.append("<table width='100%'>");
        String col1 = "<tr><td width='55%'>";
        String col2 = "</td><td width='45%' align='left'>R$ ";
        String fimLinha = "</td></tr>";

        long dias = (long) resumo.get("dias");
        BigDecimal subtotal = (BigDecimal) resumo.get("subtotal");
        sb.append(col1).append("Diárias (").append(dias).append("x)").append(col2).append(String.format("%.2f", subtotal)).append(fimLinha);

        if(resumo.containsKey("seguro")){
        	sb.append(col1).append("Seguro Completo").append(col2).append(String.format("%.2f", (BigDecimal) resumo.get("seguro"))).append(fimLinha);
        }
        if(resumo.containsKey("taxaJovem")){
        	sb.append(col1).append("Taxa Jovem (< 21a)").append(col2).append(String.format("%.2f", (BigDecimal) resumo.get("taxaJovem"))).append(fimLinha);
        }
        if(resumo.containsKey("taxaRetorno")){
        	sb.append(col1).append("Retorno Interestadual").append(col2).append(String.format("%.2f", (BigDecimal) resumo.get("taxaRetorno"))).append(fimLinha);
        }
        
        BigDecimal servico = (BigDecimal) resumo.get("taxaServico");
        sb.append(col1).append("Taxa Serviço (5%)").append(col2).append(String.format("%.2f", servico)).append(fimLinha);
        sb.append("</table><hr>");
        
        BigDecimal totalGeral = (BigDecimal) resumo.get("totalGeral");
        sb.append("<table width='100%'><tr>");
        sb.append("<td width='55%'><b style='font-size: 15px; color: green;'>Total:</b></td>");
        sb.append("<td width='45%' align='left'><b style='font-size: 15px; color: green;'>R$ ").append(String.format("%.2f", totalGeral)).append("</b></td>");
        sb.append("</tr></table></body></html>");
        return sb.toString();
    }

    public void finalizarReserva() {
        if (!usuarioAptoParaAlugar) return;

        int idxFilialDev = view.getFilialDevolucaoIndex();
        if (idxFilialDev < 0) return;
        
        String dtRetirada = view.getDataRetirada();
        String dtDevolucao = view.getDataDevolucao();
        
        if(dtRetirada.contains("_") || dtDevolucao.contains("_")){
            view.exibirMensagem("Preencha as datas corretamente.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            int filialDevId = listaFiliais.get(idxFilialDev).getId();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime inicio = LocalDate.parse(dtRetirada, fmt).atTime(10, 0);
            LocalDateTime fim = LocalDate.parse(dtDevolucao, fmt).atTime(10, 0);

            Locacao novaLocacao = new Locacao(clienteLogado.getId(), veiculoSelecionado.getId(), veiculoSelecionado.getFilialAtualId(), filialDevId, inicio, fim);
            
            locacaoService.realizarLocacao(novaLocacao, view.isSeguroSelecionado());
            
            view.exibirMensagem("Reserva confirmada com sucesso! Valor Final: R$ " + novaLocacao.getValorTotal(), "Locação Confirmada", JOptionPane.INFORMATION_MESSAGE);
            view.fecharTela();
            
        }catch(RegraNegocioException e){
            view.exibirMensagem("Erro: " + e.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }catch(Exception e){
            view.exibirMensagem("Erro técnico ao realizar locação.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}