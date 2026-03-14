package SERVICES;

import DAO.CnhDAO;
import MODELS.Cnh;
import EXCEPTIONS.RegraNegocioException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Concentra as regras de negócio relacionadas à entidade Cnh realizando validação dos dados.
 */
public class CnhService {
    private CnhDAO cnhDAO = new CnhDAO();

    /**
     * Valida uma nova CNH no sistema.
     * @throws RegraNegocioException
     */
    public void cadastrarCNH(Cnh cnh) throws RegraNegocioException {
        //Regra 1: CNH não pode estar vencida no ato do cadastro
        if(cnh.getDataValidade().isBefore(LocalDate.now())) {
            throw new RegraNegocioException("A CNH informada está vencida.");
        }

        //Regra 2: Validação de Tempo de Carteira (Novato não aluga)
        long anosHabilitado = ChronoUnit.YEARS.between(cnh.getDataPrimeiraHabilitacao(), LocalDate.now());
        if(anosHabilitado < 2) {
            throw new RegraNegocioException("É necessário ter no mínimo 2 anos de habilitação para alugar conosco.");
        }

        //Regra 3: Unicidade (CNH única)
        if(cnhDAO.buscarCnhPorClienteId(cnh.getClienteId()) != null) {
            throw new RegraNegocioException("Este cliente já possui uma CNH cadastrada.");
        }

        //Salva ao banco
        cnhDAO.salvarCnh(cnh);
    }
    
    /**
     * Valida a atualização da CNH no sistema.
     * @return A entidade Cliente preenchida.
     * @throws RegraNegocioException
     */
    public void atualizarCNH(Cnh cnh) throws RegraNegocioException {
        if(cnh.getDataValidade().isBefore(LocalDate.now())){
            throw new RegraNegocioException("A nova validade informada já está vencida.");
        }
        try{
            cnhDAO.atualizarDadosCnh(cnh);
        }catch(Exception e){
            throw new RegraNegocioException("Erro ao atualizar CNH: " + e.getMessage());
        }
    }
    
    /**
     * Sem regras de negócio.
     */
    public Cnh buscarPorCliente(int clienteId){
        return cnhDAO.buscarCnhPorClienteId(clienteId);
    }
}