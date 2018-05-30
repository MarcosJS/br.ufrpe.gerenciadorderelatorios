package br.ufrpe.gerenciadorderelatorios.control;

import java.util.ArrayList;

import br.ufrpe.gerenciadorderelatorios.model.Gravavel;
import br.ufrpe.gerenciadorderelatorios.model.Relatorio;


public class HistoricoGeRel extends Gravavel{
	/**
	 * 
	 */
	private static transient final long serialVersionUID = -1498544530598941169L;
	private transient ArrayList<Relatorio> relatorios;
	//private transient PerfilAnalise perfil;
	
	public HistoricoGeRel() {
		this.relatorios = new ArrayList<Relatorio>();
	}
	
	/**Adiciona um relatório ao histórico desde que aquele ainda não exista.
	 * @author Marcos Jose*/
	public void adicionarRelatorio(Relatorio rel) {
		/*Verifica se o relatória não esta na lista.*/
		if(this.ConsultarRelatorio(rel.obterId()) == null) {
			if(rel.obterHistorico() == null) {
				/*Apontando para o histórico para qual foi adicionado.*/
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
	
	/*Returna um relatrório da lista caso exista.*/
	public Relatorio obterRelatorio(int indice) {
		Relatorio relatorio = null;
		if(indice >= 0 && indice < this.obterQuantidadeRelatorios()) {
			relatorio = this.relatorios.get(indice);
		}
		return relatorio;
	}

	public ArrayList<Relatorio> obterRelatorios() {
		return relatorios;
	}
	
	public Relatorio[] obterListaRelatorios() {
		return this.relatorios.toArray(new Relatorio[relatorios.size()]);
	}
	
	public void definirRelatorios(ArrayList<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}

	@Override
	public String obterId() {
		return super.obterId();
	}

	public int obterQuantidadeRelatorios() {
		return this.relatorios.size();
	}
}