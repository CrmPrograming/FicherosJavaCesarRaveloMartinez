package aed.proyectos.ficheros_java.model.xml;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contrato {
	private StringProperty nombreFutbolista = new SimpleStringProperty();
	private ObjectProperty<LocalDate> fechaInicio = new SimpleObjectProperty<LocalDate>();
	private ObjectProperty<LocalDate> fechaFin = new SimpleObjectProperty<LocalDate>();

	public final StringProperty nombreFutbolistaProperty() {
		return this.nombreFutbolista;
	}

	public final String getNombreFutbolista() {
		return this.nombreFutbolistaProperty().get();
	}

	public final void setNombreFutbolista(final String nombreFutbolista) {
		this.nombreFutbolistaProperty().set(nombreFutbolista);
	}

	public final ObjectProperty<LocalDate> fechaInicioProperty() {
		return this.fechaInicio;
	}

	public final LocalDate getFechaInicio() {
		return this.fechaInicioProperty().get();
	}

	public final void setFechaInicio(final LocalDate fechaInicio) {
		this.fechaInicioProperty().set(fechaInicio);
	}

	public final ObjectProperty<LocalDate> fechaFinProperty() {
		return this.fechaFin;
	}

	public final LocalDate getFechaFin() {
		return this.fechaFinProperty().get();
	}

	public final void setFechaFin(final LocalDate fechaFin) {
		this.fechaFinProperty().set(fechaFin);
	}

}
