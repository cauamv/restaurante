package br.com.senai.core.domain;

import java.util.Objects;

public class Restaurante {
	
	private int id;
	
	private String nome;

	public Restaurante(String nome) {		
		this.nome = nome;
	}

	public Restaurante(int id, String nome) {
		this(nome);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurante other = (Restaurante) obj;
		return id == other.id && Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return nome;
	}	
	
}
