package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

public class RelatorioPDF extends Relatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RelatorioPDF(Linha[] linhas) {
		super(linhas);
	}
	
	public RelatorioPDF(File arquivo) {
		super(arquivo);
	}

	@Override
	Linha[] carregarArquivo(File arquivo) throws InvalidPasswordException, IOException {
		PDDocument document = null;
        document = PDDocument.load(arquivo);
	    PDFTextStripper stripper = new PDFTextStripper();
	    String pdfText = stripper.getText(document).toString();
	    String pdfLinhas[] = pdfText.split("\n");
	    Linha[] linhasRel = new Linha[pdfLinhas.length];
	    
	    for(int i = 0; i < pdfLinhas.length; i++) {
	    	linhasRel[i] = new Linha(pdfLinhas[i], i);
	    }
	    
	    document.close();
	    
	    return linhasRel;	    
	}

}
