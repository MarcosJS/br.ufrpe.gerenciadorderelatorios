package br.ufrpe.gerenciadorderelatorios.model;

import java.util.ArrayList;

public class Estrutura extends Gravavel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10992113208536148L;
	private String raiz;
	private ArrayList <Estrutura> subDiretorios;
	
	public Estrutura(String raiz, ArrayList<Estrutura> subDiretorios) {
		this.definirRaiz(raiz);
		this.definirSubDiretorios(subDiretorios);
	}
	
	public void adicionar(Estrutura e) {
		if(this.subDiretorios == null) {
			this.definirSubDiretorios(new ArrayList <Estrutura> ());
		}
		this.subDiretorios.add(e);
	}
	
	public String obterRaiz() {
		return raiz;
	}
	
	public void definirRaiz(String raiz) {
		this.raiz = raiz;
	}
	
	public Estrutura[] obterSubDiretorios() {
		Estrutura[] sub = null;
		if(this.subDiretorios != null) {
			sub = subDiretorios.toArray(new Estrutura[subDiretorios.size()]);
		}
		return sub;
	}
	
	public void definirSubDiretorios(ArrayList<Estrutura> subDiretorios) {
		this.subDiretorios = subDiretorios;
	}

	@Override
	public String obterId() {
		return this.obterRaiz();
	}
}
