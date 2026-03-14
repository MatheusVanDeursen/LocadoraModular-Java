package VIEW.INTERFACE;

import MODELS.Cliente;

/**
 * Interface da view Login
 */
public interface LoginInterface {
	// --- Coleta de dados da View
    String getEmail();
    String getSenha();

    // --- Interações de Tela
    
    /**
     * Exibe um popup genérico de mensagem na tela.
     */
    void exibirMensagem(String mensagem, String titulo, int tipoAlerta);
    
    void fecharTela();
    
    // --- Navegação e Fluxo
    
    /**
     * Navega para a tela principal (Menu) passando o cliente autenticado.
     */
    void navegarParaMenu(Cliente cliente);
    
    /**
     * Abre a janela de cadastro de novos clientes.
     */
    void navegarParaCadastro();
}