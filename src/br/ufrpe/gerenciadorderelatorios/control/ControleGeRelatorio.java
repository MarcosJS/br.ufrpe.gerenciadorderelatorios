package br.ufrpe.gerenciadorderelatorios.control;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import br.ufrpe.gerenciadorderelatorios.model.*;

public class ControleGeRelatorio {
	
	public static final String CONSIG_ATUAL = null;
	public static final int BANCO_BRASIL = 0;
	private Relatorio relatorio;
	
	/* Converte o arquivo para objeto Relatorio.*/
	public void carregarRelatorio(File arquivo) throws InvalidPasswordException, IOException {
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

	public String[] obterListaNovos(int tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterListaExcluidos(int tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterListaInalterados(int tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterListaConsignacoes(int tipo, String consigAtual) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] obterListaTexto() {
		String[] linhasTexto = new String[this.relatorio.getQuantLinhas()];
		Linha[] linhas = this.relatorio.getLinhas();
		for(int i = 0; i < this.relatorio.getQuantLinhas(); i++) {
			linhasTexto[i] = linhas[i].getLinha();
		}
		return linhasTexto;
	}
	
}
