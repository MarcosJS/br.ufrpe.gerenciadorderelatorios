package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe abstrai uma linha e um relat�rio ou documento de texto.
 * @author Daniel Bruno.
 */

public class Linha extends Gravavel{
	
	private static final long serialVersionUID = 4929477468964210438L;
	private int posicaoOriginal;
	private String Relatorio;//O atributo relat�rio s� atualizado se o relat�rio n�o for tempor�rio
	private String texto;
	
	/**
	 * Construtor Linha.
	 * @param linha que representa o conte�do de texto.
	 * @param posicaOriginal que representa a ordem em relacao ao texto.
	 */
	public Linha(String texto, int posicaOriginal) {
		this.texto = texto;
		this.posicaoOriginal = posicaOriginal;
	}
	
	/**
	* Obt�m posicao da linha. 
	* @return um <code>integer</code> que representa a ordem em relacao ao texto.
	*/	
	public int obterPosicaoOriginal() {
		return posicaoOriginal;
	}
	
	/**
	* Obt�m o relat�rio. 
	* @return uma <code>String</code> que representa o id do relat�rio o qual a linha pertence.
	*/
	public String obterRelatorio() {
		return Relatorio;
	}
	
	/**
	 * Defini o relat�rio.
	 * @param idRelatorio que representa o relat�rio o qual a linha pertence.
	 */
	public void definirRelatorio(String idRelatorio) {
		this.Relatorio = idRelatorio;
	}
	
	/**
	* Obt�m o texto. 
	* @return uma <code>String</code> que representa o texto de linha.
	*/
	public String obterTexto() {
		return this.texto;
	}
}
