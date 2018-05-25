package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public abstract class Gravavel implements Serializable {
	/**
	 * 
	 */
	private static transient final long serialVersionUID = -4631082646531515126L;
	private String id;
	
	public abstract String obterId();
	
	public void definirId(String id) {
		this.id = id;
	}

	public String obtertId() {
		return id;
	}
	
}
