package br.ufrpe.gerenciadorderelatorios.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import br.ufrpe.gerenciadorderelatorios.control.NucleoGeRel;

public class TelaPrincipal extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TelaPrincipal() {
		super();
		
		NucleoGeRel controle = new NucleoGeRel();
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setSize(1280, 744);
    	this.setTitle("Ge Relatorio");
    	this.getContentPane().setLayout(new CardLayout(0, 0));
    	
    	JTabbedPane abas = new JTabbedPane(JTabbedPane.TOP);
    	abas.setBackground(Color.WHITE);
    	this.getContentPane().add(abas, "name_15804303378159");
    	
    	PainelDeSelecaoDeArquivo PainelArq = new PainelDeSelecaoDeArquivo(controle);
    	
    	SaidaSistema aba = new SaidaSistema(controle, new Color(230, 230, 255), new Color(150,255, 150));
    	
    	MenuSistema menu = new MenuSistema(controle, new Color(100, 149, 237), SystemColor.WHITE, PainelArq, aba);
    	
    	aba.add(menu);
    	
    	abas.addTab("Relatorio", null, aba, null);
	}
	
	public void renderizar() {
		this.setVisible(true);
	}
}
