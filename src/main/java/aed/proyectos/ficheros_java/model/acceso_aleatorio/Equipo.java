package aed.proyectos.ficheros_java.model.acceso_aleatorio;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Equipo {

	private IntegerProperty codEquipo = new SimpleIntegerProperty();
	private StringProperty nombreEquipo = new SimpleStringProperty();
	private StringProperty codLiga = new SimpleStringProperty();
	private StringProperty localidad = new SimpleStringProperty();
	private IntegerProperty copasGanadas = new SimpleIntegerProperty();
	private BooleanProperty internacional = new SimpleBooleanProperty();

	public final IntegerProperty codEquipoProperty() {
		return this.codEquipo;
	}

	public final int getCodEquipo() {
		return this.codEquipoProperty().get();
	}

	public final void setCodEquipo(final int codEquipo) {
		this.codEquipoProperty().set(codEquipo);
	}

	public final StringProperty nombreEquipoProperty() {
		return this.nombreEquipo;
	}

	public final String getNombreEquipo() {
		return this.nombreEquipoProperty().get();
	}

	public final void setNombreEquipo(final String nombreEquipo) {
		this.nombreEquipoProperty().set(nombreEquipo);
	}

	public final StringProperty codLigaProperty() {
		return this.codLiga;
	}

	public final String getCodLiga() {
		return this.codLigaProperty().get();
	}

	public final void setCodLiga(final String codLiga) {
		this.codLigaProperty().set(codLiga);
	}

	public final StringProperty localidadProperty() {
		return this.localidad;
	}

	public final String getLocalidad() {
		return this.localidadProperty().get();
	}

	public final void setLocalidad(final String localidad) {
		this.localidadProperty().set(localidad);
	}

	public final IntegerProperty copasGanadasProperty() {
		return this.copasGanadas;
	}

	public final int getCopasGanadas() {
		return this.copasGanadasProperty().get();
	}

	public final void setCopasGanadas(final int copasGanadas) {
		this.copasGanadasProperty().set(copasGanadas);
	}

	public final BooleanProperty internacionalProperty() {
		return this.internacional;
	}

	public final boolean isInternacional() {
		return this.internacionalProperty().get();
	}

	public final void setInternacional(final boolean internacional) {
		this.internacionalProperty().set(internacional);
	}

}
