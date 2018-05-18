package br.ufrpe.gerenciadorderelatorios.model;

import java.util.Date;

public class Relatorio {
	private Linha[] linhas;
	private String id;
	private int ordem;
	private int quantLinhas;
	private Date data;
	private PerfilAnalise perfil;
	
	public Relatorio(Linha[] linhas) {
		this.setLinhas(linhas);
	}
	
	public Linha[] getLinhas() {
		return linhas;
	}

	private void setLinhas(Linha[] linhas) {
		this.linhas = linhas;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getQuantLinhas() {
		return linhas.length;
	}
}
