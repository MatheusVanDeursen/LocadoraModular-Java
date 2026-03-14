package SERVICES;

import DAO.VeiculoDAO;
import MODELS.Veiculo;
//import EXCEPTIONS.RegraNegocioException;
//import java.math.BigDecimal;
import java.util.List;

/**
 * Concentra as regras de negócio relacionadas à entidade Veiculo realizando validação dos dados.
 */
public class VeiculoService {
    private VeiculoDAO veiculoDAO = new VeiculoDAO();
    
    /**
     * Sem regras de negócio.
     */
    public List<Veiculo> listarDisponiveis() { 
    	return veiculoDAO.listarVeiculosDisponiveis(); 
	}
    
    /**
     * Sem regras de negócio.
     */
    public Veiculo buscarPorId(int id) { 
    	return veiculoDAO.buscarVeiculoPorId(id); 
	}
    
    /**
     * Sem regras de negócio.
     */
    public Veiculo buscarPorPlaca(String placa) { 
    	return veiculoDAO.buscarVeiculoPorPlaca(placa); 
	}
    
    /**
     * Sem regras de negócio.
     */
    public void atualizarStatus(int id, Veiculo.StatusVeiculo novoStatus) { 
    	veiculoDAO.atualizarStatusVeiculo(id, novoStatus);
	}
}