import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLBunch {
	private HashSet<String> sigs = new HashSet<String>();
	private HashSet<String> relacoes = new HashSet<String>();

	public String callerString = "";
	public Node caller;
	
	public static void main(String argv[]) {
		  
		  
		  System.out.println(new ReadXMLBunch().gerarAlloy());
	}
	
	public String gerarAlloy(){
		
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
						
						
						
						for (int j = 0; j < eElement.getElementsByTagName("outbound").item(i).getAttributes().getLength(); j++) {
						
							Node n1 = eElement.getElementsByTagName("outbound").item(i);
							String temp2 = n1.getAttributes().item(j).getTextContent();
							System.out.println(temp2);
							
						}
						
						

						//Separa a classe chamadora da chamada; Se contem "(" é um método
						if (eElement.getElementsByTagName("outbound").item(i).getTextContent().contains("(")){
							caller = eElement.getElementsByTagName("outbound").item(i).getParentNode();
							
						} else{
							caller = eElement.getElementsByTagName("outbound").item(i);
						}					
						
						//limpa o nome retirando os identificadores de pacote
						//String[] result = caller.contains("(")?caller.split("\\.(.*?)\\)"):caller.split(".");
						String[] result = caller.getTextContent().split("\\.");
						
						sigs.add(result[5]);	
						relacoes.add("");
					}
				}
				
				//sigs.add(eElement.getElementsByTagName("outbound").item(i).getTextContent());
			//System.out.println(pai.getElementsByTagName("name").item(0).getTextContent() + "  --->  " + eElement.getElementsByTagName("outbound").item(i).getTextContent());
				for (String s : sigs) {
					System.out.println("signature: "+s);	
				}
		
			}
		
		}
		} catch (Exception e) {
		e.printStackTrace();
		}
		return("");
	}

}