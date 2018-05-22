package br.ufrpe.gerenciadorderelatorios.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Estrutura {
	private String raiz;
	private ArrayList <Estrutura> subDiretorios;
	
	public Estrutura(String raiz, Estrutura[] subDiretorios) {
		this.definirRaiz(raiz);
		this.definirSubDiretorios(subDiretorios);
	}
	
	public void adicionar(Estrutura e) {
		this.subDiretorios.add(e);
	}
	
	public String obterRaiz() {
		return raiz;
	}
	
	public void definirRaiz(String raiz) {
		this.raiz = raiz;
	}
	
	public Estrutura[] obterSubDiretorios() {
		return subDiretorios.toArray(new Estrutura[subDiretorios.size()]);
	}
	
	public void definirSubDiretorios(Estrutura[] subDiretorios) {
		this.subDiretorios = new ArrayList <Estrutura>(Arrays.asList(subDiretorios));
	}
}
