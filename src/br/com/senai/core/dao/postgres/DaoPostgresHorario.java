package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.DiasDaSemana;
import br.com.senai.core.domain.Horario;
import br.com.senai.core.domain.Restaurante;

public class DaoPostgresHorario implements DaoHorario {

	private final String INSERT = "INSERT INTO horarios_atendimento (dia_semana, hora_abertura, hora_fechamento, id_restaurante)"
								+ " VALUES (?, ?, ?, ?)";

	private final String UPDATE = "UPDATE horarios_atendimento"
								+ " SET dia_semana = ?, hora_abertura = ?, hora_fechamento = ?, id_restaurante = ?" + " WHERE id = ?";

	private final String DELETE = "DELETE FROM horarios_atendimento WHERE id = ?";

	private final String SELECT_BY_ID = "SELECT h.id, h.dia_semana, h.hora_abertura, h.hora_fechamento,"
									  + " r.id id_restaurante, r.nome nome_restaurante, r.descricao,"
			+ " r.cidade, r.logradouro, r.bairro, r.complemento," + " c.id id_categoria, c.nome nome_categoria"
			+ " FROM horarios_atendimento h, restaurantes r, categorias c " + " WHERE h.id_restaurante = r.id"
			+ " AND r.id_categoria = c.id" + " AND h.id = ?";

	private final String SELECT_BY_RESTAURANTE = "SELECT h.id, h.dia_semana, h.hora_abertura, h.hora_fechamento,"
			+ " r.id id_restaurante, r.nome nome_restaurante, r.descricao,"
			+ " r.cidade, r.logradouro, r.bairro, r.complemento," 
			+ " c.id id_categoria, c.nome nome_categoria"
			+ " FROM horarios_atendimento h, restaurantes r, categorias c " 
			+ " WHERE h.id_restaurante = r.id"
			+ " AND r.id_categoria = c.id" + " AND r.id = ?";

	private Connection conexao;

	public DaoPostgresHorario() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}

	@Override
	public void inserir(Horario horarioAtendimento) {
		PreparedStatement ps = null;
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, horarioAtendimento.getDiaDaSemana().toString());
			ps.setTime(2, Time.valueOf(horarioAtendimento.getAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException(
					"Ocorreu um erro ao inserir o horário de atendimento. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
	}

	@Override
	public void alterar(Horario	 horarioAtendimento) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);	
			ps = conexao.prepareStatement(UPDATE);
			ps.setString(1, horarioAtendimento.getDiaDaSemana().toString());
			ps.setTime(2, Time.valueOf(horarioAtendimento.getAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			ps.setInt(5, horarioAtendimento.getId());
			boolean isAlteracaoOK = ps.executeUpdate() == 1;
			if (isAlteracaoOK) {
				this.conexao.commit();
			}else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException(
					"Ocorreu um erro ao alterar o horário de atendimento. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
	}

	@Override
	public void excluirPor(int id) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			ps = conexao.prepareStatement(DELETE);
			ps.setInt(1, id);
			boolean isExclusaoOK = ps.executeUpdate() == 1;
			if (isExclusaoOK) {
				this.conexao.commit();				
			}else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "excluir o horário de atendimento. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}
	}
	
	private Horario extrairDo(ResultSet rs) {
		try {
			int id = rs.getInt("id");
			String diaSemana = rs.getString("dia_semana");
			LocalTime horarioAbertura = rs.getTime("hora_abertura").toLocalTime();
			LocalTime horarioFechamento = rs.getTime("hora_fechamento").toLocalTime();
			
			int idDoRestaurante = rs.getInt("id_restaurante");
			String nomeDoRestaurante = rs.getString("nome_restaurante");

			Restaurante restaurante = new Restaurante(idDoRestaurante, nomeDoRestaurante);

			return new Horario(id, DiasDaSemana.valueOf(diaSemana), horarioAbertura, horarioFechamento, restaurante);
			
		} catch (Exception e) {
			throw new RuntimeException(
					"Ocorreu um erro ao extrair o horário de atendimento. Motivo: " + e.getMessage());
		}
	}

	@Override
	public Horario buscarPor(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				return extrairDo(rs);
			}
			return null;
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "buscar o restaurante. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<Horario> listarPor(Restaurante restaurante) {
		List<Horario> horariosAtendimento = new ArrayList<Horario>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_RESTAURANTE);
			ps.setInt(1, restaurante.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				horariosAtendimento.add(extrairDo(rs));
			}
			return horariosAtendimento;
		} catch (Exception e) {
			throw new RuntimeException(
					"Ocorreu um erro ao listar os horários de atendimento. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

}
