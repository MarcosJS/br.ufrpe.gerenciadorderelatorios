package br.ufrpe.gerenciadorderelatorios.model;

public class Linha extends Gravavel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4929477468964210438L;
	private int posicaoOriginal;
	private String Relatorio;
	private String texto;
	
	public Linha(String linha, int posicaOriginal, int posicaoPosAnalise) {
		this.definirTexto(linha);
		this.definirPosicaoOriginal(posicaOriginal);
	}
	
	public int obterPosicaoOriginal() {
		return posicaoOriginal;
	}
	
	private void definirPosicaoOriginal(int posicao) {
		this.posicaoOriginal = posicao;
	}
	
	public String obterRelatorio() {
		return Relatorio;
	}
	
	public void definirRelatorio(String idRelatorio) {
		this.Relatorio = idRelatorio;
	}
	
	public String obterTexto() {
		return this.texto;
	}
	
	public void definirTexto(String texto) {
		this.texto = texto;
	}
	
}
