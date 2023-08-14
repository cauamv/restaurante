package br.com.senai.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JFormattedTextField;

public class ViewHorarios extends JFrame {

	private JPanel contentPane;
	private JTable tableHorarios;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewHorarios frame = new ViewHorarios();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
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
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(106, 7, 513, 22);
		contentPane.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(106, 37, 134, 22);
		contentPane.add(comboBox_1);
		
		JLabel lblDiaDaSemana = new JLabel("Dia da Semana");
		lblDiaDaSemana.setBounds(10, 41, 90, 14);
		contentPane.add(lblDiaDaSemana);
		
		JLabel lblAbertura = new JLabel("Abertura");
		lblAbertura.setBounds(250, 41, 50, 14);
		contentPane.add(lblAbertura);
		
		JLabel lblFechamento = new JLabel("Fechamento");
		lblFechamento.setBounds(379, 41, 73, 14);
		contentPane.add(lblFechamento);
		
		JButton btnNewButton = new JButton("Adicionar");
		btnNewButton.setBounds(531, 37, 88, 23);
		contentPane.add(btnNewButton);
		
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
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.setBounds(12, 30, 202, 26);
		panel.add(btnEditar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setBounds(12, 68, 202, 26);
		panel.add(btnExcluir);
		
		JScrollPane scrollPane = new JScrollPane((Component) null);
		scrollPane.setBounds(21, 111, 350, 140);
		contentPane.add(scrollPane);
		
		tableHorarios = new JTable();
		scrollPane.setColumnHeaderView(tableHorarios);
		
		JFormattedTextField tfAbertura = new JFormattedTextField();
		tfAbertura.setBounds(306, 38, 65, 22);
		contentPane.add(tfAbertura);
		
		JFormattedTextField tfFechamento = new JFormattedTextField();
		tfFechamento.setBounds(458, 38, 65, 22);
		contentPane.add(tfFechamento);
	}
}
