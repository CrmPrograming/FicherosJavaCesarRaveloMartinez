package aed.proyectos.ficheros_java.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

public class RandomController implements Initializable {

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
	private TableView<?> tvEquipos;

	@FXML
	private TableColumn<?, Number> tcID;

	@FXML
	private TableColumn<?, String> tcNombre;

	public RandomController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RandomAccessView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@FXML
	void onAbrirAction(ActionEvent event) {

	}

	@FXML
	void onGuardarAction(ActionEvent event) {

	}

	@FXML
	void onModCopasAction(ActionEvent event) {

	}

	@FXML
	void onVerDatosAction(ActionEvent event) {

	}

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
