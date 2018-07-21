package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe abstrai uma linha e um relat�rio ou documento de texto.
 * @author Daniel Bruno.
 */

public class Linha extends Gravavel{
	public enum Condicao {
		NOVA, EXCLUIDA, ESTAVEL;
	}
	
	private static final long serialVersionUID = 4929477468964210438L;
	private int posicaoOriginal;
	private String relatorio;//O atributo relat�rio s� atualizado se o relat�rio n�o for tempor�rio
	private String texto;
	private Condicao condicao;
	
	/**
	 * Construtor Linha.
	 * @param texto que representa o conte�do de texto.
	 * @param posicaOriginal que representa a ordem em relacao ao texto.
	 */
	public Linha(String texto, int posicaOriginal) {
		this.texto = texto;
		this.posicaoOriginal = posicaOriginal;
		this.definirCondicao(Condicao.NOVA);
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
		return relatorio;
	}
	
	/**
	 * Defini o relat�rio.
	 * @param idRelatorio que representa o relat�rio o qual a linha pertence.
	 */
	public void definirRelatorio(String idRelatorio) {
		this.relatorio = idRelatorio;
	}
	
	/**
	* Obt�m o texto. 
	* @return uma <code>String</code> que representa o texto de linha.
	*/
	public String obterTexto() {
		return this.texto;
	}

	/**
	* Obt�m a condi��o. 
	* @return uma <code>Condicao</code> que representa a condicao.
	*/
	public Condicao obterCondicao() {
		return condicao;
	}

	/**
	 * Defini a condi��o.
	 * @param condicao que representa a nova condi��o da linha.
	 */
	public void definirCondicao(Condicao condicao) {
		this.condicao = condicao;
	}
}
