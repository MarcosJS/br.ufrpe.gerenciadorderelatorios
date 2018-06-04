package br.ufrpe.gerenciadorderelatorios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import br.ufrpe.gerenciadorderelatorios.excecoes.DiretorioNaoPodeSerCriadoException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.model.BancoDeDadosGeRel;
import br.ufrpe.gerenciadorderelatorios.model.Estrutura;
import br.ufrpe.gerenciadorderelatorios.model.HistoricoGeRel;
import br.ufrpe.gerenciadorderelatorios.model.Linha;
import br.ufrpe.gerenciadorderelatorios.model.Relatorio;

public class TesteGeRel {
	
	private HistoricoGeRel historico;
	private BancoDeDadosGeRel bancoDeDados;
	
	public TesteGeRel() {
		this.bancoDeDados = new BancoDeDadosGeRel();
		bancoDeDados.iniciarBancoDeDados(System.getProperty("user.dir"));
		this.historico = new HistoricoGeRel();
		this.historico.definirId("h0001");
	}
	
	public void armazenarHistorico() throws DiretorioNaoPodeSerCriadoException, JaExisteArquivoOuDiretorioException {
		/*Salvando os historicos.*/
		String[] camHistorico = {BancoDeDadosGeRel.BD_HISTORICOS, this.historico.obterId()};
		Estrutura estHistorico = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(camHistorico)), this.historico.obterId()+".ser");

		try {
			this.bancoDeDados.adicionar(estHistorico, this.historico);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int i = 1;
		for(Relatorio r: this.historico.obterListaRelatorios()) {
			/*Salvando os relatorios.*/
			String[] camRelatorio = {BancoDeDadosGeRel.BD_HISTORICOS, this.historico.obterId(), "rel"};
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
	    	linhasRel[i] = new Linha(pdfLinhas[i], i, i);
	    }
	    
	    //this.relatorio = new Relatorio(linhasRel);
	    Relatorio relatorio = new Relatorio(linhasRel);
	    this.adicionarRelatorio(relatorio);
	    
	    document.close();
	    
	}
	
	private void adicionarRelatorio(Relatorio rel) {
		this.historico.adicionarRelatorio(rel);
	}

	public String[] obterLista(int indice) {
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
	
	public int obterQuantidadeRelatorios() {
		return this.historico.obterQuantidadeRelatorios();
	}

	public static void main(String[] args) {
		
	}
	
}
