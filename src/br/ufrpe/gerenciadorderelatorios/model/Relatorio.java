package br.ufrpe.gerenciadorderelatorios.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

/**
 * Essa classe abstrai um relatório ou documento de texto.
 * @author Marcos Jose.
 */

public abstract class Relatorio extends Gravavel{
	private static transient  final long serialVersionUID = -9063960994820430012L;
	private Linha[] relatorioOriginal;
	private ArrayList<Linha> diffRelatorio;
	private String historico;
	
	/**
	 * Construtor Relatorio.
	 * @param linhas[] que representa todas as linhas do relatório.
	 */
	public Relatorio(Linha[] linhas) {
		this.definirRelatorioOriginal(linhas);
	}
	
	/**
	 * Construtor Relatorio.
	 * @param arquivo que representa o arquivo pdf.
	 * */
	public Relatorio(File arquivo) {
		try {
			this.relatorioOriginal = this.carregarArquivo(arquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo abstrato que converte um arquivo pdf em um objeto Relatorio.
	 * @param arquivo que representa o arquivo pdf.
	 * @return um <code>Linha[]</code> que representa todas as linhas extraidas do pdf.
	 * @throws InvalidPasswordException dependendo da implementação caso haja um erro no carregamento do arquivo.
	 * @throws IOException dependendo da implementação caso haja um erro no carregamento do arquivo. 
	 **/
	abstract Linha[] carregarArquivo(File arquivo) throws InvalidPasswordException, IOException;
	
	public void diff(Relatorio rel) {
		this.diffRelatorio = new ArrayList<Linha>();
		this.definirEstaveis(rel);
		this.definirExcluidas(rel);
	}
	
	private void definirEstaveis(Relatorio rel) {
		if(rel == null) {
			for(Linha l: this.relatorioOriginal) {
				Linha novaLinha = new Linha(l.obterTexto(), l.obterPosicaoOriginal());
				this.diffRelatorio.add(novaLinha);
			}
		} else {
			for (Linha l : this.relatorioOriginal) {
				for (Linha lRel : rel.obterRelatorioOriginal()) {
					Linha novaLinha = new Linha(l.obterTexto(), l.obterPosicaoOriginal());
					if (novaLinha.equals(lRel)) {
						novaLinha.definirCondicao(Condicao.ESTAVEL);
					}
					this.diffRelatorio.add(novaLinha);
				}
			} 
		}
	}
	
	private void definirExcluidas(Relatorio rel) {
		if (rel != null) {
			for (Linha lRelAnterior : rel.obterRelatorioOriginal()) {

				Condicao condicao = Condicao.EXCLUIDA;

				for (Linha l : this.relatorioOriginal) {
					if (lRelAnterior.equals(l)) {
						condicao = Condicao.ESTAVEL;
						break;
					}
				}

				if (condicao.equals(Condicao.EXCLUIDA)) {
					Linha linhaExcluida = new Linha(lRelAnterior.obterTexto(), lRelAnterior.obterPosicaoOriginal());
					linhaExcluida.definirCondicao(condicao);
					this.diffRelatorio.add(linhaExcluida.calcNovaPosicao(this.diffRelatorio), linhaExcluida);
				}
			} 
		}
	}
	
	/**
	* Obtêm relatorioOriginal[]. 
	* @return um <code>String[]</code> que representa todas as linhas do relatorio original.
	*/	
	public Linha[] obterRelatorioOriginal() {
		return relatorioOriginal;
	}
	
	/**
	 * Defini linhas[].
	 * @param linhas[] que representa todas as linhas do relatório.
	 */
	private void definirRelatorioOriginal(Linha[] linhas) {
		this.relatorioOriginal = linhas;
	}
	
	public ArrayList<Linha> obterDiffRelatorio() {
		return diffRelatorio;
	}

	/**
	 * Obtêm o numero de linhas do relatório.
	 * @return um <code>integer</code> que representa a quantidade de linhas do relatório.
	 */
	public int obterQuantLinhas() {
		return relatorioOriginal.length;
	}
	
	/**
	 * Obtêm o id do histórico o qual o relatório pertence.
	 * @return um <code>integer</code> que representa a quantidade de linhas do relatório.
	 */
	public String obterHistorico() {
		return historico;
	}
	
	/**
	 * Defini o id do histórico o qual o relatório pertence.
	 * @param historico que representa o id do histórico.
	 */
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
		for(Linha l: this.relatorioOriginal) {
			l.definirRelatorio(this.obterId());
		}
	}

	@Override
	public String toString() {
		return "Relatorio [id=" + this.obterId() + ", historico=" + historico + "]";
	}
}
