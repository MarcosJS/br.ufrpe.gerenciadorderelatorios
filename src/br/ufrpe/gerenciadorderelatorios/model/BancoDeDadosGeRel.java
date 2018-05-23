package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.excecoes.TipoDeArquivoDifereDoEsperadoException;

public class BancoDeDadosGeRel {
	private static final String BANCO_DE_DADOS = "bd_gerel";
	private static final String[] BANCOS = {"bd_historicos", "bd_acesso"};
	
	private File bancoDeDados;
	private Estrutura estrutura;
	
	/**Inicializa o banco de dados.*/
	public void iniciarBancoDeDados() {
		/*Criando a estrutura de diret�rios para o banco de dados.*/
		this.definirEstrutura(new Estrutura(BANCO_DE_DADOS, null));
		
		for(String nomeBanco: BANCOS) {
			Estrutura banco = new Estrutura(nomeBanco, null);
			this.obterEstrutura().adicionar(banco);
		}
		

		this.bancoDeDados = new File(this.construirCaminho(new String[] {System.getProperty("user.dir"), BANCO_DE_DADOS}));
		
		this.criarEstrutura(new File(System.getProperty("user.dir")), estrutura);
	}
	
	/**Criar uma estrutura de pastas de acordo com o paramentro 'estrutura'.*/
	private void criarEstrutura(File base, Estrutura estrutura) {
		
		File diretorio = new File(this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()}));
		
		/*Criando novo diret�rio com 'mkdir()'*/
		if(!diretorio.exists()) {
			diretorio.mkdir();
		}		
		if(estrutura.obterSubDiretorios() != null) {
			for(Estrutura e: estrutura.obterSubDiretorios()) {
				criarEstrutura(diretorio, e);
			}
		}
	}
	
	/**Salva o objeto gravavel no diret�rio apontado pela estrutura.
	 * @throws JaExisteArquivoOuDiretorioException */
	public void adicionar(File base, Estrutura estrutura , Gravavel gravavel) throws JaExisteArquivoOuDiretorioException {
		String caminhoDiretorio =  this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o his�rico j� existe.*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			
			/*Verificando se j� esta no diret�rio correto para salvar.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), gravavel.obterId()+".ser"});
				File file = new File(caminhoArquivo);
				
				/*Verificando se o arquivo ainda n�o existe.*/
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
					throw new JaExisteArquivoOuDiretorioException("O arquivo j� existe no banco e dados em "+estrutura.obterRaiz());
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
	
	/**Remove o arquivo ou diret�rio especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException*/
	public void remover(File base, Estrutura estrutura , String nomeArquivo) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		String caminhoDiretorio =  this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()});
		File diretorio = new File(caminhoDiretorio);
		
		/*Verificando se o diret�rio existe*/
		if (diretorio.exists()) {
			
			/*Verificando se j� � esta o diret�rio correto para exlus�o.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), nomeArquivo});
				File alvo = new File(caminhoArquivo);
				
				/*Verificando se o arquivo existe.*/
				if(alvo.exists()) {
					
					/*Deletando o arquivo ou diret�rio.*/
					if(!alvo.delete()) {
						throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo ou diret�rio negada: "+alvo.getName());
					}
					
				} else {
					throw new ArquivoOuDiretorioNaoExisteException("O diret�rio ou arquivo n�o existe: "+alvo.getName());
				}
			} else {
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					this.remover(diretorio, e, nomeArquivo);
				}
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O diretorio n�o existe: "+estrutura.obterRaiz());
		}
	}
	
	/**Retorna o arquivo especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException 
	 * @throws TipoDeArquivoDifereDoEsperadoException */
	public File consultar(String idHhistorico, String nomeArquivo) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException, TipoDeArquivoDifereDoEsperadoException {
		File alvo = null;
		File historico = this.buscarDiretorioArquivo(this.obterBancoDeDados(), idHhistorico);
		/*Verificando se diret�rio existe*/
		if (historico != null) {
			String caminhoArquivo = this.construirCaminho(new String[] {historico.getAbsolutePath(), nomeArquivo});
			File arquivo = new File(caminhoArquivo);
			/*Verificando se a estrutura esta completa e o arquivo existe.*/
			if(arquivo.exists()) {
				if(arquivo.isFile()) {
					alvo = arquivo;
				} else {
					throw new TipoDeArquivoDifereDoEsperadoException("Permissao para excluir o arquivo foi negada ou diret�rio n�o esta vazio: "+arquivo.getName());
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O diret�rio ou arquivo n�o existe: "+arquivo.getName());
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O hist�rico n�o existe: "+idHhistorico);
		}
		return alvo;
	}
	
	/**Recebe um diret�rio e sub-diret�rios e retorna o caminho completo*/
	private String construirCaminho(String[] caminho) {
		String caminhoConstruido = caminho[0];
		for(int i = 1; i < caminho.length; i++) {
			caminhoConstruido = caminhoConstruido.concat(File.separator+caminho[i]);
		}
		System.out.println("caminho construido: "+caminhoConstruido);
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
