package br.univel.servidor;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.univel.comum.Arquivo;
import br.univel.comum.Cliente;
import br.univel.comum.IServer;
import br.univel.comum.TipoFiltro;
import br.univel.util.LeituraEscritaDeArquivos;

public class Servidor implements IServer{
	
	private List<Cliente> listaClientes = new ArrayList<>();
	private Map<Cliente, List<Arquivo>> mapArquivos = new HashMap<>();
	private String ip;
	private Integer porta;
	public IServer servidor;

	public Servidor(String ip, Integer porta){
		
		this.ip 	= ip;
		this.porta  = porta;
		try {
			inicializaServerRMI();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}	
	
	public String serverToString(){
		return "Servidor (" + this.ip + " - " + this.porta.toString() + ")";
	}
	
	private void inicializaClientRMI() throws RemoteException{
		Registry registry;
		
		try {
			registry = LocateRegistry.getRegistry(this.ip, this.porta);		
			servidor = (IServer)  registry.lookup(IServer.NOME_SERVICO);
		
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		listaClientes.add(c);
		System.out.println(c.getNome() + "(" + c.getIp() + " - " + c.getPorta() + ")" + " se conectou do JShare " + serverToString() + ".");		
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		mapArquivos.put(c, lista);
		
		System.out.println(c.getNome().concat(" publicou sua lista de arquivos."));
		System.out.println("Arquivos:");
		for (Arquivo arq : lista) {
			System.out.println("\t" + arq.getNome().concat(arq.getExtensao()));
		}		
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		return null;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
	
		System.out.println(serverToString() + " - Cliente " + cli.getNome() + " baixando o arquivo ".concat(arq.getNome()).concat(arq.getExtensao()));
		byte[] dados = null;			
		
		LeituraEscritaDeArquivos io = new LeituraEscritaDeArquivos();
		dados = io.leia(new File(arq.getPath()+ arq.getNome() + arq.getExtensao()));
		
		return dados;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		listaClientes.remove(c);
		mapArquivos.remove(c);
		System.out.println(c.getNome().concat(" se desconectou do JShare."));
	}	
	
	private void inicializaServerRMI() throws RemoteException{
		System.out.println(serverToString().concat(" iniciando..."));
		System.setProperty("java.rmi.server.hostname", this.ip);
		
		IServer servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);
		
		Registry registry = LocateRegistry.createRegistry(this.porta);
		
		registry.rebind(IServer.NOME_SERVICO, servidor);
		
		System.out.println(serverToString().concat(" iniciado com sucesso."));
	}

	public Integer getPorta() {
		return porta;
	}

	public void setPorta(Integer porta) {
		this.porta = porta;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}	
}
