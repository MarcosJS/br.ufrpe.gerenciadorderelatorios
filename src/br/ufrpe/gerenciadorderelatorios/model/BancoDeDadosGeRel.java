package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.IOException;

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
		/*Um novo banco é criado caso ainda não exista.*/
		if(banco != null) {
			bancoDeDados = criarDiretorio(diretorioDeTrabalho, NOME_BANCO_DE_DADOS);
		} else {
			bancoDeDados = banco;
		}
	}
	
	/**Retorna um diretório ou arquivo apartir de diretório dado.*/
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
	}
	
	private File criarDiretorio(File diretorioAtual, String nome) {
		File diretorio = new File(this.construirCaminho(diretorioAtual, nome));
		/*Criando novo diretório com 'mkdir()'*/
		diretorio.mkdir();
		return diretorio;
	}
	
	/**Salva o arquivo no no diretório de histórico especificado.
	 * @throws JaExisteArquivoOuDiretorioException*/
	public void adicionar(String idHistorico, String nomeArquivo) throws JaExisteArquivoOuDiretorioException {
		File diretorio = this.buscarDiretorioArquivo(this.getBancoDeDados(), idHistorico);
		/*Verificando se diretório existe.*/
		if (diretorio != null) {
			String caminhoArquivo = this.construirCaminho(diretorio, nomeArquivo);
			/*Verificando se a estrutura esta completa para adicionar o arquivo.*/
			try {
				/*Verificando se já existe o arquivo*/
				if(!new File(caminhoArquivo).createNewFile()) {
					throw new JaExisteArquivoOuDiretorioException("O arquivo já existe");
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Erro: Estrutura incompleta\nRestaurando estrutura");
				diretorio = criarDiretorio(diretorio, "rel");
				this.adicionar(idHistorico, nomeArquivo);
			}
		} else {
			diretorio = criarDiretorio(this.getBancoDeDados(), idHistorico);
			diretorio = criarDiretorio(diretorio, "rel");
			this.adicionar(idHistorico, nomeArquivo);
		}
	}
	
	/**Remove o arquivo ou diretório especificado se houver.
	 * @throws ArquivoOuDiretorioNaoExisteException 
	 * @throws ExclusaoDeArquivoOuDiretorioNegadaException*/
	public void remover(String diretorioPai, String nomeArquivo) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException {
		File diretorio = this.buscarDiretorioArquivo(this.getBancoDeDados(), diretorioPai);
		/*Verificando se diretório existe*/
		if (diretorio != null) {
			String caminhoArquivo = this.construirCaminho(diretorio, nomeArquivo);
			/*Verificando se a estrutura esta completa para adicionar o arquivo*/
			File alvo = new File(caminhoArquivo);
			/*Verificando se já existe o arquivo*/
			if(alvo.exists()) {
				if(!alvo.delete()) {
					throw new ExclusaoDeArquivoOuDiretorioNegadaException("Permissao para excluir o arquivo foi negada ou diretório não esta vazio: "+alvo.getName());
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
		File historico = this.buscarDiretorioArquivo(this.getBancoDeDados(), idHhistorico);
		/*Verificando se diretório existe*/
		if (historico != null) {
			String caminhoArquivo = this.construirCaminho(historico, nomeArquivo);
			File arquivo = new File(caminhoArquivo);
			/*Verificando se a estrutura esta completa e o arquivo existe.*/
			if(arquivo.exists()) {
				if(arquivo.isFile()) {
					alvo = arquivo;
				} else {
					throw new TipoDeArquivoDifereDoEsperadoException("Permissao para excluir o arquivo foi negada ou diretório não esta vazio: "+arquivo.getName());
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
	private String construirCaminho(File diretorioAtual, String caminho) {
		String[] listaDeArquivos = caminho.split("/");
		String caminhoArquivo = diretorioAtual.getAbsolutePath();
		for(int i = 0; i < listaDeArquivos.length; i++) {
			caminhoArquivo = caminhoArquivo.concat(File.separator+listaDeArquivos[i]);
		}
		System.out.println("caminho construido: "+caminhoArquivo);
		return caminhoArquivo;
	}
	
	@Override
	public String toString() {
		return "BancoDeDadosGeRel [diretorioDeTrabalho=" + diretorioDeTrabalho + ", bancoDeDados=" + bancoDeDados.getAbsolutePath() + "]";
	}

	public File getDiretorioDeTrabalho() {
		return diretorioDeTrabalho;
	}

	public void setDiretorioDeTrabalho(File diretorioDeTrabalho) {
		this.diretorioDeTrabalho = diretorioDeTrabalho;
	}

	public File getBancoDeDados() {
		return bancoDeDados;
	}
}
