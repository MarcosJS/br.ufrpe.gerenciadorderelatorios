package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.TipoDeArquivoDifereDoEsperadoException;

public class BancoDeDadosGeRel {
	private static final String BANCO_DE_DADOS = "bd_gerel";
	private static final String[] BANCOS = {"bd_historicos", "bd_acesso"};
	
	private File bancoDeDados;
	private Estrutura estrutura;
	
	/**Inicializa o banco de dados.*/
	public void iniciarBancoDeDados() {
		/*Criando a estrutura de diretórios para o banco de dados.*/
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
	
	/**Salva o objeto no diretório de acordo com o seu tipo.*/
	public void adicionar(File base, Estrutura estrutura , Gravavel gravavel) {
		String caminho =  this.construirCaminho(new String[] {base.getAbsolutePath(), estrutura.obterRaiz()});
		File diretorio = new File(caminho);
		/*Verificando se o hisórico já existe.*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			/*Verificando se já esta no diretório correto para salvar.*/
			if(estrutura.obterSubDiretorios() == null) {
				String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), gravavel.obterId()+".ser"});
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
				for(Estrutura e: estrutura.obterSubDiretorios()) {
					this.adicionar(base, e, gravavel);
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
	public void remover(String diretorioPai, String nomeArquivo) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		File diretorio = this.buscarDiretorioArquivo(this.obterBancoDeDados(), diretorioPai);
		/*Verificando se diretório existe*/
		if (diretorio != null) {
			String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), nomeArquivo});
			/*Verificando se a estrutura esta completa para adicionar o arquivo*/
			File alvo = new File(caminhoArquivo);
			/*Verificando se já existe o arquivo*/
			if(alvo.exists()) {
				if(!alvo.delete()) {
					throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo foi negada ou diretório não esta vazio: "+alvo.obterName());
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O diretório ou arquivo não existe: "+alvo.getName());
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O diretorio não existe: "+diretorioPai);
		}
	}
	
	/**Retorna o arquivo especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException 
	 * @throws TipoDeArquivoDifereDoEsperadoException */
	public File consultar(String idHhistorico, String nomeArquivo) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException, TipoDeArquivoDifereDoEsperadoException {
		File alvo = null;
		File historico = this.buscarDiretorioArquivo(this.obterBancoDeDados(), idHhistorico);
		/*Verificando se diretório existe*/
		if (historico != null) {
			String caminhoArquivo = this.construirCaminho(new String[] {historico.getAbsolutePath(), nomeArquivo});
			File arquivo = new File(caminhoArquivo);
			/*Verificando se a estrutura esta completa e o arquivo existe.*/
			if(arquivo.exists()) {
				if(arquivo.isFile()) {
					alvo = arquivo;
				} else {
					throw new TipoDeArquivoDifereDoEsperadoException("Permissao para excluir o arquivo foi negada ou diretório não esta vazio: "+arquivo.obterName());
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O diretório ou arquivo não existe: "+arquivo.getName());
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O histórico não existe: "+idHhistorico);
		}
		return alvo;
	}
	
	/**Recebe um diretório e sub-diretórios e retorna o caminho completo*/
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
		return "BancoDeDadosGeRel [diretorioDeTrabalho=" + diretorioDeTrabalho + ", bancoDeDados=" + bancoDeDados.getAbsolutePath() + "]";
	}

	public File obterBancoDeDados() {
		return bancoDeDados;
	}

	public Estrutura obterEstrutura() {
		return bancos;
	}

	public void definirEstrutura(Estrutura bancos) {
		this.bancos = bancos;
	}

}
