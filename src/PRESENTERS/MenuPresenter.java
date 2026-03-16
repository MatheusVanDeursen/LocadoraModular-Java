package PRESENTERS;

import MODELS.Cliente;
import MODELS.Cnh;
import MODELS.Endereco;
import MODELS.Filial;
import MODELS.Locacao;
import MODELS.Veiculo;
import SERVICES.*;
import VIEW.INTERFACE.MenuInterface;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuPresenter {
    private MenuInterface view;
    private Cliente clienteLogado;
    
    private LocacaoService locacaoService;
    private VeiculoService veiculoService;
    private FilialService filialService;
    private EnderecoService enderecoService;
    private CnhService cnhService;

    private List<Locacao> listaLocacoesAtual;
    private boolean ordemCrescente = false;

    public MenuPresenter(MenuInterface view, Cliente clienteLogado) {
        this.view = view;
        this.clienteLogado = clienteLogado;
        
        this.locacaoService = new LocacaoService();
        this.veiculoService = new VeiculoService();
        this.filialService = new FilialService();
        this.enderecoService = new EnderecoService();
        this.cnhService = new CnhService();
    }

    public void inicializar() {
        verificarStatusCadastro();
        sincronizarHistorico();
    }

    public void verificarStatusCadastro() {
        try{
            Endereco end = enderecoService.buscarEnderecoPorIdCliente(clienteLogado.getId());
            Cnh cnh = cnhService.buscarPorCliente(clienteLogado.getId());
            
            if(end == null || cnh == null){
                view.atualizarStatusCadastro("⚠️ Cadastro Incompleto: Preencha seus dados para alugar.", new Color(204, 102, 0));
            }else if(cnh.getDataValidade().isBefore(LocalDate.now())) {
                view.atualizarStatusCadastro("❌ CNH Vencida! Atualize no menu Meus Dados.", Color.RED);
            }else{
                view.atualizarStatusCadastro("✅ Conta Verificada! Tudo pronto para alugar.", new Color(40, 167, 69));
            }
        }catch(Exception e){
            e.printStackTrace();
            view.atualizarStatusCadastro("Status Indisponível", Color.GRAY);
        }
    }

    public void sincronizarHistorico() {
        try{
            this.listaLocacoesAtual = locacaoService.buscarHistorico(clienteLogado);
            aplicarOrdenacaoEAtualizarTabela();
            if (this.listaLocacoesAtual != null) {
                aplicarOrdenacaoEAtualizarTabela();
            }
        }catch(Exception e){
            e.printStackTrace();
            view.exibirMensagem("Erro ao carregar histórico técnico.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reordenarHistorico() {
        aplicarOrdenacaoEAtualizarTabela();
    }

    public void alternarSentidoOrdenacao() {
        ordemCrescente = !ordemCrescente;
        view.atualizarTextoBotaoOrdem(ordemCrescente ? "⬆ Crescente" : "⬇ Decrescente");
        aplicarOrdenacaoEAtualizarTabela();
    }

    private void aplicarOrdenacaoEAtualizarTabela() {
        int index = view.getCriterioOrdenacao();
        
        listaLocacoesAtual.sort((l1, l2) -> {
            int resultado = 0;
            switch(index){
                case 0: resultado = l1.getDataRetirada().compareTo(l2.getDataRetirada()); break;
                case 1: resultado = l1.getDataDevolucaoPrevista().compareTo(l2.getDataDevolucaoPrevista()); break;
                case 2: resultado = l1.getStatus().name().compareTo(l2.getStatus().name()); break;
            }
            return (ordemCrescente ? resultado : -resultado);
        });
        
        renderizarHistorico();
    }

    private void renderizarHistorico() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Object[][] dados = new Object[listaLocacoesAtual.size()][4];

        for(int i = 0; i < listaLocacoesAtual.size(); i++){
            Locacao locacao = listaLocacoesAtual.get(i);
            Veiculo v = veiculoService.buscarPorId(locacao.getVeiculoId());
            
            String celulaCarro = "<html><b>" + (v != null ? v.getMarca() + " " + v.getModelo() : "Desconhecido") + "</b><br>" +
                                 "<span style='color:gray; font-size:9px;'>" + (v != null ? v.getTipo() + " | " + v.getTransmissao() : "") + "</span></html>";
            
            String corStatus = locacao.getStatus() == Locacao.StatusLocacao.ABERTA ? "green" : 
                              (locacao.getStatus() == Locacao.StatusLocacao.CANCELADA ? "red" : "gray");
            String celulaStatus = "<html><b style='color:" + corStatus + "'>" + locacao.getStatus() + "</b></html>";

            String preco = (locacao.getValorTotal() != null) ? "R$ " + locacao.getValorTotal().toString() : "R$ 0.00";
            
            dados[i][0] = celulaCarro;
            dados[i][1] = locacao.getDataRetirada().format(formatter);
            dados[i][2] = celulaStatus;
            dados[i][3] = "<html><b>" + preco + "</b></html>";
        }
        
        view.atualizarTabela(dados);
    }

    public void exibirDetalhesLocacao() {
        int row = view.getLinhaSelecionadaTabela();
        if (row == -1) { return; }
        
        Locacao locacao = listaLocacoesAtual.get(row);
        Veiculo v = veiculoService.buscarPorId(locacao.getVeiculoId());
        Filial fRetirada = filialService.buscarPorId(locacao.getFilialRetiradaId());
        Filial fDevolucao = filialService.buscarPorId(locacao.getFilialDevolucaoPrevistaId());
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        //Montagem do HTML
        String mensagem = "<html><body style='width: 350px; font-family: sans-serif;'>" +
                "<h2 style='color: #333; margin-bottom: 2px;'>Locação #" + locacao.getId() + "</h2>" +
                "<hr style='border: 1px solid #ccc;'>" +
                "<h3 style='color: #0056b3; margin-bottom: 2px;'>Dados do Veículo</h3>" +
                "<table width='100%'>" +
                "<tr><td><b>Modelo:</b> " + v.getMarca() + " " + v.getModelo() + "</td><td><b>Placa:</b> " + v.getPlaca() + "</td></tr>" +
                "<tr><td><b>Ano:</b> " + v.getAnoFabricacao() + " (" + v.getCor() + ")</td><td><b>Categoria:</b> " + v.getTipo() + "</td></tr>" +
                "<tr><td><b>Câmbio:</b> " + v.getTransmissao() + "</td><td><b>Exige CNH:</b> " + v.getGrupoCnhNecessario() + "</td></tr>" +
                "<tr><td><b>Passageiros:</b> " + v.getCapacidadePassageiros() + "</td><td><b>Malas:</b> " + v.getCapacidadeMalas() + "</td></tr>" +
                "<tr><td><b>Quilometragem Registrada:</b> " + v.getKmAtual() + " km</td></tr>" +
                "</table>" +
                "<h3 style='color: #0056b3; margin-bottom: 2px;'>Logística</h3>" +
                "<b>Retirada:</b> " + locacao.getDataRetirada().format(fmt) + " <br><span style='font-size: 10px; color: gray;'>Local: " + fRetirada.getNome() + " (" + fRetirada.getCidade() + "-" + fRetirada.getEstado() + ")</span><br><br>" +
                "<b>Devolução Prevs.:</b> " + locacao.getDataDevolucaoPrevista().format(fmt) + " <br><span style='font-size: 10px; color: gray;'>Local: " + fDevolucao.getNome() + " (" + fDevolucao.getCidade() + "-" + fDevolucao.getEstado() + ")</span><br>" +
                "<hr style='border: 1px dashed #ccc; margin-top: 15px;'>" +
                "<table width='100%'>" +
                "<tr>" +
                "<td style='font-size: 14px;'><b>Status:</b> " + locacao.getStatus() + "</td>" +
                "<td style='font-size: 16px; color: green; text-align: right;'><b>R$ " + locacao.getValorTotal() + "</b></td>" +
                "</tr>" +
                "</table>" +
                "</body></html>";
                
        view.exibirMensagemHtml(mensagem, "Detalhes da Reserva");
    }

    public void processarCancelamento() {
        int row = view.getLinhaSelecionadaTabela();
        if(row == -1){
            view.exibirMensagem("Selecione uma locação na tabela para cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Locacao locacao = listaLocacoesAtual.get(row);
        if(locacao.getStatus() != Locacao.StatusLocacao.ABERTA){
            view.exibirMensagem("Apenas locações 'ABERTA' podem ser canceladas.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = view.exibirConfirmacao("Deseja realmente cancelar a locação do veículo?", "Cancelar Locação");
        if(confirm == JOptionPane.YES_OPTION){
            try{
                locacaoService.cancelarLocacao(locacao);
                view.exibirMensagem("Locação cancelada com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                sincronizarHistorico(); //Atualiza a tabela
            }catch(Exception e){
                view.exibirMensagem("Erro ao cancelar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void iniciarNovaLocacao() {
        view.abrirTelaNovaLocacao(clienteLogado);
        sincronizarHistorico(); //Ao voltar da tela, atualiza a lista
    }

    public void gerenciarPerfil() {
        view.abrirTelaDadosPessoais(clienteLogado);
        verificarStatusCadastro(); //Ao voltar da tela, reavalia o status
    }

    public void efetuarLogout() {
        int opcao = view.exibirConfirmacao("Tem certeza que deseja sair do sistema?", "Sair");
        if(opcao == JOptionPane.YES_OPTION){
            view.encerrarAplicacao();
        }
    }
}