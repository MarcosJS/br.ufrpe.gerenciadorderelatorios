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
	private static final String[] BANCOS = {"bd_historicos", "bd_acesso"};
	
	private File bancoDeDados;
	private Estrutura estrutura;
	
	/**Inicializa o banco de dados.*/
	public void iniciarBancoDeDados(String raizBanco) {
		/*Criando a estrutura de diretórios para o banco de dados.*/
		this.definirEstrutura(new Estrutura(BANCO_DE_DADOS, null, null));
		
		
		
		for(String nomeBanco: BANCOS) {
			Estrutura banco = new Estrutura(nomeBanco, null, null);
			this.obterEstrutura().adicionar(banco);
		}
		
		this.bancoDeDados = new File(this.construirCaminho(new String[] {raizBanco, BANCO_DE_DADOS}));
		this.criarEstrutura(new File(raizBanco), this.estrutura);
		
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
	public void adicionar(File base, Estrutura estrutura , Gravavel gravavel) throws JaExisteArquivoOuDiretorioException {
		String caminhoDiretorio =  this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o hisórico já existe.*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se já esta no diretório correto para salvar.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()+".ser"});
				File file = new File(caminhoArquivo);
				
				/*Verificando se o arquivo ainda não existe.*/
				if(!file.exists()) {
					FileOutputStream arquivoSaida = null;
					ObjectOutputStream objetoSaida = null;
					
					try {
						arquivoSaida = new FileOutputStream(caminhoArquivo);
						objetoSaida = new ObjectOutputStream(arquivoSaida);
						objetoSaida.writeObject(gravavel);
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
					this.adicionar(diretorio, e, gravavel);
				}
			}
			
		} else {
			criarEstrutura(base, estrutura);
			this.adicionar(base, estrutura, gravavel);
		}
	}
	
	/**Remove o arquivo ou diretório especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException*/
	public void remover(File base, Estrutura estrutura) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		String caminhoDiretorio =  this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o diretório existe*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se já é esta o diretório correto para exlusão.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), estrutura.obterNomeArquivo()});
				File alvo = new File(caminhoArquivo);
				
				/*Verificando se o arquivo existe.*/
				if(alvo.exists()) {
					
					/*Deletando o arquivo ou diretório.*/
					if(!alvo.delete()) {
						throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo ou diretório negada: "+alvo.getName());
					}
					
				} else {
					throw new ArquivoOuDiretorioNaoExisteException("O diretório ou arquivo não existe: "+alvo.getName());
				}
			} else {
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					this.remover(diretorio, e);
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
				caminhoArquivo = caminhoArquivo.concat(".ser");
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
					this.consultar(diretorio, e);
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
			caminhoConstruido = caminhoConstruido.concat(File.separator+caminho[i]);
		}
		return caminhoConstruido;
	}
	
	@Override
	public String toString() {
		return "BancoDeDadosGeRel [bancoDeDados=" + bancoDeDados + ", estrutura=" + estrutura + "]";
	}

	public File obterBancoDeDados() {
		return bancoDeDados;
	}

	public Estrutura obterEstrutura() {
		return this.estrutura;
	}

	public void definirEstrutura(Estrutura estrutura) {
		this.estrutura = estrutura;
	}

}
