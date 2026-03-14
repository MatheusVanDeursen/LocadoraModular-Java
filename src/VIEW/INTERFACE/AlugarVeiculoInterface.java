package VIEW.INTERFACE;

import java.util.List;

/**
 * Interface da view AlugarVeiculo
 */
public interface AlugarVeiculoInterface {
    // -- Coleta de Estado - Filtros (Esquerda)
    int getFilialRetiradaIndex();
    String getBuscaRapida();
    String getCategoriaSelecionada();
    String getCambioSelecionado();
    int getOrdenacaoSelecionada();
    int getVeiculoSelecionadoId();

    // --- Coleta de Estado - Locação (Direita)
    String getDataRetirada();
    String getDataDevolucao();
    int getFilialDevolucaoIndex();
    boolean isSeguroSelecionado();

    // --- Comandos de Atualização Visual
    
    /**
     * Preenche as ComboBoxes de Retirada e Devolução com a lista de filiais disponíveis.
     */
    void popularComboFiliais(List<String> nomesFiliais);
    
    void setFilialRetiradaIndex(int index);
    void setFilialDevolucaoIndex(int index);
    
    /**
     * Atualiza a tabela de veículos aplicando a renderização visual e ocultando a coluna de ID.
     */
    void atualizarTabelaVeiculos(Object[][] dados);
    
    /**
     * Atualiza o cabeçalho do painel de locação com o veículo selecionado.
     */
    void atualizarLabelCarroSelecionado(String html);
    
    /**
     * Atualiza a área de recibo com a simulação de custos em formato HTML.
     */
    void atualizarReciboVirtual(String html);
    
    /**
     * Bloqueia ou desbloqueia o painel direito de locação com base na seleção do usuário e status da conta.
     */
    void travarPainelDireito(boolean destravado, boolean isApto);
    
    /**
     * Exibe ou oculta a mensagem de alerta vermelho de cadastro incompleto/CNH vencida.
     */
    void exibirAvisoCadastroIncompleto(boolean exibir);
    
    // --- Interações de Tela
    
    /**
     * Exibe um popup genérico de mensagem na tela.
     */
    void exibirMensagem(String mensagem, String titulo, int tipoAlerta);
    
    /**
     * Exibe um popup com a ficha técnica detalhada em HTML do veículo selecionado.
     */
    void exibirFichaTecnica(String htmlFicha);
    
    void fecharTela();
}