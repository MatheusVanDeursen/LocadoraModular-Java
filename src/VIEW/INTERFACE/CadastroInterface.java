package VIEW.INTERFACE;

/**
 * Interface da view Cadastro
 */
public interface CadastroInterface {
	// --- Coleta de dados da View
    String getNome();
    String getSobrenome();
    String getCpf();
    String getDataNascimento();
    String getEmail();
    String getTelefone();
    String getSenha();

    // --- Interações de Tela
    
    /**
     * Exibe um popup genérico de mensagem na tela.
     */
    void exibirMensagem(String mensagem, String titulo, int tipoAlerta);
    
    void fecharTela();
}
