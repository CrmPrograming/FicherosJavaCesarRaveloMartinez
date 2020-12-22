package aed.proyectos.ficheros_java.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;

import org.jdom2.JDOMException;

import aed.proyectos.ficheros_java.App;
import aed.proyectos.ficheros_java.Main;
import aed.proyectos.ficheros_java.model.XML;
import aed.proyectos.ficheros_java.model.xml.Contrato;
import aed.proyectos.ficheros_java.model.xml.Equipo;
import aed.proyectos.ficheros_java.utils.GestorXML;
import aed.proyectos.ficheros_java.utils.IntegerDialog;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.NumberStringConverter;

public class XMLController implements Initializable {
	
	// model
	
	private ObjectProperty<XML> xml = new SimpleObjectProperty<>(new XML());
	
	// view
	
	@FXML
    private VBox view;

    @FXML
    private Button btAbrir;
    
    @FXML
    private Button btActualizarCopas;

    @FXML
    private Button btEliminar;

    @FXML
    private Button btNuevo;

    @FXML
    private Button btGuardar;

    @FXML
    private TableView<Equipo> tbEquipo;

    @FXML
    private TableColumn<Equipo, String> tcNomEquipo;

    @FXML
    private TableColumn<Equipo, Number> tcCopas;

    @FXML
    private TableColumn<Equipo, String> tcLiga;

    @FXML
    private TableView<Contrato> tbContrato;

    @FXML
    private TableColumn<Contrato, String> tcFutbolista;

    @FXML
    private TableColumn<Contrato, LocalDate> tcFechaInicio;

    @FXML
    private TableColumn<Contrato, LocalDate> tcFechaFin;
    
    public XMLController() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/XMLView.fxml"));
		loader.setController(this);
		loader.load();
    }

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		tcNomEquipo.setCellValueFactory(v -> v.getValue().nombreEquipoProperty());
		tcCopas.setCellValueFactory(v -> v.getValue().totalCopasProperty());
		tcLiga.setCellValueFactory(v -> v.getValue().codLigaProperty());
		
		tcNomEquipo.setCellFactory(TextFieldTableCell.forTableColumn());
		tcCopas.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
		tcLiga.setCellFactory(TextFieldTableCell.forTableColumn());
		
		tcFutbolista.setCellValueFactory(v -> v.getValue().nombreFutbolistaProperty());
		tcFechaInicio.setCellValueFactory(v -> v.getValue().fechaInicioProperty());
		tcFechaFin.setCellValueFactory(v -> v.getValue().fechaFinProperty());
		
		tcFutbolista.setCellFactory(TextFieldTableCell.forTableColumn());
		tcFechaInicio.setCellFactory(TextFieldTableCell.forTableColumn(
				new LocalDateStringConverter(FormatStyle.SHORT)));
		tcFechaFin.setCellFactory(TextFieldTableCell.forTableColumn(
				new LocalDateStringConverter(FormatStyle.SHORT)));
		
		xml.addListener((o, ov, nv) -> onXMLChanged(o, ov, nv));		
		
		try {
			File file = new File("Equipos.xml");
			xml.set(GestorXML.leerFichero(file));
			xml.get().setFichero(file);
		} catch (JDOMException | IOException e) {
			App.error("Error de lectura", "Se ha producido un error intentando leer el fichero.\nAsegúrese que esté en un formato válido.");
		}
	}
    
    private void onXMLChanged(ObservableValue<? extends XML> o, XML ov, XML nv) {
    	if (ov != null) {
    		tbEquipo.setItems(null);
    		ov.equipoSeleccionadoProperty().unbind();
    		btActualizarCopas.disableProperty().unbind();
    		btEliminar.disableProperty().unbind();
    	}
    	
    	if (nv != null) {
    		tbEquipo.setItems(nv.getEquipos());
    		nv.equipoSeleccionadoProperty().bind(tbEquipo.getSelectionModel().selectedItemProperty());
    		nv.equipoSeleccionadoProperty().addListener((observable, oldvalue, newvalue) -> onEquipoSelectedChanged(observable, oldvalue, newvalue));
    		btActualizarCopas.disableProperty().bind(nv.equipoSeleccionadoProperty().isNull());
    		btEliminar.disableProperty().bind(nv.equipoSeleccionadoProperty().isNull());
    	}
	}

	private void onEquipoSelectedChanged(ObservableValue<? extends Equipo> o, Equipo ov, Equipo nv) {
		if (ov != null) {
			tbContrato.setItems(null);
		}
		
		if (nv != null) {
			tbContrato.setItems(nv.getContratos());
		}
	}

	@FXML
    void onAbrirFicheroAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Abrir");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML (*.xml)", "*.xml"));
		
		File file = fileChooser.showOpenDialog(App.getPrimaryStage());
		if (file != null && file.isFile()) {
			try {
				xml.set(GestorXML.leerFichero(file));
				xml.get().setFichero(file);
			} catch (JDOMException | IOException e) {
				App.error("Error de lectura", "Se ha producido un error intentando leer el fichero.\nAsegúrese que esté en un formato válido.");
			}
		}
    }
	
	@FXML
    void onActualizarCopasAction(ActionEvent event) {
		IntegerDialog dialog = new IntegerDialog(
				"Modificar copas",
				"Actualizar",
				"Indique a continuación cuál es el nuevo valor del total de copas del equipo dado.",
				"Nuevo valor:",
				xml.get().getEquipoSeleccionado().getTotalCopas()
		);
		Optional<Integer> result = dialog.showAndWait();
		
		if (result.isPresent()) {
			try {
				boolean estado = GestorXML.modificarCopas(xml.get().getFichero(), xml.get().getEquipoSeleccionado().getNombreEquipo(), result.get());
				// TODO: Actualizar campo en la tabla
			} catch (JDOMException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    @FXML
    void onEliminarEquipoAction(ActionEvent event) {
    	
    }

    @FXML
    void onGuardarAction(ActionEvent event) {

    }

    @FXML
    void onNuevoContratoAction(ActionEvent event) {

    }

	public VBox getView() {
		return view;
	}

	public void setView(VBox view) {
		this.view = view;
	}

}
