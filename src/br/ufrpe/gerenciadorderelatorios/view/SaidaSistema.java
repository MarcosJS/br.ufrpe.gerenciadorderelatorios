package br.ufrpe.gerenciadorderelatorios.view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import br.ufrpe.gerenciadorderelatorios.control.NucleoGeRel;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JMenuBar;

public class SaidaSistema extends JPanel {
	public static enum TipoRelatorio {
		COMPLETO, INCLUIDOS, EXCLUIDOS, INALTERADOS;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TipoRelatorio tipoRelatorio;
	private int indice;
	private JScrollPane scrollPane;
	private NucleoGeRel cGR;
	private String relExibido[] = null;
	private String cabecalho = null;
	private String nomeArq = null;
	private Color corFaixas;
	private Color corSelecao;
	
	public SaidaSistema(NucleoGeRel cGR, Color corFaixas, Color corSelecao) {
		super();
		this.setBounds(0, 0, 1264, 750);
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		
		JPanel saida = new JPanel();
		saida.setBounds(318, 0, 943, 677);
		saida.setBackground(Color.WHITE);
		this.add(saida);
		saida.setLayout(null);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(2);
		panel.setOpaque(false);
		panel.setBounds(0, 649, 943, 27);
		saida.add(panel);
		
		JMenuBar menuBar = new JMenuBar();
		panel.add(menuBar);
		
		JButton bAnterior = new JButton("Anterior");
		bAnterior.setToolTipText("Relat\u00F3rio anterior");
		bAnterior.setForeground(Color.WHITE);
		bAnterior.setBackground(new Color(100, 149, 237));
		bAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(obterIndice() > 0) {
					definirIndice(obterIndice() - 1);
					renderizarRelatorio();
				}
			}
		});
		menuBar.add(bAnterior);
		
		JButton bProximo = new JButton("Pr\u00F3ximo");
		bProximo.setToolTipText("Pr\u00F3ximo relat\u00F3rio");
		bProximo.setForeground(Color.WHITE);
		bProximo.setBackground(new Color(100, 149, 237));
		bProximo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(obterIndice() < (cGR.obterQuantidadeRelatorios()) - 1){
					System.out.println(cGR.obterQuantidadeRelatorios());
					definirIndice(obterIndice() + 1);
					renderizarRelatorio();
				}
			}
		});
		menuBar.add(bProximo);
		
		JButton bSalvarComoPDF = new JButton("Salvar como PDF");
		menuBar.add(bSalvarComoPDF);
		bSalvarComoPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(relExibido != null) {
					JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					jfc.setDialogTitle("Escolha um local para salvar seu arquivo: ");
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					int returnValue = jfc.showSaveDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						if (jfc.getSelectedFile().isDirectory()) {
							String local = jfc.getSelectedFile().getAbsolutePath();
							local = local.concat(File.pathSeparator);
							salvarRelExibido(local);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Não há relatórios para serem salvos!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		bSalvarComoPDF.setToolTipText("Salva o documento em formato pdf");
		bSalvarComoPDF.setBackground(new Color(100, 149, 237));
		bSalvarComoPDF.setForeground(Color.WHITE);
		
		this.scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBackground(Color.BLUE);
		scrollPane.setBounds(0, 0, 943, 650);
		saida.add(scrollPane);
		this.setcGR(cGR);
		this.setCorFaixas(corFaixas);
		this.setCorSelecao(corSelecao);
		
		/*Indice.*/
		this.definirIndice(0);
}
	public void definirTipoRelatorio(TipoRelatorio tipo) {
		this.tipoRelatorio = tipo;
	}
	
	public int obterIndice() {
		return indice;
	}
	public void definirIndice(int i) {
		this.indice = i;
	}
	
	public void renderizarRelatorio() {
		switch(this.tipoRelatorio) {
		case COMPLETO:
			this.relExibido = cGR.obterRelatorio(this.indice);
			this.cabecalho = "RELATÓRIO COMPLETO";
			break;
		case INCLUIDOS:
			this.relExibido = cGR.obterIncluidos(this.indice);
			this.cabecalho = "ÍTENS INCLUÍDOS NO RELATRIO";
			break;
		case EXCLUIDOS:
			this.relExibido = cGR.obterExcluidos(this.indice);
			this.cabecalho = "ÍTENS EXCLUÍDOS DO RELATÓRIO";
			break;
		case INALTERADOS:
			this.relExibido = cGR.obterInalterados(this.indice);
			this.cabecalho = "ÍTENS INALTERADOS NO RELATÓRIO";
			break;
		default:
			break;
		}
		
		PainelTextoDestacado textPane;
		
		try {
			textPane = new PainelTextoDestacado(Relatorios.obterRelatorioRenderizado(this.relExibido, this.cabecalho), this.corFaixas, this.corSelecao);
			textPane.setEditable(false);
			this.scrollPane.setViewportView(textPane);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(null, "Falha na renderização dos consignados!", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		this.nomeArq = this.tipoRelatorio.toString();
	}
	
	private void salvarRelExibido(String local) {
		try {
			Relatorios.gerarArquivo(this.relExibido, this.cabecalho, local+this.nomeArq);
			JOptionPane.showMessageDialog(null, "O relatório foi salvo.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha no salvamento do relatório!", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void setcGR(NucleoGeRel cGR) {
		this.cGR = cGR;
	}
	
	public void setCorFaixas(Color corFaixas) {
		this.corFaixas = corFaixas;
	}

	public void setCorSelecao(Color corSelecao) {
		this.corSelecao = corSelecao;
	}
}
