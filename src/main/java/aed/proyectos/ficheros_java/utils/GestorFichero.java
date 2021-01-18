package aed.proyectos.ficheros_java.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import aed.proyectos.ficheros_java.App;
import aed.proyectos.ficheros_java.model.Fichero;
import javafx.stage.DirectoryChooser;

public abstract class GestorFichero {
	
	public static File crearFichero(Fichero fichero, String[] error) {
		String ruta = fichero.getRuta();
		String nombre = fichero.getNombre();
		File file = null;
		File padre = new File(ruta);

		if (padre.exists() && padre.isDirectory()) {
			try {
				if (fichero.isCarpeta()) {
					file = new File(ruta + "\\" + nombre);

					if (!file.exists() || (file.exists() && !file.isDirectory())) {
						file.mkdir();
					} else {
						error[0] = "Se ha producido un error al intentar crear la carpeta.";
						error[1] = "Ya existe una carpeta con el nombre '" + nombre + "'";
					}
				} else if (fichero.isFichero()) {
					file = new File(ruta + "\\" + nombre);

					if (!file.exists() || (file.exists() && !file.isFile())) {
						file.createNewFile();
					} else {
						error[0] = "Se ha producido un error al intentar crear el fichero.";
						error[1] = "Ya existe un fichero con el nombre '" + nombre + "'";
					}
				}
			} catch (IOException e) {
				error[0] = "Se ha producido un error al intentar realizar la operación.";
				error[1] = "Asegúrese de tener los permisos correspondientes.";
			}
		} else {
			if (!padre.exists()) {
				error[0] = "Se ha producido un error al intentar realizar la operación.";
				error[1] = "No existe la ruta especificada.";
			} else {
				error[0] = "Se ha producido un error al intentar realizar la operación.";
				error[1] = "No se puede crear teniendo como ruta destino un fichero.\nDebe crearse dentro de una carpeta.";
			}
		}
		
		return file;
	}
	
	public static void borrarEstructuraDirectorios(File file) {
		File[] ficheros = file.listFiles();

		for (File actual : ficheros) {
			if (actual.isDirectory())
				borrarEstructuraDirectorios(actual);
			actual.delete();
		}
	}
	
	public static void moverFichero(Fichero fichero, String[] error) {
		
		String ruta = fichero.getRuta();

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
						moverEnProfundidad(origen, destino);
						fichero.setRuta(destino.getAbsolutePath());
					} catch (IOException e) {
						error[0] = "Error de fichero";
						error[1] = "No se han podido mover los ficheros. Asegúrese de tener los permisos correspondientes.";
					}
				} else {
					if (destino != null) {
						error[0] = "Error de fichero";
						error[1] = "No se puede mover un directorio dentro de otro que sea hijo del original.";
					}
				}
			} else {
				error[0] = "Error de fichero";
				error[1] = "La ruta especificada no existe.";
			}

		}
	}
	
	private static void moverEnProfundidad(File origen, File destino) throws IOException {
		if (origen.isDirectory()) {
			File ficheroDestino = new File(destino.getAbsolutePath() + "\\" + origen.getName());
			ficheroDestino.mkdir();
			for (File actual : origen.listFiles())
				moverEnProfundidad(actual, ficheroDestino);
			origen.delete();
		} else {			
			Files.move(
					origen.toPath(),
					destino.toPath().resolve(origen.getName()),
					StandardCopyOption.REPLACE_EXISTING
			);
		}
	}

	private static boolean esDirectorioPadre(File origen, File destino) {
		String rutaOrigen = origen.getAbsolutePath();
		String rutaDestino = destino.getAbsolutePath();
		
		return rutaDestino.contains(rutaOrigen);
	}
	
	public static void listarDirectorio(Fichero fichero, String[] error) {
		String ruta = fichero.getRuta();

		if (ruta != null && !ruta.equals("")) {
			File root = new File(ruta);

			if (root.isDirectory()) {
				fichero.getListado().clear();
				for (String file : root.list()) {
					fichero.getListado().add(file);
				}

			} else if (root.isFile()) {
				error[0] = "Error de visualización";
				error[1] = "No se puede generar un listado de directorios con un fichero seleccionado.";
			} else {
				error[0] = "Error de visualización";
				error[1] = "La ruta especificada no es válida.";
			}
		} else {
			error[0] = "Error de visualización";
			error[1] = "No hay ninguna carpeta seleccionada.";
		}
	}
	
	public static void mostrarContenido(Fichero fichero, String[] error) {
		String ruta = fichero.getRuta();

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
						fichero.setContenido(contenido);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					error[0] = "Error de fichero";
					error[1] = "Revise los permisos del fichero.";
				}
			} else {
				if (root.exists()) {
					error[0] = "Error de fichero";
					error[1] = "No se puede mostrar el contenido de un directorio como texto. Para eso, utilizar la opción superior.";
				} else {
					error[0] = "Error de fichero";
					error[1] = "El fichero no existe.";
				}
			}
		} else {
			error[0] = "Error de fichero";
			error[1] = "El fichero no existe.";
		}
	}
	
	public static void modificarContenido(Fichero fichero, String[] error) {
		String ruta = fichero.getRuta();

		if (ruta != null && !ruta.equals("")) {
			File root = new File(ruta);

			if (root.exists() && root.isFile()) {
				if (root.canRead() && root.canWrite()) {
					try {
						FileWriter fileWriter = new FileWriter(root);
						fileWriter.write(fichero.getContenido());
						fileWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					error[0] = "Error de fichero";
					error[1] = "Revise los permisos del fichero.";
				}
			} else {
				if (root.exists()) {
					error[0] = "Error de fichero";
					error[1] = "No se puede mostrar el contenido de un directorio como texto. Para eso, utilizar la opción superior.";
				} else {
					error[0] = "Error de fichero";
					error[1] = "El fichero no existe.";
				}
			}
		} else {
			error[0] = "Error de fichero";
			error[1] = "El fichero no existe.";
		}
	}

}
