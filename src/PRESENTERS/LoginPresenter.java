package PRESENTERS;

import MODELS.Cliente;
import SERVICES.ClienteService;
import VIEW.INTERFACE.LoginInterface;
import EXCEPTIONS.RegraNegocioException;
import javax.swing.JOptionPane;

public class LoginPresenter {
    private LoginInterface view;
    private ClienteService clienteService;

    public LoginPresenter(LoginInterface view) {
        this.view = view;
        this.clienteService = new ClienteService();
    }

    public void autenticarUsuario() {
        String email = view.getEmail();
        String senha = view.getSenha();

        //Validações de Sintaxe
        if(email.isEmpty() || senha.isEmpty()){
            view.exibirMensagem("Por favor, preencha o e-mail e a senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            Cliente clienteLogado = clienteService.autenticarCliente(email, senha);

            view.navegarParaMenu(clienteLogado);
            view.fecharTela();

        }catch(RegraNegocioException e){
            view.exibirMensagem(e.getMessage(), "Acesso Negado", JOptionPane.WARNING_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            view.exibirMensagem("Erro interno de sistema. Tente novamente mais tarde.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void solicitarNovoCadastro() {
        view.navegarParaCadastro();
    }
}