package SERVICES;

import DAO.ClienteDAO;
import MODELS.Cliente;
import EXCEPTIONS.RegraNegocioException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;


/**
 * Concentra as regras de negócio relacionadas à entidade Cliente realizando validação dos dados.
 */
public class ClienteService {
    private ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Valida um novo cliente no sistema.
     * @throws RegraNegocioException
     */
    public void cadastrarCliente(Cliente cliente) throws RegraNegocioException {
        //Regra 1: Duplicidade de Email
        if(clienteDAO.buscarClientePorEmail(cliente.getEmail()) != null) {
            throw new RegraNegocioException("Este e-mail já está em uso.");
        }

        //Regra 2: Maioridade
        int idade = Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears();
        if(idade < 18) {
            throw new RegraNegocioException("O cliente deve ser maior de 18 anos para criar conta.");
        }
        
        //regra 3: Criptografia de senha
        String senhaCriptografada = gerarHashSenhaCliente(cliente.getSenhaHash());
        cliente.setSenhaHash(senhaCriptografada);

        //Salva ao banco
        clienteDAO.salvarCliente(cliente);
    }
    
    /**
     * Verifica as credenciais de acesso do cliente.
     * @return A entidade Cliente preenchida.
     * @throws RegraNegocioException
     */
    public Cliente autenticarCliente(String email, String senhaDigitada) throws RegraNegocioException {
        Cliente cliente = clienteDAO.buscarClientePorEmail(email);
        //Cliente não encontrado
        if(cliente == null) {
            throw new RegraNegocioException("E-mail ou senha incorretos.");
        }
        //Senha incorreta
        String hashDaSenhaDigitada = gerarHashSenhaCliente(senhaDigitada);
        if(hashDaSenhaDigitada == null || !hashDaSenhaDigitada.equals(cliente.getSenhaHash())) {
            throw new RegraNegocioException("E-mail ou senha incorretos.");
        }
        return cliente;
    }

    /**
     * Aplica o algoritmo SHA-256 para gerar o hash unidirecional de uma senha.
     * @param senhaPlain A senha em texto plano.
     * @return A representação hexadecimal do hash gerado.
     * @throws RuntimeException Caso o algoritmo SHA-256 não esteja disponível na JVM atual.
     */
    private String gerarHashSenhaCliente(String senhaPlain) {
        try{
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(senhaPlain.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(String.format("%02X", 0xFF & b));
            return hexString.toString();       
        }catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro interno de criptografia.", e);
        }
    }
    
    /**
     * Sem regras de negócio.
     */
    public void atualizarDadosCliente(Cliente cliente) {
        clienteDAO.atualizarDadosCliente(cliente);
    }
    
    /**
     * Sem regras de negócio.
     */
    public Cliente buscarPorId(int id) {
        return clienteDAO.buscarClientePorId(id);
    }
}