package br.univel.jshare.servidor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.univel.jshare.cliente.TelaPrincipal;
import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.util.LeituraEscritaDeArquivos;

public class Servidor implements IServer{
	
	private List<Cliente> listaClientes = new ArrayList<>();
	public Map<Cliente, List<Arquivo>> mapArquivos = new HashMap<>();
	private String ip;
	private Integer porta;
	private TelaPrincipal telaPrincipal;

	public Servidor(TelaPrincipal telaPrincipal, String ip, Integer porta){
		this.telaPrincipal = telaPrincipal;
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

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		listaClientes.add(c);
		telaPrincipal.addLog(c.getNome() + "(" + c.getIp() + " - " + c.getPorta() + ")" + " se conectou ao JShare " + serverToString() + ".");		
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		mapArquivos.put(c, lista);
		
		telaPrincipal.addLog(c.getNome().concat(" publicou sua lista de arquivos."));
		telaPrincipal.addLog("Arquivos:");
		for (Arquivo arq : lista) {
			telaPrincipal.addLog("\t" + arq.getNome().concat(".").concat(arq.getExtensao()));
		}		
		
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		
		Map<Cliente, List<Arquivo>> map = new HashMap<>();	
		query = query.toUpperCase();
		filtro = filtro.toUpperCase();

		for(Cliente c : listaClientes){
						
			List<Arquivo> listaArquivos = new ArrayList<>();
			
			for(Arquivo arq : mapArquivos.get(c)){
												
				if((query.isEmpty()) || (arq.getNome().toUpperCase().contains(query))){
				
					switch(tipoFiltro){
					case EXTENSAO:
						if((filtro.isEmpty()) || (arq.getExtensao().toUpperCase().contains(filtro))){
							listaArquivos.add(arq);
						}
						break;
						
					case TAMANHO_MAX:
						if((filtro.isEmpty()) || (arq.getTamanho() <= (Integer.parseInt(filtro) * 1024))){
							listaArquivos.add(arq);
						}
						break;
					case TAMANHO_MIN:
						if((filtro.isEmpty()) || (arq.getTamanho() >= (Integer.parseInt(filtro) * 1024))){
							listaArquivos.add(arq);
						}
						break;
						
					default:
						listaArquivos.add(arq);				
						break;
					}							
				}
				
			}
			
			if(listaArquivos.size() > 0){				
				map.put(c, listaArquivos);
								
			}			
		}									
		
		return map;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
	
		telaPrincipal.addLog("Cliente " + cli.getNome() + " baixando o arquivo ".concat(arq.getNome()).concat(".").concat(arq.getExtensao()));
		byte[] dados = null;			
		
		LeituraEscritaDeArquivos io = new LeituraEscritaDeArquivos();
		dados = io.leia(new File(arq.getPath().concat("\\").concat(arq.getNome()).concat(".").concat(arq.getExtensao()) ) );
		
		telaPrincipal.addLog("Cliente " + cli.getNome() + " finalizou o download do arquivo ".concat(arq.getNome()).concat(".").concat(arq.getExtensao()));
		return dados;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		listaClientes.remove(c);
		mapArquivos.remove(c);
		telaPrincipal.addLog(c.getNome().concat(" se desconectou do JShare."));
	}	
	
	private void inicializaServerRMI() throws RemoteException{
		telaPrincipal.addLog(serverToString().concat(" iniciando..."));
		System.setProperty("java.rmi.server.hostname", this.ip);
		
		IServer servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);
		
		Registry registry = LocateRegistry.createRegistry(this.porta);
		
		registry.rebind(IServer.NOME_SERVICO, servidor);
		
		telaPrincipal.addLog(serverToString().concat(" iniciado com sucesso."));
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
	
	public TelaPrincipal getTelaPrincipal() {
		return telaPrincipal;
	}

	public void setTelaPrincipal(TelaPrincipal telaPrincipal) {
		this.telaPrincipal = telaPrincipal;
	}	
}
