package br.ufrpe.gerenciadorderelatorios.model;

import java.util.ArrayList;

/**
 * Essa classe abstrai uma linha e um relat�rio ou documento de texto.
 * @author Daniel Bruno.
 */

public class Linha extends Gravavel{
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
	
	public int calcNovaPosicao(Linha[] novoRelatorio) {
		int linhasPuladas = 0;
		int indice = 0;
		System.out.println("posicao original: "+this.posicaoOriginal);
		
		for(int i = 0; linhasPuladas < this.posicaoOriginal; i++) {
			if(novoRelatorio[i].condicao.equals(Condicao.ESTAVEL) || novoRelatorio[i].condicao.equals(Condicao.EXCLUIDA)) {
				linhasPuladas++;
				indice = i + 1;
			}
		}
		System.out.println("indice: "+indice);
		return indice;
	}
	
	public int calcNovaPosicao(ArrayList<Linha> array) {
		return this.calcNovaPosicao(array.toArray(new Linha[array.size()]));
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linha other = (Linha) obj;
		if (texto == null) {
			if (other.texto != null)
				return false;
		} else if (!texto.equals(other.texto))
			return false;
		return true;
	}
	
	
}
