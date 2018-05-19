package br.ufrpe.gerenciadorderelatorios.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import br.ufrpe.gerenciadorderelatorios.control.ControleGeRelatorio;

import javax.swing.JButton;

public class TelaPrincipal extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TelaPrincipal() {
		super();
		
		ControleGeRelatorio controle = new ControleGeRelatorio();
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setSize(1136, 450);
    	this.setTitle("Ge Relatorio");
    	this.getContentPane().setLayout(new CardLayout(0, 0));
    	
    	JTabbedPane abas = new JTabbedPane(JTabbedPane.TOP);
    	abas.setBackground(Color.WHITE);
    	this.getContentPane().add(abas, "name_15804303378159");
    	
    	PainelDeSelecaoDeArquivo arqBB = new PainelDeSelecaoDeArquivo(controle);
    	//SelArqPanelBra arqBra = new SelArqPanelBra(controle);
    	
    	AbaSistema aba = new AbaSistema(controle, ControleGeRelatorio.BANCO_BRASIL, new Color(230, 230, 255), new Color(150,255, 150));
    	//AbaSistema abaBra = new AbaSistema(controle, ControlDiff.BRADESCO, new Color(255, 230, 230), new Color(150, 150, 255));
    	
    	MenuSistema menuBB = new MenuSistema(controle, new Color(100, 149, 237), SystemColor.WHITE, arqBB, aba);
    	//MenuSistema menuBra = new MenuSistema(controle, SystemColor.RED, SystemColor.WHITE, arqBra, abaBra);
    	
    	//menuBra.setBackground(SystemColor.WHITE);  	
    	
    	aba.add(menuBB);
    	//abaBra.add(menuBra);
    	abas.addTab("Relatorio", null, aba, null);
    	
    	JButton bSalvar = new JButton("Salvar");
    	bSalvar.setToolTipText("Salva documento em formato pdf");
    	bSalvar.setBackground(new Color(100, 149, 237));
    	bSalvar.setForeground(Color.WHITE);
    	bSalvar.setBounds(690, 0, 89, 23);
    	aba.add(bSalvar);
    	//abas.addTab("Bradesco", null, abaBra, null);
	}
	
	public void renderizar() {
		this.setVisible(true);
	}
}
