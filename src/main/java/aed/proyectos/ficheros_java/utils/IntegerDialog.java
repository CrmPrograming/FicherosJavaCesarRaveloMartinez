package aed.proyectos.ficheros_java.utils;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class IntegerDialog extends Dialog<Integer> {

	public IntegerDialog(String title, String btAceptar, String lbContent, String lbDesc) {
		this(title, btAceptar, lbContent, lbDesc, 0);
	}
	
	public IntegerDialog(String title, String btAceptar, String lbContent, String lbDesc, Integer defValue) {
		setTitle(title);

		Stage stage = (Stage) getDialogPane().getScene().getWindow();
		stage.setMinWidth(350);
		stage.setMinHeight(150);

		ButtonType btCrear = new ButtonType(btAceptar, ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(btCrear, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(15);

		TextField tfValor = new TextField(defValue.toString());
		tfValor.setPrefColumnCount(10);
		
		tfValor.textProperty().addListener((o, ov, nv) -> {
			if (nv != null && !nv.matches("\\d*")) {
				tfValor.setText(nv.replaceAll("[^\\d]", ""));
			}
		});
		
		Separator separator = new Separator();
		HBox hbox = new HBox(new Label(lbDesc), tfValor);
		
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.BASELINE_LEFT);

		grid.add(new Label(lbContent), 0, 0);
		grid.add(separator, 0, 1);
		grid.add(hbox, 0, 2);
		
		ColumnConstraints[] cols = { new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints() };
		
		cols[2].setHgrow(Priority.ALWAYS);
		cols[2].setFillWidth(true);
		
		grid.getColumnConstraints().setAll(cols);

		getDialogPane().setContent(grid);

		Platform.runLater(() -> tfValor.requestFocus());

		setResultConverter(dialogButton -> {
			if (dialogButton == btCrear) {
				return Integer.valueOf(tfValor.getText());
			}
			return null;
		});

	}

}
