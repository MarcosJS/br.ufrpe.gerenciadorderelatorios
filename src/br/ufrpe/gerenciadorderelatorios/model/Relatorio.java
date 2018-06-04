package br.ufrpe.gerenciadorderelatorios.model;

public class Relatorio extends Gravavel{
	/**
	 * 
	 */
	private static transient  final long serialVersionUID = -9063960994820430012L;
	private Linha[] linhas;
	private String historico;
	//private PerfilAnalise perfil;
	
	public Relatorio(Linha[] linhas) {
		this.definirLinhas(linhas);
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

	@Override
	public String obterId() {
		return super.obterId();
	}
	
	@Override
	public void definirId(String id) {
		super.definirId(id);
		for(Linha l: this.linhas) {
			l.definirRelatorio(this.obterId());
		}
	}

}
