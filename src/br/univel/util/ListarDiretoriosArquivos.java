package br.univel.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.univel.comum.Arquivo;

public class ListarDiretoriosArquivos {
	
	public static List<Arquivo> listarArquivos(File dirStart){
		List<Arquivo> listaArquivos = new ArrayList<>();
		
		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				
				Arquivo arq = new Arquivo();
				arq.setNome(file.getName());
				arq.setTamanho(file.length());
				arq.setPath(file.getParent());
				arq.setDataHoraModificacao(new Date(file.lastModified() * 1000));
				arq.setMd5(Md5Util.getMD5Checksum(file.getPath()));
				arq.setExtensao(file.getName().substring((file.getName().lastIndexOf("."))));
				arq.setNome(arq.getNome().replace(arq.getExtensao(), ""));
				arq.setId(0);
				listaArquivos.add(arq);
			} 
		}		
		
		return listaArquivos;		
	}
}
