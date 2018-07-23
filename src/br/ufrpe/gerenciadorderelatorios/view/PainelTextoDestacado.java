package br.ufrpe.gerenciadorderelatorios.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
//import javax.swing.text.StyledDocument;

import br.ufrpe.gerenciadorderelatorios.model.Linha;

public class PainelTextoDestacado extends JTextPane {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	//private Color corFaixas;
	private Color corSelecao;
	private Linha[] linhas;

	public PainelTextoDestacado(Linha[] linhas, String cabecalho/*StyledDocument documento*/, Color corFaixas, Color corSelecao) throws BadLocationException {
		super(Relatorios.obterRelatorioRenderizado(linhas, cabecalho)/*documento*/);
		setOpaque(false);
		//this.setCorFaixas(corFaixas);
		this.setCorSelecao(corSelecao);
		this.linhas = linhas;
	}
	@Override
	protected void paintComponent(Graphics g) {
	    g.setColor(getBackground());
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
	    System.out.println("tamaho da area de texto: "+this.getHeight());
	    
	    int linhasInternas = (this.getHeight() - 32) / 15;
	    
	    System.out.println("posiveis numero de linhas internas"+linhasInternas);
	    
	    int inicio = 19;
	    
	    for(int i = 0; i< linhasInternas; i++) {
	    	Rectangle rect;
			try {
				rect = modelToView(17);
				g.setColor(this.escolherCorDiff(linhas[i])/*this.corFaixas*/);
		        g.fillRect(0, inicio, getWidth(), rect.height);
		        //inicio += 30;
		        inicio += 15;
		    } catch (BadLocationException e) {
				e.printStackTrace();
			}
	    }

	    
	    try {
	      Rectangle rect = modelToView(getCaretPosition());
	      System.out.println("posicao do cursor: "+rect.getY());
	      if (rect != null) {
	        g.setColor(this.corSelecao);
	        g.fillRect(0, rect.y, getWidth(), rect.height);
	        System.out.println("altura do retangulo: "+rect.height);
	      }
	    } catch (BadLocationException e) {
	      System.out.println(e);	
	    }
	    	    
	    super.paintComponent(g);
	  }

	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
	    super.repaint(tm, 0, 0, getWidth(), getHeight());
	}
	
	private Color escolherCorDiff(Linha linha) {
		Color cor = new Color(255, 255, 255);
		switch(linha.obterCondicao()) {
		case NOVA:
			cor = new Color(230, 255, 230);
			break;
		case EXCLUIDA:
			cor = new Color(255, 230, 230);
			break;
		default:
			break;
		}
		return cor;
	}
	
	/*public void setCorFaixas(Color corFaixas) {
		this.corFaixas = corFaixas;
	}*/
	
	public void setCorSelecao(Color corSelecao) {
		this.corSelecao = corSelecao;
	}
	
}
