package PRESENTERS;

import MODELS.Cliente;
import MODELS.Cnh;
import MODELS.Endereco;
import SERVICES.ClienteService;
import SERVICES.CnhService;
import SERVICES.EnderecoService;
import UTILS.Validador;
import VIEW.INTERFACE.DadosPessoaisInterface;
import EXCEPTIONS.RegraNegocioException;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.time.LocalDate;

public class DadosPessoaisPresenter {
    private DadosPessoaisInterface view;
    private Cliente clienteLogado;
    private Endereco enderecoLogado;
    private Cnh cnhLogada;
    
    private ClienteService clienteService;
    private EnderecoService enderecoService;
    private CnhService cnhService;

    private boolean modoEdicao = false;

    public DadosPessoaisPresenter(DadosPessoaisInterface view, Cliente clienteLogado) {
        this.view = view;
        this.clienteLogado = clienteLogado;
        
        this.clienteService = new ClienteService();
        this.enderecoService = new EnderecoService();
        this.cnhService = new CnhService();
    }

    public void inicializar() {
        //Busca os dados complementares
        this.enderecoLogado = enderecoService.buscarEnderecoPorIdCliente(clienteLogado.getId());
        if (this.enderecoLogado == null) this.enderecoLogado = new Endereco();
        
        try{
            this.cnhLogada = cnhService.buscarPorCliente(clienteLogado.getId());
        }catch(Exception e){
            this.cnhLogada = null;
        }

        view.preencherCampos(clienteLogado, enderecoLogado, cnhLogada);
        atualizarIndicadoresStatus();
        view.setModoEdicao(false);
    }

    private void atualizarIndicadoresStatus() {
        //Lógica do Aviso de Endereço
        if(enderecoLogado != null && enderecoLogado.getCep() != null && !enderecoLogado.getCep().isEmpty()){
            view.atualizarAvisoEndereco("Endereço já registrado.", new Color(0, 102, 204));
        }else{
            view.atualizarAvisoEndereco("Preencha o endereço para utilizar os serviços de locação!", new Color(204, 102, 0));
        }

        //Lógica do Aviso da CNH
        if(cnhLogada != null){
            if(cnhLogada.getDataValidade().isBefore(LocalDate.now())){
                view.atualizarAvisoCnh("⚠️ CNH Vencida! Atualize seus dados para alugar.", Color.RED);
            }else{
                view.atualizarAvisoCnh("CNH registrada e dentro da validade.", new Color(0, 102, 204));
            }
        }else{
            view.atualizarAvisoCnh("Preencha a CNH para utilizar os serviços de locação!", new Color(204, 102, 0));
        }
    }

    public void alternarModoEdicao() {
        modoEdicao = !modoEdicao;
        view.setModoEdicao(modoEdicao);
        
        if(!modoEdicao){
            //Se o usuário cancelou a edição, restauramos os dados originais
            view.limparSenhaConfirmacao();
            view.preencherCampos(clienteLogado, enderecoLogado, cnhLogada);
        }
    }

    public void confirmarAtualizacaoDados() {
        String ufLimpa = view.getEstado().replace("_", "").trim();
        String cepLimpo = view.getCep().replace("-", "").replace("_", "").trim();
        
        //1. Validações de Sintaxe
        if(Validador.isVazio(view.getNome()) || Validador.isVazio(view.getSobrenome()) || 
            Validador.isVazio(view.getTelefone()) || cepLimpo.isEmpty() || ufLimpa.isEmpty() ||
            Validador.isVazio(view.getCidade()) || Validador.isVazio(view.getLogradouro()) || 
            Validador.isVazio(view.getBairro()) || Validador.isVazio(view.getNumero())) {
            
            view.exibirMensagem("Por favor, preencha todos os campos obrigatórios (*) do perfil e endereço.", "Aviso de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!Validador.isTelefoneValido(view.getTelefone())) {
            view.exibirMensagem("Formato de telefone inválido.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validador.isCepValido(cepLimpo)) {
        	view.exibirMensagem("CEP inválido. Deve conter 8 números.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        

        if(ufLimpa.length() != 2){
            view.exibirMensagem("O campo UF (Estado) deve conter exatamente 2 letras.", "Aviso de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numRegistroCnh = view.getCnhRegistro().trim();
        String valStr = view.getCnhValidade().replace("_", "").replace("/", "").trim();
        String primHabStr = view.getCnhPrimeiraHab().replace("_", "").replace("/", "").trim();

        if(!numRegistroCnh.isEmpty() && (valStr.length() != 8 || primHabStr.length() != 8)){
            view.exibirMensagem("Preencha as datas da CNH corretamente.", "Aviso de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //2. Autenticação da senha
        String senha = view.getSenhaConfirmacao();
        if(senha.isEmpty()){
            view.exibirMensagem("Digite sua senha atual para confirmar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            clienteService.autenticarCliente(clienteLogado.getEmail(), senha);
        }catch(RegraNegocioException e){
            view.exibirMensagem("Senha incorreta. Alterações canceladas.", "Segurança", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //3. Montagem do Objetos e Persistências
        try{
            //Atualiza Cliente
            clienteLogado.setNome(view.getNome());
            clienteLogado.setSobrenome(view.getSobrenome());
            clienteLogado.setTelefone(view.getTelefone());
            clienteService.atualizarDadosCliente(clienteLogado);

            //Atualiza Endereço
            enderecoLogado.setCep(view.getCep());
            enderecoLogado.setEstado(view.getEstado());
            enderecoLogado.setCidade(view.getCidade());
            enderecoLogado.setBairro(view.getBairro());
            enderecoLogado.setLogradouro(view.getLogradouro());
            enderecoLogado.setNumero(view.getNumero());
            enderecoLogado.setComplemento(view.getComplemento());
            
            if(enderecoLogado.getId() > 0){
                enderecoService.atualizarEndereco(enderecoLogado);
            }else{
                enderecoLogado.setClienteId(clienteLogado.getId());
                enderecoService.salvarEndereco(enderecoLogado);
            }

            //Atualiza/Cria CNH
            if(!numRegistroCnh.isEmpty()){
                //Formatação já sem o underline
                String valFormatada = view.getCnhValidade().replace("_", "");
                String primHabFormatada = view.getCnhPrimeiraHab().replace("_", "");
                
                if(cnhLogada == null){
                    Cnh novaCnh = new Cnh(clienteLogado.getId(), numRegistroCnh, view.getCnhCategoria(), 
                        LocalDate.parse(valFormatada, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                        LocalDate.parse(primHabFormatada, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    cnhService.cadastrarCNH(novaCnh);
                }else{
                    cnhLogada.setNumeroRegistro(numRegistroCnh);
                    cnhLogada.setCategoria(view.getCnhCategoria());
                    cnhLogada.setDataValidade(LocalDate.parse(valFormatada, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    cnhLogada.setDataPrimeiraHabilitacao(LocalDate.parse(primHabFormatada, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    cnhService.atualizarCNH(cnhLogada);
                }
            }

            view.exibirMensagem("Dados atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            //Recarrega tudo e volta ao modo leitura
            inicializar();

        }catch(RegraNegocioException e){
            view.exibirMensagem(e.getMessage(), "Regra de Negócio", JOptionPane.WARNING_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            view.exibirMensagem("Falha técnica ao salvar dados.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}