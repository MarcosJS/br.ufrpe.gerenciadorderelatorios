package br.ufrpe.gerenciadorderelatorios;

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
		Estrutura estr = new Estrutura("dir_estrutura_teste", "nomeDaEstruturaTeste", null);
		
		try {
			bancoTeste.adicionar(bancoTeste.obterBancoDeDados(), estr, estr);
		} catch (JaExisteArquivoOuDiretorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(estr.obterId());
		Estrutura estrDes = (Estrutura) bancoTeste.consultar(bancoTeste.obterBancoDeDados(), estr);
		System.out.println("Arquivo antes da busca: "+estrDes.obterRaiz());
		
		System.out.println("fim");
	}
}
