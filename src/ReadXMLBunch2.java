import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.Iterator;

public class ReadXMLBunch2 {

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
	//NodeList nClass = nList.item(0).get
	//NodeList nList = doc.getElementsByTagName("staff");
	
	
	System.out.println(nList.getLength());
	System.out.println("----------------------------");

	for (int temp = 0; temp < nList.getLength(); temp++) {
		
		Node nNode = nList.item(temp);
//		
//		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//			Element eElement = (Element) nNode;
//			System.out.println(eElement.getElementsByTagName("inbound").item(0).getTextContent());
//		}
//		
		
//		NodeList list = nNode.getChildNodes();
//		for (int i = 0; i < list.getLength(); i++) {
//			System.out.println(list.item(i).getNodeName());
//		}
//		
//		System.out.println(list.getLength());

		
		
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
			
			System.out.println(nNode.getNodeName());
			System.out.println(eElement.getElementsByTagName("outbound").getLength());
		}
		
		
		
		
		
		
    }
    }catch (Exception e) {
	e.printStackTrace();
    }
  }

}