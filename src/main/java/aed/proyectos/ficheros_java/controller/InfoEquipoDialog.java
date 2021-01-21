package aed.proyectos.ficheros_java.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import aed.proyectos.ficheros_java.App;
import aed.proyectos.ficheros_java.model.acceso_aleatorio.Equipo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase gestora de la ventana que
 * mostrará la información asociada a un
 * equipo.
 * 
 * @author César Ravelo Martínez
 *
 */
public class InfoEquipoDialog extends Stage implements Initializable {

	// model
	
	private Equipo equipo;
	
	// view
	
	@FXML
    private VBox view;

    @FXML
    private Label lbCodEquipo;

    @FXML
    private Label lbNombreEquipo;

    @FXML
    private Label lbCodLiga;

    @FXML
    private Label lbLocalidad;

    @FXML
    private Label lbCopas;

    @FXML
    private Label lbInternacional;
	
	public InfoEquipoDialog(Equipo equipo) throws IOException {
		super();
		this.equipo = equipo;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DialogInfoEquipoView.fxml"));
		loader.setController(this);
		loader.load();
		
		this.setScene(new Scene(this.view));
		this.setTitle("Información del equipo");
		this.getIcons().add(new Image("/images/cv64x64.png"));
		this.initModality(Modality.WINDOW_MODAL);
		this.initOwner(App.getPrimaryStage());
		this.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (equipo != null) {
			lbCodEquipo.setText(equipo.getCodEquipo() + "");
			lbNombreEquipo.setText(equipo.getNombreEquipo());
			lbCodLiga.setText(equipo.getCodLiga());
			lbLocalidad.setText(equipo.getLocalidad());
			lbCopas.setText(equipo.getCopasGanadas() + "");
			lbInternacional.setText((equipo.isInternacional())?"Sí":"No");
		}
	}

}
