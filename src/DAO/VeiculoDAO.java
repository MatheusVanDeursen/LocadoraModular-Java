package DAO;

import CONFIG_DB.ConnMySQL;
import MODELS.Veiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável pela persistência de dados da entidade Veiculo.
 */
public class VeiculoDAO {

	/**
     * Salva um novo Veiculo no banco.
     * @throws RuntimeException Encapsula a SQLException
     */
    public void salvarVeiculo(Veiculo veiculo) {
        String sql = "INSERT INTO veiculos (placa, marca, modelo, cor, filial_atual_id, tipo, transmissao, " +
                     "grupo_cnh_necessario, capacidade_passageiros, capacidade_malas, " +
                     "ano_fabricacao, valor_diaria, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getMarca());
            stmt.setString(3, veiculo.getModelo());
            stmt.setString(4, veiculo.getCor());
            stmt.setInt(5, veiculo.getFilialAtualId());
            stmt.setString(6, veiculo.getTipo().name()); 			//Enum -> String
            stmt.setString(7, veiculo.getTransmissao().name());		//Enum -> String
            stmt.setString(8, veiculo.getGrupoCnhNecessario());
            stmt.setInt(9, veiculo.getCapacidadePassageiros());
            stmt.setInt(10, veiculo.getCapacidadeMalas());
            stmt.setInt(12, veiculo.getAnoFabricacao());
            stmt.setBigDecimal(13, veiculo.getValorDiaria());
            stmt.setString(14, veiculo.getStatus().name());
            stmt.executeUpdate();
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar salvar o veículo.", e);
        }
    }
    
    /**
     * @return Lista de Veiculo com status DISPONIVEL
     * @throws RuntimeException Encapsula a SQLException
     */
    public List<Veiculo> listarVeiculosDisponiveis() {
    	return buscarVeiculos("SELECT * FROM veiculos WHERE status = ?", Veiculo.StatusVeiculo.DISPONIVEL.name());
    }
    
    /**
     * @return Veiculo ou null
     * @throws RuntimeException Encapsula a SQLException
     */
    public Veiculo buscarVeiculoPorId(int id) {
    	List<Veiculo> res = buscarVeiculos("SELECT * FROM veiculos WHERE id = ?", id);
        return (res.isEmpty() ? null : res.get(0));
    }
    
    /**
     * @return Filial ou null
     * @throws RuntimeException Encapsula a SQLException
     */
    public Veiculo buscarVeiculoPorPlaca(String placa) {
        List<Veiculo> res = buscarVeiculos("SELECT * FROM veiculos WHERE placa = ?", placa);
        return (res.isEmpty() ? null : res.get(0));
    }

    /**
     * Atualiza o veículo após uma locação, alterando a localização da filial (caso tenha mudado), a kilometragem do carro e o status do veículo
     * @throws RuntimeException Encapsula a SQLException
     */
    public void atualizarLocalizacaoVeiculo(int veiculoId, int novaFilialId, int novoKm, Veiculo.StatusVeiculo novoStatus) {
        String sql = "UPDATE veiculos SET filial_atual_id = ?, km_atual = ?, status = ? WHERE id = ?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novaFilialId);
            stmt.setInt(2, novoKm);
            stmt.setString(3, novoStatus.name());
            stmt.setInt(4, veiculoId);
            stmt.executeUpdate();
        }catch(SQLException e) {
        	throw new RuntimeException("Erro de banco de dados ao tentar atualizar a localização do veículo.", e);
        }
    }
    
    /**
     * @param id
     * @param novoStatus (enum do status do objeto)
     * @throws RuntimeException Encapsula a SQLException
     */
    public void atualizarStatusVeiculo(int id, Veiculo.StatusVeiculo novoStatus) {
        String sql = "UPDATE veiculos SET status = ? WHERE id = ?";
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }catch(SQLException e) { 
        	throw new RuntimeException("Erro de banco de dados ao tentar atualizar o status do veículo.", e);
    	}
        
    }
    
    /**
     * Método centralizador dinâmico para consultas de Veículos.
     * Recebe a query SQL e uma quantidade variável de parâmetros, mapeando-os automaticamente 
     * para o PreparedStatement na mesma ordem em que foram passados.
     * @param sql    A query SQL (ex: "SELECT * FROM veiculos WHERE id = ?").
     * @param params Argumentos variáveis que substituirão as interrogações (?) na query.
     * @throws RuntimeException 
     */
    private List<Veiculo> buscarVeiculos(String sql, Object...params) {
    	List<Veiculo> lista = new ArrayList<>();
        try(Connection conn = ConnMySQL.conexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for(int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Veiculo v = new Veiculo();
                    v.setId(rs.getInt("id"));
                    v.setPlaca(rs.getString("placa"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));
                    v.setCor(rs.getString("cor"));
                    v.setFilialAtualId(rs.getInt("filial_atual_id"));
                    v.setTipo(Veiculo.TipoVeiculo.valueOf(rs.getString("tipo"))); 
                    v.setTransmissao(Veiculo.Transmissao.valueOf(rs.getString("transmissao")));
                    v.setGrupoCnhNecessario(rs.getString("grupo_cnh_necessario"));
                    v.setCapacidadePassageiros(rs.getInt("capacidade_passageiros"));
                    v.setCapacidadeMalas(rs.getInt("capacidade_malas"));
                    v.setKmAtual(rs.getInt("km_atual"));
                    v.setAnoFabricacao(rs.getInt("ano_fabricacao"));
                    v.setValorDiaria(rs.getBigDecimal("valor_diaria"));
                    v.setStatus(Veiculo.StatusVeiculo.valueOf(rs.getString("status")));
                    lista.add(v);
                }
            }
        }catch(SQLException e) {
            throw new RuntimeException("Erro ao buscar veículos no banco de dados.", e);
        }
        return lista;
    }

}