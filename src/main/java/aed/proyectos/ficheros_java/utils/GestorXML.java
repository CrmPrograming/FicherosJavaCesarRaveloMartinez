package aed.proyectos.ficheros_java.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import aed.proyectos.ficheros_java.model.XML;
import aed.proyectos.ficheros_java.model.xml.Contrato;
import aed.proyectos.ficheros_java.model.xml.Equipo;

public abstract class GestorXML {

	public static XML leerFichero(File file) throws FileNotFoundException, JDOMException, IOException {
		XML xml = new XML();
		SAXBuilder builder = new SAXBuilder();
		
		Document documentJDOM = builder.build(file);
		
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
	
	public static boolean modificarCopas(File file, String nomEquipo, Integer nuevoValor) throws JDOMException, IOException {
		boolean result = true;
		int i = 0;
		
		SAXBuilder builder = new SAXBuilder();		
		Document documentJDOM = builder.build(file);		
		Element root = documentJDOM.getRootElement();		
		List<Element> equipos = root.getChildren();
		
		while (i < equipos.size() && !equipos.get(i).getAttributeValue("nomEquipo").equals(nomEquipo))
			i++;
		
		// 34
		if (i < equipos.size()) {
			equipos.get(i).getAttribute("copasGanadas").setValue(nuevoValor.toString());			
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			out.output(documentJDOM, fileOutputStream);
		} else
			result = false;			
		
		return result;
	}
}
