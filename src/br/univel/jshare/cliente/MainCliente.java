package br.univel.jshare.cliente;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.javafx.charts.Legend.LegendItem;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.util.LeituraEscritaDeArquivos;
import br.univel.jshare.util.LerIp;
import br.univel.jshare.util.ListarDiretoriosArquivos;

public class MainCliente implements IServer{
	
	private IServer servidor = null;

	public MainCliente(){
		
		try {
			inicializaRMI();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		//cliente 1
		Cliente cliente1 = new Cliente();
		cliente1.setId(1);
		cliente1.setNome("Cliente 1");
		cliente1.setIp(LerIp.RetornarIp());
		cliente1.setPorta(1819);
		
		registraCliente(cliente1);		
		
		List<Arquivo> lista = new ArrayList<>();
		lista = ListarDiretoriosArquivos.listarArquivos(new File("Share"));
		Arquivo arq = new Arquivo();
		arq.setExtensao(".txt");
		arq.setId(1);
		arq.setNome("teste");
		arq.setPath("C:\\Users\\Lucas\\workspace-5semestre\\JShare\\Share\\");
		
		try {
			servidor.publicarListaArquivos(cliente1, lista);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		
		//cliente2
		Cliente cliente2 = new Cliente();
		cliente2.setId(2);
		cliente2.setNome("Cliente 2");
		cliente2.setIp(LerIp.RetornarIp());
		cliente2.setPorta(1819);
				
		registraCliente(cliente2);
		lista = ListarDiretoriosArquivos.listarArquivos(new File("D:\\OneDrive\\TCC"));		
		try {
			servidor.publicarListaArquivos(cliente2, lista);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		//baixa arquivo
		byte[] dados = null;
		try {
			dados =  servidor.baixarArquivo(cliente1, arq);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		LeituraEscritaDeArquivos io = new LeituraEscritaDeArquivos();
		io.escreva(new File("D:\\OneDrive\\" + arq.getNome().concat(arq.getExtensao())), dados);
	}

	private void registraCliente(Cliente cliente){
		Registry registry;
		
		try {
			System.out.println("Registrando ".concat(cliente.getNome()).concat(" no servidor."));
			registry = LocateRegistry.getRegistry("127.0.0.1", 1818);
		
			servidor = (IServer)  registry.lookup(IServer.NOME_SERVICO);
			servidor.registrarCliente(cliente);
			System.out.println(cliente.getNome().concat(" registrado no servidor."));			
		
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			System.out.println("Falha ao registrar ".concat(cliente.getNome()).concat(" no servidor."));
		}
				
	}
	
	public static void main(String[] args) {
		MainCliente main = new MainCliente();
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		return null;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		LeituraEscritaDeArquivos io = new LeituraEscritaDeArquivos();
		byte[] dados = null;
		
		
		dados = io.leia(new File(arq.getPath() + arq.getNome() + arq.getExtensao()));		
		return dados;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		
	}
	
	private void inicializaRMI() throws RemoteException{
		System.setProperty("java.rmi.server.hostname", LerIp.RetornarIp());		
		IServer servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);		
		Registry registry = LocateRegistry.createRegistry(1819);		
		registry.rebind(IServer.NOME_SERVICO, servidor);
	}		
}
