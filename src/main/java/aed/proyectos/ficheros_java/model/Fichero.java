package aed.proyectos.ficheros_java.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Fichero {

	private StringProperty ruta = new SimpleStringProperty();
	private BooleanProperty carpeta = new SimpleBooleanProperty();
	private BooleanProperty fichero = new SimpleBooleanProperty();
	private StringProperty nombre = new SimpleStringProperty();
	private ListProperty<String> listado = new SimpleListProperty<>(FXCollections.observableArrayList());
	private StringProperty contenido = new SimpleStringProperty();

	public final StringProperty rutaProperty() {
		return this.ruta;
	}

	public final String getRuta() {
		return this.rutaProperty().get();
	}

	public final void setRuta(final String ruta) {
		this.rutaProperty().set(ruta);
	}

	public final BooleanProperty carpetaProperty() {
		return this.carpeta;
	}

	public final boolean isCarpeta() {
		return this.carpetaProperty().get();
	}

	public final void setCarpeta(final boolean carpeta) {
		this.carpetaProperty().set(carpeta);
	}

	public final BooleanProperty ficheroProperty() {
		return this.fichero;
	}

	public final boolean isFichero() {
		return this.ficheroProperty().get();
	}

	public final void setFichero(final boolean fichero) {
		this.ficheroProperty().set(fichero);
	}

	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	public final String getNombre() {
		return this.nombreProperty().get();
	}

	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final ListProperty<String> listadoProperty() {
		return this.listado;
	}

	public final ObservableList<String> getListado() {
		return this.listadoProperty().get();
	}

	public final void setListado(final ObservableList<String> listado) {
		this.listadoProperty().set(listado);
	}

	public final StringProperty contenidoProperty() {
		return this.contenido;
	}

	public final String getContenido() {
		return this.contenidoProperty().get();
	}

	public final void setContenido(final String contenido) {
		this.contenidoProperty().set(contenido);
	}

}
