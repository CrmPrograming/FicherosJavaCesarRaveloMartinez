package aed.proyectos.ficheros_java;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jdom2.JDOMException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, JDOMException, IOException {
		App.main(args);
		
//		SAXBuilder builder = new SAXBuilder();
//		
//		Document documentJDOM = builder.build(new FileInputStream(Main.class.getResource("/Equipos.xml").getFile()));
//		
//		Element root = documentJDOM.getRootElement();
//		
//		List<Element> hijos = root.getChildren();
//		
//		for (Element actual : hijos) {
//			System.out.println("Nombre: " + actual.getAttributeValue("nomEquipo"));
//			System.out.println("Total de copas: " + actual.getAttributeValue("copasGanadas"));
//			System.out.println("Liga: " + actual.getChildText("codLiga"));
//			
//			Element contratos = actual.getChild("Contratos");
//			
//			for (Element futbolista : contratos.getChildren()) {
//				System.out.println("Nombre futbolista: " + futbolista.getValue());				
//				System.out.println("Fecha inicio: " + futbolista.getAttributeValue("fechaInicio"));
//				System.out.println("Fecha fin: " + futbolista.getAttributeValue("fechaFin"));
//			}
//			
//		}
	}

}
