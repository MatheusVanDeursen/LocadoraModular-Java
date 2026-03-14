package VIEW;

import PRESENTERS.LoginPresenter;
import VIEW.INTERFACE.LoginInterface;
import MODELS.Cliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements LoginInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LoginPresenter presenter;
	
    private JTextField emailFiled;
    private JPasswordField senhaField;
    
    public Login() {
        this.presenter = new LoginPresenter(this);
        initialize();
    }

    private void initialize() {
        this.setTitle("Login - Locadora de Veículos");
        this.setBounds(100, 100, 500, 350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);      
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        JLabel tituloLabel = new JLabel("Locadora de Veículos");
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setBounds(0, 20, 484, 30); 
        this.getContentPane().add(tituloLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        emailLabel.setBounds(50, 70, 46, 14);
        this.getContentPane().add(emailLabel);
        
        emailFiled = new JTextField();
        emailFiled.setBounds(50, 90, 384, 25);
        this.getContentPane().add(emailFiled);
        
        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        senhaLabel.setBounds(50, 130, 46, 14);
        this.getContentPane().add(senhaLabel);
        
        senhaField = new JPasswordField();
        senhaField.setBounds(50, 150, 384, 25);
        this.getContentPane().add(senhaField);

        JButton autenticarButton = new JButton("Entrar");
        autenticarButton.setForeground(new Color(0, 128, 0));
        autenticarButton.setBackground(Color.WHITE);
        autenticarButton.setFont(new Font("Tahoma", Font.BOLD, 16));      
        int larguraBotao = 120;
        int xCentralizado = (484 - larguraBotao) / 2;   
        autenticarButton.setBounds(xCentralizado, 200, larguraBotao, 35);
        
        autenticarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                presenter.autenticarUsuario();
            }
        });
        
        this.getContentPane().add(autenticarButton);
        
        JLabel cadastroLabel = new JLabel("Não possui conta?");
        cadastroLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        cadastroLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cadastroLabel.setBounds(0, 250, 484, 14);
        this.getContentPane().add(cadastroLabel);

        JButton cadastroButton = new JButton("Cadastre-se");
        cadastroButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        int larguraBtnCad = 140;
        int xCad = (484-larguraBtnCad)/2;
        cadastroButton.setBounds(xCad, 270, larguraBtnCad, 25);
        
        cadastroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                presenter.solicitarNovoCadastro();
            }
        });
        
        this.getContentPane().add(cadastroButton);
    }

    // --- IMPLEMENTAÇÃO DA INTERFACE LoginInterface ---
    
    @Override
    public String getEmail() { return emailFiled.getText(); }

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

    @Override
    public void navegarParaMenu(Cliente cliente) {
        Menu menu = new Menu(cliente); 
        menu.setVisible(true);
    }

    @Override
    public void navegarParaCadastro() {
        Cadastro cadastro = new Cadastro(null);
        cadastro.setVisible(true);
    }
}