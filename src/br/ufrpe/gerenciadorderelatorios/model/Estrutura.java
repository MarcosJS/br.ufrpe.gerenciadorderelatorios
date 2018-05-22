package br.ufrpe.gerenciadorderelatorios.model;

public class Estrutura {
	private String raiz;
	private Estrutura[] subDiretorios;
	
	public Estrutura(String raiz, Estrutura[] subDiretorios) {
		this.definirRaiz(raiz);
		this.definirSubDiretorios(subDiretorios);
	}
	
	public String obterRaiz() {
		return raiz;
	}
	
	public void definirRaiz(String raiz) {
		this.raiz = raiz;
	}
	
	public Estrutura[] obterSubDiretorios() {
		return subDiretorios;
	}
	
	public void definirSubDiretorios(Estrutura[] subDiretorios) {
		this.subDiretorios = subDiretorios;
	}
}
