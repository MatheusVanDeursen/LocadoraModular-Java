package VIEW;

import MODELS.Cliente;
import PRESENTERS.MenuPresenter;
import VIEW.INTERFACE.MenuInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame implements MenuInterface {
	private MenuPresenter presenter;
	
	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableHistorico;
    private JComboBox<String> ordenacaoCombo;
    private JButton ordenacaoButton;
    private JLabel statusCadastroLabel;

    private String[] colunasTabela = {"Veículo", "Data Retirada", "Status", "Total Estimado"};

    public Menu(Cliente clienteLogado) {
        this.presenter = new MenuPresenter(this, clienteLogado);
        
        setTitle("Menu Principal - Locadora de Veículos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null); 
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel tituloLabel = new JLabel("Bem-vindo(a), " + clienteLogado.getNome() + "!");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloLabel.setBounds(20, 15, 600, 30);
        contentPane.add(tituloLabel);
        
        statusCadastroLabel = new JLabel("Verificando status...");
        statusCadastroLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        statusCadastroLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statusCadastroLabel.setBounds(360, 20, 400, 20);
        contentPane.add(statusCadastroLabel);
        
        JLabel subtituloLabel = new JLabel("Seu Histórico de Locações (Clique duplo para detalhes):");
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtituloLabel.setBounds(20, 50, 400, 20);
        contentPane.add(subtituloLabel);

        JLabel ordenarLabel = new JLabel("Ordenar por:");
        ordenarLabel.setBounds(406, 81, 80, 30);
        contentPane.add(ordenarLabel);

        ordenacaoCombo = new JComboBox<>(new String[]{
            "Data de Retirada", "Data de Devolução", "Status"
        });
        ordenacaoCombo.setBounds(496, 81, 144, 30);
        ordenacaoCombo.setBackground(Color.WHITE); 
        ordenacaoCombo.addActionListener(e -> presenter.reordenarHistorico());
        contentPane.add(ordenacaoCombo);
        
        ordenacaoButton = new JButton("⬇ Decrescente");
        ordenacaoButton.setBounds(650, 80, 120, 30);
        ordenacaoButton.setBackground(Color.WHITE); 
        ordenacaoButton.setFocusPainted(false); 
        ordenacaoButton.addActionListener(e -> presenter.alternarSentidoOrdenacao());
        contentPane.add(ordenacaoButton);

        JScrollPane historicoField = new JScrollPane();
        historicoField.setBounds(20, 125, 745, 345); 
        contentPane.add(historicoField);

        tableHistorico = new JTable();
        tableHistorico.setRowHeight(40); 
        tableHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableHistorico.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tableHistorico.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    presenter.exibirDetalhesLocacao();
                }
            }
        });
        historicoField.setViewportView(tableHistorico);

        JButton alugarButton = new JButton("Nova Locação");
        alugarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        alugarButton.setBackground(new Color(40, 167, 69));
        alugarButton.setForeground(Color.WHITE);
        alugarButton.setBounds(20, 500, 150, 40);
        alugarButton.addActionListener(e -> presenter.iniciarNovaLocacao());
        contentPane.add(alugarButton);
        
        JButton dadosButton = new JButton("Meus Dados");
        dadosButton.setForeground(Color.WHITE);
        dadosButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosButton.setBackground(new Color(240, 180, 0));
        dadosButton.setBounds(190, 500, 150, 40);
        dadosButton.addActionListener(e -> presenter.gerenciarPerfil());
        contentPane.add(dadosButton);
        
        JButton cancelarButton = new JButton("Cancelar Seleção");
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelarButton.setBackground(new Color(108, 117, 125));
        cancelarButton.setBounds(360, 500, 160, 40);
        cancelarButton.addActionListener(e -> presenter.processarCancelamento());
        contentPane.add(cancelarButton);

        JButton sairButton = new JButton("Sair");
        sairButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sairButton.setBackground(new Color(220, 53, 69)); 
        sairButton.setForeground(Color.WHITE);
        sairButton.setBounds(650, 500, 115, 40);
        sairButton.addActionListener(e -> presenter.efetuarLogout());
        contentPane.add(sairButton);

        presenter.inicializar(); 
    }

    // --- IMPLEMENTAÇÃO DA INTERFACE MenuInterface ---

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipoAlerta) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoAlerta);
    }

    @Override
    public void exibirMensagemHtml(String mensagemHtml, String titulo) {
        JOptionPane.showMessageDialog(this, mensagemHtml, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public int exibirConfirmacao(String mensagem, String titulo) {
        Object[] options = {"Sim", "Não"};
        return JOptionPane.showOptionDialog(this, mensagem, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    @Override
    public void atualizarTabela(Object[][] dados) {
        @SuppressWarnings("serial")
        DefaultTableModel model = new DefaultTableModel(dados, colunasTabela) {
            @Override
            public boolean isCellEditable(int row, int column){ return false; }
        };
        tableHistorico.setModel(model);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 1; i < tableHistorico.getColumnCount(); i++){
            tableHistorico.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // --- LÓGICA DE MENSAGEM VAZIA ---
        Container pai = tableHistorico.getParent();
        if (pai instanceof JViewport) {
            JViewport viewport = (JViewport) pai;
            viewport.removeAll();
            
            if (dados.length == 0) {
                JLabel lblVazio = new JLabel("Você ainda não possui nenhuma locação registrada.");
                lblVazio.setForeground(Color.GRAY);
                lblVazio.setHorizontalAlignment(SwingConstants.CENTER);
                lblVazio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                viewport.add(lblVazio);
            } else {
                viewport.add(tableHistorico);
            }
            
            viewport.revalidate();
            viewport.repaint();
        }
    }

    @Override
    public void atualizarStatusCadastro(String texto, Color cor) {
        statusCadastroLabel.setText(texto);
        statusCadastroLabel.setForeground(cor);
    }

    @Override
    public void atualizarTextoBotaoOrdem(String texto) {
        ordenacaoButton.setText(texto);
    }

    @Override
    public int getLinhaSelecionadaTabela() { return tableHistorico.getSelectedRow(); }

    @Override
    public int getCriterioOrdenacao() { return ordenacaoCombo.getSelectedIndex(); }

    @Override
    public void abrirTelaNovaLocacao(Cliente cliente) {
        AlugarVeiculo janela = new AlugarVeiculo(this, cliente);
        janela.setVisible(true);
    }

    @Override
    public void abrirTelaDadosPessoais(Cliente cliente) {
        DadosPessoais dialog = new DadosPessoais(this, cliente);
        dialog.setVisible(true);
    }

    @Override
    public void encerrarAplicacao() {
        System.exit(0);
    }
}