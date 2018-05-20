package br.ufrpe.gerenciadorderelatorios;

import br.ufrpe.gerenciadorderelatorios.control.BancoDeDadosGeRel;

public class TesteBancoDeDados {
	public static void main(String[] args) {
		BancoDeDadosGeRel bancoTeste = new BancoDeDadosGeRel();
		System.out.println(bancoTeste.buscarBancoDeDados());
	}
}
