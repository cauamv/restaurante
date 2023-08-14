package br.com.senai.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import br.com.senai.core.domain.DiasDaSemana;
import br.com.senai.core.domain.Horario;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.HorarioService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.table.HorarioTableModel;

public class ViewHorarios extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JFormattedTextField tfAbertura;
	private JFormattedTextField tfFechamento;
	private JTable tableHorario;
	private JComboBox<String> cbDiaDaSemana;
	private HorarioService horarioService;
	private RestauranteService restauranteService;
	private JComboBox<Restaurante> cbRestaurante;
	private Horario horario;
	
	String[] diasSemana = { 
			DiasDaSemana.DOMINGO.toString(), 
			DiasDaSemana.SEGUNDA.toString(),
			DiasDaSemana.TERCA.toString(),
			DiasDaSemana.QUARTA.toString(),
			DiasDaSemana.QUINTA.toString(),
			DiasDaSemana.SEXTA.toString(),
			DiasDaSemana.SABADO.toString(),
			};
	
	public void carregarComboSemana() {
		for (String dia : diasSemana) {
			cbDiaDaSemana.addItem(dia);
		}
	}
	
	public void carregarComboRestaurante() {
		List<Restaurante> restaurantes = restauranteService.listarTodos();
		for (Restaurante re : restaurantes) {
			cbRestaurante.addItem(re);
		}
	}

	public ViewHorarios() {
		setTitle("Gerenciar Horários - Cadastro");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 646, 298);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Restaurante");
		lblNewLabel.setBounds(21, 11, 75, 14);
		contentPane.add(lblNewLabel);
		
		cbRestaurante = new JComboBox<Restaurante>();
		cbRestaurante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    Restaurante restauranteSelecionado = (Restaurante) cbRestaurante.getSelectedItem();
			    if (restauranteSelecionado != null) {
			        List<Horario> horarios = horarioService.listarPor(restauranteSelecionado);
			        HorarioTableModel model = new HorarioTableModel(horarios);
			        tableHorario.setModel(model);
			        tableHorario.updateUI();
			        clearFieldsWithoutRestaurante();
			   }
			}
		});
		cbRestaurante.setBounds(106, 7, 513, 22);
		contentPane.add(cbRestaurante);
		
		cbDiaDaSemana = new JComboBox<String>();
		cbDiaDaSemana.setBounds(106, 37, 134, 22);
		contentPane.add(cbDiaDaSemana);
		
		JLabel lblDiaDaSemana = new JLabel("Dia da Semana");
		lblDiaDaSemana.setBounds(10, 41, 90, 14);
		contentPane.add(lblDiaDaSemana);
		
		JLabel lblAbertura = new JLabel("Abertura");
		lblAbertura.setBounds(250, 41, 50, 14);
		contentPane.add(lblAbertura);
		
		JLabel lblFechamento = new JLabel("Fechamento");
		lblFechamento.setBounds(379, 41, 73, 14);
		contentPane.add(lblFechamento);
		
		JButton btAdicionar = new JButton("Adicionar");
		btAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String diaDaSemana = (String) cbDiaDaSemana.getSelectedItem();
					String aberturaStr = tfAbertura.getText();
					String fechamentoStr = tfFechamento.getText();
					Restaurante restaurante = (Restaurante) cbRestaurante.getSelectedItem();

					if (aberturaStr.isEmpty() || fechamentoStr.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Todos os campos são obrigatórios!");
					} else {
						LocalTime horarioDeAbertura = LocalTime.parse(aberturaStr);
						LocalTime horarioDeFechamento = LocalTime.parse(fechamentoStr);

						if (horario == null) {
							horario = new Horario(DiasDaSemana.valueOf(diaDaSemana), horarioDeAbertura,
									horarioDeFechamento, restaurante);
							horarioService.salvar(horario);
							JOptionPane.showMessageDialog(contentPane, "Horário de atendimento inserido com sucesso!");
							clearFields();
							horario = null;
						} else {
							horario.setDiaDaSemana(DiasDaSemana.valueOf(diaDaSemana));
							horario.setAbertura(horarioDeAbertura);
							horario.setFechamento(horarioDeFechamento);
							horario.setRestaurante(restaurante);
							horarioService.salvar(horario);
							JOptionPane.showMessageDialog(contentPane, "Horário de atendimento alterado com sucesso!");
							clearFields();
							horario = null;
						}

					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
					if (horario.getId() <= 0) {
						horario = null;
					}
				}

			}
		});
		btAdicionar.setBounds(531, 37, 88, 23);
		contentPane.add(btAdicionar);
		
		JLabel lblNewLabel_1 = new JLabel("Horários");
		lblNewLabel_1.setBounds(10, 79, 55, 16);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblLinha = new JLabel("_________________________________________________________");
		lblLinha.setBounds(9, 84, 409, 16);
		contentPane.add(lblLinha);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Ações", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(389, 102, 226, 120);
		contentPane.add(panel);
		
		JButton btEditar = new JButton("Editar");
		btEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableHorario.getSelectedRow();
				HorarioTableModel model = (HorarioTableModel) tableHorario.getModel();
				if (linhaSelecionada >= 0) {
					Horario horarioSelecionado = model.getPor(linhaSelecionada);
					setHorario(horarioSelecionado);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
				}
			}
		});
		btEditar.setBounds(12, 30, 202, 26);
		panel.add(btEditar);
		
		JButton btExcluir = new JButton("Excluir");
		btExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableHorario.getSelectedRow();
				if (linhaSelecionada >= 0) {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente remover?", "Remoção",
							JOptionPane.YES_NO_OPTION);
					if (opcao == 0) {
						HorarioTableModel model = (HorarioTableModel) tableHorario.getModel();
						Horario horarioSelecionado = model.getPor(linhaSelecionada);
						try {
							horarioService.removerPor(horarioSelecionado.getId());
							List<Horario> horariosRestantes = horarioService.listarPor((Restaurante) cbRestaurante.getSelectedItem());
							model = new HorarioTableModel(horariosRestantes);
							tableHorario.setModel(model);	
							JOptionPane.showMessageDialog(contentPane, "Horário de atendimento removido com sucesso!");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPane, ex.getMessage());
						}
						tableHorario.clearSelection();
					}
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para remoção.");
				}
			}
		});
		btExcluir.setBounds(12, 68, 202, 26);
		panel.add(btExcluir);
		
		tableHorario = new JTable(new HorarioTableModel(new ArrayList<Horario>()));
		this.configurarTabela();
		JScrollPane scrollPane = new JScrollPane(tableHorario);
		scrollPane.setBounds(21, 111, 350, 140);
		contentPane.add(scrollPane);
		
		tableHorario = new JTable();
		scrollPane.setColumnHeaderView(tableHorario);
		
		tfAbertura = new JFormattedTextField();
		tfAbertura.setBounds(306, 38, 65, 22);
		contentPane.add(tfAbertura);
		
		tfFechamento = new JFormattedTextField();
		tfFechamento.setBounds(458, 38, 65, 22);
		contentPane.add(tfFechamento);
		
		this.restauranteService = new RestauranteService();
		this.carregarComboRestaurante();
		this.carregarComboSemana();
	    mascara(tfAbertura, "##:##");
	    mascara(tfFechamento, "##:##");
		
	}
	
	private void mascara(JFormattedTextField field, String mask) {
	    try {
	        MaskFormatter maskFormatter = new MaskFormatter(mask);
	        maskFormatter.setPlaceholderCharacter('0');
	        maskFormatter.install(field);
	    } catch (ParseException ex) {
	        ex.printStackTrace();
	    }
	}
	
	private void clearFields() {
        cbDiaDaSemana.setSelectedItem(null);
        tfAbertura.setText("");
        tfFechamento.setText("");
        cbRestaurante.setSelectedIndex(0);
        horario = null;
    }
	
	private void clearFieldsWithoutRestaurante() {
        cbDiaDaSemana.setSelectedItem(null);
        tfAbertura.setText("");
        tfFechamento.setText("");
        horario = null;
    }
	
	private void setHorario(Horario horario) {
		this.horario = horario;
		cbRestaurante.setSelectedItem(horario.getRestaurante());
		tfAbertura.setValue(horario.getAbertura());
		tfFechamento.setValue(horario.getFechamento());
		cbDiaDaSemana.setSelectedItem(horario.getDiaDaSemana());
	}
	
	private void configurarColuna(int indice, int largura) {
		this.tableHorario.getColumnModel().getColumn(indice).setResizable(false);
		this.tableHorario.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_ID = 0;
		final int COLUNA_NOME = 1;
		this.tableHorario.getTableHeader().setReorderingAllowed(false);
		this.tableHorario.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_ID, 50);
		this.configurarColuna(COLUNA_NOME, 550);
	}
	
}
