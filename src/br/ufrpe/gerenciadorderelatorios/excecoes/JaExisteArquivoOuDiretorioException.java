package br.ufrpe.gerenciadorderelatorios.excecoes;

public class JaExisteArquivoOuDiretorioException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4048831319083844485L;

	public JaExisteArquivoOuDiretorioException(String msg){
        super(msg);
	}
}
