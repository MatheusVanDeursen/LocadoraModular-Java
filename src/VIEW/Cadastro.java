package VIEW;

import PRESENTERS.CadastroPresenter;
import VIEW.INTERFACE.CadastroInterface;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class Cadastro extends JDialog implements CadastroInterface {
	private CadastroPresenter presenter; 
	
	private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField nomeField;
    private JTextField sobrenomeField;
    private JFormattedTextField cpfField;
    private JFormattedTextField dataNscField;
    private JTextField emailField;
    private JTextField telefoneField;
    private JPasswordField senhaField;

    public Cadastro(Frame owner) {
        super(owner, true);
        this.presenter = new CadastroPresenter(this);
        
        setTitle("Nova conta - Locadora Veículos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 480, 450); 
        setLocationRelativeTo(owner);
        this.setResizable(false);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        //Título
        JLabel tituloLabel = new JLabel("Cadastro de Conta");
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        tituloLabel.setBounds(0, 10, 464, 30);
        contentPane.add(tituloLabel);

        //Nome
        JLabel nomeLabel = new JLabel("Nome");
        nomeLabel.setBounds(30, 60, 190, 14);
        contentPane.add(nomeLabel);
        nomeField = new JTextField();
        nomeField.setBounds(30, 80, 190, 25);
        contentPane.add(nomeField);
        
        //Sobrenome
        JLabel sobrenomeLabel = new JLabel("Sobrenome");
        sobrenomeLabel.setBounds(240, 60, 190, 14);
        contentPane.add(sobrenomeLabel);
        sobrenomeField = new JTextField();
        sobrenomeField.setBounds(240, 80, 190, 25);
        contentPane.add(sobrenomeField);

        //CPF com Máscara
        JLabel cpfLabel = new JLabel("CPF");
        cpfLabel.setBounds(30, 120, 190, 14);
        contentPane.add(cpfLabel);
        try {
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(mascaraCpf);
        } catch (ParseException e) {
            cpfField = new JFormattedTextField();
        }
        cpfField.setBounds(30, 140, 190, 25);
        contentPane.add(cpfField);

        //Data de Nascimento com Máscara
        JLabel dataNscLabel = new JLabel("Data Nasc. (dd/MM/yyyy)");
        dataNscLabel.setBounds(240, 120, 190, 14);
        contentPane.add(dataNscLabel);
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            dataNscField = new JFormattedTextField(mascaraData);
        } catch (ParseException e) {
            dataNscField = new JFormattedTextField();
        }
        dataNscField.setBounds(240, 140, 190, 25);
        contentPane.add(dataNscField);

        //Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(30, 180, 400, 14);
        contentPane.add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(30, 200, 400, 25);
        contentPane.add(emailField);

        //Telefone
        JLabel telefoneLabel = new JLabel("Telefone");
        telefoneLabel.setBounds(30, 240, 190, 14);
        contentPane.add(telefoneLabel);
        telefoneField = new JTextField();
        telefoneField.setBounds(30, 260, 190, 25);
        contentPane.add(telefoneField);
        
        //Senha
        JLabel senhaLabel = new JLabel("Senha (Mín. 8 caracteres)");
        senhaLabel.setBounds(240, 240, 190, 14);
        contentPane.add(senhaLabel);
        senhaField = new JPasswordField();
        senhaField.setBounds(240, 260, 190, 25);
        contentPane.add(senhaField);

        //Botão Cadastrar
        JButton cadastrarButton = new JButton("Criar Cadastro");
        cadastrarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        cadastrarButton.setBackground(new Color(255, 255, 255));
        cadastrarButton.setForeground(new Color(0, 128, 0)); 
        cadastrarButton.setBounds(130, 310, 200, 40);
        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                presenter.registrarNovoCliente();
            }
        });
        contentPane.add(cadastrarButton);
        
        //Botão Cancelar
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(180, 360, 100, 25);
        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                presenter.cancelarCadastro();
            }
        });
        contentPane.add(cancelarButton);
    }

    // --- IMPLEMENTAÇÃO DA INTERFACE CadastroInterface ---

    @Override
    public String getNome() { return nomeField.getText(); }

    @Override
    public String getSobrenome() { return sobrenomeField.getText(); }

    @Override
    public String getCpf() { return cpfField.getText(); }

    @Override
    public String getDataNascimento() { return dataNscField.getText(); }

    @Override
    public String getEmail() { return emailField.getText(); }

    @Override
    public String getTelefone() { return telefoneField.getText(); }

    @Override
    public String getSenha() { return new String(senhaField.getPassword()); }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipoAlerta) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoAlerta);
    }

    @Override
    public void fecharTela() {
        this.dispose();
    }
}