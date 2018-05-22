package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public abstract class Gravavel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4631082646531515126L;
	private Estrutura estrutura;
	private String Id;
	
	public Estrutura obterEstrutura() {
		return estrutura;
	}
	public void definirEstrutura(Estrutura estrutura) {
		this.estrutura = estrutura;
	}
	public String obterId() {
		return Id;
	}
	public void definirId(String id) {
		Id = id;
	}
	
}
