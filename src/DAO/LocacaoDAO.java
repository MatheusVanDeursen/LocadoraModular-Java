package DAO;

import CONFIG_DB.ConnMySQL;
import MODELS.Locacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável pela persistência de dados da entidade Locacao.
 */
public class LocacaoDAO {
	
	/**
     * Salva um novo Locacao no banco.
     * @throws RuntimeException Encapsula a SQLException
     */
    public void salvarLocacao(Locacao locacao) {
        String sql = "INSERT INTO locacoes (cliente_id, veiculo_id, filial_retirada_id, filial_devolucao_prevista_id, " +
                     "data_retirada, data_devolucao_prevista, status, valor_total) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locacao.getClienteId());
            stmt.setInt(2, locacao.getVeiculoId());
            stmt.setInt(3, locacao.getFilialRetiradaId());
            stmt.setInt(4, locacao.getFilialDevolucaoPrevistaId());
            stmt.setTimestamp(5, Timestamp.valueOf(locacao.getDataRetirada()));
            stmt.setTimestamp(6, Timestamp.valueOf(locacao.getDataDevolucaoPrevista()));
            stmt.setString(7, locacao.getStatus().name());
            stmt.setBigDecimal(8, locacao.getValorTotal());
            stmt.executeUpdate();
            System.out.println("Locação salva! Valor estimado: " + locacao.getValorTotal());
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar salvar a locação.", e);
        }
    }
    
    /**
     * Efetiva corretamente uma locação ao banco de dados, atualizando o status do veículo junto (Atômico).
     * @throws RuntimeException Encapsula a SQLException e faz o Rollback em caso de erro.
     */
    public void efetivarLocacao(Locacao locacao, int veiculoId) {
        String sqlInsertLocacao = "INSERT INTO locacoes (cliente_id, veiculo_id, filial_retirada_id, filial_devolucao_prevista_id, " +
                                  "data_retirada, data_devolucao_prevista, status, valor_total) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        String sqlUpdateVeiculo = "UPDATE veiculos SET status = 'ALUGADO' WHERE id = ?";

        try (Connection conn = ConnMySQL.conexao()) {
            conn.setAutoCommit(false); 

            try (PreparedStatement stmtLoc = conn.prepareStatement(sqlInsertLocacao);
                 PreparedStatement stmtVei = conn.prepareStatement(sqlUpdateVeiculo)) {

                stmtLoc.setInt(1, locacao.getClienteId());
                stmtLoc.setInt(2, locacao.getVeiculoId());
                stmtLoc.setInt(3, locacao.getFilialRetiradaId());
                stmtLoc.setInt(4, locacao.getFilialDevolucaoPrevistaId());
                stmtLoc.setTimestamp(5, Timestamp.valueOf(locacao.getDataRetirada()));
                stmtLoc.setTimestamp(6, Timestamp.valueOf(locacao.getDataDevolucaoPrevista()));
                stmtLoc.setString(7, locacao.getStatus().name());
                stmtLoc.setBigDecimal(8, locacao.getValorTotal());
                stmtLoc.executeUpdate();

                stmtVei.setInt(1, veiculoId);
                stmtVei.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                //Se falhou no INSERT ou no UPDATE, desfaz tudo (Rollback)
                conn.rollback();
                throw new RuntimeException("Erro ao processar a locação. Operação revertida com segurança.", e);
            } finally {
                conn.setAutoCommit(true); //Restaura o comportamento padrão da conexão antes de devolvê-la
            }

        } catch (SQLException e) {
            throw new RuntimeException("Falha de comunicação com o banco de dados.", e);
        }
    }
    
    /**
     * Cancela uma locação no banco de dados, liberando o status do veículo junto (Atômico).
     * @throws RuntimeException Encapsula a SQLException e faz o Rollback em caso de erro.
     */
    public void cancelarLocacao(Locacao locacao, int veiculoId) {
        String sqlUpdateLocacao = "UPDATE locacoes SET data_cancelamento = ?, status = 'CANCELADA' WHERE id = ?";
        String sqlUpdateVeiculo = "UPDATE veiculos SET status = 'DISPONIVEL' WHERE id = ?";

        try (Connection conn = ConnMySQL.conexao()) {          
            conn.setAutoCommit(false); 
            try (PreparedStatement stmtLoc = conn.prepareStatement(sqlUpdateLocacao);
                 PreparedStatement stmtVei = conn.prepareStatement(sqlUpdateVeiculo)) {

                stmtLoc.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
                stmtLoc.setInt(2, locacao.getId());
                stmtLoc.executeUpdate();

                stmtVei.setInt(1, veiculoId);
                stmtVei.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao cancelar a locação. Operação revertida com segurança.", e);
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Falha de comunicação com o banco de dados.", e);
        }
    }
    
    /**
     * @return Lista de todas as locacões do cliente
     * @throws RuntimeException Encapsula a SQLException
     */
    public List<Locacao> listarLocacoesPorCliente(int clienteId) {
        List<Locacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM locacoes WHERE cliente_id = ? ORDER BY data_retirada DESC";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Locacao l = new Locacao();
                l.setId(rs.getInt("id"));
                l.setClienteId(rs.getInt("cliente_id"));
                l.setVeiculoId(rs.getInt("veiculo_id"));
                l.setFilialRetiradaId(rs.getInt("filial_retirada_id"));
                l.setFilialDevolucaoPrevistaId(rs.getInt("filial_devolucao_prevista_id")); 
                int filialReal = rs.getInt("filial_devolucao_real_id");		// Integers que podem ser nulos (usando getObject ou lógica de 0)
                if (!rs.wasNull()) {
                	l.setFilialDevolucaoRealId(filialReal);
            	}
                l.setDataRetirada(rs.getTimestamp("data_retirada").toLocalDateTime());
                l.setDataDevolucaoPrevista(rs.getTimestamp("data_devolucao_prevista").toLocalDateTime());
                
                if(rs.getTimestamp("data_devolucao_real") != null) {
                	l.setDataDevolucaoReal(rs.getTimestamp("data_devolucao_real").toLocalDateTime());
                }
                if(rs.getTimestamp("data_cancelamento") != null) {
                	l.setDataCancelamento(rs.getTimestamp("data_cancelamento").toLocalDateTime());
                }
                // KMs
                int kmIni = rs.getInt("km_inicial");
                if(!rs.wasNull()) l.setKmInicial(kmIni);
                
                int kmFim = rs.getInt("km_final");
                if(!rs.wasNull()) l.setKmFinal(kmFim);

                l.setValorTotal(rs.getBigDecimal("valor_total"));
                l.setStatus(Locacao.StatusLocacao.valueOf(rs.getString("status")));

                lista.add(l);
            }
        } catch (SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar listar as locações do cliete.", e);
        }
        return lista;
    }

    /*
    public void finalizarLocacao(Locacao locacao) {
        String sql = "UPDATE locacoes SET data_devolucao_real = ?, filial_devolucao_real_id = ?, " +
                     "km_final = ?, valor_total = ?, status = ? WHERE id = ?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(locacao.getDataDevolucaoReal()));
            stmt.setInt(2, locacao.getFilialDevolucaoRealId());
            stmt.setInt(3, locacao.getKmFinal());
            stmt.setBigDecimal(4, locacao.getValorTotal());
            stmt.setString(5, locacao.getStatus().name());
            stmt.setInt(6, locacao.getId());
            stmt.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    */
    
    /**
     * Atualiza o status da locação para CANCELADA no banco
     * @throws RuntimeException Encapsula a SQLException
     */
    public void cancelarLocacao(Locacao locacao) {
        //Registra a data do cancelamento para histórico
        String sql = "UPDATE locacoes SET data_cancelamento = ?, status = 'CANCELADA' WHERE id = ?";
        try (Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
             
             stmt.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
             stmt.setInt(2, locacao.getId());
             stmt.executeUpdate();
             
        } catch (SQLException e) { 
        	throw new RuntimeException("Erro de banco de dados ao tentar cancelar a locação.", e);
        }
    }
    
}