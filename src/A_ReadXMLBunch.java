import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
		
		File fXmlFile = new File("/test/df.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		
		NodeList nList = doc.getElementsByTagName("outbound");
		
		
		System.out.println("----------------------------");
		System.out.println(nList.getLength());
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			
			Node nNode = nList.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		
		
				Element eElement = (Element) nNode;
				
				
				if (eElement.getAttribute("type").equals("class") && eElement.getAttribute("confirmed").equals("yes")){

					Element e = (Element) eElement.getParentNode();
					NodeList nl = e.getElementsByTagName("name");
					String[] nomeChamador = nl.item(0).getTextContent().split("\\.");
							
					String[] nomeClasse = eElement.getTextContent().split("\\.");
					
					//guarda "camada.classe"
					sigs.add(nomeChamador[4]+"."+nomeChamador[5]);
					sigs.add(nomeClasse[4]+"."+nomeClasse[5]);
					
					//relação de dependencia (chamada entre classes)
					relacoes.add((nomeChamador[5]+ "->"+nomeClasse[5]).replace('$', '_'));
					
					//System.out.println(eElement.getTextContent()+ " -> "+eElement.getParentNode().getTextContent());
					System.out.println(nomeChamador[5]+ " -> "+nomeClasse[5]);
				}
		
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
		String visao = "";
		String negocio = "";
		String persistencia = "";
		String pojo ="";
		String vo = "";
		
		try{		
			//Monta a string de declaração das signatures
			//declaracao = sigs.iterator().next()+"\n";	
			for (String s : sigz) {
				
				String[] stt = s.split("\\.");
				String st = stt[1].replace('$', '_');
				
				declaracao += declaracao.equals("")?st:", "+ st;
				
				if (stt[0].equals("visao")){
					visao += visao.equals("")?st:" + "+ st;
				}else if (stt[0].equals("negocio")){
					negocio += negocio.equals("")?st:" + "+ st;
				}else if (stt[0].equals("persistencia")){
					persistencia += persistencia.equals("")?st:" + "+ st;
				}else if (stt[0].equals("pojo")){
					pojo += pojo.equals("")?st:" + "+ st;
				}else if (stt[0].equals("vo")){
					vo += vo.equals("")?st:" + "+ st;
				}else {
					System.out.println("Classe ignorada: "+s);
				}
			}
			
			//Monta a string de declaração de relações
			relacionamento = rel.iterator().next()+"\n";
			for (String s : rel) {
				if (!s.equals(rel.iterator().next())){
					relacionamento += "+ "+s+"\n";	
				}
			}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            //new FileOutputStream("C:\\test\\resultadoAlloy.als"), "utf-8"))) {
			new FileOutputStream("C:\\test\\resultadoNovo.als"), "utf-8"))) {
		writer.write(
				"module " + modulo +"\n\n"
						+"abstract sig Classe { usa: set Classe}\n"
						+ "one sig "+declaracao+" extends Classe{}\n\n"
						+"sig Camada {\n"
						+"visao: set Classe,\n"
						+"negocio: set Classe,\n"
						+"persistencia: set Classe,\n"
						+"pojo: set Classe,\n"
						+"vo: set Classe\n"
						+"}\n\n"	
						+"fact visao{\n"
						+"Camada.visao = "+visao
						+"}\n\n"
						+"fact negocio{\n"
						+"Camada.negocio = "+negocio
						+"}\n\n"
						+"fact persistencia{\n"
						+"Camada.persistencia = "+persistencia
						+"}\n\n"
						+"fact pojo{\n"
						+"Camada.pojo = "+pojo
						+"}\n\n"
						+"fact vo{\n"
						+"Camada.vo = "+vo
						+"}\n\n"
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