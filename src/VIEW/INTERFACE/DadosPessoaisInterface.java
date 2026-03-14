package VIEW.INTERFACE;

import MODELS.Cliente;
import MODELS.Cnh;
import MODELS.Endereco;
import java.awt.Color;

/**
 * Interface da view DadosPessoais
 */
public interface DadosPessoaisInterface {
    // --- Coleta de Dados - Pessoais
    String getNome();
    String getSobrenome();
    String getTelefone();
    
    // --- Coleta de Dados - Endereço
    String getCep();
    String getEstado();
    String getCidade();
    String getBairro();
    String getLogradouro();
    String getNumero();
    String getComplemento();
    
    // --- Coleta de Dados - CNH
    String getCnhRegistro();
    String getCnhCategoria();
    String getCnhValidade();
    String getCnhPrimeiraHab();
    
    // --- Autenticação
    String getSenhaConfirmacao();
    void limparSenhaConfirmacao();

    // --- Interações de Tela
    
    /**
     * Exibe um popup genérico de mensagem na tela.
     */
    void exibirMensagem(String mensagem, String titulo, int tipoAlerta);
    
    void fecharTela();
    
    /**
     * Alterna o visual da tela entre o modo de leitura (bloqueado) e o modo de edição (editável).
     */
    void setModoEdicao(boolean ativado);
    
    // --- Preenchimento e Status
    
    /**
     * Preenche todos os campos da tela com os dados carregados do banco.
     */
    void preencherCampos(Cliente cliente, Endereco endereco, Cnh cnh);
    
    /**
     * Atualiza o texto e a cor da label de aviso na aba de Endereço.
     */
    void atualizarAvisoEndereco(String texto, Color cor);
    
    /**
     * Atualiza o texto e a cor da label de aviso na aba de CNH.
     */
    void atualizarAvisoCnh(String texto, Color cor);
}