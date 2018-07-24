package br.ufrpe.gerenciadorderelatorios.control;

/**
 * Essa classe concentra as principais funcionalidades do sistema e coordena os objetos da camada de dados.
 * @author Daniel Bruno.
 * @author Marcos Jose.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.DiretorioNaoPodeSerCriadoException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.model.*;

public class NucleoGeRel {
	
	private PortifolioGeRel historicoSelecionado;
	private ArrayList<PortifolioGeRel> historicos;
	private BancoDeDadosGeRel bancoDeDados;
	
	/**
	 * Construtor NucleoGeRel que inicializa o banco de dados, carrega os dados do sistema.
	 * */
	public NucleoGeRel() {
		this.historicos = new ArrayList<PortifolioGeRel>();
		
		/*Iniciando o banco de dados.*/
		this.bancoDeDados = new BancoDeDadosGeRel();
		this.bancoDeDados.iniciarBancoDeDados(System.getProperty("user.dir"));
		
		/*Carregando sistema a partir do banco de dados.*/
		this.carregarHistoricos();
		
		this.prepararNovoHistorico();
	}
	
	/**
	 * Adiciona o histórico selecionado a lista de históricos e o armazena no banco de dados.
	 * */
	public void salvarHistorico() {
		if(this.historicoSelecionado.obterId() == null) {
			/*Acrescentando id do histórico ao relatório.*/
			String newId = "h"+String.format("%04d", this.obterQuantidadeHistoricos() + 1);
			this.historicoSelecionado.definirId(newId);
			this.historicos.add(this.historicoSelecionado);
			try {
				this.armazenarHistorico();
			} catch (DiretorioNaoPodeSerCriadoException | JaExisteArquivoOuDiretorioException e) {
				e.printStackTrace();
			}
		} else {
			try {
				this.armazenarHistorico();
			} catch (DiretorioNaoPodeSerCriadoException | JaExisteArquivoOuDiretorioException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Seleciona um histórico da lista.
	 * @param indice que representa a posição do histórico na lista.
	 * */
	public void selecionarHistorico(int indice) {
		if(indice >= 0 && indice < this.historicos.size()) {
			this.historicoSelecionado = this.historicos.get(indice);
		}
	}
	
	/**
	 * Armazena o histórico e os relatórios pertencentes no banco de dados separadademente.
	 * @throws DiretorioNaoPodeSerCriadoException.
	 * @throws JaExisteArquivoOuDiretorioException caso o arquivo já exista.
	 * */
	public void armazenarHistorico() throws DiretorioNaoPodeSerCriadoException, JaExisteArquivoOuDiretorioException {
		/*Salvando os historicos.*/
		String[] camHistorico = {BancoDeDadosGeRel.BD_HISTORICOS, this.historicoSelecionado.obterId()};
		Estrutura estHistorico = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(camHistorico)), this.historicoSelecionado.obterId()+".ser");

		try {
			this.bancoDeDados.adicionar(estHistorico, this.historicoSelecionado);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int i = 1;
		for(Relatorio r: this.historicoSelecionado.obterListaRelatorios()) {
			/*Salvando os relatorios.*/
			String[] camRelatorio = {BancoDeDadosGeRel.BD_HISTORICOS, this.historicoSelecionado.obterId(), "rel"};
			Estrutura estRelatorio = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(camRelatorio)), r.obterId()+".ser");
			
			try {
				System.out.println("\tentrando relatorio "+i+"...");
				this.bancoDeDados.adicionar(estRelatorio, r);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
	}
	
	/**
	 * Converte um arquivo pdf em um objeto Relatorio.
	 * @param arquivo que representa o arquivo pdf.
	 * */
	public void carregarArquivo(File arquivo) {
		RelatorioPDF relatorio = new RelatorioPDF(arquivo);
	    this.adicionarRelatorio(relatorio);
	}
	
	/**
	 * Adiciona um novo relatório ao histórico.
	 * @param rel que representa o relatorio a ser adicionado.
	 * */
	private void adicionarRelatorio(Relatorio rel) {
		int relAnterior = this.historicoSelecionado.obterQuantidadeRelatorios() - 1;
		rel.diff(this.historicoSelecionado.consultarRelatorio(relAnterior));
		this.historicoSelecionado.adicionarRelatorio(rel);
	}
	
	/**
	 * Retorna um vertor de String.
	 * @param indice que representa a posicao do relatório na lista.
	 * @return <code>String[]</code> que representa o relatorio selecionado.
	 * */
	public String[] obterRelatorio(int indice) {
		Relatorio relatorio = this.historicoSelecionado.consultarRelatorio(indice);
		String[] linhasTexto = new String[relatorio.obterQuantLinhas()];
		Linha[] linhas = relatorio.obterRelatorioOriginal();
		for(int i = 0; i < relatorio.obterQuantLinhas(); i++) {
			linhasTexto[i] = linhas[i].obterTexto();
		}
		return linhasTexto;
	}
	
	public Linha[] obterDiffRelatorio(int indice) {
		Relatorio relatorio = this.historicoSelecionado.consultarRelatorio(indice);
		int tamanho = relatorio.obterDiffRelatorio().size();
		Linha[] linhas = relatorio.obterDiffRelatorio().toArray(new Linha[tamanho]);
		
		return linhas;
	}
	
	public Linha[] obterNovas(int indice) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Carrega todos os históricos armazenados no banco de dados.
	 */
	private void carregarHistoricos() {
		
		Estrutura indice = this.bancoDeDados.obterEstruturaIndiceBanco().obterListaSubDiretorios()[0];
		if(indice.obterSubDiretorios() != null) {
			
			/*Obtendo estruturas 'indices' referentes a cada histórico.*/
			PortifolioGeRel historicoRecuperado = null;
			for(Estrutura e: indice.obterListaSubDiretorios()) {
				try {
					historicoRecuperado = (PortifolioGeRel) bancoDeDados.consultar(e.obterListaSubDiretorios()[0]);
					historicoRecuperado.definirRelatorios(new ArrayList<Relatorio>());
					System.out.println(historicoRecuperado);
					
					Relatorio relatorioRecuperado = null;
					for(int i = 1; i< e.obterListaSubDiretorios().length; i++) {
						relatorioRecuperado = (Relatorio) bancoDeDados.consultar(e.obterListaSubDiretorios()[i]);
						System.out.println(relatorioRecuperado);
						historicoRecuperado.adicionarRelatorio(relatorioRecuperado);
					}

					this.historicos.add(historicoRecuperado);
				} catch (ArquivoOuDiretorioNaoExisteException e1) {
					e1.printStackTrace();
				}
			}	
		}
	}
	
	/**
	 * Retorna o número de históticos.
	 * @return <code>Integer</code> que representa a quantidade.
	 * */
	public int obterQuantidadeHistoricos() {
		return this.historicos.size();
	}
	
	/**
	 * Retorna o número de relatórios do histórico selecionado.
	 * @return <code>Integer</code> que representa a quantidade.
	 * */
	public int obterQuantidadeRelatorios() {
		return this.historicoSelecionado.obterQuantidadeRelatorios();
	}
	
	/**
	 * Retorna o histórico selecionado.
	 * @return <code>PortifolioGeRel</code> que representa o histórico.
	 * */
	public PortifolioGeRel obterHistoricoSelecionado() {
		return this.historicoSelecionado;
	}
	
	/**
	 * Criar um novo histórico.
	 */
	public void prepararNovoHistorico() {
		this.historicoSelecionado = new PortifolioGeRel();
	}
	
	/**
	 * Retorna o identificador do histórico selecionado.
	 * @return <code>String</code> que representa o identificador.
	 * */
	public String obterId() {
		return this.historicoSelecionado.obterId();
	}

	public Linha[] obterExcluidos(int indice) {
		// TODO Auto-generated method stub
		return null;
	}

	public Linha[] obterEstaveis(int indice) {
		// TODO Auto-generated method stub
		return null;
	}
}