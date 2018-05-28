package br.ufrpe.gerenciadorderelatorios.control;

import java.util.ArrayList;

import br.ufrpe.gerenciadorderelatorios.model.Gravavel;
import br.ufrpe.gerenciadorderelatorios.model.Relatorio;


public class HistoricoGeRel extends Gravavel{
	/**
	 * 
	 */
	private static transient int quantidadeHistoricos;
	private static transient final long serialVersionUID = -1498544530598941169L;
	private transient ArrayList<Relatorio> relatorios;
	private transient int quantidadeRelatorios;
		
	//private transient PerfilAnalise perfil;
	
	public HistoricoGeRel(Relatorio relatorio, String id) {
		super.definirId(id);
		this.definirRelatorios(new ArrayList<Relatorio>());
		relatorio.definirHistorico(this.obterId());
		/*Acrescentando id do histórico ao relatório.*/
		String newId = "h"+String.format("%04d", this.obterQuantidadeRelatorios());
		relatorio.definirId(newId);
		relatorio.definirOrdem(1);
		this.adicionarRelatorio(relatorio);
		this.quantidadeRelatorios = this.relatorios.size();
		HistoricoGeRel.definirQuantidadeHistoricos(HistoricoGeRel.obterQuantidadeHistoricos() + 1);
	}
	
	/**Adiciona um relatório ao histórico desde que aquele ainda não exista.
	 * @author Marcos Jose*/
	public void adicionarRelatorio(Relatorio rel) {
		/*Verifica se o a lista de relatorios não é nula e se ainda não existe o relatório.*/
		if((relatorios != null) && (this.ConsultarRelatorio(rel.obterId()) == null)) {
			if(rel.obterOrdem() == 0) {
				rel.definirOrdem(++this.quantidadeRelatorios);
			}
			if(rel.obterHistorico() == null) {
				rel.definirHistorico(this.obterId());
			}
			/*Acrescentando id do histórico ao relatório.*/
			String newId = "h"+String.format("%04d", this.obterQuantidadeRelatorios());
			rel.definirId(newId);
			this.relatorios.add(rel);
		}
	}
	
	private Relatorio ConsultarRelatorio(String Id) {
		Relatorio relatorio = null;
		if(this.relatorios != null) {
			for (Relatorio r : this.relatorios.toArray(new Relatorio[this.relatorios.size()])) {
				if (r.obterId().equals(Id)) {
					relatorio = r;
				}
			} 
		}
		return relatorio;
	}

	public ArrayList<Relatorio> obterRelatorios() {
		return relatorios;
	}
	
	public void definirRelatorios(ArrayList<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}

	@Override
	public String obterId() {
		return super.obterId();
	}

	public int obterQuantidadeRelatorios() {
		return quantidadeRelatorios;
	}

	public static int obterQuantidadeHistoricos() {
		return quantidadeHistoricos;
	}

	public static void definirQuantidadeHistoricos(int quantidadeHistoricos) {
		HistoricoGeRel.quantidadeHistoricos = quantidadeHistoricos;
	}

}
