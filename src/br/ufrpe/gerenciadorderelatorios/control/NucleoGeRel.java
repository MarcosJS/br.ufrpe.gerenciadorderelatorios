/***/
package br.ufrpe.gerenciadorderelatorios.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import br.ufrpe.gerenciadorderelatorios.excecoes.DiretorioNaoPodeSerCriadoException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.model.*;

public class NucleoGeRel {
	
	private Relatorio relatorio;
	private HistoricoGeRel historico;
	private BancoDeDadosGeRel bancoTeste;
	private Estrutura bdHistoricos = new Estrutura(null, BancoDeDadosGeRel.BD_HISTORICOS, null, null);
	private Estrutura bdAceso = new Estrutura(null, BancoDeDadosGeRel.BD_ACESSO, null, null);
	
	public NucleoGeRel() {
		this.historico = new HistoricoGeRel();
		this.historico.definirId("h0001");
		this.bancoTeste = new BancoDeDadosGeRel();
		bancoTeste.iniciarBancoDeDados(System.getProperty("user.dir"));
		
	}
	
	public void armazenarHistorico() throws DiretorioNaoPodeSerCriadoException, JaExisteArquivoOuDiretorioException {
		/*Salvando os historicos.*/
		String[] camHistorico = {this.bdHistoricos.obterDiretorioAtual(), this.historico.obterId()};
		Estrutura estHistorico = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(camHistorico)), this.historico.obterId()+".ser");

		try {
			this.bancoTeste.adicionar(estHistorico, this.historico);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		for(Relatorio r: this.historico.obterListaRelatorios()) {
			/*Salvando os relatorios.*/
			String[] camRelatorio = {this.bdHistoricos.obterDiretorioAtual(), this.historico.obterId(), "rel"};
			Estrutura estRelatorio = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(camRelatorio)), r.obterId()+".ser");
			
			try {
				System.out.println("\tentrando relatorio...");
				this.bancoTeste.adicionar(estRelatorio, r);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	    	linhasRel[i] = new Linha(pdfLinhas[i], i, i);
	    }
	    
	    //this.relatorio = new Relatorio(linhasRel);
	    Relatorio relatorio = new Relatorio(linhasRel);
	    this.adicionarRelatorio(relatorio);
	    
	    document.close();
	    
	}
	
	public void adicionarRelatorio(Relatorio rel) {
		this.historico.adicionarRelatorio(rel);
	}

	public String[] obterIncluidos(int indice) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterExcluidos(int indice) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterInalterados(int indice) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterLista(int indice) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterRelatorio(int indice) {
		Relatorio relatorio = this.historico.obterRelatorio(indice);
		String[] linhasTexto = new String[relatorio.obterQuantLinhas()];
		Linha[] linhas = relatorio.obterLinhas();
		for(int i = 0; i < relatorio.obterQuantLinhas(); i++) {
			linhasTexto[i] = linhas[i].obterLinha();
		}
		return linhasTexto;
	}
	
	public String[] obterRelatorio() {
		String[] linhasTexto = new String[relatorio.obterQuantLinhas()];
		Linha[] linhas = this.relatorio.obterLinhas();
		for(int i = 0; i < this.relatorio.obterQuantLinhas(); i++) {
			linhasTexto[i] = linhas[i].obterLinha();
		}
		return linhasTexto;
	}
	
	public int obterQuantidadeRelatorios() {
		return this.historico.obterQuantidadeRelatorios();
	}
}