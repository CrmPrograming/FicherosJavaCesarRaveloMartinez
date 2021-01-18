package aed.proyectos.ficheros_java;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase gestora del Stage principal y demás operaciones
 * de interfaz de usuario básicas (como por ejemplo,
 * mostrar diálogos).
 * 
 * @author César Ravelo Martínez
 *
 */
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
		primaryStage.getIcons().add(new Image("/images/cv64x64.png"));
		primaryStage.show();
	}
	
	/**
	 * Método encargado de mostrar un diálogo de información.
	 * 
	 * @param header String con la cabecera del diálogo
	 * @param content String con el contenido del diálogo
	 */
	public static void info(String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(primaryStage);
		alert.setTitle("Información");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	/**
	 * Método encargado de mostrar un diálogo de error.
	 * 
	 * @param header String con la cabecera del diálogo
	 * @param content String con el contenido del diálogo
	 */
	public static void error(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		
		if (App.primaryStage != null)
			alert.initOwner(App.primaryStage);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	/**
	 * Método encargado de mostrar un diálogo de confirmación.
	 * 
	 * @param title String con el título del diálogo
	 * @param header String con la cabecera del diálogo
	 * @param content String con el contenido del diálogo
	 */
	public static boolean confirm(String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(App.primaryStage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		Optional<ButtonType> result = alert.showAndWait();
		
		return (result.get() == ButtonType.OK);
	}
	
	/**
	 * Método requerido por JavaFX para inicializar la aplicación.
	 * @param args Listado de argumentos de entrada.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

}
