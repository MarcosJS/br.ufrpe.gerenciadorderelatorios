package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe abstrai um relatório ou documento de texto.
 * @author Marcos Jose.
 */

public class Relatorio extends Gravavel{
	private static transient  final long serialVersionUID = -9063960994820430012L;
	private Linha[] linhas;
	private String historico;
	
	/**
	 * Construtor Relatorio.
	 * @param linhas[] que representa todas as linhas do relatório.
	 */
	public Relatorio(Linha[] linhas) {
		this.definirLinhas(linhas);
	}
	
	/**
	* Obtêm linhas[]. 
	* @return um <code>String[]</code> que representa todas as linhas do relatorio.
	*/	
	public Linha[] obterLinhas() {
		return linhas;
	}
	
	/**
	 * Defini linhas[].
	 * @param linhas[] que representa todas as linhas do relatório.
	 */
	private void definirLinhas(Linha[] linhas) {
		this.linhas = linhas;
	}
	
	/**
	 * Obtêm o numero de linhas do relatório.
	 * @return um <code>integer</code> que representa a quantidade de linhas do relatório.
	 */
	public int obterQuantLinhas() {
		return linhas.length;
	}
	
	/**
	 * Obtêm o id do histórico o qual o relatório pertence.
	 * @return um <code>integer</code> que representa a quantidade de linhas do relatório.
	 */
	public String obterHistorico() {
		return historico;
	}
	
	/**
	 * Defini o id do histórico o qual o relatório pertence.
	 * @param historico que representa o id do histórico.
	 */
	public void definirHistorico(String historico) {
		this.historico = historico;
	}

	@Override
	public String obterId() {
		return super.obterId();
	}
	
	@Override
	public void definirId(String id) {
		super.definirId(id);
		for(Linha l: this.linhas) {
			l.definirRelatorio(this.obterId());
		}
	}

	@Override
	public String toString() {
		return "Relatorio [id=" + this.obterId() + ", historico=" + historico + "]";
	}
}
