package br.com.senai.core.service;

import java.time.LocalTime;
import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Horario;
import br.com.senai.core.domain.Restaurante;

public class HorarioService {
	
	private DaoHorario daoHorario;
	public HorarioService() {
		this.daoHorario = FactoryDao.getInstance().getDaoHorario();
	}
	
	public void salvar(Horario horario) {
		
		this.validar(horario);
		boolean isPersistido = horario.getId() > 0;
		this.validarConflitoHorario(horario, daoHorario.listarPor(horario.getRestaurante()));
		if (isPersistido) {
			this.daoHorario.alterar(horario);
		} else {
			this.daoHorario.inserir(horario);
		}
	}

	private void validar(Horario horario) {
		if (horario != null) {
			if (horario.getRestaurante() != null && horario.getRestaurante().getId() > 0) {
				
				String diaDaSemana = horario.getDiaDaSemana().toString();
				LocalTime horarioDeAbertura = horario.getAbertura();
				LocalTime horarioDeFechamento = horario.getFechamento();
				if (diaDaSemana == null) {
					throw new IllegalArgumentException("O dia da semana é obrigatório");
				}
				if (horarioDeAbertura == null || horarioDeFechamento == null) {
					throw new IllegalArgumentException("Os horários de abertura e fechamento são obrigatórios");
				}
				if (horarioDeAbertura == null || horarioDeFechamento == null) {
					throw new IllegalArgumentException("Os horários de abertura e fechamento são obrigatórios");
				}
				if (horarioDeAbertura.isAfter(horarioDeFechamento)) {
					throw new IllegalArgumentException(
							"O horário de abertura deve ser anterior ao horário de fechamento");
				}
				if (horarioDeAbertura.equals(horarioDeFechamento)) {
					throw new IllegalArgumentException(
							"O horário de abertura é igual ao horário de fechamento");
				}
			} else {
				throw new NullPointerException("O restaurante do horário é obrigatória");
			}
		} else {
			throw new NullPointerException("O horário de atendimento não pode ser nulo");
		}
	}

	public void removerPor(int idDoHorario) {
		if (idDoHorario > 0) {
			this.daoHorario.excluirPor(idDoHorario);
		} else {
			throw new IllegalArgumentException("O id para remoção do horário deve ser maior que zero");
		}
	}
	
	public Horario buscarPor(int idDoHorario) {
		if (idDoHorario > 0) {
			Horario horarioEncontrado = daoHorario.buscarPor(idDoHorario);
			if (horarioEncontrado == null) {
				throw new IllegalArgumentException("Não foi encontrado restaurante para o código informado");
			}
			return horarioEncontrado;
		} else {
			throw new IllegalArgumentException("O id para busca do restaurante deve ser maior que zero");
		}
	}

	public List<Horario> listarPor(Restaurante restaurante) {
		if (restaurante == null || restaurante.getId() <= 0) {
			throw new IllegalArgumentException("O restaurante é obrigatório para listar os horários de atendimento");
		}
		return daoHorario.listarPor(restaurante);
	}
	
    private boolean horariosConflitantes(LocalTime abertura1, LocalTime fechamento1, LocalTime abertura2, LocalTime fechamento2) {
        return (abertura1.isBefore(fechamento2) && abertura2.isBefore(fechamento1)) ||
               abertura1.equals(abertura2) ||
               fechamento1.equals(fechamento2);
    }

    public void validarConflitoHorario(Horario horarioNovo, List<Horario> horariosSalvos) {
        for (Horario horarioSalvo : horariosSalvos) {
            if (horarioSalvo.getDiaDaSemana().equals(horarioNovo.getDiaDaSemana())) {
                if (horariosConflitantes(
                        horarioNovo.getAbertura(), horarioNovo.getFechamento(),
                        horarioSalvo.getAbertura(), horarioSalvo.getFechamento())) {
                    throw new IllegalArgumentException("ERRO: O novo horário conflita com um horário já cadastrado");
                }
            }
        }
    }

}