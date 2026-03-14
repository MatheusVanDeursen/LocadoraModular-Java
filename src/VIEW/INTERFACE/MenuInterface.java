package VIEW.INTERFACE;

import java.awt.Color;
import MODELS.Cliente;

/**
 * Interface da view Menu
 */
public interface MenuInterface {
	// --- Interações de Tela
	
	/**
     * Exibe um popup genérico de mensagem na tela.
     */
    void exibirMensagem(String mensagem, String titulo, int tipoAlerta);
    
    /**
     * Exibe um popup renderizando marcações HTML (usado para recibos/fichas).
     */
    
    void exibirMensagemHtml(String mensagemHtml, String titulo);
    
    /**
     * Exibe um popup de confirmação (Sim/Não) e retorna a escolha do usuário.
     */
    int exibirConfirmacao(String mensagem, String titulo);
    
    // --- Preenchimento e Status
    
    /**
     * Atualiza o conteúdo visual da tabela de histórico com os novos dados.
     */
    void atualizarTabela(Object[][] dados);
    
    /**
     * Atualiza o texto e a cor do aviso de status do cadastro no topo da tela.
     */
    void atualizarStatusCadastro(String texto, Color cor);
    
    /**
     * Atualiza o texto do botão indicando a direção atual da ordenação (Crescente/Decrescente).
     */
    void atualizarTextoBotaoOrdem(String texto);
    
    // --- Coleta de Estado - Histórico
    
    /**
     * Retorna o índice da linha atualmente selecionada na tabela de histórico.
     */
    int getLinhaSelecionadaTabela();
    
    /**
     * Retorna o índice do critério de ordenação selecionado no ComboBox.
     */
    int getCriterioOrdenacao();
    
    // --- Navegação e Fluxo
    void abrirTelaNovaLocacao(Cliente cliente);
    void abrirTelaDadosPessoais(Cliente cliente);
    void encerrarAplicacao();
}
