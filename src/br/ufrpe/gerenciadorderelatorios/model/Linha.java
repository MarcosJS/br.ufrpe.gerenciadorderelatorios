package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public class Linha implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4929477468964210438L;
	private int posicaoOriginal;
	private int posicaoPosAnalise;
	private String idRelatorio;
	private boolean modificada;
	private String linha;
	private String modificacaoLinha;
	
	public Linha(String linha, int posicaOriginal, int posicaoPosAnalise) {
		this.setLinha(linha);
		this.setPosicaoOriginal(posicaOriginal);
		this.setPosicaoPosAnalise(posicaoPosAnalise);
	}
	
	public int getPosicaoOriginal() {
		return posicaoOriginal;
	}
	
	private void setPosicaoOriginal(int posicao) {
		this.posicaoOriginal = posicao;
	}
	
	public int getPosicaoPosAnalise() {
		return posicaoPosAnalise;
	}
	
	public void setPosicaoPosAnalise(int posicao) {
		this.posicaoPosAnalise = posicao;
	}
	
	public String getIdRelatorio() {
		return idRelatorio;
	}
	
	public void setIdRelatorio(String idRelatorio) {
		this.idRelatorio = idRelatorio;
	}
	
	public boolean isModificada() {
		return modificada;
	}
	
	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}
	
	public String getLinha() {
		return linha;
	}
	
	public void setLinha(String linha) {
		this.linha = linha;
	}
	
	public String getModificacaoLinha() {
		return modificacaoLinha;
	}
	
	public void setModificacaoLinha(String modificacaoLinha) {
		this.modificacaoLinha = modificacaoLinha;
	}
}
