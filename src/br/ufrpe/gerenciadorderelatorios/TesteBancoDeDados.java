package br.ufrpe.gerenciadorderelatorios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.TipoDeArquivoDifereDoEsperadoException;
import br.ufrpe.gerenciadorderelatorios.model.BancoDeDadosGeRel;
import br.ufrpe.gerenciadorderelatorios.model.Estrutura;

public class TesteBancoDeDados {
	public static void main(String[] args) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException, TipoDeArquivoDifereDoEsperadoException{
		BancoDeDadosGeRel bancoTeste = new BancoDeDadosGeRel();
		bancoTeste.iniciarBancoDeDados(System.getProperty("user.dir"));
		bancoTeste.toString();
		
		String[] pastas2 = {BancoDeDadosGeRel.BD_ACESSO, "dir_estrutura", "pasta_1", "pasta_2", "pasta_final"};
		Estrutura est2 = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(pastas2)), "funcaoMontarEstrutura.ser");
		Estrutura est = null;
		while(est2 != null) {
			System.out.println("diretorio atual: "+est2.obterDiretorioAtual());
			System.out.println("caminho ate o ancestral: "+est2.obterCaminhoAncestrais());
			if(est2.obterPai() == null) {
				System.out.println("pai: "+est2.obterPai());
			} else {
				System.out.println("pai: "+est2.obterPai().obterDiretorioAtual());
			}
			
			if(est2.obterListaSubDiretorios() != null) {
				est2 = est2.obterListaSubDiretorios()[0];
			} else {
				est = est2;
				est2 = null;
			}
		}
		System.out.println("diretorio atual da est: "+est.obterDiretorioAtual());
		
		System.out.println("raiz da est: "+est.obterRaiz().obterDiretorioAtual());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		
		//File file = new File(System.getProperty("user.dir"));

		/*try {
			bancoTeste.adicionar(bancoTeste.obterBancoDeDados(), estr, estr);
		} catch (JaExisteArquivoOuDiretorioException e) {
			e.printStackTrace();
		}
		
		try {
			bancoTeste.adicionar(bancoTeste.obterBancoDeDados(), est2, est2);
		} catch (JaExisteArquivoOuDiretorioException e) {
			e.printStackTrace();
		}
		
		Estrutura estrDes;
		
		try {
			estrDes = (Estrutura) bancoTeste.consultar(bancoTeste.obterBancoDeDados(), estr);
			System.out.println("Arquivo retornado: "+estrDes.obterRaiz()+" "+estrDes.obterNomeArquivo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bancoTeste.remover(bancoTeste.obterBancoDeDados(), estr);
		
		try {
			estrDes = (Estrutura) bancoTeste.consultar(bancoTeste.obterBancoDeDados(), estr);
			System.out.println("Arquivo retornado: "+estrDes.obterRaiz()+" "+estrDes.obterNomeArquivo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		System.out.println("fim");
	}
}
