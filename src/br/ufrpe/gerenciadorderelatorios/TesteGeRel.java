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
	
	private HistoricoGeRel historicoSelecionado;
	private HistoricoGeRel[] historicosVetor;
	private BancoDeDadosGeRel bancoDeDados;
	
	public TesteGeRel() {
		historicosVetor = new HistoricoGeRel[2];
		this.bancoDeDados = new BancoDeDadosGeRel();
		bancoDeDados.iniciarBancoDeDados(System.getProperty("user.dir"));
		this.historicosVetor[0] = new HistoricoGeRel();
		this.historicosVetor[0].definirId("h0001");
		this.historicosVetor[1] = new HistoricoGeRel();
		this.historicosVetor[1].definirId("h0002");
		this.historicoSelecionado = this.historicosVetor[0];
	}
	
	public static void main(String[] args) {
		
		TesteGeRel teste = new TesteGeRel();
		System.out.println("-------------------------------------------SCRIPT DE EXECUÇÃO--------------------------------------------------\n");
		
		System.out.println("- Selecionando os arquivos para serem carregados.");
		
		File[] conjunto1 = new File[3];
		
		conjunto1[0] = new File(System.getProperty("user.dir")+File.separator+"testepdf"+File.separator+"jan.pdf");
		conjunto1[1] = new File(System.getProperty("user.dir")+File.separator+"testepdf"+File.separator+"fev.pdf");
		conjunto1[2] = new File(System.getProperty("user.dir")+File.separator+"testepdf"+File.separator+"mar.pdf");
		
		File[] conjunto2 = new File[3];
		
		conjunto2[0] = new File(System.getProperty("user.dir")+File.separator+"testepdf"+File.separator+"bbjan1.pdf");
		conjunto2[1] = new File(System.getProperty("user.dir")+File.separator+"testepdf"+File.separator+"bbfev1.pdf");
		conjunto2[2] = new File(System.getProperty("user.dir")+File.separator+"testepdf"+File.separator+"bbmar1.pdf");
		
		for(int i = 0; i < 3; i++) {
			System.out.println(conjunto1[i].getAbsolutePath());
		}
		
		for(int i = 0; i < 3; i++) {
			System.out.println(conjunto2[i].getAbsolutePath());
		}
		
		System.out.println("---------------------------------------------------------------------------------------------------------------\n");
		System.out.println("- Historico selecionado: "+teste.historicoSelecionado.obterId());
		System.out.println("- Carregando o primeiro conjunto de relatorios.");
				
		for(int i = 0; i < 3; i++) {
			try {
				teste.carregarRelatorioPdf(conjunto1[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("---------------------------------------------------------------------------------------------------------------\n");
		System.out.println("- Selecionando o segundo historico.");
		teste.selecionarHistorico(1);
		System.out.println("- Historico selecionado: "+teste.historicoSelecionado.obterId());
		
		System.out.println("- Carregando o segundo conjunto de relatorios.");
		
		for(int i = 0; i < 3; i++) {
			try {
				teste.carregarRelatorioPdf(conjunto2[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void selecionarHistorico(int indice) {
		if(indice >= 0 && indice < this.historicosVetor.length) {
			this.historicoSelecionado = this.historicosVetor[indice];
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

	public String[] obterLista(int indice) {
		return null;
	}

	public String[] obterRelatorio(int indice) {
		Relatorio relatorio = this.historicoSelecionado.obterRelatorio(indice);
		String[] linhasTexto = new String[relatorio.obterQuantLinhas()];
		Linha[] linhas = relatorio.obterLinhas();
		for(int i = 0; i < relatorio.obterQuantLinhas(); i++) {
			linhasTexto[i] = linhas[i].obterLinha();
		}
		return linhasTexto;
	}
	
	public int obterQuantidadeRelatorios() {
		return this.historicoSelecionado.obterQuantidadeRelatorios();
	}
}
