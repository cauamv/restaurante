package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Restaurante;

public class DaoPostgresRestaurante implements DaoRestaurante {
	
	private final String SELECT_BY_ID = "SELECT r.id id_restaurante, r.nome nome_restaurante, r.descricao, "
			                          + "r.cidade, r.logradouro, r.bairro, r.complemento, "
			                          + "c.id id_categoria, c.nome nome_categoria "
			                          + "FROM restaurantes r, "
			                          + "categorias c "
			                          + "WHERE r.id_categoria = c.id "
			                          + "AND r.id = ?";
	
	private final String DELETE = "DELETE FROM restaurantes WHERE id = ?";
	
	private final String SELECT_BY_NOME_CATEG = "SELECT r.id id_restaurante, r.nome nome_restaurante, "
											  + "r.descricao, "
											  + "r.cidade, r.logradouro, r.bairro, r.complemento, "
											  + "c.id id_categoria, c.nome nome_categoria "
											  + "FROM restaurantes r,"
											  + " categorias c "
											  + "WHERE r.id_categoria = c.id ";
	
	private final String COUNT_BY_CATEG = "SELECT Count(*) qtde "
			+ "FROM restaurantes r "
			+ "WHERE r.id_categoria = ?";
	
	
	private Connection conexao;
	
	public DaoPostgresRestaurante() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}

	@Override
	public Restaurante buscarPor(int id) {
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
			throw new RuntimeException("Ocorreu um erro ao buscar o restaurante. "
					+ "Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
		
	}

	@Override
	public List<Restaurante> listarPor(String nome) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		
		try {
			
			StringBuilder consulta = new StringBuilder(SELECT_BY_NOME_CATEG);
			
			if (nome != null && !nome.isBlank()) {
				consulta.append(" AND Upper(r.nome) LIKE Upper(?) ");
			}
			
			consulta.append(" ORDER BY r.nome ");
			
			ps = conexao.prepareStatement(consulta.toString());
			
			int indice = 1;
			
			if (nome != null && !nome.isBlank()) {
				ps.setString(indice, nome);
			}
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				restaurantes.add(extrairDo(rs));
			}
			
			return restaurantes;
			
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao listar os restaurantes. "
					+ "Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}		
				
	}
	
	private Restaurante extrairDo(ResultSet rs) {
		try {
			
			int idDoRestaurante = rs.getInt("id_restaurante");
			String nomeDoRestaurante = rs.getString("nome_restaurante");	
			
			return new Restaurante(idDoRestaurante, nomeDoRestaurante);
			
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao extrair o restaurante. "
					+ "Motivo: " + e.getMessage());
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
			throw new RuntimeException("Ocorreu um erro ao excluir o restaurante. "
					+ "Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}

	}

	public int contarPor(int idDaCategoria) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = conexao.prepareStatement(COUNT_BY_CATEG);
			ps.setInt(1, idDaCategoria);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				return rs.getInt("qtde");
			}
			
			return 0;
			
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao contar os restaurantes. "
					+ "Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

}
