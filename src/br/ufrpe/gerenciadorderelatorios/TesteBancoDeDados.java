package br.ufrpe.gerenciadorderelatorios;

import java.util.ArrayList;
import java.util.Arrays;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.excecoes.TipoDeArquivoDifereDoEsperadoException;
import br.ufrpe.gerenciadorderelatorios.model.BancoDeDadosGeRel;
import br.ufrpe.gerenciadorderelatorios.model.Estrutura;

public class TesteBancoDeDados {
	public static void main(String[] args) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException, TipoDeArquivoDifereDoEsperadoException{
		BancoDeDadosGeRel bancoTeste = new BancoDeDadosGeRel();
		bancoTeste.iniciarBancoDeDados(System.getProperty("user.dir"));
		bancoTeste.toString();
		
		String[] pastas = {BancoDeDadosGeRel.BD_HISTORICOS, "dir_estrutura_teste"};
		String[] pastas2 = {BancoDeDadosGeRel.BD_ACESSO, "dir_estrutura", "pasta_1", "pasta_2", "pasta_final"};
		Estrutura estr = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(pastas)), "nomeDoArquivoTeste.ser");
		Estrutura est2 = Estrutura.montarEstrutura(new ArrayList <String> (Arrays.asList(pastas2)), "funcaoMontarEstrutura.ser");
		System.out.println(est2.obterCaminho());		
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
