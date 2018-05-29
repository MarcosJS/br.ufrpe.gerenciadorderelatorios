/**Realiza a conversão dos arquivos para inicialização dos históricos de relatórios.*/
package br.ufrpe.gerenciadorderelatorios.control;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import br.ufrpe.gerenciadorderelatorios.model.*;

public class NucleoGeRel {
	
	private Relatorio relatorio;
	private HistoricoGeRel historico;
	
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
	    
	    this.relatorio = new Relatorio(linhasRel);
	    
	    document.close();
	    
	}

	public String[] obterNovos(int indice) {
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
	
}
