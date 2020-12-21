package aed.proyectos.ficheros_java.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import aed.proyectos.ficheros_java.Main;
import aed.proyectos.ficheros_java.model.XML;
import aed.proyectos.ficheros_java.model.xml.Contrato;
import aed.proyectos.ficheros_java.model.xml.Equipo;

public abstract class GestorXML {

	public static XML leerFichero() throws FileNotFoundException, JDOMException, IOException {
		XML xml = new XML();
		SAXBuilder builder = new SAXBuilder();
		
		Document documentJDOM = builder.build(new FileInputStream(Main.class.getResource("/Equipos.xml").getFile()));
		
		Element root = documentJDOM.getRootElement();
		
		List<Element> hijos = root.getChildren();
		
		List<Equipo> equipos = new ArrayList<Equipo>();
		
		for (Element actual : hijos) {
			Equipo equipoActual = new Equipo();
			equipoActual.setNombreEquipo(actual.getAttributeValue("nomEquipo"));
			equipoActual.setTotalCopas(Integer.valueOf(actual.getAttributeValue("copasGanadas")));
			equipoActual.setCodLiga(actual.getChildText("codLiga"));
			
			Element contratos = actual.getChild("Contratos");
			
			List<Contrato> listadoContratos = new ArrayList<Contrato>();
			
			for (Element futbolista : contratos.getChildren()) {
				Contrato contratoActual = new Contrato();
				
				contratoActual.setNombreFutbolista(futbolista.getValue());				
				contratoActual.setFechaInicio(LocalDate.parse(futbolista.getAttributeValue("fechaInicio")));
				contratoActual.setFechaFin(LocalDate.parse(futbolista.getAttributeValue("fechaFin")));				
				
				listadoContratos.add(contratoActual);
			}
			
			equipoActual.getContratos().addAll(listadoContratos);
			equipos.add(equipoActual);
		}
		
		xml.getEquipos().addAll(equipos);
		return (xml);
	}
}
