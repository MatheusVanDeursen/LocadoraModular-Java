package MODELS;

import java.math.BigDecimal;

/**
 * Entidade Veículo.
 */
public class Veiculo {
    public enum StatusVeiculo { DISPONIVEL, ALUGADO, MANUTENCAO, INATIVO }
    public enum TipoVeiculo { HATCH, SEDAN, SUV, MOTO, UTILITARIO }
    public enum Transmissao { MANUAL, AUTOMATICO }

    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private String cor;
    
    //Logística e Perfil
    private int filialAtualId;
    private TipoVeiculo tipo;
    private Transmissao transmissao;
    private String grupoCnhNecessario;
    private int capacidadePassageiros;
    private int capacidadeMalas;
    private int kmAtual;
    
    //Financeiro e Histórico
    private int anoFabricacao;
    private BigDecimal valorDiaria;
    private StatusVeiculo status;

    public Veiculo() {}
    
    public Veiculo(String placa, String marca, String modelo, String cor, int filialAtualId, TipoVeiculo tipo, Transmissao transmissao, String grupoCnhNecessario, int capacidadePassageiros, int capacidadeMalas, int anoFabricacao, BigDecimal valorDiaria) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.filialAtualId = filialAtualId;
        this.tipo = tipo;
        this.transmissao = transmissao;
        this.grupoCnhNecessario = grupoCnhNecessario;
        this.capacidadePassageiros = capacidadePassageiros;
        this.capacidadeMalas = capacidadeMalas;
        this.anoFabricacao = anoFabricacao;
        this.valorDiaria = valorDiaria;
        this.kmAtual = 0;
        this.status = StatusVeiculo.DISPONIVEL;
    }

    //Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public int getFilialAtualId() { return filialAtualId; }
    public void setFilialAtualId(int filialAtualId) { this.filialAtualId = filialAtualId; }

    public TipoVeiculo getTipo() { return tipo; }
    public void setTipo(TipoVeiculo tipo) { this.tipo = tipo; }

    public Transmissao getTransmissao() { return transmissao; }
    public void setTransmissao(Transmissao transmissao) { this.transmissao = transmissao; }

    public String getGrupoCnhNecessario() { return grupoCnhNecessario; }
    public void setGrupoCnhNecessario(String grupoCnhNecessario) { this.grupoCnhNecessario = grupoCnhNecessario; }

    public int getCapacidadePassageiros() { return capacidadePassageiros; }
    public void setCapacidadePassageiros(int capacidadePassageiros) { this.capacidadePassageiros = capacidadePassageiros; }

    public int getCapacidadeMalas() { return capacidadeMalas; }
    public void setCapacidadeMalas(int capacidadeMalas) { this.capacidadeMalas = capacidadeMalas; }

    public int getKmAtual() { return kmAtual; }
    public void setKmAtual(int kmAtual) { this.kmAtual = kmAtual; }

    public int getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(int anoFabricacao) { this.anoFabricacao = anoFabricacao; }

    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }

    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
}