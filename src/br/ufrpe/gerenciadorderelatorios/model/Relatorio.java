package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public class Relatorio implements Serializable, IGravavel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9063960994820430012L;
	private Linha[] linhas;
	private String id;
	private Estrutura estrutura;
	//private int ordem;
	//private PerfilAnalise perfil;
	
	public Relatorio(Linha[] linhas) {
		this.definirLinhas(linhas);
		/*Criando estrutura padrão de diretórios.(historicos/historico/rel)*/
		Estrutura rel = new Estrutura("rel", null);
		this.definirEstrutura(rel);
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

	@Override
	public String obterIdOriginador() {
		return this.obterId();
	}

	@Override
	public Estrutura obterEstrutura() {
		return estrutura;
	}

	private void definirEstrutura(Estrutura estrutura) {
		this.estrutura = estrutura;
	}
}
