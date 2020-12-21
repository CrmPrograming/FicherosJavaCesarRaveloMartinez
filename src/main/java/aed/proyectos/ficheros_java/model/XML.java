package aed.proyectos.ficheros_java.model;

import aed.proyectos.ficheros_java.model.xml.Equipo;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class XML {
	
	private ListProperty<Equipo> equipos = new SimpleListProperty<Equipo>(FXCollections.observableArrayList());
	private ObjectProperty<Equipo> equipoSeleccionado = new SimpleObjectProperty<Equipo>();
	
	public final ListProperty<Equipo> equiposProperty() {
		return this.equipos;
	}
	
	public final ObservableList<Equipo> getEquipos() {
		return this.equiposProperty().get();
	}
	
	public final void setEquipos(final ObservableList<Equipo> equipos) {
		this.equiposProperty().set(equipos);
	}
	
	public final ObjectProperty<Equipo> equipoSeleccionadoProperty() {
		return this.equipoSeleccionado;
	}
	
	public final Equipo getEquipoSeleccionado() {
		return this.equipoSeleccionadoProperty().get();
	}
	
	public final void setEquipoSeleccionado(final Equipo equipoSeleccionado) {
		this.equipoSeleccionadoProperty().set(equipoSeleccionado);
	}

}
