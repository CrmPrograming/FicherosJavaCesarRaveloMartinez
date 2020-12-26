package aed.proyectos.ficheros_java.utils.dialog;

import java.time.LocalDate;

public class ContratoFutbolista {

	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private String nombreFutbolista;

	public ContratoFutbolista(LocalDate fechaInicio, LocalDate fechaFin, String nombreFutbolista) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.nombreFutbolista = nombreFutbolista;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getNombreFutbolista() {
		return nombreFutbolista;
	}

	public void setNombreFutbolista(String nombreFutbolista) {
		this.nombreFutbolista = nombreFutbolista;
	}

}
