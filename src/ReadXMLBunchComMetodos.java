import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadXMLBunchComMetodos {

  public static void main(String argv[]) {

    try {

	File fXmlFile = new File("/test/siex_test.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList nList = doc.getElementsByTagName("class");

	System.out.println("----------------------------");

	for (int temp = 0; temp < nList.getLength(); temp++) {
		
		Node nNode = nList.item(temp);
		
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {


			Element eElement = (Element) nNode;
			
			for (int i = 0; i < eElement.getElementsByTagName("outbound").getLength(); i++) {
				if (eElement.getElementsByTagName("outbound").item(i).getTextContent().contains("br.unb.")){
					Element pai = (Element) eElement.getElementsByTagName("outbound").item(i).getParentNode();
				System.out.println(pai.getElementsByTagName("name").item(0).getTextContent() + "  --->  " + eElement.getElementsByTagName("outbound").item(i).getTextContent());
				}
			}

		}

	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }

}