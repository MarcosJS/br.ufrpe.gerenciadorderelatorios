package br.ufrpe.gerenciadorderelatorios.model;

public class Relatorio extends Gravavel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9063960994820430012L;
	private Linha[] linhas;
	private String historico;
	private Estrutura estrutura;
	//private int ordem;
	//private PerfilAnalise perfil;
	
	public Relatorio(Linha[] linhas) {
		this.definirLinhas(linhas);
		
		/*Criando estrutura padrão de diretórios.(historicos/historico/rel)*/
		Estrutura rel = new Estrutura("rel", null);
		Estrutura historico = new Estrutura(this.obterHistorico(), new Estrutura[] {rel});
		Estrutura bd = new Estrutura("bd_historicos", new Estrutura[] {historico});
		this.definirEstrutura(bd);
	}
	
	public Linha[] obterLinhas() {
		return linhas;
	}

	private void definirLinhas(Linha[] linhas) {
		this.linhas = linhas;
	}
	
	public int obterQuantLinhas() {
		return linhas.length;
	}

	public String obterHistorico() {
		return historico;
	}

	public void definirHistorico(String historico) {
		this.historico = historico;
	}
}
