package DAO;

import CONFIG_DB.ConnMySQL;
import MODELS.Cliente;
import java.sql.*;

/**
 * Responsável pela persistência de dados da entidade Cliente.
 */
public class ClienteDAO {

	/**
     * Salva um novo cliente no banco. O objeto 'cliente' passado como parâmetro é atualizado com o ID gerado pelo banco após a inserção.
     * @throws RuntimeException Encapsula a SQLException
     */
    public void salvarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, sobrenome, email, cpf, senha_hash, telefone, data_nascimento) VALUES (?, ?, ?, ?, ?, ?, ?)";    
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {       
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getCpf());
            stmt.setString(5, cliente.getSenhaHash());
            stmt.setString(6, cliente.getTelefone());
            stmt.setDate(7, Date.valueOf(cliente.getDataNascimento()));
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();		//Recupera o ID gerado para usar no objeto
            if(generatedKeys.next()) {
                cliente.setId(generatedKeys.getInt(1));
            }
            //System.out.println("Cliente salvo com sucesso! ID: " + cliente.getId());
        }catch(SQLException e) {
            throw new RuntimeException("Erro ao tentar salvar o cliente: " + cliente.getNome(), e);
        }
    }

    /**
     * @return Cliente ou null
     * @throws RuntimeException Encapsula a SQLException
     */
    public Cliente buscarClientePorEmail(String email) {
        String sql = "SELECT * FROM clientes WHERE email = ?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return mapearResultSet(rs);
            }
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar buscar o cleinte.", e);
        }
        return null;
    }
    
    /**
     * @return Cliente ou null
     * @throws RuntimeException Encapsula a SQLException
     */
    public Cliente buscarClientePorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return mapearResultSet(rs);
            }
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar buscar o cleinte.", e);
        }
        return null;
    }
    
    /**
     * Atualiza exclusivamente os dados cadastrais básicos (nome, sobrenome e telefone).
     * @throws RuntimeException Encapsula a SQLException
     */
    public void atualizarDadosCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nome=?, sobrenome=?, telefone=? WHERE id=?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar atualizar os dados do cleinte.", e);
        }
    }

    
    /**
     * Centraliza a conversão do ResultSet para o objeto Cliente.
     */
    private Cliente mapearResultSet(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setSobrenome(rs.getString("sobrenome"));
        c.setEmail(rs.getString("email"));
        c.setCpf(rs.getString("cpf"));
        c.setSenhaHash(rs.getString("senha_hash"));
        c.setTelefone(rs.getString("telefone"));
        c.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        c.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        return c;
    }
}