package aed.proyectos.ficheros_java.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import aed.proyectos.ficheros_java.App;
import aed.proyectos.ficheros_java.model.RandomAccess;
import aed.proyectos.ficheros_java.model.acceso_aleatorio.Equipo;
import aed.proyectos.ficheros_java.utils.GestorAccesoAleatorio;
import aed.proyectos.ficheros_java.utils.dialog.IntegerDialog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.NumberStringConverter;

/**
 * Clase controladora de la pestaña asociada
 * a la manipulación de ficheros de Acceso Aleatorio.
 * 
 * @author César Ravelo Martínez
 *
 */
public class RandomController implements Initializable {
	
	// model
	
	private ObjectProperty<RandomAccess> random = new SimpleObjectProperty<RandomAccess>(new RandomAccess());

	// view
	
	@FXML
	private BorderPane view;

	@FXML
	private Button btAbrir;

	@FXML
	private Button btVerDatos;

	@FXML
	private Button btModCopas;

	@FXML
	private Button btGuardar;

	@FXML
	private TitledPane tpFichero;

	@FXML
	private TableView<Equipo> tvEquipos;

	@FXML
	private TableColumn<Equipo, Number> tcID;

	@FXML
	private TableColumn<Equipo, String> tcNombre;

	/**
     * Constructor de la clase.
     * 
     * @throws IOException
     */
	public RandomController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RandomAccessView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tcID.setCellValueFactory(v -> v.getValue().codEquipoProperty());
		tcNombre.setCellValueFactory(v -> v.getValue().nombreEquipoProperty());

		tcID.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		tcNombre.setCellFactory(TextFieldTableCell.forTableColumn());
		
		random.addListener((o, ov, nv) -> onRandomChanged(o, ov, nv));
		
		try {
			File file = new File("Equipos.dat");
			random.set(GestorAccesoAleatorio.inicializar(file));
		} catch (IOException e) {
			App.error("Error de lectura", "Se ha producido un error intentando leer el fichero.\nAsegúrese que esté en un formato válido.");
		}
	}

	/**
     * Método encargado de actualizar los componentes
     * cuando cambie el objeto modelo principal del controlador.
     * 
     * @param o
     * @param ov
     * @param nv
     */
	private void onRandomChanged(ObservableValue<? extends RandomAccess> o, RandomAccess ov, RandomAccess nv) {
		if (ov != null) {
			tvEquipos.setItems(null);
			ov.equipoSeleccionadoProperty().unbind();
			btVerDatos.disableProperty().unbind();
			btModCopas.disableProperty().unbind();
		}
		
		if (nv != null) {
			tvEquipos.setItems(nv.getEquipos());
			nv.equipoSeleccionadoProperty().bind(tvEquipos.getSelectionModel().selectedItemProperty());
			tpFichero.setText(nv.getFichero().getName());
			btVerDatos.disableProperty().bind(nv.equipoSeleccionadoProperty().isNull());
			btModCopas.disableProperty().bind(nv.equipoSeleccionadoProperty().isNull());
		}
	}

	@FXML
	void onAbrirAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Abrir");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Datos (*.dat)", "*.dat"));
		
		File file = fileChooser.showOpenDialog(App.getPrimaryStage());
		if (file != null && file.isFile()) {
			try {
				random.set(GestorAccesoAleatorio.leerFichero(file));
				App.info("Operación realizada con éxito.", "Se ha cargado el fichero '" + file.getName() + "' sin problemas.");
			} catch (IOException e) {
				App.error("Error de lectura", "Se ha producido un error intentando leer el fichero.\nAsegúrese que esté en un formato válido.");
			}
		}
	}

	@FXML
	void onGuardarAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar como");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Datos (*.dat)", "*.dat"));
		
		File file = fileChooser.showSaveDialog(App.getPrimaryStage());
		if (file != null) {
			try {
				GestorAccesoAleatorio.volcarDatos(random.get().getEquipos(), file);
				random.get().setFichero(file);
				tpFichero.setText(file.getName());
				App.info("Operación realizada con éxito.", "Se ha guardado correctamente en el fichero '" + file.getName() + "'.");
			} catch (IOException e) {
				App.error("Error de guardado", "Se ha producido un error intentando guardar el fichero.\nAsegúrese de tener los permisos necesarios.");
			}
		}
	}

	@FXML
	void onModCopasAction(ActionEvent event) {
		IntegerDialog dialog = new IntegerDialog(
				"Modificar copas",
				"Actualizar",
				"Indique a continuación cuál es el nuevo valor del total de copas del equipo dado.",
				"Nuevo valor:",
				random.get().getEquipoSeleccionado().getCopasGanadas()
		);
		Optional<Integer> result = dialog.showAndWait();
		
		if (result.isPresent()) {
			try {
				Integer oldValue = random.get().getEquipoSeleccionado().getCopasGanadas();
				Integer newValue = result.get();
				
				GestorAccesoAleatorio.modificarCopas(random.get().getFichero(), random.get().getEquipoSeleccionado().getCodEquipo(), newValue);
				random.get().getEquipoSeleccionado().setCopasGanadas(newValue);
				App.info("Operación realizada con éxito.", "Se ha modificado correctamente el total de copas de '" + oldValue + "' a '" + newValue + "'.");				
				
			} catch (IOException e) {
				App.error("Error de lectura", "Se ha producido un error intentando leer el fichero.\nAsegúrese que esté en un formato válido.");
			}
		}
	}

	@FXML
	void onVerDatosAction(ActionEvent event) {
		try {
			new InfoEquipoDialog(
					GestorAccesoAleatorio.consultarEquipo(
							random.get().getFichero(),
							random.get().getEquipoSeleccionado().getCodEquipo()
					)
			);
		} catch (IOException e) {
			App.error("Error de lectura", "Se ha producido un error intentando leer el fichero.\nAsegúrese que esté en un formato válido.");
		}
	}

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
