import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class A_ReadXMLBunch {
	private HashSet<String> sigs = new HashSet<String>();
	private HashSet<String> relacoes = new HashSet<String>();

	public String callerString = "";
	public Node caller;
	private String declaracao = "";
	private String relacionamento = "";
	
	public static void main(String argv[]) {
		  
		  
		  new A_ReadXMLBunch().gerarAlloy();
	}
	
	public void gerarAlloy(){
		
		try {
		
		File fXmlFile = new File("/test/siex_test.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		
		NodeList nList = doc.getElementsByTagName("outbound");
		
		
		System.out.println("----------------------------");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			
			Node nNode = nList.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		
		
				Element eElement = (Element) nNode;
				
				
				if (eElement.getAttribute("type").equals("class") && eElement.getAttribute("confirmed").equals("yes")){
					
					String[] nomeClasse = eElement.getTextContent().split("\\.");

					Element e = (Element) eElement.getParentNode();
					NodeList nl = e.getElementsByTagName("name");
					String[] nomeChamador = nl.item(0).getTextContent().split("\\.");
							
					
							
					sigs.add(nomeClasse[5]);
					sigs.add(nomeChamador[5]);
					relacoes.add(nomeChamador[5]+ "->"+nomeClasse[5]);
					
					//System.out.println(eElement.getTextContent()+ " -> "+eElement.getParentNode().getTextContent());
					System.out.println(nomeChamador[5]+ " -> "+nomeClasse[5]);
				}

				

				
				
//				for (int i = 0; i < eElement.getElementsByTagName("outbound").getLength(); i++) {
//					if (eElement.getElementsByTagName("outbound").item(i).getTextContent().contains("br.unb.")){
//						
//						
//						
//						for (int j = 0; j < eElement.getElementsByTagName("outbound").item(i).getAttributes().getLength(); j++) {
//						
//							Node n1 = eElement.getElementsByTagName("outbound").item(i);
//							String temp2 = n1.getAttributes().item(j).getTextContent();
//							System.out.println(temp2);
//							
//						}
//						
//						
//
//						//Separa a classe chamadora da chamada; Se contem "(" é um método
//						if (eElement.getElementsByTagName("outbound").item(i).getTextContent().contains("(")){
//							caller = eElement.getElementsByTagName("outbound").item(i).getParentNode();
//							
//						} else{
//							caller = eElement.getElementsByTagName("outbound").item(i);
//						}					
//						
//						//limpa o nome retirando os identificadores de pacote
//						//String[] result = caller.contains("(")?caller.split("\\.(.*?)\\)"):caller.split(".");
//						String[] result = caller.getTextContent().split("\\.");
//						
//						sigs.add(result[5]);	
//						relacoes.add("");
//					}
//				}
				
				//sigs.add(eElement.getElementsByTagName("outbound").item(i).getTextContent());
			//System.out.println(pai.getElementsByTagName("name").item(0).getTextContent() + "  --->  " + eElement.getElementsByTagName("outbound").item(i).getTextContent());
//				for (String s : sigs) {
//					System.out.println("signature: "+s);	
//				}
		
			}
		
		}
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		criarArquivo(sigs, relacoes);
		
	}
	
	public void criarArquivo(HashSet<String> sigz, HashSet<String> rel){
		
		//tenho que buscar o módulo depois
		String modulo = "siex";
		
		//Monta a string de declaração das signatures
		declaracao = sigs.iterator().next()+"\n";	
		for (String s : sigz) {
			if (!s.equals(sigz.iterator().next())){
				declaracao += ", "+ s;
			}
		}
		
		//Monta a string de declaração de relações
		relacionamento = rel.iterator().next()+"\n";
		for (String s : rel) {
			if (!s.equals(rel.iterator().next())){
				relacionamento += "+ "+s+"\n";	
			}
		}
			
		
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            //new FileOutputStream("C:\\test\\resultadoAlloy.als"), "utf-8"))) {
			new FileOutputStream("C:\\test\\resultadoNovo.als"), "utf-8"))) {
		writer.write(
				"module " + modulo +"\n\n"
						+"abstract sig Classe { usa: set Classe}\n"
						+ "one sig "+declaracao+" extends Classe{}\n\n"
						+"sig Camada {\n"
						+"model: set Classe,\n"
						+"view: set Classe,\n"
						+"control: set Classe\n"
						+"}\n\n"	
						+"fact view{\n"
						
						+"}\n"
						+ "fact chamada {usa = \n" + relacionamento + "}\n\n"
						//+ mensagem.getViolacao()+"\n\n"
						+ "pred show {}\n\n"
						+ "run show\n\n\n"							
					);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}