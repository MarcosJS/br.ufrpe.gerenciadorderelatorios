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
	
	public void diff(Relatorio relAnterior) {
		this.diffRelatorio = new ArrayList<Linha>();
		this.definirEstaveis(relAnterior);
		this.definirExcluidas(relAnterior);
	}
	
	private void definirEstaveis(Relatorio relAnterior) {
		if(relAnterior == null) {
			for(Linha linhaOriginal: this.relatorioOriginal) {
				Linha novaLinha = new Linha(linhaOriginal.obterTexto(), linhaOriginal.obterPosicaoOriginal());
				this.diffRelatorio.add(novaLinha);
			}
		} else {
			for (Linha linhaRelAtual : this.relatorioOriginal) {
				Linha novaLinha = new Linha(linhaRelAtual.obterTexto(), linhaRelAtual.obterPosicaoOriginal());
				for (Linha linhaRelAnterior : relAnterior.obterRelatorioOriginal()) {
					if (novaLinha.equals(linhaRelAnterior)) {
						novaLinha.definirCondicao(Condicao.ESTAVEL);
					}
				}
				this.diffRelatorio.add(novaLinha);
			}
		}
	}

	private void definirExcluidas(Relatorio relAnterior) {
		if (relAnterior != null) {
			for (Linha linhaRelAnterior : relAnterior.obterRelatorioOriginal()) {

				Condicao condicao = Condicao.EXCLUIDA;

				for (Linha linhaRelAtual : this.relatorioOriginal) {
					if (linhaRelAnterior.equals(linhaRelAtual)) {
						condicao = Condicao.ESTAVEL;
						break;
					}
				}

				if (condicao.equals(Condicao.EXCLUIDA)) {
					Linha linhaExcluida = new Linha(linhaRelAnterior.obterTexto(), linhaRelAnterior.obterPosicaoOriginal());
					linhaExcluida.definirCondicao(condicao);
					//Inserindo a linha excluida no diff
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
	
	public ArrayList<Linha> obterDiffRelatorio(Condicao condicao) {
		ArrayList<Linha> linhas = new ArrayList<Linha>();
		for(Linha l: this.diffRelatorio.toArray(new Linha[this.diffRelatorio.size()])) {
			if(l.obterCondicao().equals(condicao)) {
				linhas.add(l);
			}
		}
		return linhas;
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
