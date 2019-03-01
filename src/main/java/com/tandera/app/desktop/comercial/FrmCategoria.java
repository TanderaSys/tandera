package com.tandera.app.desktop.comercial;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tandera.core.dao.springjpa.CategoriaRepository;
import com.tandera.core.model.comercial.Categoria;

import edu.porgamdor.util.desktop.Formato;
import edu.porgamdor.util.desktop.FormularioCrud;
import edu.porgamdor.util.desktop.ss.SSCampoNumero;
import edu.porgamdor.util.desktop.ss.SSCampoTexto;
import edu.porgamdor.util.desktop.ss.SSMensagem;

@Component
public class FrmCategoria extends FormularioCrud {
	
	@Autowired
	private CategoriaRepository dao;

	private Categoria entidade;

	private SSCampoTexto txtDescr = new SSCampoTexto();
	private SSCampoNumero txtFatorDeposito = new SSCampoNumero();
	private SSCampoNumero txtFatorTroca = new SSCampoNumero();
	private SSCampoNumero txtFatorDoacao = new SSCampoNumero();

	private JCheckBox chkNovo = new JCheckBox("Novo?");

	public FrmCategoria() {
		setPreferredSize(new Dimension(280, 270));
		init();
	}

	protected void init() {
		super.setTitulo("Categoria");
		super.setDescricao("Cadastro de Categorias");
		super.getRodape().add(chkNovo);
		super.getRodape().add(cmdSalvar);
		super.getRodape().add(cmdSair);

		// IMPORTANTE
		JPanel panelCampos = super.getConteudo();
		panelCampos.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagLayout gbl_panelCampos = new GridBagLayout();
		panelCampos.setLayout(gbl_panelCampos);

		GridBagConstraints gbcTxtDescr = new GridBagConstraints();
		gbcTxtDescr.weightx = 2.0;
		gbcTxtDescr.insets = new Insets(5, 5, 5, 5);
		gbcTxtDescr.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtDescr.gridx = 0;
		gbcTxtDescr.gridy = 0;
		panelCampos.add(txtDescr, gbcTxtDescr);

		txtDescr.setColunas(10);
		txtDescr.setRotulo("Descrição");
		
		GridBagConstraints gbcTxtFatorDeposito = new GridBagConstraints();
		gbcTxtFatorDeposito.insets = new Insets(5, 5, 0, 5);
		gbcTxtFatorDeposito.fill = GridBagConstraints.BOTH;
		gbcTxtFatorDeposito.gridx = 0;
		gbcTxtFatorDeposito.gridy = 1;
		txtFatorDeposito.setRotulo("Fator Depósito");
		txtFatorDeposito.setFormato(Formato.MOEDA);
		panelCampos.add(txtFatorDeposito, gbcTxtFatorDeposito);
		
		GridBagConstraints gbcTxtFatorTroca = new GridBagConstraints();
		gbcTxtFatorTroca.insets = new Insets(5, 5, 0, 5);
		gbcTxtFatorTroca.fill = GridBagConstraints.BOTH;
		gbcTxtFatorTroca.gridx = 0;
		gbcTxtFatorTroca.gridy = 2;
		txtFatorTroca.setRotulo("Fator Troca");
		txtFatorTroca.setFormato(Formato.MOEDA);
		panelCampos.add(txtFatorTroca, gbcTxtFatorTroca);
		
		GridBagConstraints gbcTxtFatorDoacao = new GridBagConstraints();
		gbcTxtFatorDoacao.insets = new Insets(5, 5, 0, 5);
		gbcTxtFatorDoacao.fill = GridBagConstraints.BOTH;
		gbcTxtFatorDoacao.gridx = 0;
		gbcTxtFatorDoacao.gridy = 3;
		txtFatorDoacao.setRotulo("Fator Doação");
		txtFatorDoacao.setFormato(Formato.MOEDA);
		panelCampos.add(txtFatorDoacao, gbcTxtFatorDoacao);
	}
	
	// public void setEntidade(Natureza entidade) {
	public void setEntidade(Object entidade) {
		this.entidade = (Categoria) entidade;
		if (entidade != null)
			atribuir();
		else
			criar();
	}

	private void atribuir() {
		try {
			txtDescr.setValue(entidade.getDescr());
			txtFatorDeposito.setValue(entidade.getFatorDeposito());
			txtFatorTroca.setValue(entidade.getFatorTroca());
			txtFatorDoacao.setValue(entidade.getFatorDoacao());
			txtDescr.requestFocus();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void criar() {
		entidade = new Categoria();
		atribuir();
	}
    
    @Override
	protected void salvar() {
		try {
			entidade.setDescr(txtDescr.getText());
			entidade.setFatorDeposito(BigDecimal.valueOf(txtFatorDeposito.getDouble()));
			entidade.setFatorTroca(BigDecimal.valueOf(txtFatorTroca.getDouble()));
			entidade.setFatorDoacao(BigDecimal.valueOf(txtFatorDoacao.getDouble()));

			if (entidade.getDescr() == null || entidade.getDescr().isEmpty() || 
				entidade.getFatorDeposito() == null || 
				entidade.getFatorDeposito().equals(BigDecimal.ZERO) ||
				entidade.getFatorTroca() == null || 
				entidade.getFatorTroca().equals(BigDecimal.ZERO) ||
				entidade.getFatorDoacao() == null || 
				entidade.getFatorDoacao().equals(BigDecimal.ZERO)) {
				SSMensagem.avisa("Dados incompletos");
				return;
			}

			dao.save(entidade);

			SSMensagem.informa("Categoria registrado com sucesso!!");
			novo();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void novo() {
		if (chkNovo.isSelected()) {
			criar();
		} else
			super.fechar();
	}

	private void sair() {
		super.fechar();
	}


}
