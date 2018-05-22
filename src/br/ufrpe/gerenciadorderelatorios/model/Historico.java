package br.ufrpe.gerenciadorderelatorios.model;

import java.io.Serializable;
import java.util.ArrayList;


public class Historico implements Serializable, IGravavel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1498544530598941169L;
	private String id;
	private transient ArrayList<Relatorio> relatorios;
	private Estrutura estrurura;
	//private transient PerfilAnalise perfil;
	
	public Historico(Relatorio relatorio, String id) {
		this.definirRelatorios(new ArrayList<Relatorio>());
		this.adicionarRelatorio(relatorio);
		/*Criando estrutura padrão de diretórios.(historicos/id/rel)*/
		Estrutura rel = new Estrutura("rel", null);
		Estrutura historico = new Estrutura(id, new Estrutura[] {rel});
		Estrutura historicos = new Estrutura("historicos", new Estrutura[] {historico});
		this.difinirEstrutura(historicos);
	}
	
	/**Adiciona um relatório ao histórico desde que aquele ainda não exista.
	 * @author Marcos Jose*/
	public void adicionarRelatorio(Relatorio rel) {
		/*Verifica se o a lista de relatorios não é nula e se ainda não existe o relatório.*/
		if((relatorios != null) && (this.ConsultarRelatorio(rel.obterId()) != null)) {
			this.relatorios.add(rel);
		}
	}
	
	private Relatorio ConsultarRelatorio(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int obterQuantRelatorios() {
		return this.obterRelatorios().size();
	}
	
	public ArrayList<Relatorio> obterRelatorios() {
		return relatorios;
	}
	
	public void definirRelatorios(ArrayList<Relatorio> relatorios) {
		this.relatorios = relatorios;
	}

	public String obterId() {
		return id;
	}

	public void definirId(String id) {
		this.id = id;
	}

	@Override
	public String obterIdOriginador() {
		return null;
	}

	public Estrutura obterEstrutura() {
		return this.estrurura;
	}

	private void difinirEstrutura(Estrutura estrurura) {
		this.estrurura = estrurura;
	}
}
