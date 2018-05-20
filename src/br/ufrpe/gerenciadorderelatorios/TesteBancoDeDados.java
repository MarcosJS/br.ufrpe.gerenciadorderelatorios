package br.ufrpe.gerenciadorderelatorios;

import java.io.File;

import br.ufrpe.gerenciadorderelatorios.excecoes.ArquivoOuDiretorioNaoExisteException;
import br.ufrpe.gerenciadorderelatorios.excecoes.ExclusaoDeArquivoOuDiretorioNegadaException;
import br.ufrpe.gerenciadorderelatorios.excecoes.JaExisteArquivoOuDiretorioException;
import br.ufrpe.gerenciadorderelatorios.excecoes.TipoDeArquivoDifereDoEsperadoException;
import br.ufrpe.gerenciadorderelatorios.model.BancoDeDadosGeRel;

public class TesteBancoDeDados {
	public static void main(String[] args) throws ArquivoOuDiretorioNaoExisteException, ExclusaoDeArquivoOuDiretorioNegadaException, TipoDeArquivoDifereDoEsperadoException{
		BancoDeDadosGeRel bancoTeste = new BancoDeDadosGeRel();
		bancoTeste.iniciarBancoDeDados();
		bancoTeste.toString();
		try {
			bancoTeste.adicionar("2135", "rel/8765.txt");
		} catch (JaExisteArquivoOuDiretorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File arquivo = null;
		System.out.println("Arquivo antes da busca: "+arquivo);
		arquivo = bancoTeste.consultar("2135", "rel/8765.txt");
		System.out.println("Arquivo antes da busca: "+arquivo);
		
		System.out.println("fim");
	}
}
