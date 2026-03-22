package VIEW;

import MODELS.Cliente;
import PRESENTERS.AlugarVeiculoPresenter;
import VIEW.INTERFACE.AlugarVeiculoInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.List;

@SuppressWarnings("serial")
public class AlugarVeiculo extends JDialog implements AlugarVeiculoInterface {
	private AlugarVeiculoPresenter presenter;
	
    private JPanel contentPane;
    
    //Esquerda: Catálogo
    private JComboBox<String> filialRetiradaCombo;
    private JTable tabelaVeiculos;
    private DefaultTableModel tableModel;
    private JTextField buscaField;
    private JComboBox<String> categoriaCombo;
    private JComboBox<String> cambioCombo;
    private JComboBox<String> ordenacaoCombo;
    
    //Direita: Painel de Locação
    private JPanel locacaoPanel;
    private JLabel carroSelecionadoLabel;
    private JFormattedTextField dataRetiradaField;
    private JFormattedTextField dataDevolucaoField;
    private JComboBox<String> filialDevolucaoCombo;
    private JCheckBox seguroCheckbox;
    private JLabel reciboLabel;
    private JButton confirmarButton;
    private JLabel avisoCadastroLabel;

    public AlugarVeiculo(Frame owner, Cliente clienteLogado) {
        super(owner, true);
        
        setTitle("Nova Locação - Escolha seu Veículo");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1050, 680); 
        setLocationRelativeTo(owner);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // =========================================================
        // COLUNA ESQUERDA
        // =========================================================
        JPanel catalagoPanel = new JPanel();
        catalagoPanel.setLayout(null);
        catalagoPanel.setBounds(10, 10, 640, 620);
        catalagoPanel.setBorder(BorderFactory.createTitledBorder("Catálogo de Veículos (Duplo clique para detalhes)"));
        contentPane.add(catalagoPanel);

        JLabel filialRetiradaLabel = new JLabel("Local de Retirada:");
        filialRetiradaLabel.setBounds(15, 25, 120, 25);
        catalagoPanel.add(filialRetiradaLabel);

        filialRetiradaCombo = new JComboBox<>(); 
        filialRetiradaCombo.setBounds(135, 25, 490, 25);
        filialRetiradaCombo.setBackground(Color.WHITE);
        filialRetiradaCombo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filialRetiradaCombo.addActionListener(e -> presenter.filtrarCatalogo());
        catalagoPanel.add(filialRetiradaCombo);

        JLabel buscaLabel = new JLabel("Pesquisa Livre:");
        buscaLabel.setBounds(15, 60, 180, 20);
        buscaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        catalagoPanel.add(buscaLabel);
        
        buscaField = new JTextField();
        buscaField.setBounds(15, 80, 180, 25);
        buscaField.putClientProperty("JTextField.placeholderText", "Digite a placa, modelo...");
        buscaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { presenter.filtrarCatalogo(); }
        });
        catalagoPanel.add(buscaField);

        JLabel categoriaLabel = new JLabel("Categoria:");
        categoriaLabel.setBounds(205, 60, 130, 20);
        categoriaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        catalagoPanel.add(categoriaLabel);

        categoriaCombo = new JComboBox<>(new String[]{"Todas Categorias", "HATCH", "SEDAN", "SUV", "MOTO", "UTILITARIO"});
        categoriaCombo.setBounds(205, 80, 130, 25);
        categoriaCombo.setBackground(Color.WHITE);
        categoriaCombo.addActionListener(e -> presenter.filtrarCatalogo());
        catalagoPanel.add(categoriaCombo);

        JLabel cambioLabel = new JLabel("Câmbio:");
        cambioLabel.setBounds(345, 60, 130, 20);
        cambioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        catalagoPanel.add(cambioLabel);

        cambioCombo = new JComboBox<>(new String[]{"Qualquer Câmbio", "MANUAL", "AUTOMATICO"});
        cambioCombo.setBounds(345, 80, 130, 25);
        cambioCombo.setBackground(Color.WHITE);
        cambioCombo.addActionListener(e -> presenter.filtrarCatalogo());
        catalagoPanel.add(cambioCombo);

        JLabel ordenacaoLabel = new JLabel("Ordenar por:");
        ordenacaoLabel.setBounds(485, 60, 140, 20);
        ordenacaoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        catalagoPanel.add(ordenacaoLabel);

        ordenacaoCombo = new JComboBox<>(new String[]{"Menor Preço", "Maior Preço"});
        ordenacaoCombo.setBounds(485, 80, 140, 25);
        ordenacaoCombo.setBackground(Color.WHITE);
        ordenacaoCombo.addActionListener(e -> presenter.filtrarCatalogo());
        catalagoPanel.add(ordenacaoCombo);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(15, 115, 610, 490);
        catalagoPanel.add(scrollPane);

        tabelaVeiculos = new JTable() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getRowCount() == 0) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setColor(new Color(150, 0, 0));
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    String text = "Não há veículos disponíveis nesta filial com os filtros selecionados.";
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() / 2) + (fm.getAscent() / 2);
                    g2d.drawString(text, x, y);
                }
            }
        };
        tabelaVeiculos.setRowHeight(45); 
        tabelaVeiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaVeiculos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tabelaVeiculos.getSelectionModel().addListSelectionListener(e -> presenter.selecionarVeiculo());
        tabelaVeiculos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) presenter.exibirFichaTecnica();
            }
        });
        scrollPane.setViewportView(tabelaVeiculos);

        // =========================================================
        // COLUNA DIREITA
        // =========================================================
        locacaoPanel = new JPanel();
        locacaoPanel.setLayout(null);
        locacaoPanel.setBounds(660, 10, 360, 620);
        locacaoPanel.setBorder(BorderFactory.createTitledBorder("Simulação e Fechamento"));
        contentPane.add(locacaoPanel);

        carroSelecionadoLabel = new JLabel("<html><h3 style='color:gray'>Selecione um veículo...</h3></html>");
        carroSelecionadoLabel.setBounds(15, 25, 330, 40);
        locacaoPanel.add(carroSelecionadoLabel);

        try {
            MaskFormatter maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
            
            JLabel dataRetiradaLabel = new JLabel("Data Retirada:");
            dataRetiradaLabel.setBounds(15, 80, 100, 20);
            locacaoPanel.add(dataRetiradaLabel);
            
            dataRetiradaField = new JFormattedTextField(maskData);
            dataRetiradaField.setBounds(15, 100, 150, 30);
            dataRetiradaField.addFocusListener(new FocusAdapter() { public void focusLost(FocusEvent e) { presenter.calcularEstimativaLocacao(); } });
            locacaoPanel.add(dataRetiradaField);
            
            JLabel dataDevolucaoLabel = new JLabel("Data Devolução:");
            dataDevolucaoLabel.setBounds(195, 80, 100, 20);
            locacaoPanel.add(dataDevolucaoLabel);
            
            dataDevolucaoField = new JFormattedTextField(maskData);
            dataDevolucaoField.setBounds(195, 100, 150, 30);
            dataDevolucaoField.addFocusListener(new FocusAdapter() { public void focusLost(FocusEvent e) { presenter.calcularEstimativaLocacao(); } });
            locacaoPanel.add(dataDevolucaoField);
        } catch (ParseException e) { e.printStackTrace(); }

        JLabel filialDevolucaoLabel = new JLabel("Local de Devolução:");
        filialDevolucaoLabel.setBounds(15, 140, 200, 20);
        locacaoPanel.add(filialDevolucaoLabel);

        filialDevolucaoCombo = new JComboBox<>();
        filialDevolucaoCombo.setBounds(15, 160, 330, 30);
        filialDevolucaoCombo.setBackground(Color.WHITE);
        filialDevolucaoCombo.addActionListener(e -> presenter.calcularEstimativaLocacao());
        locacaoPanel.add(filialDevolucaoCombo);

        seguroCheckbox = new JCheckBox("Incluir Seguro Completo (+R$ 30,00/dia)");
        seguroCheckbox.setBounds(15, 200, 330, 25);
        seguroCheckbox.addActionListener(e -> presenter.calcularEstimativaLocacao());
        locacaoPanel.add(seguroCheckbox);

        reciboLabel = new JLabel("<html><i>Preencha as datas para simular...</i></html>");
        reciboLabel.setVerticalAlignment(SwingConstants.TOP);
        reciboLabel.setBounds(15, 240, 330, 280);
        locacaoPanel.add(reciboLabel);

        avisoCadastroLabel = new JLabel("<html><center>⚠️ Cadastro Incompleto ou CNH Vencida.<br>Atualize no menu 'Meus Dados'.</center></html>");
        avisoCadastroLabel.setForeground(Color.RED);
        avisoCadastroLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        avisoCadastroLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avisoCadastroLabel.setBounds(15, 520, 330, 35);
        locacaoPanel.add(avisoCadastroLabel);

        confirmarButton = new JButton("Confirmar Reserva");
        confirmarButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmarButton.setBackground(new Color(40, 167, 69));
        confirmarButton.setForeground(Color.WHITE);
        confirmarButton.setBounds(15, 560, 330, 45);
        confirmarButton.addActionListener(e -> presenter.finalizarReserva());
        locacaoPanel.add(confirmarButton);

        this.presenter = new AlugarVeiculoPresenter(this, clienteLogado);
        presenter.inicializar();
    }

    // --- IMPLEMENTAÇÃO DA INTERFACE IAlugarVeiculoView ---

    @Override public int getFilialRetiradaIndex() { return filialRetiradaCombo.getSelectedIndex(); }
    @Override public String getBuscaRapida() { return buscaField.getText(); }
    @Override public String getCategoriaSelecionada() { return categoriaCombo.getSelectedItem().toString(); }
    @Override public String getCambioSelecionado() { return cambioCombo.getSelectedItem().toString(); }
    @Override public int getOrdenacaoSelecionada() { return ordenacaoCombo.getSelectedIndex(); }
    
    @Override 
    public int getVeiculoSelecionadoId() { 
        int row = tabelaVeiculos.getSelectedRow();
        return (row >= 0) ? (int) tabelaVeiculos.getValueAt(row, 0) : -1;
    }

    @Override public String getDataRetirada() { return dataRetiradaField.getText(); }
    @Override public String getDataDevolucao() { return dataDevolucaoField.getText(); }
    @Override public int getFilialDevolucaoIndex() { return filialDevolucaoCombo.getSelectedIndex(); }
    @Override public boolean isSeguroSelecionado() { return seguroCheckbox.isSelected(); }

    @Override 
    public void popularComboFiliais(List<String> nomesFiliais) {
        filialRetiradaCombo.removeAllItems();
        filialDevolucaoCombo.removeAllItems();
        for (String nome : nomesFiliais) {
            filialRetiradaCombo.addItem(nome);
            filialDevolucaoCombo.addItem(nome);
        }
    }

    @Override public void setFilialRetiradaIndex(int index) { filialRetiradaCombo.setSelectedIndex(index); }
    @Override public void setFilialDevolucaoIndex(int index) { filialDevolucaoCombo.setSelectedIndex(index); }

    @Override 
    public void atualizarTabelaVeiculos(Object[][] dados) {
        String[] colunas = {"ID", "Veículo", "Especificações", "Diária"};
        tableModel = new DefaultTableModel(dados, colunas) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaVeiculos.setModel(tableModel);
        
        // Esconde o ID
        tabelaVeiculos.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaVeiculos.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaVeiculos.getColumnModel().getColumn(0).setWidth(0);
        
        tabelaVeiculos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabelaVeiculos.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaVeiculos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }

    @Override public void atualizarLabelCarroSelecionado(String html) { carroSelecionadoLabel.setText(html); }
    @Override public void atualizarReciboVirtual(String html) { reciboLabel.setText(html); }

    @Override 
    public void travarPainelDireito(boolean destravado, boolean isApto) {
        dataRetiradaField.setEnabled(destravado);
        dataDevolucaoField.setEnabled(destravado);
        filialDevolucaoCombo.setEnabled(destravado);
        seguroCheckbox.setEnabled(destravado);
        
        confirmarButton.setEnabled(destravado && isApto);
        
        if (!destravado) reciboLabel.setText("<html><i>Selecione um veículo na tabela...</i></html>");
    }

    @Override public void exibirAvisoCadastroIncompleto(boolean exibir) { avisoCadastroLabel.setVisible(exibir); }

    @Override public void exibirMensagem(String mensagem, String titulo, int tipoAlerta) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoAlerta);
    }

    @Override public void exibirFichaTecnica(String htmlFicha) {
        JOptionPane.showMessageDialog(this, htmlFicha, "Ficha Técnica", JOptionPane.PLAIN_MESSAGE);
    }

    @Override public void fecharTela() { this.dispose(); }
}