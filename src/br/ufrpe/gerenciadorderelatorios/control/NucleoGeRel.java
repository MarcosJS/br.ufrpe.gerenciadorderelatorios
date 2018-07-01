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
	
	private HistoricoGeRel historicoSelecionado;
	private HistoricoGeRel[] historicosVetor;
	private ArrayList<HistoricoGeRel> historicos;
	private BancoDeDadosGeRel bancoDeDados;
	
	public NucleoGeRel() {
		this.historicos = new ArrayList<HistoricoGeRel>();
		this.historicosVetor = new HistoricoGeRel[2];
		this.bancoDeDados = new BancoDeDadosGeRel();
		this.bancoDeDados.iniciarBancoDeDados(System.getProperty("user.dir"));
		this.historicosVetor[0] = new HistoricoGeRel();
		
		/*Acrescentando id do histórico ao relatório.*/
		String newId = "h"+String.format("%04d", this.obterQuantidadeHistoricos() + 1);
		this.historicosVetor[0].definirId(newId);
		
		this.historicosVetor[1] = new HistoricoGeRel();
		/*Acrescentando id do histórico ao relatório.*/
		newId = "h"+String.format("%04d", this.obterQuantidadeHistoricos() + 1);
		
		this.historicosVetor[1].definirId(newId);
		
		this.historicos.add(this.historicosVetor[0]);
		this.historicos.add(this.historicosVetor[1]);
		this.historicoSelecionado = this.historicosVetor[0];
	}
	
	public void selecionarHistorico(int indice) {
		/*if(indice >= 0 && indice < this.historicosVetor.length) {
			this.historicoSelecionado = this.historicosVetor[indice];
		}*/	
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
	    	linhasRel[i] = new Linha(pdfLinhas[i], i, i);
	    }
	    
	    Relatorio relatorio = new Relatorio(linhasRel);
	    this.adicionarRelatorio(relatorio);
	    
	    document.close();
	    
	}
	
	private void adicionarRelatorio(Relatorio rel) {
		this.historicoSelecionado.adicionarRelatorio(rel);
	}

	public String[] obterRelatorio(int indice) {
		Relatorio relatorio = this.historicoSelecionado.obterRelatorio(indice);
		String[] linhasTexto = new String[relatorio.obterQuantLinhas()];
		Linha[] linhas = relatorio.obterLinhas();
		for(int i = 0; i < relatorio.obterQuantLinhas(); i++) {
			linhasTexto[i] = linhas[i].obterTexto();
		}
		return linhasTexto;
	}
	
	public int obterQuantidadeHistoricos() {
		return this.historicos.size();
	}
	
	public int obterQuantidadeRelatorios() {
		return this.historicoSelecionado.obterQuantidadeRelatorios();
	}
	
	public String obterId() {
		return this.historicoSelecionado.obterId();
	}
}