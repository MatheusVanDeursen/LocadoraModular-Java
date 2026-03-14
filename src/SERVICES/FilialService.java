package SERVICES;

import DAO.FilialDAO;
import MODELS.Filial;
import java.util.List;

/**
 * Concentra as regras de negócio relacionadas à entidade Filial realizando validação dos dados.
 */
public class FilialService {
    private FilialDAO filialDAO = new FilialDAO();

    /**
     * Sem regras de negócio.
     */
    public List<Filial> listarTodas() {
        return filialDAO.listarTodasFiliais();
    }

    /**
     * Sem regras de negócio.
     */
    public Filial buscarPorId(int id){
        return filialDAO.buscarFilialPorId(id);
    }
}