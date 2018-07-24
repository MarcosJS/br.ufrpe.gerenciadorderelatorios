package br.ufrpe.gerenciadorderelatorios.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import br.ufrpe.gerenciadorderelatorios.control.NucleoGeRel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuSistema extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean estaCarregado = false;
	private NucleoGeRel controle;
	
	public MenuSistema(NucleoGeRel controle, Color corFundoBotoes, Color corFonteBotoes, SelArqPanel selArq, SaidaSistema saida) {
		super();
		this.definirControle(controle);
		
		this.setForeground(corFonteBotoes);
    	this.setBounds(0, 0, 319, 677);
    	this.setBackground(new Color(255, 255, 102));
    	this.setLayout(null);
    	
    	JSeparator separator = new JSeparator();
    	separator.setForeground(Color.BLUE);
    	separator.setBounds(10, 214, 294, 1);
    	this.add(separator);
    	
    	JButton btnNewButton = new JButton("CARREGAR");
    	btnNewButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			setEstaCarregado(selArq.carregarArquivos());
    			if(getEstaCarregado()) {
    				JOptionPane.showMessageDialog(null, "Os arquivos foram carregados!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
    			}
    		}
    	});
    	btnNewButton.setForeground(corFonteBotoes);
    	btnNewButton.setBackground(corFundoBotoes);
    	btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
    	btnNewButton.setToolTipText("Clique \"CARREGAR\" para carregar os consignados selecionados.");
    	btnNewButton.setBounds(10, 176, 91, 23);
    	this.add(btnNewButton);
    	
    	JButton btnRelatorioCompleto = new JButton("RELAT\u00D3RIO COMPLETO");
    	btnRelatorioCompleto.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (getEstaCarregado()) {
    				saida.definirTipoRelatorio(SaidaSistema.TipoRelatorio.COMPLETO);
					saida.renderizarRelatorio();
				} else {
					JOptionPane.showMessageDialog(null, "Os arquivos não foram carregados corretamente!", "Alerta", JOptionPane.WARNING_MESSAGE);
				}
    		}
    	});
    	btnRelatorioCompleto.setForeground(corFonteBotoes);
    	btnRelatorioCompleto.setBackground(corFundoBotoes);
    	btnRelatorioCompleto.setToolTipText("Clique para exibir o relat\u00F3rio completo.");
    	btnRelatorioCompleto.setFont(new Font("Tahoma", Font.BOLD, 10));
    	btnRelatorioCompleto.setBounds(66, 226, 182, 23);
    	this.add(btnRelatorioCompleto);
    	
    	JButton btnSalvarRelatorios = new JButton("SALVAR RELAT\u00D3RIOS");
    	btnSalvarRelatorios.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (getEstaCarregado()) {
    				controle.salvarHistorico();
    				JOptionPane.showMessageDialog(null, "Os arquivos foram armazenados!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Os arquivos não foram carregados corretamente!", "Alerta", JOptionPane.WARNING_MESSAGE);
				}
    		}
    	});
    	btnSalvarRelatorios.setForeground(corFonteBotoes);
    	btnSalvarRelatorios.setBackground(corFundoBotoes);
    	btnSalvarRelatorios.setToolTipText("Clique para salvar os relat\u00F3rios no banco de dados.");
    	btnSalvarRelatorios.setFont(new Font("Tahoma", Font.BOLD, 10));
    	btnSalvarRelatorios.setBounds(66, 260, 182, 23);
    	this.add(btnSalvarRelatorios);
    	
    	JButton btnNovos = new JButton("NOVOS");
    	btnNovos.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (getEstaCarregado()) {
    				saida.definirTipoRelatorio(SaidaSistema.TipoRelatorio.NOVAS);
					saida.renderizarRelatorio();
				} else {
					JOptionPane.showMessageDialog(null, "Os arquivos não foram carregados corretamente!", "Alerta", JOptionPane.WARNING_MESSAGE);
				}
    		}
    	});
    	btnNovos.setForeground(corFonteBotoes);
    	btnNovos.setBackground(corFundoBotoes);
    	btnNovos.setToolTipText("Clique para exibir os novos consignados.");
    	btnNovos.setFont(new Font("Tahoma", Font.BOLD, 10));
    	btnNovos.setBounds(66, 294, 182, 23);
    	this.add(btnNovos);
    	
    	JButton btnExcludos = new JButton("EXCLU\u00CDDOS");
    	btnExcludos.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (getEstaCarregado()) {
    				saida.definirTipoRelatorio(SaidaSistema.TipoRelatorio.EXCLUIDAS);
					saida.renderizarRelatorio();
				} else {
					JOptionPane.showMessageDialog(null, "Os arquivos não foram carregados corretamente!", "Alerta", JOptionPane.WARNING_MESSAGE);
				}
    		}
    	});
    	btnExcludos.setForeground(corFonteBotoes);
    	btnExcludos.setBackground(corFundoBotoes);
    	btnExcludos.setToolTipText("Clique para exibir os consignados exclu\u00EDdos.");
    	btnExcludos.setFont(new Font("Tahoma", Font.BOLD, 10));
    	btnExcludos.setBounds(66, 328, 182, 23);
    	this.add(btnExcludos);
    	
    	JButton btnInalterado = new JButton("INALTERADOS");
    	btnInalterado.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (getEstaCarregado()) {
    				saida.definirTipoRelatorio(SaidaSistema.TipoRelatorio.ESTAVEIS);
					saida.renderizarRelatorio();
				} else {
					JOptionPane.showMessageDialog(null, "Os arquivos não foram carregados corretamente!", "Alerta", JOptionPane.WARNING_MESSAGE);
				}
    		}
    	});
    	btnInalterado.setForeground(corFonteBotoes);
    	btnInalterado.setBackground(corFundoBotoes);
    	btnInalterado.setToolTipText("Clique para exibir consignados inalterados.");
    	btnInalterado.setFont(new Font("Tahoma", Font.BOLD, 10));
    	btnInalterado.setBounds(66, 362, 182, 23);
    	this.add(btnInalterado);
    	this.add(selArq);
	}
	
	public boolean getEstaCarregado() {
		return this.estaCarregado;
	}
	
	public void setEstaCarregado(boolean estaCarregado) {
		this.estaCarregado = estaCarregado;
	}

	public NucleoGeRel obterControle() {
		return controle;
	}

	public void definirControle(NucleoGeRel controle) {
		this.controle = controle;
	}
	
	/*public void setSelArq(SelArqPanel selArq) {
		this.selArq = selArq;
	}*/
}
