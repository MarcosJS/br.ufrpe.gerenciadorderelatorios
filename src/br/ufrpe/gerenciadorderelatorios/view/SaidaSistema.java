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
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLayeredPane;

public class SaidaSistema extends JPanel {
	public static enum Relatorio {
		RECENTE, ANTERIORES, NOVOS, EXCLUIDOS, INALTERADOS;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		saida.setBounds(318, 0, 946, 703);
		saida.setBackground(Color.WHITE);
		this.add(saida);
		saida.setLayout(null);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		panel.setOpaque(false);
		panel.setBounds(850, 5, 73, 33);
		saida.add(panel);
		
		JButton bSalvar = new JButton("Salvar");
		bSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(relExibido != null) {
					JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					jfc.setDialogTitle("Escolha um local para salvar seu arquivo: ");
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					int returnValue = jfc.showSaveDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						if (jfc.getSelectedFile().isDirectory()) {
							String local = jfc.getSelectedFile().getAbsolutePath();
							local = local.concat("\\");
							salvarRelExibido(local);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Não há relatórios para serem salvos!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		panel.add(bSalvar);
		bSalvar.setToolTipText("Salva documento em formato pdf");
		bSalvar.setBackground(new Color(100, 149, 237));
		bSalvar.setForeground(Color.WHITE);
		panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{bSalvar}));
		
		this.scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBackground(Color.BLUE);
		scrollPane.setBounds(0, 0, 943, 703);
		saida.add(scrollPane);
		saida.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{panel, bSalvar, scrollPane}));
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{bSalvar, panel, scrollPane, saida}));
		this.setcGR(cGR);
		this.setCorFaixas(corFaixas);
		this.setCorSelecao(corSelecao);
}
	
	public void renderizar(Relatorio rel) {
		switch(rel) {
		case RECENTE:
			this.relExibido = cGR.obterListaTexto();
			this.cabecalho = "ULTIMO RELATORIO";
			break;
		/*case ANTERIORES:
			this.relExibido = cGR.obterListaConsignacoes(this.tipo, ControlDiff.CONSIG_ANTERIOR);
			this.cabecalho = "CONSIGNADOS DO MÊS ANTERIOR";
			break;*/
		case NOVOS:
			this.relExibido = cGR.obterListaNovos();
			this.cabecalho = "NOVOS";
			break;
		case EXCLUIDOS:
			this.relExibido = cGR.obterListaExcluidos();
			this.cabecalho = "EXCLUÍDOS";
			break;
		case INALTERADOS:
			this.relExibido = cGR.obterListaInalterados();
			this.cabecalho = "INALTERADOS";
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
		this.nomeArq = rel.toString();
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
