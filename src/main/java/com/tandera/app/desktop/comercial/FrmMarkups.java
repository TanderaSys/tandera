package com.tandera.app.desktop.comercial;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tandera.app.spring.SpringDesktopApp;
import com.tandera.core.dao.springjpa.MarkupRepository;
import com.tandera.core.model.comercial.Categoria;
import com.tandera.core.model.comercial.Markup;
import com.tandera.core.util.Constantes;

import edu.porgamdor.util.desktop.Formulario;
import edu.porgamdor.util.desktop.FormularioConsulta;
import edu.porgamdor.util.desktop.ss.SSBotao;
import edu.porgamdor.util.desktop.ss.SSCampoTexto;
import edu.porgamdor.util.desktop.ss.SSGrade;
import edu.porgamdor.util.desktop.ss.SSMensagem;
import edu.porgamdor.util.desktop.ss.util.Validacao;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FrmMarkups extends FormularioConsulta {

	@Autowired
	MarkupRepository dao;

	Class formInclusao = FrmMarkup.class;

	// JA PODERIA VIR DE FormularioConsulta
	private JPanel filtro = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	private SSGrade tabela = new SSGrade();

	private SSCampoTexto txtFiltro = new SSCampoTexto();
	private SSBotao cmdBuscar = new SSBotao();

	private SSBotao cmdIncluir = new SSBotao();
	private SSBotao cmdAlterar = new SSBotao();
	private final SSBotao cmdExcluir = new SSBotao();

	private String acao; // NOVO | ALTERAR | EXCLUIR | CONSULTAR

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public FrmMarkups() {
		// JA PODERIA VIR DE FormularioConsulta
		setTitulo("Consulta de Markups");
		setDescricao("Listagem dos Markups");
		setConteudoLayout(new BorderLayout());
		setAlinhamentoRodape(FlowLayout.LEFT);
		filtro.setLayout(new GridBagLayout());

		configurarCamposPesquisa();
		configurarCamposTabela();
		configurarConstraintsGrid();
		adicionarComponentesNoConteudo();
		adicionarActionListener();
	}

	private void configurarCamposPesquisa() {
		txtFiltro.setRotulo("Sigla");
		txtFiltro.setColunas(50);
		cmdBuscar.setText("Buscar");

		cmdIncluir.setText("Incluir");
		cmdIncluir.setIcone("novo");
		cmdAlterar.setText("Alterar");
		cmdExcluir.setText("Excluir");
		cmdFechar.setText("Fechar");
		txtFiltro.setColunas(30);
	}

	private void configurarCamposTabela() {
		// campos da tabela
		// BASICAMENTE O QUE VC TERÁ QUE MUDAR ENTRE FORMULARIOS
		tabela.getModeloTabela().addColumn("Id");
		tabela.getModeloTabela().addColumn("Sigla");
		tabela.getModeloTabela().addColumn("Valor");

		tabela.getModeloColuna().getColumn(0).setPreferredWidth(30);
		tabela.getModeloColuna().getColumn(1).setPreferredWidth(280);
		tabela.getModeloColuna().getColumn(2).setPreferredWidth(70);

		tabela.getModeloColuna().setCampo(0, "id");
		tabela.getModeloColuna().setCampo(1, "sigla");
		tabela.getModeloColuna().setCampo(2, "valor");

	}

	private void configurarConstraintsGrid() {
		// constraints - grid bag layout
		GridBagConstraints gbcTxtFiltro = new GridBagConstraints();
		gbcTxtFiltro.weightx = 1.0;
		gbcTxtFiltro.anchor = GridBagConstraints.NORTHWEST;
		gbcTxtFiltro.insets = new Insets(5, 5, 5, 5);
		gbcTxtFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtFiltro.gridx = 0;
		gbcTxtFiltro.gridy = 0;

		GridBagConstraints gbcCmdBuscar = new GridBagConstraints();
		gbcCmdBuscar.anchor = GridBagConstraints.SOUTHWEST;
		gbcCmdBuscar.fill = GridBagConstraints.HORIZONTAL;
		gbcCmdBuscar.insets = new Insets(0, 0, 5, 5);
		gbcCmdBuscar.gridx = 1;
		gbcCmdBuscar.gridy = 0;

		// adicionando componentes aos seus Naturezainers
		filtro.add(txtFiltro, gbcTxtFiltro);
		filtro.add(cmdBuscar, gbcCmdBuscar);
	}

	private void adicionarComponentesNoConteudo() {

		scroll.setViewportView(tabela);

		getConteudo().add(filtro, BorderLayout.NORTH);
		getConteudo().add(scroll, BorderLayout.CENTER);

		getRodape().add(cmdIncluir);
		getRodape().add(cmdAlterar);
		getRodape().add(cmdExcluir);
		getRodape().add(cmdFechar);
	}

	private void adicionarActionListener() {
		// métodos
		cmdFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sair();
			}
		});
		cmdBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listar();
			}
		});
		cmdIncluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				incluir();
			}
		});
		cmdAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alterar();
			}
		});
		cmdExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				excluirItem();
			}
		});
	}

	public JPanel getFiltro() {
		return filtro;
	}

	@Override
	protected void sair() {
		super.fechar();
	}

	private void listar() {
		List<Markup> lista = new ArrayList<Markup>();
		try {
			String sigla = txtFiltro.getText();
			if (Validacao.vazio(sigla)) {
				lista = dao.findAll();

			} else {
				lista = dao.findBySiglaContainingIgnoreCase(sigla);
			}
			if (lista.size() == 0)
				SSMensagem.avisa("Nenhum dado encontrado");

			tabela.setValue(lista);
		} catch (Exception e) {
			e.printStackTrace();
			SSMensagem.erro(e.getMessage());
		}
	}

	private void incluir() {
		exibirCadastro(null);
	}

	@Override
	protected void alterar() {
		Markup entidade = (Markup) tabela.getLinhaSelecionada();
		if (entidade == null) {
			SSMensagem.avisa("Selecione um item da lista");
			return;
		}
		exibirCadastro(entidade);
	}
	
	private void excluirItem() {
		Markup markup = (Markup) tabela.getLinhaSelecionada();
		if (markup == null) {
			SSMensagem.avisa("Selecione um item da lista");
			return;
		}

		if (SSMensagem.confirma("Confirma exclusão do Registro (" + markup.getId() + "-"
				+ markup.getSigla() + ")?")) {
			this.acao = Constantes.ACAO_EXCLUSAO;
			dao.deleteItemCategoria(markup.getId());
			SSMensagem.informa("Item Excluido com sucesso!!");
			listar();
		}

	}

	private void exibirCadastro(Markup entidade) {
		Formulario frm = SpringDesktopApp.getBean(formInclusao);
		frm.setEntidade(entidade);
		this.dialogo(frm);
		listar();
	}

}
