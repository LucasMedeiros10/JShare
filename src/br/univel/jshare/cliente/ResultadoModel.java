package br.univel.jshare.cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;

public class ResultadoModel extends AbstractTableModel {

	private Object[][] matrix;

	public ResultadoModel(Map<Cliente, List<Arquivo>> dados) {

		int tempCli = 0;
		for (Entry<Cliente, List<Arquivo>> e : dados.entrySet()) {
			if (e.getValue() != null) {
				tempCli += e.getValue().size();
			}
		}

		matrix = new Object[tempCli][6];
		
		List<Cliente> list = new ArrayList<>(dados.keySet());
		
		list.sort((o1, o2) -> o1.getNome().compareTo(o2.getNome()));
		
		int cont = 0;
		for (Cliente cli : list) {
			for (Arquivo arq : dados.get(cli)) {
				matrix[cont][0] = cli.getNome().concat("(").concat(cli.getIp()).concat(")");
				matrix[cont][1] = arq.getNome().concat(".").concat(arq.getExtensao());
				matrix[cont][2] = arq;
				matrix[cont][3] = cli;
				cont++;
			}
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return matrix.length;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
			case 0:
				return "Cliente"; 
			case 1:
				return "Arquivo"; 
			default:
				return super.getColumnName(column);
		}
	}	
	
	@Override
	public Object getValueAt(int arg0, int arg1) {
		return matrix[arg0][arg1];
	}
	
	public Cliente getCliente(int row){
		return (Cliente) matrix[row][3];
	}
	
	public Arquivo getArquivo(int row){
		return (Arquivo) matrix[row][2];
	}

}
