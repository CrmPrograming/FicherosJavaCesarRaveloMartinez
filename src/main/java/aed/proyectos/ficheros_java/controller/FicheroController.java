package aed.proyectos.ficheros_java.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private ListView<?> lvFicherosCarpetas;

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
		
	}

	public VBox getView() {
		return view;
	}

	public void setView(VBox view) {
		this.view = view;
	}    
	
}
