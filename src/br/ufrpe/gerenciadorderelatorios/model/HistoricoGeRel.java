package br.ufrpe.gerenciadorderelatorios.model;

import java.util.ArrayList;


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
	
	/**Adiciona um relat�rio ao hist�rico desde que aquele ainda n�o exista.
	 * @author Marcos Jose*/
	public void adicionarRelatorio(Relatorio rel) {
		/*Verifica se o relat�ria n�o esta na lista.*/
		if(this.ConsultarRelatorio(rel.obterId()) == null) {
			if(rel.obterHistorico() == null) {
				/*Apontando para o hist�rico para qual foi adicionado.*/
				rel.definirHistorico(this.obterId());
			}
			
			/*Acrescentando id do hist�rico ao relat�rio.*/
			String newId = "r"+String.format("%04d", this.obterQuantidadeRelatorios() + 1);
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
	
	/*Returna um relatr�rio da lista caso exista.*/
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoGeRel other = (HistoricoGeRel) obj;
		if (this.obterId() == null) {
			if (other.obterId() != null)
				return false;
		} else if (!this.obterId().equals(other.obterId()))
			return false;
		return true;
	}
	
	@Override
	public String obterId() {
		return super.obterId();
	}

	public int obterQuantidadeRelatorios() {
		return this.relatorios.size();
	}
}