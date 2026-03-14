package EXCEPTIONS;

@SuppressWarnings("serial")
public class RegraNegocioException extends Exception {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}