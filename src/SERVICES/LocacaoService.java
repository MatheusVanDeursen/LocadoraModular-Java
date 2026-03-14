package SERVICES;

import DAO.LocacaoDAO;
import MODELS.*;
import EXCEPTIONS.RegraNegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Concentra as regras de negócio relacionadas à entidade Locacao realizando validação dos dados.
 */
public class LocacaoService {
    private LocacaoDAO locacaoDAO = new LocacaoDAO();
    private VeiculoService veiculoService = new VeiculoService();
    private ClienteService clienteService = new ClienteService();
    private EnderecoService enderecoService = new EnderecoService();
    private CnhService cnhService = new CnhService();
    private FilialService filialService = new FilialService();

    /**
     * Efetiva a abertura de um contrato de locação, validando regras de negócio e alterando o status do veículo para ALUGADO.
     * @param locacao Entidade Locacao
     * @param comSeguro Flag que indica a contratação da cobertura opcional de seguro.
     * @throws RegraNegocioException
     */
    public void realizarLocacao(Locacao locacao, boolean comSeguro) throws RegraNegocioException {
    	//Regra 1: Data de devolução não pode ser antes da data de retirada
        if (locacao.getDataDevolucaoPrevista().isBefore(locacao.getDataRetirada())) {
            throw new RegraNegocioException("A data de devolução não pode ser anterior à retirada.");
        }
        
        //Regra 2: Data de devolução não pode ser antes da data de retirada
        if (locacao.getDataRetirada().isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new RegraNegocioException("A data de retirada não pode ser no passado.");
        }

        //Regra 3: Precisa ter um endereço cadastrado para alugar
        Cliente cliente = clienteService.buscarPorId(locacao.getClienteId());
        Endereco endereco = enderecoService.buscarEnderecoPorIdCliente(cliente.getId());
        if (endereco == null) {
            throw new RegraNegocioException("O cliente precisa completar o cadastro de Endereço antes de alugar.");
        }
        
        //Regra 4: Precisa ter uma CNHH cadastrada para alugar
        Cnh cnh = cnhService.buscarPorCliente(cliente.getId());
        if (cnh == null) {
            throw new RegraNegocioException("O cliente precisa cadastrar a CNH antes de alugar.");
        }
        
        //Regra 5: CNH não pode estar expirada
        if (cnh.getDataValidade().isBefore(locacao.getDataDevolucaoPrevista().toLocalDate())) {
            throw new RegraNegocioException("A CNH do cliente vencerá durante o período da locação.");
        }

        //Regra 6: O veículo precisa estar disponível para ser alugado
        Veiculo veiculo = veiculoService.buscarPorId(locacao.getVeiculoId());
        if (veiculo.getStatus() != Veiculo.StatusVeiculo.DISPONIVEL) {
            throw new RegraNegocioException("Veículo indisponível. Status atual: " + veiculo.getStatus());
        }
        
        //Regra 7: A filial de retirada e localização do veíuculo precisam ser o mesmo
        if (veiculo.getFilialAtualId() != locacao.getFilialRetiradaId()) {
            Filial filialCarro = filialService.buscarPorId(veiculo.getFilialAtualId());
            throw new RegraNegocioException("Este veículo não está na filial selecionada. Ele está em: " + filialCarro.getNome());
        }

        //Regra 8: A cattegoria da CNH precisa bater com a do veículo
        String categoriaNecessaria = veiculo.getGrupoCnhNecessario(); 
        if (!cnh.getCategoria().contains(categoriaNecessaria)) {
            throw new RegraNegocioException("Categoria da CNH (" + cnh.getCategoria() + ") não permite conduzir este veículo (Exige: " + categoriaNecessaria + ").");
        }

        //Regra 9: Cálculo financeiro
        long dias = ChronoUnit.DAYS.between(locacao.getDataRetirada().toLocalDate(), locacao.getDataDevolucaoPrevista().toLocalDate());
        if (dias < 1) dias = 1;
        BigDecimal bdDias = new BigDecimal(dias);
        BigDecimal total = veiculo.getValorDiaria().multiply(bdDias);
        
        //Taxa de seguro
        if (comSeguro) {
            total = total.add(new BigDecimal("30.00").multiply(bdDias));
        }
        //Taxa de motorista jovem
        if (cliente.getDataNascimento() != null) {
            int idade = Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 21) {
                total = total.add(new BigDecimal("20.00").multiply(bdDias));
            }
        }
        //Taxa de transporte (devolução em estado diferente da retirada)
        Filial fRetirada = filialService.buscarPorId(locacao.getFilialRetiradaId());
        Filial fDevolucao = filialService.buscarPorId(locacao.getFilialDevolucaoPrevistaId());
        if (!fRetirada.getEstado().equalsIgnoreCase(fDevolucao.getEstado())) {
            BigDecimal taxaFixa = new BigDecimal("150.00");
            BigDecimal taxaVar = total.multiply(new BigDecimal("0.10"));
            total = total.add(taxaFixa).add(taxaVar);
        }

        total = total.add(total.multiply(new BigDecimal("0.05")));
        locacao.setValorTotal(total.setScale(2, RoundingMode.HALF_UP));

        //Salvar locação (operação atômica)
        locacaoDAO.efetivarLocacao(locacao, veiculo.getId());
    }

    /**
     * Cancela uma locação ainda não finalizada, estornando a disponibilidade do veículo.
     */
    public void cancelarLocacao(Locacao locacao) throws RegraNegocioException {
        if (locacao.getStatus() != Locacao.StatusLocacao.ABERTA) {
            throw new RegraNegocioException("Apenas locações em aberto podem ser canceladas.");
        }
        //(não atômica. Consertar depois)
        locacaoDAO.cancelarLocacao(locacao, locacao.getVeiculoId());
    }
    
    /**
     * Sem regras de negócio.
     */
    public List<Locacao> buscarHistorico(Cliente cliente) {
        return locacaoDAO.listarLocacoesPorCliente(cliente.getId());
    }
    
    /**
     * Projeta o custo de uma locação com suas taxas para exibição na tela (View/Front-end).
     * @return Um dicionário (Map) mapeando os nomes das taxas aos seus respectivos valores.
     */
    public Map<String, Object> simularValores(Locacao locacao, boolean comSeguro, Cliente cliente) throws RegraNegocioException {
        if (locacao.getDataDevolucaoPrevista().isBefore(locacao.getDataRetirada())) {
            throw new RegraNegocioException("A data de devolução não pode ser anterior à retirada.");
        }
        
        long dias = ChronoUnit.DAYS.between(locacao.getDataRetirada().toLocalDate(), locacao.getDataDevolucaoPrevista().toLocalDate());
        if (dias < 1) dias = 1;
        BigDecimal bdDias = new BigDecimal(dias);

        Veiculo veiculo = veiculoService.buscarPorId(locacao.getVeiculoId());
        if (veiculo == null) throw new RegraNegocioException("Veículo inválido.");

        Map<String, Object> resumo = new HashMap<>();
        resumo.put("dias", dias);
        
        BigDecimal total = veiculo.getValorDiaria().multiply(bdDias);
        resumo.put("subtotal", total);

        if (comSeguro) {
            BigDecimal valorSeguro = new BigDecimal("30.00").multiply(bdDias);
            resumo.put("seguro", valorSeguro);
            total = total.add(valorSeguro);
        }

        if (cliente.getDataNascimento() != null) {
            int idade = Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 21) {
                BigDecimal taxaJovem = new BigDecimal("20.00").multiply(bdDias);
                resumo.put("taxaJovem", taxaJovem);
                total = total.add(taxaJovem);
            }
        }

        Filial fRetirada = filialService.buscarPorId(locacao.getFilialRetiradaId());
        Filial fDevolucao = filialService.buscarPorId(locacao.getFilialDevolucaoPrevistaId());
        
        if (fRetirada != null && fDevolucao != null && !fRetirada.getEstado().equalsIgnoreCase(fDevolucao.getEstado())) {
            BigDecimal taxaFixa = new BigDecimal("150.00");
            BigDecimal taxaVar = total.multiply(new BigDecimal("0.10"));
            BigDecimal taxaRetorno = taxaFixa.add(taxaVar);
            resumo.put("taxaRetorno", taxaRetorno);
            total = total.add(taxaRetorno);
        }

        BigDecimal taxaServico = total.multiply(new BigDecimal("0.05"));
        resumo.put("taxaServico", taxaServico);
        total = total.add(taxaServico);
        
        resumo.put("totalGeral", total);

        return resumo; 
    }
}