package aed.proyectos.ficheros_java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class App extends Application {
	
	private MainController controller;
	private static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		App.primaryStage = primaryStage;
		
		controller = new MainController();
		
		Scene escena = new Scene(controller.getView());
		
		primaryStage.setScene(escena);
		primaryStage.setTitle("Ficheros en Java - Proyecto AED - César Ravelo Martínez");		
		primaryStage.show();

	}
	
	public static void error(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		
		alert.initOwner(App.primaryStage);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
