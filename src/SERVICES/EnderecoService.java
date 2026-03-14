package SERVICES;

import DAO.EnderecoDAO;
import MODELS.Endereco;

/**
 * Concentra as regras de negócio relacionadas à entidade Endereco realizando validação dos dados.
 */
public class EnderecoService {
	private EnderecoDAO enderecoDAO = new EnderecoDAO();
	
	/**
     * Sem regras de negócio.
     */
	public void salvarEndereco(Endereco endereco) { 
		enderecoDAO.salvarEndereco(endereco);
	}
	
	/**
     * Sem regras de negócio.
     */
	public void atualizarEndereco(Endereco endereco) {
		enderecoDAO.atualizarDadosEndereco(endereco);
	}
	
	/**
     * Sem regras de negócio.
     */
	public Endereco buscarEnderecoPorIdCliente(int clienteId) {
		return enderecoDAO.buscarEnderecoPorIdCliente(clienteId);
	}
}
