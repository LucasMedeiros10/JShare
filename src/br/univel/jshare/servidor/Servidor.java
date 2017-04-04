package br.univel.jshare.servidor;

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

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.util.LeituraEscritaDeArquivos;

public class Servidor implements IServer{
	
	private List<Cliente> listaClientes = new ArrayList<>();
	private Map<Cliente, List<Arquivo>> mapArquivos = new HashMap<>();

	public Servidor(){
		try {
			inicializaRMI();
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}	
	
	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		listaClientes.add(c);
		System.out.println(c.getNome().concat(" se conectou do JShare."));		
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
	
		System.out.println("Baixando o arquivo ".concat(arq.getNome()).concat(arq.getExtensao()).concat(" do cliente ").concat(cli.getNome()));
		byte[] dados = null;			
		
		Registry registry;
		
		try {
			registry = LocateRegistry.getRegistry(cli.getIp(), cli.getPorta());
		
			IServer servcliente = (IServer)  registry.lookup(IServer.NOME_SERVICO);
			servcliente.registrarCliente(new Cliente());			
			dados = servcliente.baixarArquivo(new Cliente(), arq);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}		
		
		return dados;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		listaClientes.remove(c);
		mapArquivos.remove(c);
		System.out.println(c.getNome().concat(" se desconectou do JShare."));
	}
	
	private void inicializaRMI() throws RemoteException{
		System.out.println("Servidor iniciando...");
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		
		IServer servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);
		
		Registry registry = LocateRegistry.createRegistry(1818);
		
		registry.rebind(IServer.NOME_SERVICO, servidor);
		
		System.out.println("Servidor iniciado com sucesso.");
	}	
	
	public static void main(String[] args) {
		Servidor  server = new Servidor();
	}

}
