package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;

public class BancoDeDadosGeRel {
	private static final String BANCO_DE_DADOS = "bd_gerel";
	public static final String BD_HISTORICOS = "bd_historicos";
	public static final String BD_ACESSO=  "bd_acesso";
	
	private File bancoDeDados;
	private Estrutura estruturaBanco;
	/*private ArrayList <Estrutura> indexHistorico;
	private ArrayList <Estrutura> indexAcesso;*/
	
	/**Inicializa o banco de dados.*/
	public void iniciarBancoDeDados(String raizBanco) {
		/*Criando a estrutura de diretórios para o banco de dados.*/
		this.definirEstruturaBanco(new Estrutura(BANCO_DE_DADOS, null, null));

		Estrutura banco = new Estrutura(BD_HISTORICOS, null, null);
		this.obterEstruturaBanco().adicionar(banco);
		banco = new Estrutura(BD_ACESSO, null, null);
		this.obterEstruturaBanco().adicionar(banco);
				
		this.bancoDeDados = new File(this.construirCaminho(new String[] {raizBanco, BANCO_DE_DADOS}));
		this.criarEstrutura(new File(raizBanco), this.estruturaBanco);
		
	}
	
	/**Criar uma estrutura de pastas de acordo com o paramentro 'estrutura'.*/
	private void criarEstrutura(File base, Estrutura estrutura) {
		
		File diretorio = new File(this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()}));
		
		/*Criando novo diretório com 'mkdir()'*/
		if(!diretorio.exists()) {
			diretorio.mkdir();
		}		
		if(estrutura.obterSubDiretorios() != null) {
			
			for(Estrutura e: estrutura.obterSubDiretorios()) {
				criarEstrutura(diretorio, e);
			}
		}
	}
	
	/**Salva o objeto gravavel no diretório apontado pela estrutura.
	 * @throws JaExisteArquivoOuDiretorioException */
	public void adicionar(Estrutura base, Estrutura estrutura , Gravavel gravavel) throws JaExisteArquivoOuDiretorioException {
		base.adicionarNaPonta(new Estrutura(estrutura.obterRaiz(), estrutura.obterNomeArquivo(), null));
		String caminhoDiretorio =  this.construirCaminho(new String[] {this.bancoDeDados.getAbsolutePath(), base.obterCaminho()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o hisórico já existe.*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se já esta no diretório correto para salvar.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()});
				File file = new File(caminhoArquivo);
				
				/*Verificando se o arquivo ainda não existe.*/
				if(!file.exists()) {
					FileOutputStream arquivoSaida = null;
					ObjectOutputStream objetoSaida = null;
					
					try {
						arquivoSaida = new FileOutputStream(caminhoArquivo);
						objetoSaida = new ObjectOutputStream(arquivoSaida);
						objetoSaida.writeObject(gravavel);
						
						/*Indexando o arquivo.*/
						this.adicionarAoIndice(base);
						
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
					throw new JaExisteArquivoOuDiretorioException("O arquivo já existe no banco e dados em "+estrutura.obterRaiz());
				}
				
			} else {
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					this.adicionar(base, e, gravavel);
				}
			}
			
		} else {
			criarEstrutura(diretorio, estrutura);
			this.adicionar(base, estrutura, gravavel);
		}
	}
	
	/**Remove o arquivo ou diretório especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException*/
	public void remover(Estrutura base, Estrutura estrutura) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		base.adicionarNaPonta(new Estrutura(estrutura.obterRaiz(), estrutura.obterNomeArquivo(), null));
		String caminhoDiretorio =  this.construirCaminho(new String[] {this.bancoDeDados.getAbsolutePath(), base.obterCaminho()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o diretório existe*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se já é esta o diretório correto para exlusão.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()});
				File alvo = new File(caminhoArquivo);
				
				/*Verificando se o arquivo existe.*/
				if(alvo.exists()) {
					System.out.println("Arquivo a ser deletado: "+estrutura.obterNomeArquivo());
					
					/*Deletando o arquivo ou diretório.*/
					if(!alvo.delete()) {
						throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo ou diretório negada: "+alvo.getName());
					} else {
						this.removerDoIndice(base);
					}
					
				} else {
					throw new ArquivoOuDiretorioNaoExisteException("O diretório ou arquivo não existe: "+alvo.getName());
				}
			} else {
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					this.remover(base, e);
				}
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O diretorio não existe: "+estrutura.obterRaiz());
		}
	}
	
	/**Retorna o arquivo especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException */
	public Serializable consultar(File base, Estrutura estrutura) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		Serializable arquivo = null;
		File alvo = null;
		String caminhoDiretorio =  this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o diretório existe*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se já é esta o diretório correto para pesquisar o arquivo.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()});
				alvo = new File(caminhoArquivo);
				
				/*Verificando se o arquivo existe.*/
				if(alvo.exists()) {
					
					/*Recuperando o arquivo.*/
					FileInputStream arquivoDeEntrada = null;
					ObjectInputStream ObjetoDeEntrada = null;

					try {
						arquivoDeEntrada = new FileInputStream(alvo.getAbsolutePath());
						ObjetoDeEntrada = new ObjectInputStream(arquivoDeEntrada);
						arquivo = (Serializable) ObjetoDeEntrada.readObject();
						
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

						if (ObjetoDeEntrada != null) {
							try {
								ObjetoDeEntrada.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
				} else {
					throw new ArquivoOuDiretorioNaoExisteException("O arquivo não existe: "+alvo.getName());
				}
			} else {
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					arquivo = this.consultar(diretorio, e);
				}
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O diretorio não existe: "+estrutura.obterRaiz());
		} 
		
		return arquivo;
	}
	
	/**Recebe um diretório e sub-diretórios e retorna o caminho completo*/
	private String construirCaminho(String[] caminho) {
		String caminhoConstruido = caminho[0];
		for(int i = 1; i < caminho.length; i++) {
			caminhoConstruido = caminhoConstruido.concat(File.separatorChar+caminho[i]);
		}
		return caminhoConstruido;
	}
	
	/**Adiciona a estrutura à estrutura geral do banco de dados que funciona como indice.
	 * @author Marcos Jose*/
	private void adicionarAoIndice(Estrutura estrutura) {
		
		/*Descobrindo o banco de dados.*/
		switch (estrutura.obterRaiz()) {
		case "bd_historicos":
			String estruturaAlvo = "est"+estrutura.obterSubDiretorios()[0].obterRaiz().substring(1, 5);
			Estrutura banco = this.estruturaBanco.obterSubDiretorios()[0];
			boolean adicionado = false;
			
			/*Verificando se existe alguma indexação.*/
			if(banco.obterSubDiretorios() != null) {
				
				/*Obtendo estruturas 'indices' referentes a cada histórico.*/
				for(Estrutura e: banco.obterSubDiretorios()) {
					
					/*Verificando se a estrutura já esta indexada.*/
					if(e.obterRaiz().equals(estruturaAlvo)) {
						
						/*Escolhendo a posição no indice através da primeira letra do nome do arquivo.*/
						switch(estrutura.obterNomeArquivo().substring(0, 1)) {
						case "h":
							e.adicionar(estrutura, 0);
							break;
						case "p":
							e.adicionar(estrutura, 1);
							break;
						default:
							e.adicionar(estrutura);
							break;
						}
						adicionado = true;
						break;
					} 
				}
			}
			
			/*Verificando se o arquivo foi indexado.*/
			if(!adicionado) {
				Estrutura itemBanco = new Estrutura(estruturaAlvo.substring(3, 7), estruturaAlvo, null);
				itemBanco.adicionar(estrutura);
				banco.adicionar(itemBanco);
			}
			
			break;

		default:
			break;
		}
	}
	
	/**Remove a estrutura da estrutura geral do banco de dados que funciona como indice.
	 * @author Marcos Jose
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException */
	private void removerDoIndice(Estrutura estrutura) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		
		/*Descobrindo o banco de dados.*/
		switch (estrutura.obterRaiz()) {
		case "bd_historicos":
			String estruturaAlvo = "est"+estrutura.obterSubDiretorios()[0].obterRaiz().substring(1, 5);
			Estrutura banco = this.estruturaBanco.obterSubDiretorios()[0];
			boolean removido = false;
			
			/*Verificando se existe alguma indexação.*/
			if(banco.obterSubDiretorios() != null) {
				
				/*Obtendo estruturas 'indices' referentes a cada histórico.*/
				for(Estrutura e: banco.obterSubDiretorios()) {
					
					/*Verificando se a estrutura já esta indexada.*/
					if(e.obterRaiz().equals(estruturaAlvo)) {
						
						/*Removendo o arquivo.*/
						e.remover(estrutura);
						removido = true;
						break;
					} 
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O banco de dados "+banco.obterRaiz()+" ainda não foi indexado!");
			}
			
			/*Verificando se o arquivo foi indexado.*/
			if(!removido) {
				throw new ArquivoOuDiretorioNaoExisteException("Falha na indexação: Arquivo "+estruturaAlvo+" não foi encotrado!");
			}
			
			break;

		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "BancoDeDadosGeRel [bancoDeDados=" + bancoDeDados + ", estrutura=" + estruturaBanco + "]";
	}

	public File obterBancoDeDados() {
		return bancoDeDados;
	}

	public Estrutura obterEstruturaBanco() {
		return this.estruturaBanco;
	}

	public void definirEstruturaBanco(Estrutura estrutura) {
		this.estruturaBanco = estrutura;
	}

}
