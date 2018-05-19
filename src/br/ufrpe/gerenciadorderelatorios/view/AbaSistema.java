package br.ufrpe.gerenciadorderelatorios.view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import br.ufrpe.gerenciadorderelatorios.control.ControleGeRelatorio;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AbaSistema extends JPanel {
	public static enum Relatorio {
		RECENTE, ANTERIORES, NOVOS, EXCLUIDOS, INALTERADOS;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private int tipo;	
	private ControleGeRelatorio cGR;
	private String relExibido[] = null;
	private String cabecalho = null;
	private String nomeArq = null;
	private Color corFaixas;
	private Color corSelecao;
	
	public AbaSistema(ControleGeRelatorio cGR, int tipo, Color corFaixas, Color corSelecao) {
		super();
			
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		
		JPanel saida = new JPanel();
		saida.setBounds(318, 0, 850, 398);
		saida.setBackground(Color.WHITE);
		this.add(saida);
		saida.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(703, 5, 73, 33);
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
					JOptionPane.showMessageDialog(null, "N�o h� relat�rios para serem salvos!", "Aten��o", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		panel.add(bSalvar);
		bSalvar.setToolTipText("Salva documento em formato pdf");
		bSalvar.setBackground(new Color(100, 149, 237));
		bSalvar.setForeground(Color.WHITE);
		
		this.scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(0, 0, 850, 398);
		saida.add(scrollPane);
		this.setcGR(cGR);
		this.setTipo(tipo);
		this.setCorFaixas(corFaixas);
		this.setCorSelecao(corSelecao);
}
	
	public void renderizar(Relatorio rel) {
		switch(rel) {
		case RECENTE:
			this.relExibido = cGR.obterListaTexto();
			//this.relExibido = cGR.obterLista(this.tipo, ControleGeRelatorio.CONSIG_ATUAL);
			this.cabecalho = "ULTIMO RELATORIO";
			break;
		/*case ANTERIORES:
			this.relExibido = cGR.obterListaConsignacoes(this.tipo, ControlDiff.CONSIG_ANTERIOR);
			this.cabecalho = "CONSIGNADOS DO M�S ANTERIOR";
			break;*/
		case NOVOS:
			this.relExibido = cGR.obterListaNovos(this.tipo);
			this.cabecalho = "NOVOS";
			break;
		case EXCLUIDOS:
			this.relExibido = cGR.obterListaExcluidos(this.tipo);
			this.cabecalho = "EXCLU�DOS";
			break;
		case INALTERADOS:
			this.relExibido = cGR.obterListaInalterados(this.tipo);
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
			JOptionPane.showMessageDialog(null, "Falha na renderiza��o dos consignados!", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		this.nomeArq = rel.toString();
	}
	
	private void salvarRelExibido(String local) {
		try {
			Relatorios.gerarArquivo(this.relExibido, this.cabecalho, local+this.nomeArq);
			JOptionPane.showMessageDialog(null, "O relat�rio foi salvo.", "Aten��o", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Falha no salvamento do relat�rio!", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void setcGR(ControleGeRelatorio cGR) {
		this.cGR = cGR;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setCorFaixas(Color corFaixas) {
		this.corFaixas = corFaixas;
	}

	public void setCorSelecao(Color corSelecao) {
		this.corSelecao = corSelecao;
	}

}
