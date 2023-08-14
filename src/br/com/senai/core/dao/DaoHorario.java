package br.com.senai.core.dao;

import java.util.List;

import br.com.senai.core.domain.Horario;
import br.com.senai.core.domain.Restaurante;

public interface DaoHorario {

	public void inserir(Horario horarioAtendimento);

	public void alterar(Horario horarioAtendimento);

	public void excluirPor(int id);
	
	public Horario buscarPor(int id);

	public List<Horario> listarPor(Restaurante restaurante);
	
}
