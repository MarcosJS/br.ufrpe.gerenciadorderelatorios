package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.util.ArrayList;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;

public class Estrutura extends Gravavel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10992113208536148L;
	private String raiz;
	private String nomeArquivo;
	private ArrayList <Estrutura> subDiretorios;
	
	public Estrutura(String raiz, String nomeArquivo, ArrayList<Estrutura> subDiretorios) {
		this.definirRaiz(raiz);
		this.definirNomeArquivo(nomeArquivo);
		this.definirSubDiretorios(subDiretorios);
	}
	
	public static Estrutura montarEstrutura(ArrayList <String> lista, String nomeArquivo) {
		Estrutura resultado = null;
		if(lista.size() == 1) {
			resultado = new Estrutura(lista.get(0), nomeArquivo, null);
		} else {
			ArrayList <Estrutura> temp = new ArrayList <Estrutura> ();
			temp.add(montarEstrutura(new ArrayList <String> (lista.subList(1, lista.size())), nomeArquivo));
			resultado = new Estrutura(lista.get(0), nomeArquivo, temp);
		}
		return resultado;
	}
	
	public void adicionar(Estrutura e) {
		if(this.subDiretorios == null) {
			this.definirSubDiretorios(new ArrayList <Estrutura> ());
		}
		this.subDiretorios.add(e);
	}
	
	public void adicionar(Estrutura e, int indice) {
		if(this.subDiretorios == null) {
			this.definirSubDiretorios(new ArrayList <Estrutura> ());
			this.subDiretorios.add(e);
		} else {
			if(this.subDiretorios.size() > indice) {
				this.subDiretorios.add(indice, e);
			} else {
				this.subDiretorios.add(e);
			}
		}
	}
	
	public void adicionarNaPonta(Estrutura e) {
		if(this.subDiretorios  == null) {
			this.adicionar(e);
		} else {
			this.obterSubDiretorios()[0].adicionarNaPonta(e);
		}
	}
	
	public void remover(Estrutura e) throws ExclusaoDeArquivoOuDiretorioNegadaException, ArquivoOuDiretorioNaoExisteException {
		if(this.subDiretorios != null) {
			int indice = this.subDiretorios.indexOf(e);
			if(indice >= 0) {
				this.subDiretorios.remove(e);
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O elemento não esta presente na lista.");
			}
		} else {
			throw new ExclusaoDeArquivoOuDiretorioNegadaException("Lista de elementos vazia.");
		}
	}
	
	public String obterRaiz() {
		return this.raiz;
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

	public String obterNomeArquivo() {
		return this.nomeArquivo;
	}

	public void definirNomeArquivo(String arquivo) {
		this.nomeArquivo = arquivo;
	}
	
	public String obterCaminho() {
		String resultado = null;
		if(this.subDiretorios == null) {
			resultado = this.raiz;
		} else {
			resultado = this.raiz+File.separatorChar+this.subDiretorios.get(0).obterCaminho();
		}
		return resultado;
	}
}
