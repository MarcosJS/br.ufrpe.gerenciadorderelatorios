package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe abstrai um relat�rio ou documento de texto.
 * @author Marcos Jose.
 */

public class Relatorio extends Gravavel{
	private static transient  final long serialVersionUID = -9063960994820430012L;
	private Linha[] linhas;
	private String historico;
	
	/**
	 * Construtor Relatorio.
	 * @param linhas[] que representa todas as linhas do relat�rio.
	 */
	public Relatorio(Linha[] linhas) {
		this.definirLinhas(linhas);
	}
	
	/**
	* Obt�m linhas[]. 
	* @return um <code>String[]</code> que representa todas as linhas do relatorio.
	*/	
	public Linha[] obterLinhas() {
		return linhas;
	}
	
	/**
	 * Defini linhas[].
	 * @param linhas[] que representa todas as linhas do relat�rio.
	 */
	private void definirLinhas(Linha[] linhas) {
		this.linhas = linhas;
	}
	
	/**
	 * Obt�m o numero de linhas do relat�rio.
	 * @return um <code>integer</code> que representa a quantidade de linhas do relat�rio.
	 */
	public int obterQuantLinhas() {
		return linhas.length;
	}
	
	/**
	 * Obt�m o id do hist�rico o qual o relat�rio pertence.
	 * @return um <code>integer</code> que representa a quantidade de linhas do relat�rio.
	 */
	public String obterHistorico() {
		return historico;
	}
	
	/**
	 * Defini o id do hist�rico o qual o relat�rio pertence.
	 * @param historico que representa o id do hist�rico.
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
