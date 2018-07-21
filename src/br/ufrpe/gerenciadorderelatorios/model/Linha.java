package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe abstrai uma linha e um relatório ou documento de texto.
 * @author Daniel Bruno.
 */

public class Linha extends Gravavel{
	public enum Condicao {
		NOVA, EXCLUIDA, ESTAVEL;
	}
	
	private static final long serialVersionUID = 4929477468964210438L;
	private int posicaoOriginal;
	private String relatorio;//O atributo relatório só atualizado se o relatório não for temporário
	private String texto;
	private Condicao condicao;
	
	/**
	 * Construtor Linha.
	 * @param texto que representa o conteúdo de texto.
	 * @param posicaOriginal que representa a ordem em relacao ao texto.
	 */
	public Linha(String texto, int posicaOriginal) {
		this.texto = texto;
		this.posicaoOriginal = posicaOriginal;
		this.definirCondicao(Condicao.NOVA);
	}
	
	/**
	* Obtêm posicao da linha. 
	* @return um <code>integer</code> que representa a ordem em relacao ao texto.
	*/	
	public int obterPosicaoOriginal() {
		return posicaoOriginal;
	}
	
	/**
	* Obtêm o relatório. 
	* @return uma <code>String</code> que representa o id do relatório o qual a linha pertence.
	*/
	public String obterRelatorio() {
		return relatorio;
	}
	
	/**
	 * Defini o relatório.
	 * @param idRelatorio que representa o relatório o qual a linha pertence.
	 */
	public void definirRelatorio(String idRelatorio) {
		this.relatorio = idRelatorio;
	}
	
	/**
	* Obtêm o texto. 
	* @return uma <code>String</code> que representa o texto de linha.
	*/
	public String obterTexto() {
		return this.texto;
	}

	/**
	* Obtêm a condição. 
	* @return uma <code>Condicao</code> que representa a condicao.
	*/
	public Condicao obterCondicao() {
		return condicao;
	}

	/**
	 * Defini a condição.
	 * @param condicao que representa a nova condição da linha.
	 */
	public void definirCondicao(Condicao condicao) {
		this.condicao = condicao;
	}
}
