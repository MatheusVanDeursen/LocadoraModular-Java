package DAO;

import CONFIG_DB.ConnMySQL;
import MODELS.Cnh;
import java.sql.*;

/**
 * Responsável pela persistência de dados da entidade CNH.
 */
public class CnhDAO {

	/**
     * Salva uma nova CNH no banco.
     * * @throws RuntimeException Encapsula a SQLException
     */
    public void salvarCnh(Cnh cnh) {
        String sql = "INSERT INTO cnhs (cliente_id, numero_registro, categoria, data_validade, data_primeira_habilitacao) VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cnh.getClienteId());
            stmt.setString(2, cnh.getNumeroRegistro());
            stmt.setString(3, cnh.getCategoria());
            stmt.setDate(4, Date.valueOf(cnh.getDataValidade()));
            stmt.setDate(5, Date.valueOf(cnh.getDataPrimeiraHabilitacao()));
            stmt.executeUpdate();
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar salvar a CNH.", e);
        }
    }

    /**
     * @return Cnh ou null
     * @throws RuntimeException Encapsula a SQLException
     */
    public Cnh buscarCnhPorClienteId(int clienteId) {
        String sql = "SELECT * FROM cnhs WHERE cliente_id = ?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Cnh c = new Cnh();
                c.setId(rs.getInt("id"));
                c.setClienteId(rs.getInt("cliente_id"));
                c.setNumeroRegistro(rs.getString("numero_registro"));
                c.setCategoria(rs.getString("categoria"));
                c.setDataValidade(rs.getDate("data_validade").toLocalDate());
                c.setDataPrimeiraHabilitacao(rs.getDate("data_primeira_habilitacao").toLocalDate());
                return c;
            }
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar buscar a CNH.", e);
        }
        return null;
    }
    
    /**
     * @throws RuntimeException Encapsula a SQLException
     */
    public void atualizarDadosCnh(Cnh cnh) {
        String sql = "UPDATE cnhs SET numero_registro=?, categoria=?, data_validade=?, data_primeira_habilitacao=? WHERE cliente_id=?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnh.getNumeroRegistro());
            stmt.setString(2, cnh.getCategoria());
            stmt.setDate(3, java.sql.Date.valueOf(cnh.getDataValidade()));
            stmt.setDate(4, java.sql.Date.valueOf(cnh.getDataPrimeiraHabilitacao()));
            stmt.setInt(5, cnh.getClienteId());
            stmt.executeUpdate();
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar atualizar os dados da CNH.", e);
        }
    }
}