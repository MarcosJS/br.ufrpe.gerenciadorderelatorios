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
	private Color corSelecao;
	private Linha[] linhas;

	public PainelTextoDestacado(Linha[] linhas, String cabecalho, Color corFaixas, Color corSelecao) throws BadLocationException {
		super(Relatorios.obterRelatorioRenderizado(linhas, cabecalho));
		setOpaque(false);
		this.setCorSelecao(corSelecao);
		this.linhas = linhas;
	}
	@Override
	protected void paintComponent(Graphics g) {
	    g.setColor(getBackground());
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
	    int inicio = 35;
	    
	    for(int i = 0; i< this.linhas.length; i++) {
	    	Rectangle rect;
			try {
				rect = modelToView(17);
				g.setColor(this.escolherCorDiff(linhas[i])/*this.corFaixas*/);
				g.fillRect(0, inicio, getWidth(), rect.height);
				inicio += 15;
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			
	    }

	    
	    try {
	      Rectangle rect = modelToView(getCaretPosition());
	      
	      if (rect != null) {
	    	  Color newCorSelecao = this.corSelecao;
	    	  if(rect.y >= 34) {
	    		  newCorSelecao = this.escolherCorDiffSelecao(linhas[(rect.y - 34) / 15]);
	    	  }
	    	  g.setColor(newCorSelecao);
	    	  g.fillRect(0, rect.y, getWidth(), rect.height);
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
			cor = new Color(210, 255, 210);
			break;
		case EXCLUIDA:
			cor = new Color(255, 210, 210);
			break;
		default:
			break;
		}
		return cor;
	}
	
	private Color escolherCorDiffSelecao(Linha linha) {
		Color cor = new Color(230, 230, 230);
		switch(linha.obterCondicao()) {
		case NOVA:
			cor = new Color(170, 255, 170);
			break;
		case EXCLUIDA:
			cor = new Color(255, 170, 170);
			break;
		default:
			break;
		}
		return cor;
	}
	
	public void setCorSelecao(Color corSelecao) {
		this.corSelecao = corSelecao;
	}
	
}
