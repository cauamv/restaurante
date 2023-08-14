package br.com.senai.core.domain;

import java.time.LocalTime;
import java.util.Objects;

public class Horario {

	private int id;
	
	private DiasDaSemana diaDaSemana;
	
	private LocalTime abertura;
	
	private LocalTime fechamento;
	
	private Restaurante restaurante;

	public Horario(DiasDaSemana diaDaSemana, LocalTime horarioDeAbertura, LocalTime horarioDeFechamento, Restaurante restaurante) {
		this.diaDaSemana = diaDaSemana;
		this.abertura = horarioDeAbertura;
		this.fechamento = horarioDeFechamento;
		this.restaurante = restaurante;
	}

	public Horario(int id, DiasDaSemana diaDaSemana, LocalTime horarioDeAbertura, LocalTime horarioDeFechamento, Restaurante restaurante) {
		this(diaDaSemana, horarioDeAbertura, horarioDeFechamento, restaurante);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DiasDaSemana getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(DiasDaSemana diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public LocalTime getAbertura() {
		return abertura;
	}

	public void setAbertura(LocalTime abertura) {
		this.abertura = abertura;
	}

	public LocalTime getFechamento() {
		return fechamento;
	}

	public void setFechamento(LocalTime fechamento) {
		this.fechamento = fechamento;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	@Override
	public int hashCode() {
		return Objects.hash(abertura, diaDaSemana, fechamento, id, restaurante);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Horario other = (Horario) obj;
		return Objects.equals(abertura, other.abertura) && Objects.equals(diaDaSemana, other.diaDaSemana)
				&& Objects.equals(fechamento, other.fechamento) && id == other.id
				&& Objects.equals(restaurante, other.restaurante);
	}	
	
}
