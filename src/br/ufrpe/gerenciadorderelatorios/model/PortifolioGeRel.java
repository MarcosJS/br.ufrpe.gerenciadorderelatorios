package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe gerencia um conjunto indefinido de relat�rios.
 * @author Marcos Jose.
 */

import java.util.ArrayList;

public class PortifolioGeRel extends Gravavel{
	private static transient final long serialVersionUID = -1498544530598941169L;
	private transient ArrayList<Relatorio> relatorios;
	
	/**
	 * Construtor PortifoliGeRel o atribuito relatorios.
	 */
	public PortifolioGeRel() {
		this.relatorios = new ArrayList<Relatorio>();
	}
	
	/**Adiciona um relat�rio ao portif�lio desde que aquele ainda n�o exista.
	 * @param rel que representa o novo relat�rio.
	 * */
	public void adicionarRelatorio(Relatorio rel) {
		/*Verifica se o relat�ria n�o esta na lista.*/
		if(this.consultarRelatorio(rel.obterId()) == null) {
			if(rel.obterHistorico() == null) {
				/*Apontando para o hist�rico para qual foi adicionado.*/
				rel.definirHistorico(this.obterId());
			}
			
			/*Acrescentando id do hist�rico ao relat�rio.*/
			if(rel.obterId() == null) {
				String newId = "r"+String.format("%04d", this.obterQuantidadeRelatorios() + 1);
				rel.definirId(newId);
				this.relatorios.add(rel);
			}
		}
	}
	
	/**
	 * Retorna um relat�rico especificado caso exista ou nulo caso contr�rio.
	 * @param id que indica o id do relat�rio que dever� ser retornado.
	 * @return um <code>Relatorio</code> que representa o relat�rio especificado.
	 */
	public Relatorio consultarRelatorio(String id) {
		Relatorio relatorio = null;
		if(this.relatorios != null) {
			for (Relatorio r : this.relatorios.toArray(new Relatorio[this.relatorios.size()])) {
				if (r.obterId().equals(id)) {
					relatorio = r;
				}
			} 
		}
		return relatorio;
	}
	
	/**
	 * Retorna um relat�rio presente no portif�lio.
	 * @param indice que indica a posi��o do relat�rio que esta contido no portif�lio.
	 * @return um <code>Relatorio</code> que representa o relat�rio contido na posi��o especificada.
	 */
	public Relatorio consultarRelatorio(int indice) {
		Relatorio relatorio = null;
		if(indice >= 0 && indice < this.obterQuantidadeRelatorios()) {
			relatorio = this.relatorios.get(indice);
		}
		return relatorio;
	}
	
	/**
	 * Retorna a lista completa de relatorio.
	 * @return um ArrayList que representa um lista de relat�rios.
	 */
	public ArrayList<Relatorio> obterRelatorios() {
		return relatorios;
	}
	
	/**
	 * Retorna a lista completa de relatorio.
	 * @return um <code>Relatorio[]</code> que representa um lista de relat�rios.
	 */
	public Relatorio[] obterListaRelatorios() {
		return this.relatorios.toArray(new Relatorio[relatorios.size()]);
	}
	
	/**
	 * Defini a lista de relat�rios do portif�lio.
	 * @param relatorios que indica uma lista de relat�rios.
	 */
	public void definirRelatorios(ArrayList<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}
	
	/**
	 * Obt�m o numero de relat�rios presente no portif�lio.
	 * @return um <code>integer</code> que representa a quantidade de relat�rios.
	 */
	public int obterQuantidadeRelatorios() {
		return this.relatorios.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PortifolioGeRel other = (PortifolioGeRel) obj;
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
	
	@Override
	public String toString() {
		return "PortifolioGeRel [id=" + this.obterId() + "]";
	}
}