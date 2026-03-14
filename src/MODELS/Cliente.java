package MODELS;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Entidade Cliente.
 */
public class Cliente {
    private int id;
    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;						//CPF é armazenado como XXX.XXX.XXX-XX no banco de dados
    private String senhaHash;				//A senha vem hasheada da camada de serviço (SHA-256)
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastro;

    public Cliente() {}
    
    public Cliente(String nome, String sobrenome, String email, String cpf, String senhaHash, String telefone, LocalDate dataNascimento) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }

    public Cliente(int id, String nome, String sobrenome, String email, String cpf, String senhaHash, String telefone, LocalDate dataNascimento, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
    }

    //Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public String getNomeCompleto() {
        return this.nome + " " + this.sobrenome;
    }
}