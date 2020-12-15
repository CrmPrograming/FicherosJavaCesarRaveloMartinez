package aed.proyectos.ficheros_java.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import aed.proyectos.ficheros_java.model.Fichero;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FicheroController implements Initializable {

	// model
	private ObjectProperty<Fichero> fichero = new SimpleObjectProperty<>(new Fichero());
	// view

	@FXML
	private VBox view;

	@FXML
	private TextField tfRutaActual;

	@FXML
	private Button btCrear;

	@FXML
	private Button btEliminar;

	@FXML
	private Button btMover;

	@FXML
	private CheckBox cbCarpeta;

	@FXML
	private CheckBox cbFichero;

	@FXML
	private TextField tfNombre;

	@FXML
	private Button btVerFicherosCarpetas;

	@FXML
	private ListView<String> lvFicherosCarpetas;

	@FXML
	private Button btVerContenidoFichero;

	@FXML
	private Button btModificarFichero;

	@FXML
	private TextArea taContenidoFichero;

	public FicheroController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FicherosView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfRutaActual.textProperty().bindBidirectional(fichero.get().rutaProperty());
		tfNombre.textProperty().bindBidirectional(fichero.get().nombreProperty());
		cbCarpeta.selectedProperty().bindBidirectional(fichero.get().carpetaProperty());
		cbFichero.selectedProperty().bindBidirectional(fichero.get().ficheroProperty());

		// Deshabilitar si:
		// - Al menos un campo de texto está vacío
		// - Ambos checkbox están sin marcar		
		btCrear.disableProperty().bind(
				Bindings.or(
						Bindings.and(cbCarpeta.selectedProperty().not(), cbFichero.selectedProperty().not()),
						Bindings.or(tfRutaActual.textProperty().isEmpty(), tfNombre.textProperty().isEmpty())
				)
		);
		
		lvFicherosCarpetas.itemsProperty().bind(fichero.get().listadoProperty());

	}

	@FXML
	void onCrearAction(ActionEvent event) {

	}

	@FXML
	void onEliminarAction(ActionEvent event) {

	}

	@FXML
	void onMoverAction(ActionEvent event) {

	}
	
	@FXML
	void onVerFicherosCarpetasAction(ActionEvent event) {
		fichero.get().getListado().clear();
		
		String ruta = fichero.get().getRuta();
		
		File root = new File(ruta);
		
		if (root.isDirectory()) {
			
			for (String file : root.list()) {
				fichero.get().getListado().add(file);
			}
			
		} else {
			// TODO: Diálogo de error si no es un directorio
		}
	}

	public VBox getView() {
		return view;
	}

	public void setView(VBox view) {
		this.view = view;
	}

}
