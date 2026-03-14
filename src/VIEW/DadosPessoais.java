package VIEW;

import MODELS.Cliente;
import MODELS.Cnh;
import MODELS.Endereco;
import PRESENTERS.DadosPessoaisPresenter;
import VIEW.INTERFACE.DadosPessoaisInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class DadosPessoais extends JDialog implements DadosPessoaisInterface {
	private DadosPessoaisPresenter presenter;
	
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    
    private JTextField nomeField, sobrenomeField, cpfField, dataNscField, emailField, telefoneField;
    private JLabel lblAvisoEndereco;
    private JTextField cidadeField, bairroField, logradouroField, numeroField, complementoField;
    private JFormattedTextField cepField, estadoField; 
    private JLabel lblAvisoCnh;
    private JTextField cnhRegistroField;
    private JComboBox<String> cnhCategoriaCombo;
    private JFormattedTextField cnhValidadeField, cnhPrimeiraHabField;
    
    private JButton editarButton;
    private JButton salvarButton;
    private JLabel confSenhaLabel;
    private JPasswordField confSenhaField;

    public DadosPessoais(Frame owner, Cliente clienteLogado) {
        super(owner, true);
        
        setTitle("Dados Pessoais - Locadora Veículos");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 480, 520); 
        setLocationRelativeTo(owner);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel tituloLabel = new JLabel("Meus Dados");
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloLabel.setBounds(0, 10, 464, 30);
        contentPane.add(tituloLabel);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(20, 50, 425, 290); 
        contentPane.add(tabbedPane);

        // --- ABA 1: DADOS PESSOAIS ---
        JPanel panelPessoal = new JPanel();
        panelPessoal.setLayout(null);
        tabbedPane.addTab("Dados Pessoais", null, panelPessoal, "Informações básicas e contato");

        JLabel nomeLabel = new JLabel("Nome *"); nomeLabel.setBounds(15, 20, 180, 14); panelPessoal.add(nomeLabel);
        nomeField = new JTextField(); nomeField.setBounds(15, 40, 180, 25); panelPessoal.add(nomeField);

        JLabel sobrenomeLabel = new JLabel("Sobrenome *"); sobrenomeLabel.setBounds(210, 20, 190, 14); panelPessoal.add(sobrenomeLabel);
        sobrenomeField = new JTextField(); sobrenomeField.setBounds(210, 40, 190, 25); panelPessoal.add(sobrenomeField);

        JLabel cpfLabel = new JLabel("CPF (Bloqueado)"); cpfLabel.setBounds(15, 80, 180, 14); panelPessoal.add(cpfLabel);
        cpfField = new JTextField(); cpfField.setBounds(15, 100, 180, 25); panelPessoal.add(cpfField);

        JLabel dataNscLabel = new JLabel("Data Nasc. (Bloqueado)"); dataNscLabel.setBounds(210, 80, 190, 14); panelPessoal.add(dataNscLabel);
        dataNscField = new JTextField(); dataNscField.setBounds(210, 100, 190, 25); panelPessoal.add(dataNscField);

        JLabel emailLabel = new JLabel("Email (Login - Bloqueado)"); emailLabel.setBounds(15, 140, 385, 14); panelPessoal.add(emailLabel);
        emailField = new JTextField(); emailField.setBounds(15, 160, 385, 25); panelPessoal.add(emailField);

        JLabel telefoneLabel = new JLabel("Telefone *"); telefoneLabel.setBounds(15, 200, 180, 14); panelPessoal.add(telefoneLabel);
        telefoneField = new JTextField(); telefoneField.setBounds(15, 220, 180, 25); panelPessoal.add(telefoneField);

        // --- ABA 2: ENDEREÇO ---
        JPanel panelEndereco = new JPanel();
        panelEndereco.setLayout(null);
        tabbedPane.addTab("Endereço", null, panelEndereco, "Endereço de cobrança");

        lblAvisoEndereco = new JLabel();
        lblAvisoEndereco.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAvisoEndereco.setBounds(15, 10, 350, 20);
        panelEndereco.add(lblAvisoEndereco);

        try{
            MaskFormatter maskCep = new MaskFormatter("#####-###");
            maskCep.setPlaceholderCharacter('_');
            cepField = new JFormattedTextField(maskCep);
            MaskFormatter maskUf = new MaskFormatter("UU");
            maskUf.setPlaceholderCharacter('_');
            estadoField = new JFormattedTextField(maskUf);
        }catch(ParseException e) {
            cepField = new JFormattedTextField();
            estadoField = new JFormattedTextField();
        }

        JLabel cepLabel = new JLabel("CEP *"); cepLabel.setBounds(15, 35, 120, 14); panelEndereco.add(cepLabel);
        cepField.setBounds(15, 55, 120, 25); panelEndereco.add(cepField);
        
        JLabel estadoLabel = new JLabel("UF *"); estadoLabel.setBounds(145, 35, 50, 14); panelEndereco.add(estadoLabel);
        estadoField.setBounds(145, 55, 50, 25); panelEndereco.add(estadoField);

        JLabel cidadeLabel = new JLabel("Cidade *"); cidadeLabel.setBounds(205, 35, 195, 14); panelEndereco.add(cidadeLabel);
        cidadeField = new JTextField(); cidadeField.setBounds(205, 55, 195, 25); panelEndereco.add(cidadeField);

        JLabel logradouroLabel = new JLabel("Logradouro (Rua, Av...) *"); logradouroLabel.setBounds(15, 95, 250, 14); panelEndereco.add(logradouroLabel);
        logradouroField = new JTextField(); logradouroField.setBounds(15, 115, 250, 25); panelEndereco.add(logradouroField);
        
        JLabel bairroLabel = new JLabel("Bairro *"); bairroLabel.setBounds(275, 95, 125, 14); panelEndereco.add(bairroLabel);
        bairroField = new JTextField(); bairroField.setBounds(275, 115, 125, 25); panelEndereco.add(bairroField);

        JLabel numeroLabel = new JLabel("Número *"); numeroLabel.setBounds(15, 155, 80, 14); panelEndereco.add(numeroLabel);
        numeroField = new JTextField(); numeroField.setBounds(15, 175, 80, 25); panelEndereco.add(numeroField);

        JLabel complementoLabel = new JLabel("Complemento"); complementoLabel.setBounds(105, 155, 295, 14); panelEndereco.add(complementoLabel);
        complementoField = new JTextField(); complementoField.setBounds(105, 175, 295, 25); panelEndereco.add(complementoField);

        // --- ABA 3: CNH ---
        JPanel panelCnh = new JPanel();
        panelCnh.setLayout(null);
        tabbedPane.addTab("CNH", null, panelCnh, "Carteira Nacional de Habilitação");

        lblAvisoCnh = new JLabel();
        lblAvisoCnh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAvisoCnh.setBounds(15, 10, 350, 20);
        panelCnh.add(lblAvisoCnh);

        JLabel registroLabel = new JLabel("Nº de Registro *"); registroLabel.setBounds(15, 40, 180, 14); panelCnh.add(registroLabel);
        cnhRegistroField = new JTextField(); cnhRegistroField.setBounds(15, 60, 180, 25); panelCnh.add(cnhRegistroField);

        JLabel categoriaLabel = new JLabel("Categoria *"); categoriaLabel.setBounds(210, 40, 190, 14); panelCnh.add(categoriaLabel);
        cnhCategoriaCombo = new JComboBox<>(new String[]{"B", "AB", "C", "D", "E"});
        cnhCategoriaCombo.setBounds(210, 60, 190, 25); 
        cnhCategoriaCombo.setBackground(Color.WHITE);
        panelCnh.add(cnhCategoriaCombo);

        try{
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            
            JLabel validadeLabel = new JLabel("Validade (dd/MM/yyyy) *"); validadeLabel.setBounds(15, 100, 180, 14); panelCnh.add(validadeLabel);
            cnhValidadeField = new JFormattedTextField(mascaraData); cnhValidadeField.setBounds(15, 120, 180, 25); panelCnh.add(cnhValidadeField);
            
            JLabel primHabLabel = new JLabel("1ª Hab. (dd/MM/yyyy) *"); primHabLabel.setBounds(210, 100, 190, 14); panelCnh.add(primHabLabel);
            cnhPrimeiraHabField = new JFormattedTextField(mascaraData); cnhPrimeiraHabField.setBounds(210, 120, 190, 25); panelCnh.add(cnhPrimeiraHabField);
        }catch(ParseException e) { e.printStackTrace(); }

        // --- ÁREA INFERIOR FIXA ---
        confSenhaLabel = new JLabel("Confirme sua senha para salvar:");
        confSenhaLabel.setForeground(Color.RED);
        confSenhaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confSenhaLabel.setBounds(20, 350, 300, 14);
        confSenhaLabel.setVisible(false);
        contentPane.add(confSenhaLabel);

        confSenhaField = new JPasswordField();
        confSenhaField.setBounds(20, 370, 200, 30);
        confSenhaField.setVisible(false);
        contentPane.add(confSenhaField);

        salvarButton = new JButton("Confirmar Alterações");
        salvarButton.setBackground(new Color(40, 167, 69)); 
        salvarButton.setForeground(Color.WHITE);
        salvarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        salvarButton.setBounds(230, 370, 215, 30); 
        salvarButton.setVisible(false); 
        salvarButton.addActionListener(e -> presenter.confirmarAtualizacaoDados());
        contentPane.add(salvarButton);

        editarButton = new JButton("Editar Dados");
        editarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editarButton.setBounds(130, 420, 200, 40);
        editarButton.addActionListener(e -> presenter.alternarModoEdicao());
        contentPane.add(editarButton);

        this.presenter = new DadosPessoaisPresenter(this, clienteLogado);
        presenter.inicializar();
    }

    // --- IMPLEMENTAÇÃO DA INTERFACE IDadosPessoaisView ---

    @Override public String getNome() { return nomeField.getText(); }
    @Override public String getSobrenome() { return sobrenomeField.getText(); }
    @Override public String getTelefone() { return telefoneField.getText(); }

    @Override public String getCep() { return cepField.getText(); }
    @Override public String getEstado() { return estadoField.getText(); }
    @Override public String getCidade() { return cidadeField.getText(); }
    @Override public String getBairro() { return bairroField.getText(); }
    @Override public String getLogradouro() { return logradouroField.getText(); }
    @Override public String getNumero() { return numeroField.getText(); }
    @Override public String getComplemento() { return complementoField.getText(); }

    @Override public String getCnhRegistro() { return cnhRegistroField.getText(); }
    @Override public String getCnhCategoria() { return cnhCategoriaCombo.getSelectedItem().toString(); }
    @Override public String getCnhValidade() { return cnhValidadeField.getText(); }
    @Override public String getCnhPrimeiraHab() { return cnhPrimeiraHabField.getText(); }

    @Override public String getSenhaConfirmacao() { return new String(confSenhaField.getPassword()); }
    @Override public void limparSenhaConfirmacao() { confSenhaField.setText(""); }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipoAlerta) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoAlerta);
    }

    @Override
    public void fecharTela() {
        this.dispose();
    }

    @Override
    public void setModoEdicao(boolean ativado) {
        //Altera propriedades visuais dependendo do estado
        nomeField.setEditable(ativado);
        sobrenomeField.setEditable(ativado);
        telefoneField.setEditable(ativado);
        
        cepField.setEditable(ativado);
        estadoField.setEditable(ativado);
        cidadeField.setEditable(ativado);
        bairroField.setEditable(ativado);
        logradouroField.setEditable(ativado);
        numeroField.setEditable(ativado);
        complementoField.setEditable(ativado);

        cnhRegistroField.setEditable(ativado);
        cnhCategoriaCombo.setEnabled(ativado);
        cnhValidadeField.setEditable(ativado);
        cnhPrimeiraHabField.setEditable(ativado);

        cpfField.setEditable(false);
        emailField.setEditable(false); 
        dataNscField.setEditable(false); 
        
        Color bgAtivo = Color.WHITE;
        Color bgInativo = new Color(240, 240, 240);
        Color bgBloqueado = new Color(225, 225, 225); 

        nomeField.setBackground(ativado ? bgAtivo : bgInativo);
        sobrenomeField.setBackground(ativado ? bgAtivo : bgInativo);
        telefoneField.setBackground(ativado ? bgAtivo : bgInativo);
        cepField.setBackground(ativado ? bgAtivo : bgInativo);
        estadoField.setBackground(ativado ? bgAtivo : bgInativo);
        cidadeField.setBackground(ativado ? bgAtivo : bgInativo);
        bairroField.setBackground(ativado ? bgAtivo : bgInativo);
        logradouroField.setBackground(ativado ? bgAtivo : bgInativo);
        numeroField.setBackground(ativado ? bgAtivo : bgInativo);
        complementoField.setBackground(ativado ? bgAtivo : bgInativo);
        
        cnhRegistroField.setBackground(ativado ? bgAtivo : bgInativo);
        cnhValidadeField.setBackground(ativado ? bgAtivo : bgInativo);
        cnhPrimeiraHabField.setBackground(ativado ? bgAtivo : bgInativo);
        
        cpfField.setBackground(bgBloqueado);
        emailField.setBackground(bgBloqueado);
        dataNscField.setBackground(bgBloqueado);

        editarButton.setText(ativado ? "Cancelar Edição" : "Editar Dados");
        editarButton.setForeground(ativado ? Color.RED : Color.BLACK);
        
        confSenhaLabel.setVisible(ativado);
        confSenhaField.setVisible(ativado);
        salvarButton.setVisible(ativado);
        
        if (ativado) confSenhaField.requestFocus();
    }

    @Override
    public void preencherCampos(Cliente cliente, Endereco endereco, Cnh cnh) {
        nomeField.setText(cliente.getNome());
        sobrenomeField.setText(cliente.getSobrenome());
        cpfField.setText(cliente.getCpf());
        if (cliente.getDataNascimento() != null) {
            dataNscField.setText(cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        emailField.setText(cliente.getEmail());
        telefoneField.setText(cliente.getTelefone());

        if (endereco != null && endereco.getCep() != null) {
            cepField.setText(endereco.getCep());
            estadoField.setText(endereco.getEstado());
            cidadeField.setText(endereco.getCidade());
            bairroField.setText(endereco.getBairro());
            logradouroField.setText(endereco.getLogradouro());
            numeroField.setText(endereco.getNumero() != null ? endereco.getNumero() : "");
            complementoField.setText(endereco.getComplemento());
        }

        if (cnh != null) {
            cnhRegistroField.setText(cnh.getNumeroRegistro());
            cnhCategoriaCombo.setSelectedItem(cnh.getCategoria());
            cnhValidadeField.setText(cnh.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            cnhPrimeiraHabField.setText(cnh.getDataPrimeiraHabilitacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            cnhRegistroField.setText("");
            cnhValidadeField.setText("");
            cnhPrimeiraHabField.setText("");
        }
    }

    @Override
    public void atualizarAvisoEndereco(String texto, Color cor) {
        lblAvisoEndereco.setText(texto);
        lblAvisoEndereco.setForeground(cor);
    }

    @Override
    public void atualizarAvisoCnh(String texto, Color cor) {
        lblAvisoCnh.setText(texto);
        lblAvisoCnh.setForeground(cor);
    }
}