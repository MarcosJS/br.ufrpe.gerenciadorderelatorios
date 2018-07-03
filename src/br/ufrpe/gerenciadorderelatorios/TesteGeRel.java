package br.ufrpe.gerenciadorderelatorios;

import java.io.File;
import java.io.IOException;
import br.ufrpe.gerenciadorderelatorios.control.NucleoGeRel;
import br.ufrpe.gerenciadorderelatorios.excecoes.DiretorioNaoPodeSerCriadoException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;

public class TesteGeRel {
	
	public static void main(String[] args) {
		
		String[] relatorioExibido;
		NucleoGeRel teste = new NucleoGeRel();
		System.out.println("------------------------------------------ SCRIPT DE TESTE DO GEREL -------------------------------------------------\n");
		
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
		System.out.println("- Historico selecionado: "+teste.obterId());
		System.out.println("- Carregando o primeiro conjunto de relatorios.");
				
		for(int i = 0; i < 3; i++) {
			
			try {
				teste.carregarRelatorioPdf(conjunto1[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		teste.salvarHistorico();
		teste.prepararNovoHistorico();
		
		System.out.println("---------------------------------------------------------------------------------------------------------------\n");
		System.out.println("- Selecionando o segundo historico.");
		System.out.println("- Historico selecionado: "+teste.obterId());
		
		System.out.println("- Carregando o segundo conjunto de relatorios.");
		
		for(int i = 0; i < 3; i++) {
			
			try {
				teste.carregarRelatorioPdf(conjunto2[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		teste.salvarHistorico();
		teste.prepararNovoHistorico();
		
		System.out.println("---------------------------------------------------------------------------------------------------------------\n");
		System.out.println("- Lendo todos os relatórios de cada histórico.\n");
		
		for(int j = 0; j < teste.obterQuantidadeHistoricos(); j++) {
			
			System.out.println("===============================================================================================================");
			System.out.println("- Imprimindo todos os relatorios do "+(j+1)+"º histórico de relatorios.");
			teste.selecionarHistorico(j);
			
			for(int i = 0; i < teste.obterQuantidadeRelatorios(); i++) {
				
				System.out.println("---------------------------------------------------------------------------------------------------------------\n");
				System.out.println("- Imprimindo o "+(i+1)+"/"+teste.obterQuantidadeRelatorios()+" relatorios.");
				relatorioExibido = teste.obterRelatorio(i);
				
				for(String s: relatorioExibido) {
					System.out.println(s);
				}
				
			}
			
		}
		
		System.out.println("===============================================================================================================\n");
		System.out.println("- Salvando os historicos em um banco de dados.\n");
				
		for(int i = 0; i < teste.obterQuantidadeHistoricos(); i++) {
			
			System.out.println("---------------------------------------------------------------------------------------------------------------\n");
			System.out.println("- Selecionando o "+(i+1)+"/"+teste.obterQuantidadeHistoricos()+" historicos.");
			teste.selecionarHistorico(i);
			System.out.println("- Historico selecionado: "+teste.obterId());
			
			try {
				teste.armazenarHistorico();
			} catch (DiretorioNaoPodeSerCriadoException | JaExisteArquivoOuDiretorioException e) {
				e.printStackTrace();
			}
		
		}
		
		System.out.println("-------------------------------------- FIM DO SCRIPT DE TESTE DO GEREL ----------------------------------------------\n");
	
	}
	
}
