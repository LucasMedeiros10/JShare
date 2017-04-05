package br.univel.cliente;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.univel.comum.Arquivo;
import br.univel.comum.Cliente;
import br.univel.comum.IServer;
import br.univel.servidor.Servidor;
import br.univel.util.LeituraEscritaDeArquivos;
import br.univel.util.LerIp;
import br.univel.util.ListarDiretoriosArquivos;

import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import br.univel.comum.TipoFiltro;

public class TelaPrincipal extends JFrame{

		
	private static Servidor servidorLocal;
	private static Servidor servidorRemoto;
	private static Servidor servidorLocal2;
	private JTextField textField;
	private JTextField textField_1;
	private JTable table;
	private JTextField txtServidor;
	private JTextField txtPorta;
	private JTextField txtNome;
	
	
	public TelaPrincipal(){
		this.setTitle("JShare - Compartilhador de Arquivos");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(719, 476);
		
		JPanel jp = new JPanel();
		setContentPane(jp);
		GridBagLayout gbl_jp = new GridBagLayout();
		gbl_jp.columnWidths = new int[]{0, 0, 0, 0};
		gbl_jp.rowHeights = new int[]{53, 0, 0};
		gbl_jp.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_jp.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		jp.setLayout(gbl_jp);
		
		JLabel lblJshareCompartilhador = new JLabel("JShare - Compartilhador de Arquivos");
		lblJshareCompartilhador.setFont(new Font("Verdana", Font.BOLD, 14));
		GridBagConstraints gbc_lblJshareCompartilhador = new GridBagConstraints();
		gbc_lblJshareCompartilhador.insets = new Insets(0, 0, 5, 0);
		gbc_lblJshareCompartilhador.gridx = 2;
		gbc_lblJshareCompartilhador.gridy = 0;
		jp.add(lblJshareCompartilhador, gbc_lblJshareCompartilhador);
		
		JTabbedPane tabsPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabsPane = new GridBagConstraints();
		gbc_tabsPane.gridwidth = 2;
		gbc_tabsPane.fill = GridBagConstraints.BOTH;
		gbc_tabsPane.gridx = 1;
		gbc_tabsPane.gridy = 1;
		jp.add(tabsPane, gbc_tabsPane);
		
		JPanel pnlConfig = new JPanel();
		tabsPane.addTab("Configurações", null, pnlConfig, null);
		GridBagLayout gbl_pnlConfig = new GridBagLayout();
		gbl_pnlConfig.columnWidths = new int[]{125, 0, 91, 0, 153, 95, 115, 0, 0, 0, 211, 0};
		gbl_pnlConfig.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlConfig.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_pnlConfig.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		pnlConfig.setLayout(gbl_pnlConfig);
		
		JRadioButton rbServidor = new JRadioButton("Servidor e Cliente");
		rbServidor.setSelected(true);
		GridBagConstraints gbc_rbServidor = new GridBagConstraints();
		gbc_rbServidor.anchor = GridBagConstraints.NORTH;
		gbc_rbServidor.insets = new Insets(0, 0, 5, 5);
		gbc_rbServidor.gridx = 2;
		gbc_rbServidor.gridy = 2;
		pnlConfig.add(rbServidor, gbc_rbServidor);
		
		JRadioButton rbCliente = new JRadioButton("Cliente");
		GridBagConstraints gbc_rbCliente = new GridBagConstraints();
		gbc_rbCliente.insets = new Insets(0, 0, 5, 5);
		gbc_rbCliente.gridx = 4;
		gbc_rbCliente.gridy = 2;
		pnlConfig.add(rbCliente, gbc_rbCliente);
		
		JLabel lblNewLabel_1 = new JLabel("Servidor");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 3;
		pnlConfig.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		txtServidor = new JTextField();
		GridBagConstraints gbc_txtServidor = new GridBagConstraints();
		gbc_txtServidor.gridwidth = 3;
		gbc_txtServidor.insets = new Insets(0, 0, 5, 5);
		gbc_txtServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtServidor.gridx = 3;
		gbc_txtServidor.gridy = 3;
		pnlConfig.add(txtServidor, gbc_txtServidor);
		txtServidor.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.insets = new Insets(0, 0, 5, 5);
		gbc_btnConectar.gridx = 6;
		gbc_btnConectar.gridy = 3;
		pnlConfig.add(btnConectar, gbc_btnConectar);
		
		JLabel lblNewLabel_2 = new JLabel("Porta");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 4;
		pnlConfig.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		txtPorta = new JTextField();
		GridBagConstraints gbc_txtPorta = new GridBagConstraints();
		gbc_txtPorta.gridwidth = 3;
		gbc_txtPorta.insets = new Insets(0, 0, 5, 5);
		gbc_txtPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPorta.gridx = 3;
		gbc_txtPorta.gridy = 4;
		pnlConfig.add(txtPorta, gbc_txtPorta);
		txtPorta.setColumns(10);
		
		JButton btnDesconectar = new JButton("Desconectar");
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.insets = new Insets(0, 0, 5, 5);
		gbc_btnDesconectar.gridx = 6;
		gbc_btnDesconectar.gridy = 4;
		pnlConfig.add(btnDesconectar, gbc_btnDesconectar);
		
		JLabel lblNewLabel_3 = new JLabel("Seu Nome ");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 5;
		pnlConfig.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		txtNome = new JTextField();
		GridBagConstraints gbc_txtNome = new GridBagConstraints();
		gbc_txtNome.gridwidth = 3;
		gbc_txtNome.insets = new Insets(0, 0, 5, 5);
		gbc_txtNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNome.gridx = 3;
		gbc_txtNome.gridy = 5;
		pnlConfig.add(txtNome, gbc_txtNome);
		txtNome.setColumns(10);
		
		JPanel pnlPesquisa = new JPanel();
		tabsPane.addTab("Pesquisa", null, pnlPesquisa, null);
		tabsPane.setEnabledAt(1, true);
		GridBagLayout gbl_pnlPesquisa = new GridBagLayout();
		gbl_pnlPesquisa.columnWidths = new int[]{0, 0, 0, 0, 0, 447, 216, 0, 0, 73, 0};
		gbl_pnlPesquisa.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlPesquisa.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlPesquisa.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		pnlPesquisa.setLayout(gbl_pnlPesquisa);
		
		JLabel lblNewLabel = new JLabel("Pesquisa");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 2;
		pnlPesquisa.add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 4;
		gbc_textField.gridy = 2;
		pnlPesquisa.add(textField, gbc_textField);
		
		JLabel lblFiltro = new JLabel("Tipo Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiltro.gridx = 2;
		gbc_lblFiltro.gridy = 3;
		pnlPesquisa.add(lblFiltro, gbc_lblFiltro);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(TipoFiltro.values()));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 4;
		gbc_comboBox.gridy = 3;
		pnlPesquisa.add(comboBox, gbc_comboBox);
		
		JLabel lblFilter = new JLabel("Filter");
		GridBagConstraints gbc_lblFilter = new GridBagConstraints();
		gbc_lblFilter.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilter.gridx = 2;
		gbc_lblFilter.gridy = 4;
		pnlPesquisa.add(lblFilter, gbc_lblFilter);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 4;
		gbc_textField_1.gridy = 4;
		pnlPesquisa.add(textField_1, gbc_textField_1);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 8;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		pnlPesquisa.add(scrollPane, gbc_scrollPane);
		
		table = new JTable();
		scrollPane.setRowHeaderView(table);
		
		JPanel pnlTransferencias = new JPanel();
		tabsPane.addTab("Transferências", null, pnlTransferencias, null);
		tabsPane.setEnabledAt(2, true);
		GridBagLayout gbl_pnlTransferencias = new GridBagLayout();
		gbl_pnlTransferencias.columnWidths = new int[]{0, 0};
		gbl_pnlTransferencias.rowHeights = new int[]{0, 0};
		gbl_pnlTransferencias.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pnlTransferencias.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		pnlTransferencias.setLayout(gbl_pnlTransferencias);
		
		JTextArea txtrFhgfhgfhgf = new JTextArea();
		txtrFhgfhgfhgf.setFont(new Font("Courier New", Font.BOLD, 16));
		txtrFhgfhgfhgf.setForeground(new Color(0, 204, 0));
		txtrFhgfhgfhgf.setSelectionColor(new Color(0, 102, 0));
		txtrFhgfhgfhgf.setSelectedTextColor(new Color(0, 204, 51));
		txtrFhgfhgfhgf.setEditable(false);
		txtrFhgfhgfhgf.setBackground(new Color(0, 0, 0));
		GridBagConstraints gbc_txtrFhgfhgfhgf = new GridBagConstraints();
		gbc_txtrFhgfhgfhgf.fill = GridBagConstraints.BOTH;
		gbc_txtrFhgfhgfhgf.gridx = 0;
		gbc_txtrFhgfhgfhgf.gridy = 0;
		pnlTransferencias.add(txtrFhgfhgfhgf, gbc_txtrFhgfhgfhgf);
		

		this.setVisible(true);
		this.setLocationRelativeTo(null);	
	}
	
	
	public static void main(String[] args) {
		TelaPrincipal tela = new TelaPrincipal();
		teste();
	}
	
	
	public static void teste() {
		//servidorRemoto = new Servidor("192.168.106.20", 8080);
		servidorRemoto = new Servidor(LerIp.RetornarIp(), 1818);
		//servidorLocal = new Servidor(LerIp.RetornarIp(), 1819);
		//servidorLocal2 = new Servidor(LerIp.RetornarIp(), 1820);
				
		
		//cliente 1
		Cliente cliente1 = new Cliente();
		cliente1.setId(1);
		cliente1.setNome("Cliente 1");
		cliente1.setIp(LerIp.RetornarIp());
		cliente1.setPorta(1819);
		
		
		//cliente2
		Cliente cliente2 = new Cliente();
		cliente2.setId(2);
		cliente2.setNome("Cliente 2");
		cliente2.setIp(LerIp.RetornarIp());
		cliente2.setPorta(1820);
		
		
		//try {
			//servidorRemoto.servidor.registrarCliente(cliente1);
			//servidorRemoto.servidor.registrarCliente(cliente2);
		//} catch (RemoteException e) {
			//e.printStackTrace();
		//}
		
		List<Arquivo> lista = new ArrayList<>();
		lista = ListarDiretoriosArquivos.listarArquivos(new File("D:\\OneDrive\\TCC"));		
		
		
		//try {
			//servidorRemoto.servidor.publicarListaArquivos(cliente1, lista);
		//} catch (RemoteException e) {
		//	e.printStackTrace();
		//}
		
		
		//lista = ListarDiretoriosArquivos.listarArquivos(new File("Share"));
		//try {
		//	servidorRemoto.servidor.publicarListaArquivos(cliente2, lista);
		//} catch (RemoteException e) {
		//	e.printStackTrace();
		//}		
		
		
		
		Arquivo arq = new Arquivo();
		arq.setExtensao(".txt");
		arq.setId(1);
		arq.setNome("teste");
		arq.setPath("C:\\Users\\Lucas\\workspace-5semestre\\JShare\\Share\\");		
		
		//baixa arquivo
		byte[] dados = null;
		//try {
			//dados =  servidorLocal2.baixarArquivo(cliente1, arq);
		//} catch (RemoteException e) {
		//	e.printStackTrace();
		//}
		
		//LeituraEscritaDeArquivos io = new LeituraEscritaDeArquivos();
		//io.escreva(new File("D:\\" + arq.getNome().concat(arq.getExtensao())), dados);		
		
	}
	
	
	
}
