package aed.proyectos.ficheros_java.utils.dialog;

import aed.proyectos.ficheros_java.model.xml.Contrato;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase de apoyo para mostrar un diálogo al usuario
 * y pedirle datos de un contrato.
 * 
 * @author César Ravelo Martínez
 *
 */
public class ContratoDialog extends Dialog<Contrato> {
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param nomEquipo Nombre del equipo a mostrar en el título
	 */
	public ContratoDialog(String nomEquipo) {
		setTitle("Nuevo contrato del equipo " + nomEquipo);

		Stage stage = (Stage) getDialogPane().getScene().getWindow();
		stage.setMinWidth(350);
		stage.setMinHeight(150);
		stage.getIcons().add(new Image(this.getClass().getResource("/images/cv64x64.png").toString()));
		
		VBox pane = new VBox();
		pane.setSpacing(5);

		ButtonType btCrear = new ButtonType("Crear", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(btCrear, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(15);
		
		Label lbContent = new Label("A continuación indique las características específicas del contrato.");
		Separator separator = new Separator();

		TextField tfNombreJugador = new TextField();
		tfNombreJugador.setPrefColumnCount(30);
		
		DatePicker dpFechaInicio = new DatePicker();
		DatePicker dpFechaFin = new DatePicker();
		
		grid.add(new Label("Nombre de futbolista:"), 0, 0);
		grid.add(tfNombreJugador, 1, 0);
		grid.add(new Label("Fecha de inicio:"), 0, 1);
		grid.add(dpFechaInicio, 1, 1);
		grid.add(new Label("Fecha de finalización:"), 0, 2);
		grid.add(dpFechaFin, 1, 2);
		
		ColumnConstraints[] cols = {
				new ColumnConstraints(),
				new ColumnConstraints(),
				new ColumnConstraints()
		};
		
		cols[0].setHalignment(HPos.RIGHT);
		cols[2].setHgrow(Priority.ALWAYS);
		cols[2].setFillWidth(true);
		
		GridPane.setColumnSpan(lbContent, 2);
		GridPane.setColumnSpan(separator, 2);
		
		grid.getColumnConstraints().setAll(cols);
		
		pane.getChildren().addAll(lbContent, separator, grid);

		getDialogPane().setContent(pane);

		Platform.runLater(() -> tfNombreJugador.requestFocus());

		setResultConverter(dialogButton -> {
			if (dialogButton == btCrear) {
				Contrato contrato = new Contrato();
				contrato.setNombreFutbolista(tfNombreJugador.getText());
				contrato.setFechaInicio(dpFechaInicio.getValue());
				contrato.setFechaFin(dpFechaFin.getValue());
				return contrato;
			}
			return null;
		});
		
		Node nodeBtAnadir = getDialogPane().lookupButton(btCrear);
		nodeBtAnadir.setDisable(true);

		nodeBtAnadir.disableProperty().bind(tfNombreJugador.textProperty().isEmpty().or(dpFechaInicio.valueProperty().isNull())
				.or(dpFechaFin.valueProperty().isNull()));
	}

}
