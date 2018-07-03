package br.ufrpe.gerenciadorderelatorios.model;

/**
 * Essa classe realiza todas as opera��es de referentes ao armazenamento de dados em arquivos.
 * @author Marcos Jose.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import br.ufrpe.gerenciadorderelatorios.excecoes.*;

public class BancoDeDadosGeRel {
	private static final String NOME_INDICE_BANCO = "indiceGeRel";
	private static final String BANCO_DE_DADOS = "bd_gerel";
	public static final String BD_HISTORICOS = "bd_historicos";
	
	private File bancoDeDados;
	private Estrutura estruturaIndiceBanco;
	
	/**
	 * Inicializa o banco de dados em um determinado local no sistema de arquivos do SO dado um caminho.
	 * @param raizBanco string que representa o caminho.
	 * */
	public void iniciarBancoDeDados(String raizBanco) {
		/*Recuperando o �ndice do banco de dados.*/
		this.bancoDeDados = new File(this.construirCaminho(new String[] {raizBanco, BANCO_DE_DADOS}));
		
		/*Recuperando �ndice do banco de dados.*/
		Estrutura estIndice = new Estrutura(null, "indice", BancoDeDadosGeRel.NOME_INDICE_BANCO+".ser", null);
		try {
			this.estruturaIndiceBanco = (Estrutura) this.consultar(estIndice);
		} catch (ArquivoOuDiretorioNaoExisteException e) {
			e.printStackTrace();
			System.out.println("\t- n�o existe arquivo de indice.");
		}
		
		if(this.estruturaIndiceBanco != null) {
			System.out.println("\t- indice carregado.");
		} else {
			
			/*Criando a estrutura de diret�rios para o banco de dados.*/
			this.definirEstruturaIndiceBanco(new Estrutura(null, BANCO_DE_DADOS, NOME_INDICE_BANCO, null));

			Estrutura banco = new Estrutura(this.estruturaIndiceBanco, BD_HISTORICOS, null, null);
			this.estruturaIndiceBanco.adicionar(banco);
					
			try {
				this.criarEstrutura(new File(raizBanco), this.estruturaIndiceBanco);
				
			} catch (DiretorioNaoPodeSerCriadoException e) {
				e.printStackTrace();
			}
		}
		System.out.println("\t- banco inicializado.");
	}
	
	/**
	 * Criar uma estrutura de pastas de acordo com o paramentro 'estrutura' no local especificado.
	 * @param base que representa um determinado local.
	 * @param estrutura representa a hierarquia de estruturas.
	 * @throws DiretorioNaoPodeSerCriadoException.
	 * */
	private void criarEstrutura(File base, Estrutura estrutura) throws DiretorioNaoPodeSerCriadoException {
		/*N�o � criado um subdiret�rio como mesmo nome do diret�rio base.*/
		if(!base.getName().equals(estrutura.obterDiretorioAtual())) {
			File diretorio = new File(this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterDiretorioAtual()}));
			
			/*Criando novo diret�rio com 'mkdir()'*/
			if(!diretorio.exists()) {
				if(!diretorio.mkdir()) {
					throw new DiretorioNaoPodeSerCriadoException("Caminho do arquivo passado como base: "+base.getAbsolutePath());
				}
			}		
			if(estrutura.obterSubDiretorios() != null) {
				
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					criarEstrutura(diretorio, e);
				}
			}
		}
	}
	
	/**
	 * Salva o objeto gravavel no diret�rio apontado pela estrutura.
	 * @param estrutura que representa a hierarquia de estruturas.
	 * @param gravavel que representa o arquivo � ser gravado.
	 * @throws JaExisteArquivoOuDiretorioException. 
	 * @throws DiretorioNaoPodeSerCriadoException.
	 * */
	public void adicionar(Estrutura estrutura , Gravavel gravavel) throws JaExisteArquivoOuDiretorioException, DiretorioNaoPodeSerCriadoException {
		/*Obtendo diret�rio para consultar se estrutura de pastas exite.*/
		String caminhoDiretorio = this.construirCaminho(new String[] {this.bancoDeDados.getAbsolutePath(), estrutura.obterCaminhoAncestrais()});
		File diretorio = new File(caminhoDiretorio);
		//System.out.println("\t[BancoDeDadosGeRel.adicionar] caminho do diretorio de consulta: "+diretorio.getAbsolutePath());
		
		/*Verificando se o his�rico j� existe.*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se j� esta no diret�rio correto para salvar.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()});
				File file = new File(caminhoArquivo);
				
				/*Verificando se o arquivo ainda n�o existe.*/
				if(!file.exists()) {
					FileOutputStream arquivoSaida = null;
					ObjectOutputStream objetoSaida = null;
					
					try {
						arquivoSaida = new FileOutputStream(caminhoArquivo);
						objetoSaida = new ObjectOutputStream(arquivoSaida);
						objetoSaida.writeObject(gravavel);
						
						System.out.println("\t[BancoDeDados.adicionar] arquivo salvo;");
						
						/*Indexando o arquivo.*/
						if(estrutura.eIndexavel()) {
							this.adicionarAoIndice(estrutura.obterRaiz());
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (arquivoSaida != null) {
							try {
								arquivoSaida.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (objetoSaida != null) {
							try {
								objetoSaida.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
				} else {
					throw new JaExisteArquivoOuDiretorioException("O arquivo"+estrutura.obterNomeArquivo()+" j� existe no banco e dados em "+estrutura.obterDiretorioAtual());
				}
				
			} else {
				/*Descendo para o nivel abaixo.*/
				for(Estrutura e: estrutura.obterListaSubDiretorios()) {
					//System.out.println("\t[BancoDeDadosGeRel.adicionar] subdiretorio entrando na funcao adicionar: "+e.obterDiretorioAtual());
					this.adicionar(e, gravavel);
				}
			}
			
		} else {
			/*Criando novo diret�rio com 'mkdir()'*/
			if(!diretorio.mkdir()) {
				throw new DiretorioNaoPodeSerCriadoException("Caminho do arquivo passado como base: "+diretorio.getAbsolutePath());
			}
			this.adicionar(estrutura, gravavel);
		}
	}
	
	/**
	 * Remove o arquivo ou diret�rio especificado apontado pela estrutura.
	 * @param estrutura que representa a hierarquia de estruturas e o arquivo ou diret�rio � ser removido.
	 * @throws ArquivoOuDiretorioNaoExisteException.
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException.
	 * */
	public void remover(Estrutura estrutura) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		/*Obtendo diret�rio para consultar se estrutura de pastas exite.*/
		String caminhoDiretorio = this.construirCaminho(new String[] {this.bancoDeDados.getAbsolutePath(), estrutura.obterCaminhoAncestrais()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o diret�rio existe*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se j� � esta o diret�rio correto para exlus�o.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()});
				File alvo = new File(caminhoArquivo);
				
				/*Verificando se o arquivo existe.*/
				if(alvo.exists()) {
					System.out.println("\tArquivo a ser deletado: "+estrutura.obterNomeArquivo());
					
					/*Deletando o arquivo ou diret�rio.*/
					if(!alvo.delete()) {
						throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo ou diret�rio negada: "+alvo.getName());
					} else {
						System.out.println("\t- arquivo removido;");
						if(estrutura.eIndexavel()) {
							this.removerDoIndice(estrutura.obterRaiz());
						}
					}
					
				} else {
					throw new ArquivoOuDiretorioNaoExisteException("O diret�rio ou arquivo n�o existe: "+alvo.getName());
				}
			} else {
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					this.remover(e);
				}
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O diretorio n�o existe: "+estrutura.obterDiretorioAtual());
		}
	}
	
	/**
	 * Retorna o arquivo apontado pela estrutura se houver.
	 * @param estrutura que representa a hierarquia de estruturas e o arquivo ou diret�rio � ser retornado.
	 * @return um <code>Gravavel</code> que representa o arquivo recuperado.
	 * @throws ArquivoOuDiretorioNaoExisteException.
	 * */
	public Gravavel consultar(Estrutura estrutura) throws ArquivoOuDiretorioNaoExisteException {
		Gravavel arquivo = null;
		
		/*Obtendo diret�rio para consultar se estrutura de pastas exite.*/
		String caminhoDiretorio = this.construirCaminho(new String[] {this.bancoDeDados.getAbsolutePath(), estrutura.obterCaminhoAncestrais()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o diret�rio existe*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se j� � esta o diret�rio correto para pesquisar o arquivo.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()});
				File alvo = new File(caminhoArquivo);
				
				/*Verificando se o arquivo existe.*/
				if(alvo.exists()) {
					
					/*Recuperando o arquivo.*/
					FileInputStream arquivoDeEntrada = null;
					ObjectInputStream objetoDeEntrada = null;

					try {
						arquivoDeEntrada = new FileInputStream(alvo.getAbsolutePath());
						objetoDeEntrada = new ObjectInputStream(arquivoDeEntrada);
						arquivo = (Gravavel) objetoDeEntrada.readObject();
						
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {

						if (arquivoDeEntrada != null) {
							try {
								arquivoDeEntrada.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						if (objetoDeEntrada != null) {
							try {
								objetoDeEntrada.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
				} else {
					throw new ArquivoOuDiretorioNaoExisteException("O arquivo n�o existe: "+alvo.getName());
				}
			} else {
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					arquivo = this.consultar(e);
				}
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O diretorio n�o existe: "+estrutura.obterDiretorioAtual());
		} 
		
		return arquivo;
	}
	
	/**
	 * Recebe uma lista de nomes de diret�rio e sub-diret�rios e retorna o caminho formatado.
	 * @param caminho que representa uma hierarquia de diret�rios.
	 * @return um <code>String</code> que representa o caminho de diret�rio formatado.
	 * */
	private String construirCaminho(String[] caminho) {
		String caminhoConstruido = caminho[0];
		for(int i = 1; i < caminho.length; i++) {
			caminhoConstruido = caminhoConstruido.concat(File.separator+caminho[i]);
		}
		return caminhoConstruido;
	}
	
	/**
	 * Adiciona a estrutura ao �ndice do banco de dados.
	 * @param estrutura que representa a hierarquia de estruturas e o arquivo ou diret�rio.
	 * */
	private void adicionarAoIndice(Estrutura estrutura) {
		
		/*Descobrindo o banco de dados.*/
		switch (estrutura.obterDiretorioAtual()) {
		case "bd_historicos":
			String estruturaAlvo = "est"+estrutura.obterListaSubDiretorios()[0].obterDiretorioAtual().substring(1, 5);
			Estrutura banco = this.estruturaIndiceBanco.obterListaSubDiretorios()[0];
			boolean adicionado = false;
			
			/*Verificando se existe alguma indexa��o.*/
			if(banco.obterSubDiretorios() != null) {
				
				/*Obtendo estruturas '�ndices' referentes a cada hist�rico.*/
				for(Estrutura e: banco.obterListaSubDiretorios()) {
					
					/*Verificando se a estrutura j� esta indexada.*/
					if(e.obterNomeArquivo().substring(0, 7).equals(estruturaAlvo)) {
						
						/*Escolhendo a posi��o no �ndice atrav�s da primeira letra do nome do arquivo.*/
						switch(estrutura.obterNomeArquivo().substring(0, 1)) {
						
						case "h":
							try {
								e.remover(0);
							} catch (ArquivoOuDiretorioNaoExisteException e1) {
								e1.printStackTrace();
							} catch (ExclusaoDeArquivoOuDiretorioNegadaException e1) {
								e1.printStackTrace();
							}
							e.adicionar(estrutura, 0);
							break;
						case "p":
							try {
								e.remover(1);
							} catch (ArquivoOuDiretorioNaoExisteException e1) {
								e1.printStackTrace();
							} catch (ExclusaoDeArquivoOuDiretorioNegadaException e1) {
								e1.printStackTrace();
							}
							e.adicionar(estrutura, 1);
							break;
						default:
							try {
								e.remover(estrutura);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							e.adicionar(estrutura);
							break;
						}
						
						/*Salvando o arquivo de �ndice.*/
						Estrutura estIndice = new Estrutura(null, "indice", this.estruturaIndiceBanco.obterNomeArquivo()+".ser", null);
						estIndice.definirIndexavel(false);
						try {
							this.remover(estIndice);
							this.adicionar(estIndice, this.estruturaIndiceBanco);
						} catch (JaExisteArquivoOuDiretorioException e1) {
							e1.printStackTrace();
						} catch (DiretorioNaoPodeSerCriadoException e1) {
							e1.printStackTrace();
						} catch (ArquivoOuDiretorioNaoExisteException e1) {
							e1.printStackTrace();
						} catch (ExclusaoDeArquivoOuDiretorioNegadaException e1) {
							e1.printStackTrace();
						}
						adicionado = true;
						System.out.println("\t- arquivo indexado;");
						break;
					} 
				}
			}
				
			/*Verificando se o arquivo foi indexado.*/
			if(!adicionado) {
				Estrutura itemBanco = new Estrutura(null, "indice", estruturaAlvo+".ser", null);
				itemBanco.adicionar(estrutura);
				banco.adicionar(itemBanco);
				
				/*Salvando �ndice.*/
				Estrutura estIndice = new Estrutura(null, "indice", this.estruturaIndiceBanco.obterNomeArquivo()+".ser", null);
				estIndice.definirIndexavel(false);
				try {
					this.adicionar(estIndice, this.estruturaIndiceBanco);
				} catch (JaExisteArquivoOuDiretorioException e1) {
					e1.printStackTrace();
				} catch (DiretorioNaoPodeSerCriadoException e1) {
					e1.printStackTrace();
				}
				System.out.println("\t- arquivo indexado;");
			}
			
			break;

		default:
			break;
		}
	}
	
	/**
	 * Remove a estrutura do �ndice do banco de dados.
	 * @param estrutura que representa a hierarquia de estruturas e o arquivo ou diret�rio.
	 * @throws ArquivoOuDiretorioNaoExisteException.
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException.
	 * */
	private void removerDoIndice(Estrutura estrutura) throws ArquivoOuDiretorioNaoExisteException {
		
		/*Descobrindo o banco de dados.*/
		switch (estrutura.obterDiretorioAtual()) {
		case "bd_historicos":
			String estruturaAlvo = "est"+estrutura.obterListaSubDiretorios()[0].obterDiretorioAtual().substring(1, 5);
			Estrutura banco = this.estruturaIndiceBanco.obterListaSubDiretorios()[0];
			boolean removido = false;
			
			/*Verificando se existe alguma indexa��o.*/
			if(banco.obterSubDiretorios() != null) {
				
				/*Obtendo estruturas '�ndices' referentes a cada hist�rico.*/
				for(Estrutura e: banco.obterSubDiretorios()) {
					
					/*Verificando se a estrutura j� esta indexada.*/
					if(e.obterNomeArquivo().substring(0, 7).equals(estruturaAlvo)) {
						
						/*Removendo o arquivo.*/
						try {
							e.remover(estrutura);
							removido = true;
							
							/*Removendo o �ndice.*/
							Estrutura estIndice = new Estrutura(null, "indice", this.estruturaIndiceBanco.obterNomeArquivo()+".ser", null);
							estIndice.definirIndexavel(false);
							this.remover(estIndice);
							System.out.println("\t- arquivo removido do indice;");
							
						} catch (ExclusaoDeArquivoOuDiretorioNegadaException e1) {
							e1.printStackTrace();
						} catch (ArquivoOuDiretorioNaoExisteException e1) {
							e1.printStackTrace();
						}
						
						break;
					} 
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O banco de dados "+banco.obterDiretorioAtual()+" ainda n�o foi indexado!");
			}
			
			/*Verificando se o arquivo foi indexado.*/
			if(!removido) {
				throw new ArquivoOuDiretorioNaoExisteException("Falha na indexa��o: Arquivo "+estruturaAlvo+" n�o foi encotrado!");
			}
			
			break;

		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "BancoDeDadosGeRel [bancoDeDados=" + bancoDeDados + ", estrutura=" + estruturaIndiceBanco + "]";
	}
	
	/**
	 * Retorna o bancoDeDados.
	 * @return um <code>File</code> que cont�m a estrutura de arquivos do banco de dados.
	 */
	public File obterBancoDeDados() {
		return bancoDeDados;
	}
	
	/**
	 * Retorna a estrutura �ndice do banco de dados.
	 * @return uma <code>Estrutura</code> que representa o �ndice do banco de dados.
	 * */
	public Estrutura obterEstruturaIndiceBanco() {
		return this.estruturaIndiceBanco;
	}
	
	/**
	 * Defini o �ndice do banco de dados.
	 * @param estrutura que representa o novo �ndice do banco de dados.
	 */
	public void definirEstruturaIndiceBanco(Estrutura estrutura) {
		this.estruturaIndiceBanco = estrutura;
	}
}
