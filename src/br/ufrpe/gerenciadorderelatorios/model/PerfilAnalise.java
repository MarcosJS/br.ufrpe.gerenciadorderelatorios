package br.ufrpe.gerenciadorderelatorios.model;

public class PerfilAnalise {
	//Constantes de referencia
	public static final int ANALISE_POR_PAGINA = 0;
	public static final int ANALISE_POR_ARQUIVO = 1;

	//Atributos da classe
	private int tipoAnalise;
	private int linhaDeInicio;
	private int ultimaLinha;

	PerfilAnalise(int tipoAnalise, int inicio, int fim) {
		setTipoAnalise(tipoAnalise);
		setLinhaDeInicio(inicio);
		setUltimaLinha(fim);
	}

	public int getLinhaDeinicio() {
		return linhaDeInicio;
	}

	public void setLinhaDeInicio(int inicio) {
		this.linhaDeInicio = inicio;
	}

	public int getUltimaLinha() {
		return ultimaLinha;
	}

	public void setUltimaLinha(int fim) {
		this.ultimaLinha = fim;
	}

	public int getTipoAnalise() {
		return tipoAnalise;
	}

	public void setTipoAnalise(int tipoAnalise) {
		this.tipoAnalise = tipoAnalise;
	}
}
