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
		/*Obtendo o diretório de trabalho*/
		this.definirDiretorioDeTrabalho(new File(System.getProperty("user.dir")));
		
		Estrutura[] estrutura = new Estrutura[BANCO_DE_DADOS.length];
		File[] banco = new File[BANCO_DE_DADOS.length];
		
		for(int i = 0; i < BANCO_DE_DADOS.length; i++) {
			/*Criando estrutura padrão dos bancos.*/
			estrutura[i] = new Estrutura(BANCO_DE_DADOS[i], null);
			
			banco[i] = new File(this.construirCaminho(new String[] {diretorioDeTrabalho.getAbsolutePath(), BANCO_DE_DADOS[i]}));
			
			/*Um novo banco é criado caso ainda não exista.*/
			if(!banco[i].exists()) {
				criarEstrutura(diretorioDeTrabalho.getAbsolutePath(), estrutura[i]);
			}
			bancoDeDados[i] = banco[i];
		}
	}
	
	/**Retorna um diretório ou arquivo apartir de diretório dado.*//*
	private File buscarDiretorioArquivo(File diretorioAtual, String alvo) {
		File diretorioOuArquivo = null;
		if(diretorioAtual.getName().equals(alvo)) {
			diretorioOuArquivo = diretorioAtual;
		} else {
			//Recebendo lista de subdiretórios e arquivos
			File[] subDiretorios = diretorioAtual.listFiles();
			
			if(subDiretorios != null) {
				for(int i = 0; i < subDiretorios.length; i++) {
					//Verificando se o arquivo atual é o buscado e é um diretorio buscado
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
		/*Criando novo diretório com 'mkdir()'*/
		if(!diretorio.exists()) {
			diretorio.mkdir();
		}		
		if(estrutura.obterSubDiretorios() != null) {
			for(Estrutura e: estrutura.obterSubDiretorios()) {
				criarEstrutura(diretorio.getAbsolutePath(), e);
			}
		}
	}
	
	/**Salva o objeto no diretório de acordo com o seu tipo.*/
	public void adicionar(String banco, IGravavel gravavel) {
		String[] lista =  {this.obterDiretorioDeTrabalho().getAbsolutePath(), banco, gravavel.obterIdOriginador()};
		File diretorio = new File(this.construirCaminho(lista));
		/*Verificando se não é um hisórico.*/
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
		/*Se for histórico, é um novo histórico, então cria-se um novo diretório para o novo histórico*/
		} else {
			diretorio = criarEstrutura(this.obterBancoDeDados(), gravavel.obterId());
			diretorio = criarEstrutura(diretorio, "rel");
			this.adicionar(gravavel);
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
				throw new ArquivoOuDiretorioNaoExisteException("O diretório ou arquivo não existe: "+alvo.obterName());
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
				throw new ArquivoOuDiretorioNaoExisteException("O diretório ou arquivo não existe: "+arquivo.obterName());
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
