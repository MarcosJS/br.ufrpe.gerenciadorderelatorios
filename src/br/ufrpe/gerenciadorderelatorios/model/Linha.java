package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public class Linha implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4929477468964210438L;
	private String Relatorio;
	private boolean nova;
	private boolean excluida;
	private String linha;
	
	public Linha(String linha, int posicaOriginal, int posicaoPosAnalise) {
		this.definirLinha(linha);
	}
	
	public String obterRelatorio() {
		return Relatorio;
	}
	
	public void definirRelatorio(String idRelatorio) {
		this.Relatorio = idRelatorio;
	}
	
	public boolean eNova() {
		return nova;
	}

	public void definirNova(boolean nova) {
		this.nova = nova;
	}
	
	public boolean eExcluida() {
		return excluida;
	}

	public void definirExcluida(boolean excluida) {
		this.excluida = excluida;
	}

	public String obterLinha() {
		return linha;
	}
	
	public void definirLinha(String linha) {
		this.linha = linha;
	}
}
