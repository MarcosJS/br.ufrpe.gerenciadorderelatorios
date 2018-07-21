package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe gerencia um conjunto indefinido de relatórios.
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
	
	/**Adiciona um relatório ao portifólio desde que aquele ainda não exista.
	 * @param rel que representa o novo relatório.
	 * */
	public void adicionarRelatorio(Relatorio rel) {
		/*Verifica se o relatória não esta na lista.*/
		if(this.consultarRelatorio(rel.obterId()) == null) {
			if(rel.obterHistorico() == null) {
				/*Apontando para o histórico para qual foi adicionado.*/
				rel.definirHistorico(this.obterId());
			}
			
			/*Acrescentando id do histórico ao relatório.*/
			if(rel.obterId() == null) {
				String newId = "r"+String.format("%04d", this.obterQuantidadeRelatorios() + 1);
				rel.definirId(newId);
				this.relatorios.add(rel);
			}
		}
	}
	
	/**
	 * Retorna um relatórico especificado caso exista ou nulo caso contrário.
	 * @param id que indica o id do relatório que deverá ser retornado.
	 * @return um <code>Relatorio</code> que representa o relatório especificado.
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
	 * Retorna um relatório presente no portifólio.
	 * @param indice que indica a posição do relatório que esta contido no portifólio.
	 * @return um <code>Relatorio</code> que representa o relatório contido na posição especificada.
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
	 * @return um ArrayList que representa um lista de relatórios.
	 */
	public ArrayList<Relatorio> obterRelatorios() {
		return relatorios;
	}
	
	/**
	 * Retorna a lista completa de relatorio.
	 * @return um <code>Relatorio[]</code> que representa um lista de relatórios.
	 */
	public Relatorio[] obterListaRelatorios() {
		return this.relatorios.toArray(new Relatorio[relatorios.size()]);
	}
	
	/**
	 * Defini a lista de relatórios do portifólio.
	 * @param relatorios que indica uma lista de relatórios.
	 */
	public void definirRelatorios(ArrayList<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}
	
	/**
	 * Obtêm o numero de relatórios presente no portifólio.
	 * @return um <code>integer</code> que representa a quantidade de relatórios.
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