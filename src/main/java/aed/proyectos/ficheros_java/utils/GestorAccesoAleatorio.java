package aed.proyectos.ficheros_java.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import aed.proyectos.ficheros_java.model.RandomAccess;
import aed.proyectos.ficheros_java.model.acceso_aleatorio.DatosIniciales;
import aed.proyectos.ficheros_java.model.acceso_aleatorio.Equipo;

public abstract class GestorAccesoAleatorio {

	private static final String TOKEN_SEPARADOR = ",";
	private static final int BYTES_NOMBRE = 40;
	private static final int BYTES_COD_LIGA = 5;
	private static final int BYTES_LOCALIDAD = 60;
	private final static int BYTES_REGISTRO_COMPLETO = 231;

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

	private static String convertirStringARegistro(String dato, final int MAX_SIZE) {
		String result = "" + dato;

		while (result.length() < MAX_SIZE)
			result += " ";

		return result;
	}

	private static String convertirRegistroAString(String dato) {
		String result = "" + dato;

		while (result.charAt(result.length() - 1) == ' ')
			result = result.substring(0, result.length() - 1);

		return result;
	}
	
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
	
}