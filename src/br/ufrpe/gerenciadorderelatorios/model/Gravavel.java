package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;

public class Gravavel implements Serializable {
	/**
	 * 
	 */
	private static transient final long serialVersionUID = -4631082646531515126L;
	private String id;
	
	public String obterId() {
		return id;
	}

	public void definirId(String id) {
		this.id = id;
	}
	
	
}
