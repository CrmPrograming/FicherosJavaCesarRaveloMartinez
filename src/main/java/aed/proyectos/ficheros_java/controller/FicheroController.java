package aed.proyectos.ficheros_java.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import aed.proyectos.ficheros_java.App;
import aed.proyectos.ficheros_java.model.Fichero;
import aed.proyectos.ficheros_java.utils.GestorFichero;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
	private ToggleGroup tipoFile;

	@FXML
	private RadioButton rbFichero;

	@FXML
	private RadioButton rbCarpeta;

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

		crearBindeosOperacionesBasicas();

		lvFicherosCarpetas.itemsProperty().bind(fichero.get().listadoProperty());

		taContenidoFichero.textProperty().bindBidirectional(fichero.get().contenidoProperty());
	}

	private void crearBindeosOperacionesBasicas() {
		tfNombre.textProperty().bindBidirectional(fichero.get().nombreProperty());
		rbCarpeta.selectedProperty().bindBidirectional(fichero.get().carpetaProperty());
		rbFichero.selectedProperty().bindBidirectional(fichero.get().ficheroProperty());

		// Deshabilitar si:
		// - Al menos un campo de texto está vacío
		// - Ambos checkbox están sin marcar
		btCrear.disableProperty()
				.bind(Bindings.or(tfRutaActual.textProperty().isEmpty(), tfNombre.textProperty().isEmpty()));

		btEliminar.disableProperty().bind(tfRutaActual.textProperty().isEmpty());

		btMover.disableProperty().bind(tfRutaActual.textProperty().isEmpty());
	}

	@FXML
	void onCrearAction(ActionEvent event) {
		String[] error = new String[] { "", ""};
		
		File ficheroCreado = GestorFichero.crearFichero(fichero.get(), error);
		
		if (!error[0].equals(""))
			App.error(error[0], error[1]);
		else {
			fichero.get().setRuta(ficheroCreado.getAbsolutePath());
			App.info("Operación realizada con éxito", "Se ha podido crear sin problemas el "
					+ ((fichero.get().isCarpeta()) ? "directorio" : "fichero")
					+ " de nombre '" + fichero.get().getNombre() + "'.");
		} 
	}
	
	@FXML
	void onEliminarAction(ActionEvent event) {
		File file = new File(fichero.get().getRuta());
		String tipo = (file.isDirectory()) ? "directorio" : "fichero";
		if (App.confirm("Borrar " + tipo, "Borrar un " + tipo + " es una operación irreversible.",
				"¿Desea continuar?")) {
			if (file.isDirectory())
				GestorFichero.borrarEstructuraDirectorios(file);
			
			if (file.delete()) {
				App.info("Operación realizada con éxito",
						"Se ha podido borrar sin problemas el "
								+ ((fichero.get().isCarpeta()) ? "directorio" : "fichero") + " de nombre '"
								+ file.getName() + "'.");
				fichero.get().setRuta("");
			} else
				App.error("Se ha producido un error al intentar hacer el borrado.",
						"No se ha podido borrar '" + fichero.getName() + "'.");
		}
	}

	@FXML
	void onMoverAction(ActionEvent event) {
		String[] error = new String[] { "", "" };
		
		GestorFichero.moverFichero(fichero.get(), error);
		
		if (!error[0].equals("")) {
			App.error(error[0], error[1]);
		} else
			App.info("Operación realizada con éxito", "Se ha podido mover sin problemas a la nueva ubicación.");
	}
	
	@FXML
	void onVerFicherosCarpetasAction(ActionEvent event) {
		String[] error = new String[] { "", "" };

		GestorFichero.listarDirectorio(fichero.get(), error);

		if (!error[0].equals("")) {
			App.error(error[0], error[1]);
		}
	}
	

	@FXML
	void onVerContenidoAction(ActionEvent event) {
		String[] error = new String[] { "", "" };

		GestorFichero.mostrarContenido(fichero.get(), error);

		if (!error[0].equals("")) {
			App.error(error[0], error[1]);
		}
	}	
	

	@FXML
	void onModificarContenidoAction(ActionEvent event) {
		String[] error = new String[] { "", "" };

		GestorFichero.modificarContenido(fichero.get(), error);

		if (!error[0].equals("")) {
			App.error(error[0], error[1]);
		} else
			App.info("Operación realizada con éxito.",
					"Se han aplicado los cambios al fichero '" + fichero.get().getNombre() + "'");
	}

	public VBox getView() {
		return view;
	}

	public void setView(VBox view) {
		this.view = view;
	}

}
