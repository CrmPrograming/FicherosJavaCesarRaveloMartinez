package aed.proyectos.ficheros_java.model.xml;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Equipo {

	private StringProperty nombreEquipo = new SimpleStringProperty();
	private IntegerProperty totalCopas = new SimpleIntegerProperty();
	private StringProperty codLiga = new SimpleStringProperty();
	private ListProperty<Contrato> contratos = new SimpleListProperty<Contrato>(FXCollections.observableArrayList());

	public final StringProperty nombreEquipoProperty() {
		return this.nombreEquipo;
	}

	public final String getNombreEquipo() {
		return this.nombreEquipoProperty().get();
	}

	public final void setNombreEquipo(final String nombreEquipo) {
		this.nombreEquipoProperty().set(nombreEquipo);
	}

	public final IntegerProperty totalCopasProperty() {
		return this.totalCopas;
	}

	public final int getTotalCopas() {
		return this.totalCopasProperty().get();
	}

	public final void setTotalCopas(final int totalCopas) {
		this.totalCopasProperty().set(totalCopas);
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

	public final ListProperty<Contrato> contratosProperty() {
		return this.contratos;
	}

	public final ObservableList<Contrato> getContratos() {
		return this.contratosProperty().get();
	}

	public final void setContratos(final ObservableList<Contrato> contratos) {
		this.contratosProperty().set(contratos);
	}

}
