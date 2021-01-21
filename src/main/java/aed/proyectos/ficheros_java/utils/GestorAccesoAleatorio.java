package aed.proyectos.ficheros_java.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import aed.proyectos.ficheros_java.model.RandomAccess;
import aed.proyectos.ficheros_java.model.acceso_aleatorio.DatosIniciales;
import aed.proyectos.ficheros_java.model.acceso_aleatorio.Equipo;

/**
 * Clase gestora de las operaciones asociadas
 * a los ficheros de acceso aleatorio.
 * 
 * @author César Ravelo Martínez
 *
 */
public abstract class GestorAccesoAleatorio {

	private static final String TOKEN_SEPARADOR = ",";
	private static final int BYTES_NOMBRE = 40;
	private static final int BYTES_COD_LIGA = 5;
	private static final int BYTES_LOCALIDAD = 60;
	private static final int BYTES_REGISTRO_COMPLETO = 231;
	private static final int BYTES_POS_COPAS = 222;

	/**
	 * Método encargado de inicializar un fichero
	 * con valores por defecto.
	 * 
	 * @param file Instancia de fichero a crear
	 * @return Instancia de RandomAccess con los datos leídos
	 * @throws IOException
	 */
	public static RandomAccess inicializar(File file) throws IOException {
		Object[] datos;
		RandomAccess result = new RandomAccess();
		Equipo actual;
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		raf.seek(0);

		for (int i = 0; i < DatosIniciales.DATOS.length; i++) {
			datos = DatosIniciales.DATOS[i];
			actual = new Equipo();

			// Identificador
			actual.setCodEquipo((Integer) datos[0]);
			raf.writeInt(actual.getCodEquipo());
			raf.writeChars(TOKEN_SEPARADOR);
			// Nombre Equipo
			actual.setNombreEquipo(convertirStringARegistro((String) datos[1], BYTES_NOMBRE));
			raf.writeChars(actual.getNombreEquipo());
			raf.writeChars(TOKEN_SEPARADOR);
			// CodLiga
			actual.setCodLiga(convertirStringARegistro((String) datos[2], BYTES_COD_LIGA));
			raf.writeChars(actual.getCodLiga());
			raf.writeChars(TOKEN_SEPARADOR);
			// Localidad
			actual.setLocalidad(convertirStringARegistro((String) datos[3], BYTES_LOCALIDAD));
			raf.writeChars(actual.getLocalidad());
			raf.writeChars(TOKEN_SEPARADOR);
			// Copas
			actual.setCopasGanadas(((Integer) datos[4]));
			raf.writeInt(actual.getCopasGanadas());
			raf.writeChars(TOKEN_SEPARADOR);
			// Internacional
			actual.setInternacional((Boolean) datos[5]);
			raf.writeBoolean(actual.isInternacional());
			raf.writeChars(TOKEN_SEPARADOR);

			result.getEquipos().add(actual);
		}

		raf.close();
		result.setFichero(file);
		return result;
	}

	/**
	 * Método encargado de convertir un String a un registro,
	 * rellenando de espacios hasta alcanzar el tamaño
	 * máximo de registro definido por MAX_SIZE.
	 * 
	 * @param dato String con el dato a convertir
	 * @param MAX_SIZE Tamaño máximo del registro
	 * @return String con el dato ya convertido
	 */
	private static String convertirStringARegistro(String dato, final int MAX_SIZE) {
		String result = "" + dato;

		while (result.length() < MAX_SIZE)
			result += " ";

		return result;
	}

	/**
	 * Método encargado de convertir un registro a String,
	 * quitando los espacios en blanco que se insertaron
	 * artificialmente para rellenar el tamaño completo
	 * del registro.
	 * 
	 * @param dato String con el registro a convertir 
	 * @return String con el dato en formato registro
	 */
	private static String convertirRegistroAString(String dato) {
		String result = "" + dato;

		while (result.charAt(result.length() - 1) == ' ')
			result = result.substring(0, result.length() - 1);

		return result;
	}
	
	/**
	 * Método encargado de leer un fichero y extraer los
	 * datos que en él se encuentren.
	 * 
	 * @param file Instancia de File con los datos a leer
	 * @return Instancia de RandomAccess con los datos extraídos
	 * @throws IOException
	 */
	public static RandomAccess leerFichero(File file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		RandomAccess result = new RandomAccess();
		char charActual;
		String stringActual = "";
		Equipo equipoActual;
		raf.seek(0);
		
		while (raf.getFilePointer() < raf.length()) {
			equipoActual = new Equipo();
			
			// Identificador
			equipoActual.setCodEquipo(raf.readInt());
			raf.readChar();
			
			// Nombre equipo
			while ((charActual = raf.readChar()) != TOKEN_SEPARADOR.charAt(0))
				stringActual += charActual;			
			equipoActual.setNombreEquipo(convertirRegistroAString(stringActual));
			stringActual = "";
			
			// Código de liga			
			while ((charActual = raf.readChar()) != TOKEN_SEPARADOR.charAt(0))
				stringActual += charActual;			
			equipoActual.setCodLiga(convertirRegistroAString(stringActual));
			stringActual = "";
			
			// Localidad			
			while ((charActual = raf.readChar()) != TOKEN_SEPARADOR.charAt(0))
				stringActual += charActual;			
			equipoActual.setLocalidad(convertirRegistroAString(stringActual));
			stringActual = "";
			
			// Copas ganadas
			equipoActual.setCopasGanadas(raf.readInt());
			raf.readChar();
			
			// Internacional
			equipoActual.setInternacional(raf.readBoolean());
			raf.readChar();
			
			result.getEquipos().add(equipoActual);
		}
		
		raf.close();
		result.setFichero(file);
		return result;
	}

	/**
	 * Método encargado de guardar en un fichero los datos
	 * de todos los equipos especificados por parámetro en un fichero dado.
	 * 
	 * @param equipos Lista con los datos de los equipos
	 * @param file Instancia de File con el fichero a volcar los datos
	 * @throws IOException
	 */
	public static void volcarDatos(List<Equipo> equipos, File file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		for (Equipo actual : equipos) {
			// Identificador
			raf.writeInt(actual.getCodEquipo());
			raf.writeChars(TOKEN_SEPARADOR);
			// Nombre Equipo
			raf.writeChars(actual.getNombreEquipo());
			raf.writeChars(TOKEN_SEPARADOR);
			// CodLiga
			raf.writeChars(actual.getCodLiga());
			raf.writeChars(TOKEN_SEPARADOR);
			// Localidad
			raf.writeChars(actual.getLocalidad());
			raf.writeChars(TOKEN_SEPARADOR);
			// Copas
			raf.writeInt(actual.getCopasGanadas());
			raf.writeChars(TOKEN_SEPARADOR);
			// Internacional
			raf.writeBoolean(actual.isInternacional());
			raf.writeChars(TOKEN_SEPARADOR);
		}

		raf.close();
	}

	/**
	 * Método encargado de extraer la información de un
	 * equipo específico según el id indicado por parámetro.
	 * 
	 * @param file Instancia de File con el fichero a examinar
	 * @param codEquipo Identificador del equipo
	 * @return Instancia de Equipo con los datos del equipo rescatado
	 * @throws IOException
	 */
	public static Equipo consultarEquipo(File file, int codEquipo) throws IOException {
		Equipo equipo = new Equipo();
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		char charActual;
		String stringActual = "";
		raf.seek((codEquipo - 1) * BYTES_REGISTRO_COMPLETO);
		
		// Identificador
		equipo.setCodEquipo(raf.readInt());
		raf.readChar();
					
		// Nombre equipo
		while ((charActual = raf.readChar()) != TOKEN_SEPARADOR.charAt(0))
			stringActual += charActual;			
		equipo.setNombreEquipo(convertirRegistroAString(stringActual));
		stringActual = "";
					
		// Código de liga			
		while ((charActual = raf.readChar()) != TOKEN_SEPARADOR.charAt(0))
			stringActual += charActual;			
		equipo.setCodLiga(convertirRegistroAString(stringActual));
		stringActual = "";
					
		// Localidad			
		while ((charActual = raf.readChar()) != TOKEN_SEPARADOR.charAt(0))
			stringActual += charActual;			
		equipo.setLocalidad(convertirRegistroAString(stringActual));
		stringActual = "";
					
		// Copas ganadas
		equipo.setCopasGanadas(raf.readInt());
		raf.readChar();
					
		// Internacional
		equipo.setInternacional(raf.readBoolean());
		raf.readChar();
		
		raf.close();
		
		return equipo;
	}
	
	/**
	 * Método encargado de cambiar las copas de un
	 * equipo especificado por parámetro.
	 * 
	 * @param file Instancia de File con el fichero a modificar
	 * @param codEquipo Identificador del equipo a editar
	 * @param nuevoValor Nuevo valor de copas a asignarle
	 * @throws IOException
	 */
	public static void modificarCopas(File file, int codEquipo, Integer nuevoValor) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		raf.seek(((codEquipo - 1) * BYTES_REGISTRO_COMPLETO) + BYTES_POS_COPAS);
		raf.writeInt(nuevoValor);
		raf.close();
	}
	
}