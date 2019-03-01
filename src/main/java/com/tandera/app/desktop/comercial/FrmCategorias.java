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
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tandera.app.spring.SpringDesktopApp;
import com.tandera.core.dao.springjpa.CategoriaRepository;
import com.tandera.core.model.comercial.Categoria;
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
public class FrmCategorias extends FormularioConsulta {

	@Autowired
	CategoriaRepository dao;

	Class formInclusao = FrmCategoria.class;

	// JA PODERIA VIR DE FormularioConsulta
	private JPanel filtro = new JPanel();
	private JScrollPane scroll = new JScrollPane();
	private SSGrade tabela = new SSGrade();

	private SSCampoTexto txtFiltro = new SSCampoTexto();
	private SSBotao cmdBuscar = new SSBotao();

	private SSBotao cmdIncluir = new SSBotao();
	private SSBotao cmdAlterar = new SSBotao();
	private final SSBotao cmdExcluir = new SSBotao();
	//private SSBotao cmdFechar = new SSBotao();
	
	private String acao; // NOVO | ALTERAR | EXCLUIR | CONSULTAR
	
	public void setAcao(String acao) {
		this.acao = acao;
	}

	public FrmCategorias() {
		// JA PODERIA VIR DE FormularioConsulta
		setTitulo("Consulta de Categorias");
		setDescricao("Listagem dos Categorias");
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
		txtFiltro.setRotulo("Descrição");
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
		tabela.getModeloTabela().addColumn("Descrição");
		tabela.getModeloTabela().addColumn("Deposito");
		tabela.getModeloTabela().addColumn("Troca");
		tabela.getModeloTabela().addColumn("Doação");

		tabela.getModeloColuna().getColumn(0).setPreferredWidth(30);
		tabela.getModeloColuna().getColumn(1).setPreferredWidth(200);
		tabela.getModeloColuna().getColumn(2).setPreferredWidth(50);
		tabela.getModeloColuna().getColumn(3).setPreferredWidth(50);
		tabela.getModeloColuna().getColumn(4).setPreferredWidth(50);

		tabela.getModeloColuna().setCampo(0, "id");
		tabela.getModeloColuna().setCampo(1, "descr");
		tabela.getModeloColuna().setCampo(2, "fatorDeposito");
		tabela.getModeloColuna().setCampo(3, "fatorTroca");
		tabela.getModeloColuna().setCampo(4, "fatorDoacao");

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
		List<Categoria> lista = new ArrayList<Categoria>();
		try {
			String descr = txtFiltro.getText();
			if (Validacao.vazio(descr)) {
				lista = dao.findAll();

			} else {
				lista = dao.findByDescrContainingIgnoreCase(descr);
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
		Categoria entidade = (Categoria) tabela.getLinhaSelecionada();
		if (entidade == null) {
			SSMensagem.avisa("Selecione um item da lista");
			return;
		}
		exibirCadastro(entidade);
	}
	
	private void excluirItem() {
		Categoria categoria = (Categoria) tabela.getLinhaSelecionada();
		if (categoria == null) {
			SSMensagem.avisa("Selecione um item da lista");
			return;
		}

		if (SSMensagem.confirma("Confirma exclusão do Registro (" + categoria.getId() + "-"
				+ categoria.getDescr() + ")?")) {
			this.acao = Constantes.ACAO_EXCLUSAO;
			dao.deleteItemCategoria(categoria.getId());
			SSMensagem.informa("Item Excluido com sucesso!!");
			listar();
		}

	}
	
	private void exibirCadastro(Categoria entidade) {
		Formulario frm = SpringDesktopApp.getBean(formInclusao);
		frm.setEntidade(entidade);
		this.dialogo(frm);
		listar();
			
	}

}
