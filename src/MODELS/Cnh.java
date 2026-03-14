package MODELS;

import java.time.LocalDate;

/**
 * Entidade CNH.
 */
public class Cnh {
    private int id;
    private int clienteId;
    private String numeroRegistro;
    private String categoria;
    private LocalDate dataValidade;
    private LocalDate dataPrimeiraHabilitacao;

    public Cnh() {}
    
    public Cnh(int clienteId, String numeroRegistro, String categoria, LocalDate dataValidade, LocalDate dataPrimeiraHabilitacao) {
        this.clienteId = clienteId;
        this.numeroRegistro = numeroRegistro;
        this.categoria = categoria;
        this.dataValidade = dataValidade;
        this.dataPrimeiraHabilitacao = dataPrimeiraHabilitacao;
    }

    public Cnh(int id, int clienteId, String numeroRegistro, String categoria, LocalDate dataValidade, LocalDate dataPrimeiraHabilitacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.numeroRegistro = numeroRegistro;
        this.categoria = categoria;
        this.dataValidade = dataValidade;
        this.dataPrimeiraHabilitacao = dataPrimeiraHabilitacao;
    }

    //Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public LocalDate getDataPrimeiraHabilitacao() { return dataPrimeiraHabilitacao; }
    public void setDataPrimeiraHabilitacao(LocalDate dataPrimeiraHabilitacao) { this.dataPrimeiraHabilitacao = dataPrimeiraHabilitacao; }
}