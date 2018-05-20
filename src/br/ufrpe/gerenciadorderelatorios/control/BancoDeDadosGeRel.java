package br.ufrpe.gerenciadorderelatorios.control;

import java.io.File;

public class BancoDeDadosGeRel {
	private static final String NOME_BANCO_DE_DADOS = "bd_relatorios";
	//Obtendo diretorio atual
	private File diretorioDeTrabalho = new File(System.getProperty("user.dir"));
			
	
	void iniciarBancoDeDados() {
		
	}
	
	public boolean buscarBancoDeDados() {
		boolean existe = false;
		//Recebendo lista de subdiret�rios e arquivos
		File[] subDiretorios = this.diretorioDeTrabalho.listFiles();
		
		if(subDiretorios != null) {
			for(int i = 0; i < subDiretorios.length; i++) {
				//Verificando se o arquivo atual � o buscado e � um diretorio buscado
				if(subDiretorios[i].getName().equals(NOME_BANCO_DE_DADOS) && subDiretorios[i].isDirectory()) {
					existe = true;
				}
			} 
		}
		
		return existe;
	}
	
	void criarBancoDeDados() {
		
	}
}
