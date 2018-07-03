package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe abstrai as características básicas de um objeto que pode ser armazenado no banco de dados "BancoDeDadosGeRel".
 * A classe também implementa a interface Serializable para que objeto possa ser gravado no sistema de arquivos do SO.
 * @author Marcos Jose.
 */

import java.io.Serializable;

public class Gravavel implements Serializable {
	private static transient final long serialVersionUID = -4631082646531515126L;
	private String id;
	
	/**
	 * Retorna o identificador do arquivo.
	 * @return um <code>String</code> que representa id.
	 */
	public String obterId() {
		return id;
	}
	
	/**
	 * Defini o identificador do arquivo.
	 * @param id que representa o identificador.
	 */
	public void definirId(String id) {
		this.id = id;
	}
	
	
}
