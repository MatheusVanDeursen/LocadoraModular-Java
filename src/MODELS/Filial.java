package MODELS;

/**
 * Entidade Filial.
 */
public class Filial {
    private int id;
    private String nome;
    private String cnpj;		//Armazena no formato XX.XXX.XXX/XXXX-XX no banco de dados
    private String cidade;
    private String estado;		//Armazena como dois caracteres do UF (ex:SP, RJ...)

    public Filial() {}

    public Filial(int id, String nome, String cnpj, String cidade, String estado) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.cidade = cidade;
        this.estado = estado;
    }

    //Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}