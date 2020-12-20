package aed.proyectos.ficheros_java.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

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
	private ToggleGroup tipoFile;

	@FXML
	private RadioButton rbFichero;

	@FXML
	private RadioButton rbCarpeta;

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
		rbCarpeta.selectedProperty().bindBidirectional(fichero.get().carpetaProperty());
		rbFichero.selectedProperty().bindBidirectional(fichero.get().ficheroProperty());

		// Deshabilitar si:
		// - Al menos un campo de texto está vacío
		// - Ambos checkbox están sin marcar
		btCrear.disableProperty()
				.bind(Bindings.or(tfRutaActual.textProperty().isEmpty(), tfNombre.textProperty().isEmpty()));

		btEliminar.disableProperty().bind(tfRutaActual.textProperty().isEmpty());

		btMover.disableProperty().bind(tfRutaActual.textProperty().isEmpty());
	}

	@FXML
	void onCrearAction(ActionEvent event) {
		String ruta = fichero.get().getRuta();
		String nombre = fichero.get().getNombre();
		File file = null;
		File padre = new File(ruta);
		String[] error = new String[] { "" };

		if (padre.exists() && padre.isDirectory()) {
			try {
				if (fichero.get().isCarpeta()) {
					file = new File(ruta + "\\" + nombre);

					if (!file.exists() || (file.exists() && !file.isDirectory())) {
						file.mkdir();
					} else {
						error = new String[] { "Se ha producido un error al intentar crear la carpeta.",
								"Ya existe una carpeta con el nombre '" + nombre + "'" };
					}
				} else if (fichero.get().isFichero()) {
					file = new File(ruta + "\\" + nombre);

					if (!file.exists() || (file.exists() && !file.isFile())) {
						file.createNewFile();
					} else {
						error = new String[] { "Se ha producido un error al intentar crear el fichero.",
								"Ya existe un fichero con el nombre '" + nombre + "'" };
					}
				}
			} catch (IOException e) {
				error = new String[] { "Se ha producido un error al intentar realizar la operación.",
						"Asegúrese de tener los permisos correspondientes." };
			}
		} else {
			if (!padre.exists())
				error = new String[] { "Se ha producido un error al intentar realizar la operación.",
						"No existe la ruta especificada." };
			else
				error = new String[] { "Se ha producido un error al intentar realizar la operación.",
						"No se puede crear teniendo como ruta destino un fichero.\nDebe crearse dentro de una carpeta." };
		}

		if (!error[0].equals(""))
			App.error(error[0], error[1]);
		else {
			fichero.get().setRuta(file.getAbsolutePath());
			App.info("Operación realizada con éxito", "Se ha podido crear sin problemas el "
					+ ((fichero.get().isCarpeta()) ? "directorio" : "fichero") + " de nombre '" + nombre + "'.");
		}
	}

	@FXML
	void onEliminarAction(ActionEvent event) {
		File file = new File(fichero.get().getRuta());
		String tipo = (file.isDirectory()) ? "directorio" : "fichero";
		if (App.confirm("Borrar " + tipo, "Borrar un " + tipo + " es una operación irreversible.",
				"¿Desea continuar?")) {
			borrarFile(file);
			if (file.delete()) {
				App.info("Operación realizada con éxito",
						"Se ha podido borrar sin problemas el "
								+ ((fichero.get().isCarpeta()) ? "directorio" : "fichero") + " de nombre '"
								+ file.getName() + "'.");
				fichero.get().setRuta("");
			} else
				App.error("Se ha producido un error al intentar hacer el borrado.",
						"No se ha podido borrar '" + fichero.getName() + "'.");
		}
	}

	@FXML
	void onMoverAction(ActionEvent event) {
		String[] error = new String[] { "" };
		String ruta = fichero.get().getRuta();

		if (ruta != null && !ruta.equals("")) {
			File origen = new File(ruta);

			if (origen.exists()) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				directoryChooser.setInitialDirectory((origen.isDirectory()) ? origen : origen.getParentFile());

				File destino = directoryChooser.showDialog(App.getPrimaryStage());

				// Comprobamos que el directorio original no sea un directorio padre de la ruta destino.
				// De no comprobar esto, al mover el directorio se puede producir un bucle infinito
				// al intentar crear una carpeta dentro de si misma indefinidamente.
				if (destino != null && (origen.isFile() || !esDirectorioPadre(origen, destino))) {
					try {
						moverFile(origen, destino);
						fichero.get().setRuta(destino.getAbsolutePath());
						App.info("Operación realizada con éxito", "Se ha podido mover sin problemas a la nueva ubicación.");
					} catch (IOException e) {
						error = new String[] {"Error de fichero", "No se han podido mover los ficheros. Asegúrese de tener los permisos correspondientes."};
					}
				} else {
					if (destino != null) {
						error = new String[] { "Error de fichero", "No se puede mover un directorio dentro de otro que sea hijo del original." };
					}
				}
			} else {
				error = new String[] { "Error de fichero", "La ruta especificada no existe." };
			}

		}
		
		if (error.length == 2) {
			App.error(error[0], error[1]);
		}
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
				error = new String[] { "Error de visualización",
						"No se puede generar un listado de directorios con un fichero seleccionado." };
			else
				error = new String[] { "Error de visualización", "La ruta especificada no es válida." };
		} else
			error = new String[] { "Error de visualización", "No hay ninguna carpeta seleccionada." };

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
					error = new String[] { "Error de fichero",
							"No se puede mostrar el contenido de un directorio como texto. Para eso, utilizar la opción superior." };
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
						App.info("Operación realizada con éxito.",
								"Se han aplicado los cambios al fichero '" + root.getName() + "'");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					error = new String[] { "Error de fichero", "Revise los permisos del fichero." };
			} else {
				if (root.exists())
					error = new String[] { "Error de fichero",
							"No se puede mostrar el contenido de un directorio como texto. Para eso, utilizar la opción superior." };
				else
					error = new String[] { "Error de fichero", "El fichero no existe." };
			}
		} else
			error = new String[] { "Error de fichero", "El fichero no existe." };

		if (error.length == 2) {
			App.error(error[0], error[1]);
		}
	}

	private void borrarFile(File file) {
		File[] ficheros = file.listFiles();

		for (File actual : ficheros) {
			if (actual.isDirectory())
				borrarFile(actual);
			actual.delete();
		}
	}

	private void moverFile(File origen, File destino) throws IOException {
		if (origen.isDirectory()) {
			File ficheroDestino = new File(destino.getAbsolutePath() + "\\" + origen.getName());
			ficheroDestino.mkdir();
			for (File actual : origen.listFiles())
				moverFile(actual, ficheroDestino);
			origen.delete();
		} else {			
			Files.move(
					origen.toPath(),
					destino.toPath().resolve(origen.getName()),
					StandardCopyOption.REPLACE_EXISTING
			);
		}
	}

	private boolean esDirectorioPadre(File origen, File destino) {
		String rutaOrigen = origen.getAbsolutePath();
		String rutaDestino = destino.getAbsolutePath();
		
		return rutaDestino.contains(rutaOrigen);
	}

	public VBox getView() {
		return view;
	}

	public void setView(VBox view) {
		this.view = view;
	}

}
