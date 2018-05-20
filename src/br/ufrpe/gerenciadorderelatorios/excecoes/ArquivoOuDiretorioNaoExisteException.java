package br.ufrpe.gerenciadorderelatorios.excecoes;

public class ArquivoOuDiretorioNaoExisteException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2172308820884046372L;

	public ArquivoOuDiretorioNaoExisteException(String msg){
        super(msg);
	}
}
