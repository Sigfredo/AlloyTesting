import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLBunch3 {

  public static void main(String argv[]) {

    try {

	File fXmlFile = new File("/test/siex_test.xml");
    	//File fXmlFile = new File("/Users/sig/staff.xml");
DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList nList = doc.getElementsByTagName("class");
	
//	for (int i = 0; i < nList.getLength(); i++) {
//		nList.item(i).get
//		NodeList nClass = nList.item(i).getChildNodes();
//		for (int j = 0; j < nClass.getLength(); j++) {
//			System.out.println((Element) nClass.item(i).);
//		}
//	}

	//NodeList nList = doc.getElementsByTagName("staff");
	
	
//	System.out.println(nList.getLength());
//	System.out.println("----------------------------");
//
	for (int temp = 0; temp < nList.getLength(); temp++) {
		
		Node nNode = nList.item(temp);
		
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {


			Element eElement = (Element) nNode;
			
			for (int i = 0; i < eElement.getElementsByTagName("outbound").getLength(); i++) {
				System.out.println(eElement.getElementsByTagName("outbound").item(i).getTextContent() + "   PAI:: " + eElement.getElementsByTagName("outbound").item(i).getParentNode());
			}

		}
		
		
//		NodeList list = nNode.getChildNodes();
//		for (int i = 0; i < list.getLength(); i++) {
//			System.out.println(list.item(i).getNodeName());
//		}
//		
//		System.out.println(list.getLength());

		
//		
//		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//			Element eElement = (Element) nNode;
//			
//			System.out.println(nNode.getNodeName());
//			System.out.println(eElement.getElementsByTagName("outbound").getLength());
//		}
//		
		
		
		
		
		
	}
    }catch (Exception e) {
	e.printStackTrace();
    }
}}