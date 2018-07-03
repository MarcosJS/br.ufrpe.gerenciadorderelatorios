package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe abstrai uma linha e um relatório ou documento de texto.
 * @author Daniel Bruno.
 */

public class Linha extends Gravavel{
	
	private static final long serialVersionUID = 4929477468964210438L;
	private int posicaoOriginal;
	private String Relatorio;//O atributo relatório só atualizado se o relatório não for temporário
	private String texto;
	
	/**
	 * Construtor Linha.
	 * @param linha que representa o conteúdo de texto.
	 * @param posicaOriginal que representa a ordem em relacao ao texto.
	 */
	public Linha(String texto, int posicaOriginal) {
		this.texto = texto;
		this.posicaoOriginal = posicaOriginal;
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
		return Relatorio;
	}
	
	/**
	 * Defini o relatório.
	 * @param idRelatorio que representa o relatório o qual a linha pertence.
	 */
	public void definirRelatorio(String idRelatorio) {
		this.Relatorio = idRelatorio;
	}
	
	/**
	* Obtêm o texto. 
	* @return uma <code>String</code> que representa o texto de linha.
	*/
	public String obterTexto() {
		return this.texto;
	}
}
