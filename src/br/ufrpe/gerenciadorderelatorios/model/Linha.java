package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public class Linha implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4929477468964210438L;
	private int posicaoOriginal;
	private int posicaoPosAnalise;
	private String Relatorio;
	private boolean modificada;
	private String linha;
	private String modificacaoLinha;
	
	public Linha(String linha, int posicaOriginal, int posicaoPosAnalise) {
		this.definirLinha(linha);
		this.definirPosicaoOriginal(posicaOriginal);
		this.definirPosicaoPosAnalise(posicaoPosAnalise);
	}
	
	public int obterPosicaoOriginal() {
		return posicaoOriginal;
	}
	
	private void definirPosicaoOriginal(int posicao) {
		this.posicaoOriginal = posicao;
	}
	
	public int obterPosicaoPosAnalise() {
		return posicaoPosAnalise;
	}
	
	public void definirPosicaoPosAnalise(int posicao) {
		this.posicaoPosAnalise = posicao;
	}
	
	public String obterRelatorio() {
		return Relatorio;
	}
	
	public void definirRelatorio(String idRelatorio) {
		this.Relatorio = idRelatorio;
	}
	
	public boolean isModificada() {
		return modificada;
	}
	
	public void definirModificada(boolean modificada) {
		this.modificada = modificada;
	}
	
	public String obterLinha() {
		return linha;
	}
	
	public void definirLinha(String linha) {
		this.linha = linha;
	}
	
	public String obterModificacaoLinha() {
		return modificacaoLinha;
	}
	
	public void definirModificacaoLinha(String modificacaoLinha) {
		this.modificacaoLinha = modificacaoLinha;
	}
}
