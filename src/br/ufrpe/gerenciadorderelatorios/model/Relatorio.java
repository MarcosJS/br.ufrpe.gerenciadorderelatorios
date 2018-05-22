package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public class Relatorio implements Serializable, IGravavel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9063960994820430012L;
	private Linha[] linhas;
	private String id;
	private String historico;
	//private int ordem;
	//private PerfilAnalise perfil;
	
	public Relatorio(Linha[] linhas) {
		this.definirLinhas(linhas);
	}
	
	public Linha[] obterLinhas() {
		return linhas;
	}

	private void definirLinhas(Linha[] linhas) {
		this.linhas = linhas;
	}
	
	public String obterId() {
		return id;
	}
	public void definirId(String id) {
		this.id = id;
	}

	public int obterQuantLinhas() {
		return linhas.length;
	}

	public String obterHistorico() {
		return historico;
	}

	public void definirHistorico(String historico) {
		this.historico = historico;
	}

	@Override
	public String obterIdOriginador() {
		return this.obterHistorico();
	}
}
