package br.ufrpe.gerenciadorderelatorios.excecoes;

public class ExclusaoDeArquivoOuDiretorioNegadaException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5216239632524143885L;

	public ExclusaoDeArquivoOuDiretorioNegadaException(String msg) {
		super(msg);
	}
}
