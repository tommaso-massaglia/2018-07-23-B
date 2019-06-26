package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento> {

	public enum TIPO {
		AVVISTAMENTO, EMERGENZA, RIPRISTINO_DEFCON
	}

	private LocalDateTime data;
	private String state;
	private TIPO tipo;

	public Evento(LocalDateTime data, String state, TIPO tipo) {
		this.data = data;
		this.state = state;
		this.tipo = tipo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public TIPO getTipo() {
		return tipo;
	}

	public void setTipo(TIPO tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Evento arg0) {
		return this.data.compareTo(arg0.data);
	}

	@Override
	public String toString() {
		return "Evento [data=" + data + ", state=" + state + ", tipo=" + tipo + "]";
	}

}
