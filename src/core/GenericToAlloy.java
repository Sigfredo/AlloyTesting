package core;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import config.Constants;
import enums.TiposRelacoesEnum;

public class GenericToAlloy {
	
	private HashSet<String> sigs = new HashSet<String>();
	private Map<String, String> relacoes = new HashMap<String, String>();
	private HashSet<String> tiposModulos = new HashSet<String>();
	
	String relacionamento = "";
	String declaracao = "";
	
	private String sql = "";
	private String visao = "";
	private String basevisao = "";
	private String negocio = "";
	private String basenegocio = "";
	private String icrudnegocio = "";
	private String persistencia = "";
	private String daofactory = "";
	private String pojo = "";
	private String vo = "";
	
	private String access = "fact {Access.r = ";
	private String declare = "fact {Declare.r = ";
	private String create = "fact {Create.r = ";
	private String extend = "fact {Extend.r = ";
	private String implement = "fact {Implement.r = ";
	private String throw_var = "fact {Throw.r = ";
	private String useannotation = "fact {Useannotation.r = ";
	
	
	public void traduzir(){
		
		BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(Constants.FILE_NAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				obterDependencia(sCurrentLine.replaceAll("\\.", "_"));
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				imprimir();
				
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		

		
		
	}
	
	
	public void obterDependencia(String line){
		String linha = adaptarLinha(line);
		if (!linha.equals("")){
			String[] tokens = linha.split(",");
			getTipo(tokens[0]);
			getTipo(tokens[2]);
			adicionarRelacao(tokens[1], tokens[0] +" -> "+tokens[2]);
		}	
	}
	
	public String adaptarLinha(String line){
		
	String retorno = line;
		//ignorar as declarações não encontradas (contendo ?)
		if (line.contains("?")){
			return "";
		}
		// O Alloy não aceita '<' na declaração, os genéricos não são importantes.
		if(line.contains("<")){
			return line.split("<")[0];
		}
		// Alloy tem "int" como palavra reservada
		if (line.contains("int")){
			return line.replaceAll("int", "int_var");
		}
		return retorno;
	}
		
	public String obterModulos(){
		return "visao, basevisao, negocio, basenegocio, icrudnegocio, daofactory, persistencia, pojo, vo, sql";
	}
	
	public void getTipo(String modulo){	
		if (modulo.contains("BaseVisao")){
			if (basevisao.equals("")){
				basevisao = "fact {basevisao.classes = "+modulo;
			}else if(!basevisao.contains(modulo)){
				basevisao += " + "+modulo;
			}else{}
		}else if (modulo.contains("Visao")){
			if (visao.equals("")){
				visao += "fact {visao.classes = "+modulo;
			}else if(!visao.contains(modulo)){
				visao += " + "+modulo;
			}
		}else if (modulo.contains("NegocioImpl")){
			if (negocio.equals("")){
				negocio = "fact {negocio.classes = "+modulo;
			}else if(!negocio.contains(modulo)){
				negocio += " + "+modulo;
			}
		}else if (modulo.contains("ICrudNegocio")){
			if (icrudnegocio.equals("")){
				icrudnegocio = "fact {icrudnegocio.classes = "+modulo;
			}else if(!icrudnegocio.contains(modulo)){
				icrudnegocio += " + "+modulo;
			}
		}else if (modulo.contains("Negocio")){
			if (basenegocio.equals("")){
				basenegocio = "fact {basenegocio.classes = "+modulo;
			}else if(!basenegocio.contains(modulo)){
				basenegocio += " + "+modulo;
			}
		}else if (modulo.contains("DAOFactory")){
			if (daofactory.equals("")){
				daofactory = "fact {daofactory.classes = "+modulo;
			}else if(!daofactory.contains(modulo)){
				daofactory += " + "+modulo;
			}
		}else if (modulo.contains("DAO")){
			if (persistencia.equals("")){
				persistencia = "fact {persistencia.classes = "+modulo;
			}else if(!persistencia.contains(modulo)){
				persistencia += " + "+modulo;
			}
		}else if (modulo.contains("web_siex_pojo")){
			if (pojo.equals("")){
				pojo = "fact {pojo.classes = "+modulo;
			}else if(!pojo.contains(modulo)){
				pojo += " + "+modulo;
			}
		}else if (modulo.contains("web_siex_vo")){
			if (vo.equals("")){
				vo = "fact {vo.classes = "+modulo;
			}else if(!vo.contains(modulo)){
				vo += " + "+modulo;
			}
		}else if (modulo.contains("java_sql")){
			if (sql.equals("")){
				sql = "fact {sql.classes = "+modulo;
			}else if(!sql.contains(modulo)){
				sql += " + "+modulo;
			}
		}
		if(declaracao.contains(modulo+",") || declaracao.contains(modulo+" ")){
			
		}else{
			declaracao += modulo+", ";
		}
	}
	
	//Escolhi colocar o " + " no final de cada adição porque suponho que um "if variavel.equals("") para capturar a primeira linha ia acabar 
	//onerando cara iteraçao. Um substring no final para retirar o ultimo "+" me parece mais verboso porém mais barato.
	public void adicionarRelacao(String tipo, String relacao){
		switch (TiposRelacoesEnum.getCodigo(tipo)){	
		case Constants.ACCESS : 
			access += relacao+" + ";
			break;
		case Constants.DECLARE : 
			declare += relacao+" + ";
			break;
		case Constants.CREATE : 
			create += relacao+" + ";
			break;
		case Constants.EXTEND : 
			extend += relacao+" + ";
			break;
		case Constants.IMPLEMENT : 
			implement += relacao+" + ";
			break;
		case Constants.THROW : 
			throw_var += relacao+" + ";
			break;
		case Constants.USEANNOTATION : 
			useannotation += relacao+" + ";
			break;
		default:
			System.out.println("Tipo não encontrado: "+tipo);
			break;
		}
		
	}
	
	public void imprimir(){
		
		fecharRelacoes();
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	            //new FileOutputStream("C:\\test\\resultadoAlloy.als"), "utf-8"))) {
				new FileOutputStream("C:\\test\\temp\\resultado.als"), "utf-8"))) {
			writer.write("sig Objeto{}\n"
				+ "sig "+obterModulos()+"  {classes: set Objeto}\n"
				+ "sig "+declaracao+" extends Objeto{}\n"
				+ "sig Access, Declare, Create, Extend, Implement, Throw, Useannotation{r: set Objeto -> Objeto}\n\n"
				+ sql+"\n"
				+ visao+"\n"
				+ basevisao+"\n"
				+ negocio+"\n"
				+ basenegocio+"\n"
				+ icrudnegocio+"\n"
				+ daofactory+"\n"
				+ persistencia+"\n"
				+ pojo+"\n"
				+ vo+"\n\n"
				+ access+"\n\n"
				+ declare+"\n\n"
				+ create+"\n\n"
				+ extend+"\n\n"
				+ implement+"\n\n"
				+ throw_var+"\n\n\n"
				+ useannotation+"\n\n"
				+ "assert no_visao_persistencia{\n"
				+ "no x: visao.classes, y: persistencia.classes | x->y in Access.r\n"
				+"}\n"
				+"check no_visao_persistencia for 1"
					);
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	//Fecha os fatos
	public void	fecharRelacoes(){
		declaracao = declaracao.substring(0, declaracao.length()-2);
		visao += "}";
		basevisao += "}";
		negocio += "}";
		basenegocio += "}";
		icrudnegocio += "}";
		persistencia += "}";
		daofactory += "}";
		pojo += "}";
		vo += "}";
		sql += "}";
		access = access.substring(0, access.length()-2)+ "}";
		declare = declare.substring(0, declare.length()-2)+ "}";
		create = create.substring(0, create.length()-2)+ "}";
		extend = extend.substring(0, extend.length()-2)+ "}";
		implement = implement.substring(0, implement.length()-2)+ "}";
		throw_var = throw_var.substring(0, throw_var.length()-2)+ "}";
		useannotation = useannotation.substring(0, useannotation.length()-2)+ "}";
		//teste
		
	}		

}
