package br.ufrpe.gerenciadorderelatorios;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import br.ufrpe.gerenciadorderelatorios.control.NucleoGeRel;
import br.ufrpe.gerenciadorderelatorios.model.Linha;
import br.ufrpe.gerenciadorderelatorios.view.PainelTextoDestacado;
import br.ufrpe.gerenciadorderelatorios.view.Relatorios;

public class TesteGui {
	public static void main(String[] args) throws InvalidPasswordException, IOException, BadLocationException {
		/**NucleoGeRel control = new NucleoGeRel();
		File file = new File("C:\\testepdfbox\\consignadobb\\bbjan1.pdf");
		control.carregarRelatorioPdf(file);
		String[] linhas = new String[1000];
		Linha[] linha = control.o;
		for(int i = 0; i < control.relatorioTeste.getQuantLinhas(); i++) {
			System.out.print(linha[i].obterLinha());
			linhas[i] = linha[i].obterLinha();
		}
		PainelTextoDestacado painel = new PainelTextoDestacado(Relatorios.obterRelatorioRenderizado(linhas, "teste"), new Color(230, 230, 255), new Color(150,255, 150));
		painel.setEditable(false);
		JScrollPane scrool = new JScrollPane();
		scrool.setViewportView(painel);
		JFrame tela = new JFrame();
		tela.getContentPane().setBackground(Color.LIGHT_GRAY);
    	tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	tela.setSize(1136, 450);
    	tela.setTitle("ConsigDiff");
    	tela.getContentPane().setLayout(new CardLayout(0, 0));
    	tela.add(scrool);
    	tela.setVisible(true);*/
	}
}
