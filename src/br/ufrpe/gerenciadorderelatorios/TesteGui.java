package br.ufrpe.gerenciadorderelatorios;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import br.ufrpe.gerenciadorderelatorios.control.ControleGeRelatorio;
import br.ufrpe.gerenciadorderelatorios.model.Linha;
import br.ufrpe.gerenciadorderelatorios.model.Relatorio;
import br.ufrpe.gerenciadorderelatorios.view.PainelTextoDestacado;
import br.ufrpe.gerenciadorderelatorios.view.Relatorios;

public class TesteGui {
	public static void main(String[] args) throws InvalidPasswordException, IOException, BadLocationException {
		ControleGeRelatorio control = new ControleGeRelatorio();
		File file = new File("C:\\testepdfbox\\consignadobb\\bbjan1.pdf");
		control.carregarRelatorio(file);
		/*String[] linhas = new String[control.getQuantLinhas()];
		Linha[] linha = control.relatorioTeste.getLinhas();
		for(int i = 0; i < control.relatorioTeste.getQuantLinhas(); i++) {
			System.out.print(linha[i].getLinha());
			linhas[i] = linha[i].getLinha();
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
