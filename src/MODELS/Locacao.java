package MODELS;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Locação.
 */
public class Locacao {
    public enum StatusLocacao { ABERTA, FINALIZADA, CANCELADA }
    
    //Chaves
    private int id;
    private int clienteId;
    private int veiculoId;
    
    //Logística Geográfica
    private int filialRetiradaId;
    private int filialDevolucaoPrevistaId;
    private Integer filialDevolucaoRealId; // Wrapper class: Permite ser null
    
    //Linha do Tempo
    private LocalDateTime dataRetirada;
    private LocalDateTime dataDevolucaoPrevista;
    private LocalDateTime dataDevolucaoReal;
    private LocalDateTime dataCancelamento;
    
    //Logística do Ativo
    private Integer kmInicial; // Wrapper class: Permite ser null
    private Integer kmFinal;   // Wrapper class: Permite ser null
    
    //Financeiro e Status
    private BigDecimal valorTotal;
    private StatusLocacao status;

    public Locacao() {}
    
    public Locacao(int clienteId, int veiculoId, int filialRetiradaId, int filialDevolucaoPrevistaId, LocalDateTime dataRetirada, LocalDateTime dataDevolucaoPrevista) {
        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.filialRetiradaId = filialRetiradaId;
        this.filialDevolucaoPrevistaId = filialDevolucaoPrevistaId;
        this.dataRetirada = dataRetirada;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.status = StatusLocacao.ABERTA;
    }

    //Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public int getVeiculoId() { return veiculoId; }
    public void setVeiculoId(int veiculoId) { this.veiculoId = veiculoId; }

    public int getFilialRetiradaId() { return filialRetiradaId; }
    public void setFilialRetiradaId(int filialRetiradaId) { this.filialRetiradaId = filialRetiradaId; }

    public int getFilialDevolucaoPrevistaId() { return filialDevolucaoPrevistaId; }
    public void setFilialDevolucaoPrevistaId(int filialDevolucaoPrevistaId) { this.filialDevolucaoPrevistaId = filialDevolucaoPrevistaId; }

    public Integer getFilialDevolucaoRealId() { return filialDevolucaoRealId; }
    public void setFilialDevolucaoRealId(Integer filialDevolucaoRealId) { this.filialDevolucaoRealId = filialDevolucaoRealId; }

    public LocalDateTime getDataRetirada() { return dataRetirada; }
    public void setDataRetirada(LocalDateTime dataRetirada) { this.dataRetirada = dataRetirada; }

    public LocalDateTime getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDateTime dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }

    public LocalDateTime getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDateTime dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }

    public LocalDateTime getDataCancelamento() { return dataCancelamento; }
    public void setDataCancelamento(LocalDateTime dataCancelamento) { this.dataCancelamento = dataCancelamento; }

    public Integer getKmInicial() { return kmInicial; }
    public void setKmInicial(Integer kmInicial) { this.kmInicial = kmInicial; }

    public Integer getKmFinal() { return kmFinal; }
    public void setKmFinal(Integer kmFinal) { this.kmFinal = kmFinal; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public StatusLocacao getStatus() { return status; }
    public void setStatus(StatusLocacao status) { this.status = status; }
}