package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe é reponsavel por definir a hierarquia de pastas e servir como indice.
 * @author Marcos Jose.
 */

import java.io.File;
import java.util.ArrayList;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;

public class Estrutura extends Gravavel {
	private static final long serialVersionUID = 10992113208536148L;
	
	private Estrutura pai;
	private String diretorioAtual;
	private String nomeArquivo;
	private ArrayList <Estrutura> subDiretorios;
	private boolean indexavel;
	
	/**
	 * Construtor Estrutura cria uma nova estrutura que pode representar um diretório e ou arquivo e pode ser indexável
	 * ou não.
	 * @param pai que representa o diretório imediatamente superior.
	 * @param diretorioAtual que representa o diretório atual.
	 * @param nomeArquivo que representa o nome do arquivo.
	 * @param subDiretorios que representa um conjunto de sub diretórios.
	 * */
	public Estrutura(Estrutura pai, String diretorioAtual, String nomeArquivo, ArrayList<Estrutura> subDiretorios) {
		this.definirPai(pai);
		this.definirDiretorioAtual(diretorioAtual);
		this.definirNomeArquivo(nomeArquivo);
		this.definirSubDiretorios(subDiretorios);
		this.definirIndexavel(true);
	}
	
	/**
	 * Retorna a estrutura que representa o diretório de nível mais alto na hierarquia.
	 * @return <code>Estrutura</code> que representa um diretório.
	 * */
	public Estrutura obterRaiz() {
		Estrutura raiz = null;
		
		if(this.obterPai() == null) {
			raiz = this;
		} else {
			raiz = this.obterPai().obterRaiz();
		}
		
		return raiz;
	}
	
	/**
	 * Retorna uma nova estrutura apartir de uma lista nomes de diretórios ordenados por nível e do nome do arquivo.
	 * @param lista que representa a hierarquia de diretórios.
	 * @param nomeArquivo que representa o nome do arquivo.
	 * @return <code>Estrutura</code> que representa a nova estrutura.
	 * */
	public static Estrutura montarEstrutura(ArrayList <String> lista, String nomeArquivo) {
		Estrutura resultado = null;
		if(lista.size() == 1) {
			resultado = new Estrutura(null, lista.get(0), nomeArquivo, null);
		} else {
			resultado = new Estrutura(null, lista.get(0), nomeArquivo, null);
			resultado.adicionar(montarEstrutura(new ArrayList <String> (lista.subList(1, lista.size())), nomeArquivo));
			resultado.obterListaSubDiretorios()[0].definirPai(resultado);
		}
		return resultado;
	}
	
	/**
	 * Retorna o caminho formatado a partir do diretório raiz até o diretório atual representado pela estrutura.
	 * @return <code>String</code> que representa o caminho do arquivo.
	 * */
	public String obterCaminhoAncestrais() {
		String resultado = null;
		if(this.pai == null) {
			resultado = this.diretorioAtual;
		} else {
			resultado = this.pai.obterCaminhoAncestrais()+File.separator+this.obterDiretorioAtual();
		}
		return resultado;
	}
	
	/**
	 * Retorna o caminho formatado a partir do diretório atual representado pela estrutura até o primeiro descendente
	 * do ultimo nível.
	 * @return <code>String</code> que representa o caminho do arquivo.
	 * */
	public String obterCaminhoDescendentes() {
		String resultado = null;
		if(this.subDiretorios == null) {
			resultado = this.diretorioAtual;
			//System.out.println("\t- caminho obtido: "+this.diretorioAtual);
		} else {
			resultado = this.diretorioAtual+File.separator+this.subDiretorios.get(0).obterCaminhoDescendentes();
			//System.out.println("\t- caminho obtido: "+this.diretorioAtual);
		}
		return resultado;
	}
	
	/**
	 * Adiciona uma nova estrutura que represeta um subdiretório.
	 * @param e que representa um novo diretório.
	 * */
	public void adicionar(Estrutura e) {
		if(this.subDiretorios == null) {
			this.definirSubDiretorios(new ArrayList <Estrutura> ());
		}
		this.subDiretorios.add(e);
	}
	/**
	 * Adiciona uma nova estrutura que represeta um subdiretório em uma determinada posicão.
	 * @param e que representa um novo diretório.
	 * @param indice que representa a posição da estrutura.
	 * */
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
	
	/**
	 * Cria mais um nível a partir do diretório raiz.
	 * @param e que representa um novo diretório.
	 * */
	public void adicionarNivel(Estrutura e) {
		if(this.subDiretorios == null) {
			this.adicionar(e);
		} else {
			this.obterListaSubDiretorios()[0].adicionarNivel(e);
		}
	}
	
	/**
	 * Remove uma estrutura que representa um subdiretório caso ele exista.
	 * @param e que representa um novo diretório.
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException caso o usuário não tenha permissão para alterar a estrutura de arquivos.
	 * @throws ArquivoOuDiretorioNaoExisteException caso um arquivo ou um dos diretórios do caminho não exista.
	 * */
	public void remover(Estrutura e) throws ExclusaoDeArquivoOuDiretorioNegadaException, ArquivoOuDiretorioNaoExisteException {
		if(this.subDiretorios != null) {
			if(!this.subDiretorios.remove(e)) {
				throw new ArquivoOuDiretorioNaoExisteException("O elemento não esta presente na lista.");
			}
			
		} else {
			throw new ExclusaoDeArquivoOuDiretorioNegadaException("Lista de elementos vazia.");
		}
	}
	
	/**
	 * Remove uma estrutura que representa um subdiretório de uma determinada posicao.
	 * @param indice que representa a posição da estrutura.
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException caso o usuário não tenha permissão para alterar a estrutura de arquivos.
	 * @throws ArquivoOuDiretorioNaoExisteException caso um arquivo ou um dos diretórios do caminho não exista.
	 * */
	public void remover(int indice) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		if(this.subDiretorios != null) {
			if(indice >= 0 && indice < this.subDiretorios.size()) {
				this.subDiretorios.remove(indice);
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O elemento não esta presente na lista.");
			}
		} else {
			throw new ExclusaoDeArquivoOuDiretorioNegadaException("Lista de elementos vazia.");
		}
	}
	
	public Estrutura obterPai() {
		return pai;
	}

	public void definirPai(Estrutura pai) {
		this.pai = pai;
	}

	public String obterDiretorioAtual() {
		return this.diretorioAtual;
	}
	
	public void definirDiretorioAtual(String diretorioAtual) {
		this.diretorioAtual = diretorioAtual;
	}
	
	public ArrayList<Estrutura> obterSubDiretorios() {
		return subDiretorios;
	}

	public Estrutura[] obterListaSubDiretorios() {
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
		return this.obterDiretorioAtual();
	}

	public String obterNomeArquivo() {
		return this.nomeArquivo;
	}

	public void definirNomeArquivo(String arquivo) {
		this.nomeArquivo = arquivo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estrutura other = (Estrutura) obj;
		if (nomeArquivo == null) {
			if (other.nomeArquivo != null)
				return false;
		} else if (!nomeArquivo.equals(other.nomeArquivo))
			return false;
		return true;
	}

	public boolean eIndexavel() {
		return indexavel;
	}

	public void definirIndexavel(boolean indexavel) {
		this.indexavel = indexavel;
	}
}
