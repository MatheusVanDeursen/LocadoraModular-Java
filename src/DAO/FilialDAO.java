package DAO;

import CONFIG_DB.ConnMySQL;
import MODELS.Filial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável pela persistência de dados da entidade Filial.
 */
public class FilialDAO {

	/**
     * Salva um novo Endereco no banco.
     * @throws RuntimeException Encapsula a SQLException
     */
    public List<Filial> listarTodasFiliais() {
        List<Filial> lista = new ArrayList<>();
        String sql = "SELECT * FROM filiais";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while(rs.next()) {
                Filial f = new Filial();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setCnpj(rs.getString("cnpj"));
                f.setCidade(rs.getString("cidade"));
                f.setEstado(rs.getString("estado"));
                lista.add(f);
            }
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar listar as filiais.", e);
        }
        return lista;
    }

    /**
     * @return Filial ou null
     * @throws RuntimeException Encapsula a SQLException
     */
    public Filial buscarFilialPorId(int id) {
        String sql = "SELECT * FROM filiais WHERE id = ?";
        try (Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Filial f = new Filial();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setCnpj(rs.getString("cnpj"));
                f.setCidade(rs.getString("cidade"));
                f.setEstado(rs.getString("estado"));
                return f;
            }
        } catch (SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar buscar a filial.", e);
        }
        return null;
    }
}