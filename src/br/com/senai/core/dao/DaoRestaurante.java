package br.com.senai.core.dao;

import java.util.List;

import br.com.senai.core.domain.Restaurante;

public interface DaoRestaurante {
	
	public Restaurante buscarPor(int id);
	
	public void excluirPor(int id);
	
	public List<Restaurante> listarPor(String nome);
	
	public int contarPor(int idDaCategoria);
}
	