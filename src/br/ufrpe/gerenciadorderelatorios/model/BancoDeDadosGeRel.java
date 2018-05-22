package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.TipoDeArquivoDifereDoEsperadoException;

public class BancoDeDadosGeRel {
	private static final String[] BANCO_DE_DADOS = {"bd_historicos", "bd_acesso"};
	//Obtendo diretorio atual
	private File diretorioDeTrabalho;
	private File [] bancoDeDados;
	
	/**Inicializa o banco de dados.*/
	public void iniciarBancoDeDados() {
		/*Obtendo o diret�rio de trabalho*/
		this.definirDiretorioDeTrabalho(new File(System.getProperty("user.dir")));
		
		Estrutura[] estrutura = new Estrutura[BANCO_DE_DADOS.length];
		File[] banco = new File[BANCO_DE_DADOS.length];
		
		for(int i = 0; i < BANCO_DE_DADOS.length; i++) {
			/*Criando estrutura padr�o dos bancos.*/
			estrutura[i] = new Estrutura(BANCO_DE_DADOS[i], null);
			
			banco[i] = new File(this.construirCaminho(new String[] {diretorioDeTrabalho.getAbsolutePath(), BANCO_DE_DADOS[i]}));
			
			/*Um novo banco � criado caso ainda n�o exista.*/
			if(!banco[i].exists()) {
				criarEstrutura(diretorioDeTrabalho.getAbsolutePath(), estrutura[i]);
			}
			bancoDeDados[i] = banco[i];
		}
	}
	
	/**Retorna um diret�rio ou arquivo apartir de diret�rio dado.*//*
	private File buscarDiretorioArquivo(File diretorioAtual, String alvo) {
		File diretorioOuArquivo = null;
		if(diretorioAtual.getName().equals(alvo)) {
			diretorioOuArquivo = diretorioAtual;
		} else {
			//Recebendo lista de subdiret�rios e arquivos
			File[] subDiretorios = diretorioAtual.listFiles();
			
			if(subDiretorios != null) {
				for(int i = 0; i < subDiretorios.length; i++) {
					//Verificando se o arquivo atual � o buscado e � um diretorio buscado
					if(subDiretorios[i].getName().equals(alvo)) {
						diretorioOuArquivo = subDiretorios[i];
					}
				} 
			}
		}
		
		return diretorioOuArquivo;
	}*/
	
	private void criarEstrutura(String diretorioRaiz, Estrutura estrutura) {
		
		File diretorio = new File(this.construirCaminho(new String[] {diretorioRaiz, estrutura.obterRaiz()}));
		/*Criando novo diret�rio com 'mkdir()'*/
		if(!diretorio.exists()) {
			diretorio.mkdir();
		}		
		if(estrutura.obterSubDiretorios() != null) {
			for(Estrutura e: estrutura.obterSubDiretorios()) {
				criarEstrutura(diretorio.getAbsolutePath(), e);
			}
		}
	}
	
	/**Salva o objeto no diret�rio de acordo com o seu tipo.*/
	public void adicionar(String banco, IGravavel gravavel) {
		String[] lista =  {this.obterDiretorioDeTrabalho().getAbsolutePath(), banco, gravavel.obterIdOriginador()};
		File diretorio = new File(this.construirCaminho(lista));
		/*Verificando se n�o � um his�rico.*/
		if (diretorio.exists() && diretorio.isDirectory()) {
			String caminhoArquivo = null;
			if(gravavel instanceof Relatorio) {
				caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), "rel", gravavel.obterId()+".ser"});
			} else {
				caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), gravavel.obterId()+".ser"});
			}
			
			FileOutputStream arquivoSaida = null;
			ObjectOutputStream objetoSaida = null;
			/*Verificando se a estrutura esta completa para adicionar o arquivo.*/
			try {
				arquivoSaida = new FileOutputStream(caminhoArquivo);
				objetoSaida = new ObjectOutputStream(arquivoSaida);
				objetoSaida.writeObject(gravavel);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Erro: Estrutura incompleta\nRestaurando estrutura");
				criarEstrutura(diretorio.getAbsolutePath(), gravavel.obterEstrutura());
				this.adicionar(banco, gravavel);
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
		/*Se for hist�rico, � um novo hist�rico, ent�o cria-se um novo diret�rio para o novo hist�rico*/
		} else {
			diretorio = criarEstrutura(this.obterBancoDeDados(), gravavel.obterId());
			diretorio = criarEstrutura(diretorio, "rel");
			this.adicionar(gravavel);
		}
	}
	
	/**Remove o arquivo ou diret�rio especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException*/
	public void remover(String diretorioPai, String nomeArquivo) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		File diretorio = this.buscarDiretorioArquivo(this.obterBancoDeDados(), diretorioPai);
		/*Verificando se diret�rio existe*/
		if (diretorio != null) {
			String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), nomeArquivo});
			/*Verificando se a estrutura esta completa para adicionar o arquivo*/
			File alvo = new File(caminhoArquivo);
			/*Verificando se j� existe o arquivo*/
			if(alvo.exists()) {
				if(!alvo.delete()) {
					throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo foi negada ou diret�rio n�o esta vazio: "+alvo.obterName());
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O diret�rio ou arquivo n�o existe: "+alvo.obterName());
			}
		} else {
			throw new ArquivoOuDiretorioNaoExisteException("O diretorio n�o existe: "+diretorioPai);
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
					throw new TipoDeArquivoDifereDoEsperadoException("Permissao para excluir o arquivo foi negada ou diret�rio n�o esta vazio: "+arquivo.obterName());
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O diret�rio ou arquivo n�o existe: "+arquivo.obterName());
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
		return "BancoDeDadosGeRel [diretorioDeTrabalho=" + diretorioDeTrabalho + ", bancoDeDados=" + bancoDeDados.getAbsolutePath() + "]";
	}

	public File obterDiretorioDeTrabalho() {
		return diretorioDeTrabalho;
	}

	public void definirDiretorioDeTrabalho(File diretorioDeTrabalho) {
		this.diretorioDeTrabalho = diretorioDeTrabalho;
	}

	public File obterBancoDeDados() {
		return bancoDeDados;
	}

}
