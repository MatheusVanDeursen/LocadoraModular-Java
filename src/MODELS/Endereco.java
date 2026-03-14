package MODELS;


/**
 * Entidade Endereço.
 */
public class Endereco {
    private int id;
    private int clienteId;
    private String cep;				//Armazena no formato XXXXX-XXX no banco de dados
    private String logradouro;
    private String numero; 			//String para aceitar "S/N", "10-A"
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado; 			//Armazena como dois caracteres do UF (ex:SP, RJ...)

    
    public Endereco() {}
    
    public Endereco(int clienteId, String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado) {
        this.clienteId = clienteId;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Endereco(int id, int clienteId, String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    //Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}