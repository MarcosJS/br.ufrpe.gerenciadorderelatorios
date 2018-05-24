package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public abstract class Gravavel implements Serializable {
	/**
	 * 
	 */
	private static transient final long serialVersionUID = -4631082646531515126L;
	private String Id;
	
	public abstract String obterId();
	
	public void definirId(String id) {
		Id = id;
	}

	public String obtertId() {
		return Id;
	}
	
}
