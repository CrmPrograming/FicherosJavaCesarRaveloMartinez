package aed.proyectos.ficheros_java;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import aed.proyectos.ficheros_java.controller.FicheroController;
import aed.proyectos.ficheros_java.controller.RandomController;
import aed.proyectos.ficheros_java.controller.XMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable {

	// controller
	
	private FicheroController ficheroController = new FicheroController();
	private RandomController randomController = new RandomController(); 
	private XMLController xmlController = new XMLController();
	
	// view
	
	@FXML
	private BorderPane view;
	
	@FXML
    private Tab tbFicheros;

    @FXML
    private Tab tbAccesoAleatorio;

    @FXML
    private Tab tbXML;
	
	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbFicheros.setContent(ficheroController.getView());
		tbAccesoAleatorio.setContent(randomController.getView());
		tbXML.setContent(xmlController.getView());
	}

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
