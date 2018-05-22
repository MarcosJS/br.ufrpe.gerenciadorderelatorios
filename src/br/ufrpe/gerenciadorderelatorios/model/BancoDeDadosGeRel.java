package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.excecoes.TipoDeArquivoDifereDoEsperadoException;

public class BancoDeDadosGeRel {
	private static final String NOME_BANCO_DE_DADOS = "bd_relatorios";
	//Obtendo diretorio atual
	private File diretorioDeTrabalho = new File(System.getProperty("user.dir"));
	private File bancoDeDados;
		
	public void iniciarBancoDeDados() {
		File banco = buscarDiretorioArquivo(diretorioDeTrabalho, NOME_BANCO_DE_DADOS);
		/*Um novo banco � criado caso ainda n�o exista.*/
		if(banco != null) {
			bancoDeDados = criarDiretorio(diretorioDeTrabalho, NOME_BANCO_DE_DADOS);
		} else {
			bancoDeDados = banco;
		}
	}
	
	/**Retorna um diret�rio ou arquivo apartir de diret�rio dado.*/
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
	}
	
	private File criarDiretorio(File diretorioAtual, String nome) {
		File diretorio = new File(this.construirCaminho(new String[] {diretorioAtual.getAbsolutePath(), nome}));
		/*Criando novo diret�rio com 'mkdir()'*/
		diretorio.mkdir();
		return diretorio;
	}
	
	/**Salva o objeto no diret�rio de acordo com o seu tipo.
	 * @throws JaExisteArquivoOuDiretorioException*/
	public void adicionar(IGravavel gravavel) {
		File diretorio = this.buscarDiretorioArquivo(this.getBancoDeDados(), gravavel.obterIdOriginador());
		/*Verificando se n�o � um his�rico.*/
		if (gravavel.obterIdOriginador() != null) {
			String caminhoArquivo = null;
			if(gravavel instanceof PerfilAnalise) {
				caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), gravavel.obterId()});
			} else {
				caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), gravavel.obterId()});
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
				diretorio = criarDiretorio(diretorio, "rel");
				this.adicionar(gravavel);
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
			diretorio = criarDiretorio(this.getBancoDeDados(), gravavel.obterId());
			//salvar historico
			diretorio = criarDiretorio(diretorio, "rel");
			this.adicionar(gravavel);
		}
	}
	
	/**Remove o arquivo ou diret�rio especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException*/
	public void remover(String diretorioPai, String nomeArquivo) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		File diretorio = this.buscarDiretorioArquivo(this.getBancoDeDados(), diretorioPai);
		/*Verificando se diret�rio existe*/
		if (diretorio != null) {
			String caminhoArquivo = this.construirCaminho(new String[] {diretorio.getAbsolutePath(), nomeArquivo});
			/*Verificando se a estrutura esta completa para adicionar o arquivo*/
			File alvo = new File(caminhoArquivo);
			/*Verificando se j� existe o arquivo*/
			if(alvo.exists()) {
				if(!alvo.delete()) {
					throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo foi negada ou diret�rio n�o esta vazio: "+alvo.getName());
				}
			} else {
				throw new ArquivoOuDiretorioNaoExisteException("O diret�rio ou arquivo n�o existe: "+alvo.getName());
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
		File historico = this.buscarDiretorioArquivo(this.getBancoDeDados(), idHhistorico);
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
		return "BancoDeDadosGeRel [diretorioDeTrabalho=" + diretorioDeTrabalho + ", bancoDeDados=" + bancoDeDados.getAbsolutePath() + "]";
	}

	public File getDiretorioDeTrabalho() {
		return diretorioDeTrabalho;
	}

	public void definirDiretorioDeTrabalho(File diretorioDeTrabalho) {
		this.diretorioDeTrabalho = diretorioDeTrabalho;
	}

	public File getBancoDeDados() {
		return bancoDeDados;
	}
}
