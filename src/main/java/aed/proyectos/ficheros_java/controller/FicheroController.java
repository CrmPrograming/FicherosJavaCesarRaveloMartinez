package aed.proyectos.ficheros_java.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import aed.proyectos.ficheros_java.App;
import aed.proyectos.ficheros_java.model.Fichero;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
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
	private ObjectProperty<Fichero> fichero = new SimpleObjectProperty<>(new Fichero());
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
	private ListView<String> lvFicherosCarpetas;

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
		tfRutaActual.textProperty().bindBidirectional(fichero.get().rutaProperty());
		
		crearBindeosOperacionesBasicas();

		lvFicherosCarpetas.itemsProperty().bind(fichero.get().listadoProperty());

		taContenidoFichero.textProperty().bindBidirectional(fichero.get().contenidoProperty());
	}

	private void crearBindeosOperacionesBasicas() {
		tfNombre.textProperty().bindBidirectional(fichero.get().nombreProperty());
		cbCarpeta.selectedProperty().bindBidirectional(fichero.get().carpetaProperty());
		cbFichero.selectedProperty().bindBidirectional(fichero.get().ficheroProperty());

		// Deshabilitar si:
		// - Al menos un campo de texto está vacío
		// - Ambos checkbox están sin marcar
		btCrear.disableProperty()
				.bind(Bindings.or(Bindings.and(cbCarpeta.selectedProperty().not(), cbFichero.selectedProperty().not()),
						Bindings.or(tfRutaActual.textProperty().isEmpty(), tfNombre.textProperty().isEmpty())));
		
		btEliminar.disableProperty().bind(tfRutaActual.textProperty().isEmpty());
		
		btMover.disableProperty().bind(tfRutaActual.textProperty().isEmpty());
	}

	@FXML
	void onCrearAction(ActionEvent event) {
		String ruta = fichero.get().getRuta();
		String nombre = fichero.get().getNombre();
		File file = null;
		String[] error = new String[] {""};
		
		try {
			if (fichero.get().isCarpeta()) {
				file = new File(ruta + "\\" + nombre);
				
				if (!file.exists() || (file.exists() && !file.isDirectory())) {
					file.mkdir();
				} else {
					error = new String[] {"Se ha producido un error al intentar crear la carpeta.", "Ya existe una carpeta con el nombre '" + nombre + "'"};
				}
			} else if (fichero.get().isFichero()) {
				file = new File(ruta + "\\" + nombre);
				
				if (!file.exists() || (file.exists() && !file.isFile())) {
					file.createNewFile();
				} else {
					error = new String[] {"Se ha producido un error al intentar crear la carpeta.", "Ya existe un fichero con el nombre '" + nombre + "'"};
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if (!error[0].equals(""))
			App.error(error[0], error[1]);
		else {
			fichero.get().setRuta(file.getAbsolutePath());
			App.info("Operación realizada con éxito", "Se ha podido crear sin problemas el " + ((fichero.get().isCarpeta())?"directorio":"fichero") + " de nombre '" + nombre + "'.");
		}
	}

	@FXML
	void onEliminarAction(ActionEvent event) {

	}

	@FXML
	void onMoverAction(ActionEvent event) {

	}

	@FXML
	void onVerFicherosCarpetasAction(ActionEvent event) {
		String[] error = new String[] { "" };

		String ruta = fichero.get().getRuta();

		if (ruta != null && !ruta.equals("")) {
			File root = new File(ruta);

			if (root.isDirectory()) {
				fichero.get().getListado().clear();
				for (String file : root.list()) {
					fichero.get().getListado().add(file);
				}

			} else if (root.isFile())
				error = new String[] { "Error de fichero", "No se puede generar un listado de directorios con un fichero seleccionado." };
			else
				error = new String[] { "Error de fichero", "La ruta especificada no es válida." };
		} else
			error = new String[] { "Error de fichero", "No hay ninguna carpeta seleccionada." };

		if (error.length == 2) {
			App.error(error[0], error[1]);
		}
	}

	@FXML
	void onVerContenidoAction(ActionEvent event) {
		String[] error = new String[] { "" };

		String ruta = fichero.get().getRuta();

		if (ruta != null && !ruta.equals("")) {
			File root = new File(ruta);

			if (root.exists() && root.isFile()) {
				if (root.canRead() && root.canWrite()) {
					try {
						Scanner scanner = new Scanner(root);
						String contenido = "";
						while (scanner.hasNextLine()) {
							contenido += scanner.nextLine();
							if (scanner.hasNextLine())
								contenido += "\n";
						}
						scanner.close();
						fichero.get().setContenido(contenido);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					error = new String[] { "Error de fichero", "Revise los permisos del fichero." };
			} else {
				if (root.exists())
					error = new String[] { "Error de fichero", "No se puede mostrar el contenido de un directorio como texto. Para eso, utilizar la opción superior." };
				else
					error = new String[] { "Error de fichero", "El fichero no existe." };
			}
		} else
			error = new String[] { "Error de fichero", "El fichero no existe." };

		if (error.length == 2) {
			App.error(error[0], error[1]);
		}
	}

	@FXML
	void onModificarContenidoAction(ActionEvent event) {
		String[] error = new String[] { "" };

		String ruta = fichero.get().getRuta();

		if (ruta != null && !ruta.equals("")) {
			File root = new File(ruta);

			if (root.exists() && root.isFile()) {
				if (root.canRead() && root.canWrite()) {
					try {
						FileWriter fileWriter = new FileWriter(root);
						fileWriter.write(fichero.get().getContenido());
						fileWriter.close();
						App.info("Operación realizada con éxito.", "Se han aplicado los cambios al fichero '" + root.getName() + "'");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					error = new String[] { "Error de fichero", "Revise los permisos del fichero." };
			} else {
				if (root.exists())
					error = new String[] { "Error de fichero", "No se puede mostrar el contenido de un directorio como texto. Para eso, utilizar la opción superior." };
				else
					error = new String[] { "Error de fichero", "El fichero no existe." };
			}
		} else
			error = new String[] { "Error de fichero", "El fichero no existe." };

		if (error.length == 2) {
			App.error(error[0], error[1]);
		}
	}

	public VBox getView() {
		return view;
	}

	public void setView(VBox view) {
		this.view = view;
	}

}
