package br.univel.jshare.cliente;

import java.io.File;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.servidor.Servidor;
import br.univel.jshare.util.LeituraEscritaDeArquivos;
import br.univel.jshare.util.LerIp;
import br.univel.jshare.util.ListarDiretoriosArquivos;
import br.univel.jshare.util.Md5Util;

import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelaPrincipal extends JFrame{

		
	private JTextField txtPesquisa;
	private JTextField txtFilter;
	private JTable tblResultados;
	private JTextField txtServidor;
	private JTextField txtPorta;
	private JTextField txtNome;
	private JRadioButton rbCliente;
	private JRadioButton rbServidor;
	private JButton btnConectar;
	private JButton btnDesconectar;
	private Servidor myServer;
	private IServer remoteServer;
	private Cliente myClient;
	private JTabbedPane tabsPane;
	private Registry registry;
	private JTextArea txtLog;
	
	
	public TelaPrincipal() {		
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
		
		tabsPane = new JTabbedPane(JTabbedPane.TOP);
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
		
		rbServidor = new JRadioButton("Servidor e Cliente");
		rbCliente = new JRadioButton("Cliente");
		txtServidor = new JTextField();
		txtPorta = new JTextField();
		txtServidor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres="0987654321.";
				if(!caracteres.contains(e.getKeyChar()+"")){
					e.consume();
				}				
			}
			
		});
		
		rbServidor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(rbServidor.isSelected()){
					rbCliente.setSelected(false);
					txtServidor.setText(LerIp.RetornarIp());
					txtServidor.setEnabled(false);
					txtPorta.setText("1818");
					txtPorta.setEnabled(false);
				}				
			}
		});
		rbServidor.setSelected(true);
		GridBagConstraints gbc_rbServidor = new GridBagConstraints();
		gbc_rbServidor.anchor = GridBagConstraints.NORTH;
		gbc_rbServidor.insets = new Insets(0, 0, 5, 5);
		gbc_rbServidor.gridx = 2;
		gbc_rbServidor.gridy = 2;
		pnlConfig.add(rbServidor, gbc_rbServidor);
		
		rbCliente.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(rbCliente.isSelected()){
					rbServidor.setSelected(false);
					txtServidor.setEnabled(true);
					txtPorta.setEnabled(true);
				}
			}
		});
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
		
		GridBagConstraints gbc_txtServidor = new GridBagConstraints();
		gbc_txtServidor.gridwidth = 3;
		gbc_txtServidor.insets = new Insets(0, 0, 5, 5);
		gbc_txtServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtServidor.gridx = 3;
		gbc_txtServidor.gridy = 3;
		pnlConfig.add(txtServidor, gbc_txtServidor);
		txtServidor.setColumns(10);
		
		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conectar();
			}
		});
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.gridwidth = 2;
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
				
		txtPorta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
                if (!((e.getKeyChar() >= KeyEvent.VK_0 && 
                        e.getKeyChar() <= KeyEvent.VK_9) || 
                       (e.getKeyChar() == KeyEvent.VK_ENTER || 
                        e.getKeyChar() == KeyEvent.VK_SPACE || 
                        e.getKeyChar() == KeyEvent.VK_BACK_SPACE))) {
                     e.consume();
                 }				
			}
		});
		txtPorta.setText("1818");
		GridBagConstraints gbc_txtPorta = new GridBagConstraints();
		gbc_txtPorta.gridwidth = 3;
		gbc_txtPorta.insets = new Insets(0, 0, 5, 5);
		gbc_txtPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPorta.gridx = 3;
		gbc_txtPorta.gridy = 4;
		pnlConfig.add(txtPorta, gbc_txtPorta);
		txtPorta.setColumns(10);
		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				desconectar();
			}
		});
		btnDesconectar.setEnabled(false);
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.gridwidth = 2;
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
		txtNome.setText("LUCAS MEDEIROS");
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
		gbl_pnlPesquisa.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlPesquisa.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlPesquisa.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlPesquisa.setLayout(gbl_pnlPesquisa);
		
		JLabel lblNewLabel = new JLabel("Pesquisa");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 2;
		pnlPesquisa.add(lblNewLabel, gbc_lblNewLabel);
		
		txtPesquisa = new JTextField();
		txtPesquisa.setColumns(10);
		GridBagConstraints gbc_txtPesquisa = new GridBagConstraints();
		gbc_txtPesquisa.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPesquisa.gridwidth = 2;
		gbc_txtPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_txtPesquisa.gridx = 4;
		gbc_txtPesquisa.gridy = 2;
		pnlPesquisa.add(txtPesquisa, gbc_txtPesquisa);
		
		JLabel lblFiltro = new JLabel("Tipo Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiltro.gridx = 2;
		gbc_lblFiltro.gridy = 3;
		pnlPesquisa.add(lblFiltro, gbc_lblFiltro);
		
		JComboBox cbbFiltro = new JComboBox();
		cbbFiltro.setModel(new DefaultComboBoxModel(TipoFiltro.values()));
		GridBagConstraints gbc_cbbFiltro = new GridBagConstraints();
		gbc_cbbFiltro.gridwidth = 2;
		gbc_cbbFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_cbbFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbFiltro.gridx = 4;
		gbc_cbbFiltro.gridy = 3;
		pnlPesquisa.add(cbbFiltro, gbc_cbbFiltro);
		
		JLabel lblFilter = new JLabel("Filter");
		GridBagConstraints gbc_lblFilter = new GridBagConstraints();
		gbc_lblFilter.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilter.gridx = 2;
		gbc_lblFilter.gridy = 4;
		pnlPesquisa.add(lblFilter, gbc_lblFilter);
		
		txtFilter = new JTextField();
		txtFilter.setColumns(10);
		GridBagConstraints gbc_txtFilter = new GridBagConstraints();
		gbc_txtFilter.gridwidth = 2;
		gbc_txtFilter.insets = new Insets(0, 0, 5, 5);
		gbc_txtFilter.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFilter.gridx = 4;
		gbc_txtFilter.gridy = 4;
		pnlPesquisa.add(txtFilter, gbc_txtFilter);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Map<Cliente, List<Arquivo>> arquivos = new HashMap<>();										
				
				TipoFiltro tipo =  (TipoFiltro) cbbFiltro.getSelectedItem();
				
				try {
					if(rbServidor.isSelected()){
						arquivos = myServer.procurarArquivo(txtPesquisa.getText(), tipo, txtFilter.getText());
					}else{
						arquivos = remoteServer.procurarArquivo(txtPesquisa.getText(), tipo, txtFilter.getText());						
					}
			
					montarConsulta(arquivos);
					
				} catch (Exception e) {
					e.printStackTrace();
				} 				
			}
		});
		GridBagConstraints gbc_btnPesquisar = new GridBagConstraints();
		gbc_btnPesquisar.insets = new Insets(0, 0, 5, 5);
		gbc_btnPesquisar.gridx = 6;
		gbc_btnPesquisar.gridy = 4;
		pnlPesquisa.add(btnPesquisar, gbc_btnPesquisar);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 8;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		pnlPesquisa.add(scrollPane, gbc_scrollPane);
		
		tblResultados = new JTable();
		scrollPane.setViewportView(tblResultados);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tblResultados.getRowCount() == 0){
					JOptionPane.showMessageDialog(null, "Nenhum registro a ser alterado.", "Aviso", JOptionPane.WARNING_MESSAGE);									
				}else{										
					if(tblResultados.getSelectedRow() == -1){						
						JOptionPane.showMessageDialog(null, "Selecione um registro para ser alterado.", "Aviso", JOptionPane.WARNING_MESSAGE);					
					}else{			
						download();			
					}
				}
			}
		});
		btnDownload.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.anchor = GridBagConstraints.EAST;
		gbc_btnDownload.insets = new Insets(0, 0, 5, 5);
		gbc_btnDownload.gridx = 6;
		gbc_btnDownload.gridy = 6;
		pnlPesquisa.add(btnDownload, gbc_btnDownload);
		
		JPanel pnlTransferencias = new JPanel();
		tabsPane.addTab("Transferências", null, pnlTransferencias, null);
		tabsPane.setEnabledAt(2, true);
		GridBagLayout gbl_pnlTransferencias = new GridBagLayout();
		gbl_pnlTransferencias.columnWidths = new int[]{0, 0};
		gbl_pnlTransferencias.rowHeights = new int[]{0, 0, 0};
		gbl_pnlTransferencias.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pnlTransferencias.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		pnlTransferencias.setLayout(gbl_pnlTransferencias);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		pnlTransferencias.add(scrollPane_1, gbc_scrollPane_1);
		
		txtLog = new JTextArea();
		txtLog.setSelectionColor(new Color(0, 102, 0));
		txtLog.setSelectedTextColor(new Color(0, 204, 51));
		txtLog.setForeground(new Color(0, 204, 0));
		txtLog.setFont(new Font("Courier New", Font.BOLD, 12));
		txtLog.setEditable(false);
		txtLog.setBackground(Color.BLACK);
		scrollPane_1.setViewportView(txtLog);
		

		this.setVisible(true);
		this.setLocationRelativeTo(null);	
	}
	
	public void addLog(String log){
		txtLog.append(log);
		txtLog.append("\n");
	}
	
	private TelaPrincipal getTelaPrincipal(){
		return this;
	}
	
	private void inicializaClientRMI() throws RemoteException{		
		try {
			registry = LocateRegistry.getRegistry(txtServidor.getText(), Integer.parseInt(txtPorta.getText()));		
			remoteServer = (IServer)  registry.lookup(IServer.NOME_SERVICO);		
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}		
	}	
	
	private void conectar(){
		myClient = new Cliente();
		myClient.setId(1);
		myClient.setIp(LerIp.RetornarIp());
		myClient.setNome(txtNome.getText());
		myClient.setPorta(1818);
		
		myServer = new Servidor(getTelaPrincipal(), myClient.getIp(), myClient.getPorta());
		
		if(rbCliente.isSelected()) {
			try {
				inicializaClientRMI();
				remoteServer.registrarCliente(myClient);				
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else{
			try {
				myServer.registrarCliente(myClient);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		

		btnConectar.setEnabled(false);
		btnDesconectar.setEnabled(true);	
		rbServidor.setEnabled(false);
		rbCliente.setEnabled(false);
		
		Thread threadArquivos = new Thread() {
			
			@Override
			public void run() {
				try {
					while(!btnConectar.isEnabled()){
						List<Arquivo> lista = ListarDiretoriosArquivos.listarArquivos(new File("Share"));								
						if(rbServidor.isSelected()){
							myServer.publicarListaArquivos(myClient, lista);
						}else{
							remoteServer.publicarListaArquivos(myClient, lista);						
						}  
						
						try {
							currentThread().sleep(30000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
			}
		};

		threadArquivos.start();
		tabsPane.setSelectedIndex(2);		
		
	}
	
	private void desconectar(){
		addLog("Desconectando do servidor...");

		//desconecta do servidor remoto
		if (remoteServer != null) {
			try {
				remoteServer.desconectar(myClient);			
				UnicastRemoteObject.unexportObject(remoteServer, true);
			} catch (NoSuchObjectException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			registry = null;
			remoteServer = null;
			
		}
				
		try {
			UnicastRemoteObject.unexportObject(myServer, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
		myServer = null;
		
		myClient = null;
		
		btnConectar.setEnabled(true);
		btnDesconectar.setEnabled(false);			
		rbServidor.setEnabled(true);
		rbCliente.setEnabled(true);
		
		addLog("Servidor desconectado.");
	}
	
	public void montarConsulta(Map<Cliente, List<Arquivo>> mapArquivos){
		
		ResultadoModel modelo = new ResultadoModel(mapArquivos);		
		tblResultados.setRowSorter(null);		
		tblResultados.setModel(modelo);		
	}	
	
	private void download(){
		byte[] dados = null;
		
		Cliente cliente = ((ResultadoModel) tblResultados.getModel()).getCliente(tblResultados.getSelectedRow());
		Arquivo arquivo = ((ResultadoModel) tblResultados.getModel()).getArquivo(tblResultados.getSelectedRow());
		File arquivoBaixado = new File("Share\\Copia de " + arquivo.getNome().concat(".").concat(arquivo.getExtensao())); 
		IServer fileServer = null;
		String md5Servidor = arquivo.getMd5();
		String md5local = "";

		addLog("Iniciando o download do arquivo " + arquivo.getNome() + " do cliente " + cliente.getNome());
		try {
			Registry registry = LocateRegistry.getRegistry(cliente.getIp(), cliente.getPorta());		
			
			fileServer = (IServer)  registry.lookup(IServer.NOME_SERVICO);
			dados = fileServer.baixarArquivo(myClient, arquivo);
			
			LeituraEscritaDeArquivos io = new LeituraEscritaDeArquivos();			
			io.escreva(arquivoBaixado, dados);			
			
			tabsPane.setSelectedIndex(2);			
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}				
		
		md5local = Md5Util.getMD5Checksum(arquivoBaixado.getPath());
		
		if(arquivoBaixado.exists()){
			if(md5local.equals(md5Servidor)){
				addLog("Arquivo com integridade OK");
				addLog("Download realizado com sucesso.");
			}else{
				addLog("Arquivo corrompido.");
				addLog("MD5 local = ".concat(md5local));
				addLog("MD5 servidor = ".concat(md5Servidor));
			}
		}else{
			addLog("Não foi possível baixar o arquivo");
		}
		
	}
	
	
	
	public static void main(String[] args) {
		TelaPrincipal tela = new TelaPrincipal();
	}
	
}