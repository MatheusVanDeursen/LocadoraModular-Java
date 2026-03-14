package PRESENTERS;

import MODELS.Cliente;
import SERVICES.ClienteService;
import VIEW.INTERFACE.CadastroInterface;
import EXCEPTIONS.RegraNegocioException;
import UTILS.Validador;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CadastroPresenter {
    private CadastroInterface view;
    private ClienteService clienteService;

    public CadastroPresenter(CadastroInterface view) {
        this.view = view;
        this.clienteService = new ClienteService();
    }

    public void registrarNovoCliente() {
        //1. Coleta dos dados
        String nome = view.getNome();
        String sobrenome = view.getSobrenome();
        String cpf = view.getCpf().replace("_", ""); 
        String dataNascStr = view.getDataNascimento().replace("_", "");
        String email = view.getEmail();
        String telefone = view.getTelefone();
        String senha = view.getSenha();

        //2. Validações de Sintaxe
        if(Validador.isVazio(nome) || Validador.isVazio(sobrenome)){
            view.exibirMensagem("Nome e Sobrenome são obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(Validador.isVazio(email) || !Validador.isEmailValido(email)){
            view.exibirMensagem("Formato de e-mail inválido ou vazio.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!Validador.isCpfValido(cpf)){
            view.exibirMensagem("CPF inválido. Deve conter 11 números.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(senha.length() < 8){
            view.exibirMensagem("A senha deve ter no mínimo 8 caracteres.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!Validador.isDataValida(dataNascStr)){
            view.exibirMensagem("Data de nascimento inválida. Use dd/MM/yyyy.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validador.isTelefoneValido(telefone)) {
            view.exibirMensagem("Formato de telefone inválido.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //3. Montagem do Objeto e Persistência
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataNascimento = LocalDate.parse(dataNascStr, formatter);
            
            Cliente novoCliente = new Cliente(nome, sobrenome, email, cpf, senha, telefone, dataNascimento);
            clienteService.cadastrarCliente(novoCliente);
            
            view.exibirMensagem("Cliente cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            view.fecharTela();

        }catch(RegraNegocioException e){
            view.exibirMensagem(e.getMessage(), "Regra de Negócio", JOptionPane.WARNING_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            view.exibirMensagem("Falha interna no servidor. Tente novamente mais tarde.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cancelarCadastro() {
        view.fecharTela();
    }
}