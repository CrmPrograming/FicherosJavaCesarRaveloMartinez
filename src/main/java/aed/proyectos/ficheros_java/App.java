package aed.proyectos.ficheros_java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	private MainController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		controller = new MainController();
		
		Scene escena = new Scene(controller.getView());
		
		primaryStage.setScene(escena);
		primaryStage.setTitle("Ficheros en Java - Proyecto AED - César Ravelo Martínez");		
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
