package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {

	private DaoRestaurante dao;

	private DaoHorario daoHorario;

	public RestauranteService() {
		this.dao = FactoryDao.getInstance().getDaoRestaurante();
		this.daoHorario = FactoryDao.getInstance().getDaoHorario();
	}

	public List<Restaurante> listarPor(String nome) {
		boolean isNomeInformado = nome != null && !nome.isBlank();
		if (!isNomeInformado) {
			throw new IllegalArgumentException("Informe o nome e/ou categoria para listagem");
		}
		String filtroNome = "";
		return dao.listarPor(filtroNome);
	}

	public List<Restaurante> listarTodos() {
		return dao.listarPor("%%");
	}

	public void excluirPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			
			int qtdeDeHorarios = daoHorario.contarPor(idDoRestaurante);
			if (qtdeDeHorarios > 0) {
				throw new IllegalArgumentException("Não foi possivel excluir o restaurante. "
						+ "Motivo: Existem " + qtdeDeHorarios + " horários vinculados ao restaurante");
			}
			
			this.dao.excluirPor(idDoRestaurante);
			
		} else {
			throw new IllegalArgumentException("O id para exclusão deve ser maior que zero");
		}
	}
	
	public Restaurante buscarPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			Restaurante restauranteEncontrado = this.dao.buscarPor(idDoRestaurante);

			if (restauranteEncontrado == null) {
				throw new IllegalArgumentException("Não existe restaurante vinculado ao id " + "informado");
			}

			return restauranteEncontrado;

		} else {
			throw new IllegalArgumentException("O id para busca não pode ser menor que zero");
		}
	}

}
