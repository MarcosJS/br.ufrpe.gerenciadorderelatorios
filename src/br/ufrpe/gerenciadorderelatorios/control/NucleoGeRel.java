/***/
package br.ufrpe.gerenciadorderelatorios.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.DiretorioNaoPodeSerCriadoException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.model.*;

public class NucleoGeRel {
	
	private PortifolioGeRel historicoSelecionado;
	private ArrayList<PortifolioGeRel> historicos;
	private BancoDeDadosGeRel bancoDeDados;
	
	public NucleoGeRel() {
		this.historicos = new ArrayList<PortifolioGeRel>();
		
		/*Iniciando o banco de dados.*/
		this.bancoDeDados = new BancoDeDadosGeRel();
		this.bancoDeDados.iniciarBancoDeDados(System.getProperty("user.dir"));
		
		/*Carregando sistema a partir do banco de dados.*/
		this.carregarHistoricos();
		
		this.prepararNovoHistorico();
	}
	
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
			
		}
	}

	public void selecionarHistorico(int indice) {
		if(indice >= 0 && indice < this.historicos.size()) {
			this.historicoSelecionado = this.historicos.get(indice);
		}
	}
	
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
	
	/* Converte o arquivo para objeto Relatorio.*/
	public void carregarRelatorioPdf(File arquivo) throws InvalidPasswordException, IOException {
		PDDocument document = null;
        document = PDDocument.load(arquivo);
	    PDFTextStripper stripper = new PDFTextStripper();
	    String pdfText = stripper.getText(document).toString();
	    String pdfLinhas[] = pdfText.split("\n");
	    Linha[] linhasRel = new Linha[pdfLinhas.length];
	    
	    for(int i = 0; i < pdfLinhas.length; i++) {
	    	linhasRel[i] = new Linha(pdfLinhas[i], i);
	    }
	    
	    Relatorio relatorio = new Relatorio(linhasRel);
	    this.adicionarRelatorio(relatorio);
	    
	    document.close();
	    
	}
	
	private void adicionarRelatorio(Relatorio rel) {
		this.historicoSelecionado.adicionarRelatorio(rel);
	}

	public String[] obterRelatorio(int indice) {
		Relatorio relatorio = this.historicoSelecionado.consultarRelatorio(indice);
		String[] linhasTexto = new String[relatorio.obterQuantLinhas()];
		Linha[] linhas = relatorio.obterLinhas();
		for(int i = 0; i < relatorio.obterQuantLinhas(); i++) {
			linhasTexto[i] = linhas[i].obterTexto();
		}
		return linhasTexto;
	}
	
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
	
	public int obterQuantidadeHistoricos() {
		return this.historicos.size();
	}
	
	public int obterQuantidadeRelatorios() {
		return this.historicoSelecionado.obterQuantidadeRelatorios();
	}
	
	public PortifolioGeRel obterHistoricoSelecionado() {
		return this.historicoSelecionado;
	}
	
	public void prepararNovoHistorico() {
		this.historicoSelecionado = new PortifolioGeRel();
	}
	
	public String obterId() {
		return this.historicoSelecionado.obterId();
	}
}