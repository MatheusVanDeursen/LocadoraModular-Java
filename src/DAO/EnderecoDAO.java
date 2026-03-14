package DAO;

import CONFIG_DB.ConnMySQL;
import MODELS.Endereco;
import java.sql.*;

/**
 * Responsável pela persistência de dados da entidade Endereco.
 */
public class EnderecoDAO {

	/**
     * Salva um novo Endereco no banco.
     * @throws RuntimeException Encapsula a SQLException
     */
    public void salvarEndereco(Endereco endereco) {
        String sql = "INSERT INTO enderecos (cliente_id, cep, logradouro, numero, complemento, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, endereco.getClienteId());
            stmt.setString(2, endereco.getCep());
            stmt.setString(3, endereco.getLogradouro());
            stmt.setString(4, endereco.getNumero());		//Aceita String (ex: "10-B")
            stmt.setString(5, endereco.getComplemento());
            stmt.setString(6, endereco.getBairro());
            stmt.setString(7, endereco.getCidade());
            stmt.setString(8, endereco.getEstado());
            stmt.executeUpdate();
        }catch (SQLException e){
        	throw new RuntimeException("Erro de banco de dados ao tentar salvar o endereço.", e);
        }
    }
    
    /**
     * @return Endereco ou null
     * @throws RuntimeException Encapsula a SQLException
     */
    public Endereco buscarEnderecoPorIdCliente(int clienteId) {
        String sql = "SELECT * FROM enderecos WHERE cliente_id = ?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Endereco e = new Endereco();
                e.setId(rs.getInt("id"));
                e.setClienteId(rs.getInt("cliente_id"));
                e.setCep(rs.getString("cep"));
                e.setLogradouro(rs.getString("logradouro"));
                e.setNumero(rs.getString("numero"));
                e.setComplemento(rs.getString("complemento"));
                e.setBairro(rs.getString("bairro"));
                e.setCidade(rs.getString("cidade"));
                e.setEstado(rs.getString("estado"));
                return e;
            }
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar buscar o endereço.", e);
        }
        return null;
    }
    
    /**
     * @throws RuntimeException Encapsula a SQLException
     */
    public void atualizarDadosEndereco(Endereco endereco) {
        String sql = "UPDATE enderecos SET cep=?, logradouro=?, numero=?, complemento=?, bairro=?, cidade=?, estado=? WHERE cliente_id=?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getEstado());
            stmt.setInt(8, endereco.getClienteId());		// Busca pelo ID do Cliente (relação 1:1)
            stmt.executeUpdate();
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar atualizar os dados do endereço.", e);
        }
    }

    
}