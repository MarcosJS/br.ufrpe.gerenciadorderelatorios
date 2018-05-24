package br.ufrpe.gerenciadorderelatorios.model;

import java.util.ArrayList;


public class Historico extends Gravavel{
	/**
	 * 
	 */
	private static transient  final long serialVersionUID = -1498544530598941169L;
	private transient ArrayList<Relatorio> relatorios;
	
	//private transient PerfilAnalise perfil;
	
	public Historico(Relatorio relatorio, String id) {
		this.definirRelatorios(new ArrayList<Relatorio>());
		this.adicionarRelatorio(relatorio);
		super.definirId(id);
	}
	
	/**Adiciona um relatório ao histórico desde que aquele ainda não exista.
	 * @author Marcos Jose*/
	public void adicionarRelatorio(Relatorio rel) {
		/*Verifica se o a lista de relatorios não é nula e se ainda não existe o relatório.*/
		if((relatorios != null) && (this.ConsultarRelatorio(rel.obterId()) != null)) {
			this.relatorios.add(rel);
		}
	}
	
	private Relatorio ConsultarRelatorio(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int obterQuantRelatorios() {
		return this.obterRelatorios().size();
	}
	
	public ArrayList<Relatorio> obterRelatorios() {
		return relatorios;
	}
	
	public void definirRelatorios(ArrayList<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}

	@Override
	public String obterId() {
		return super.obtertId();
	}
}
