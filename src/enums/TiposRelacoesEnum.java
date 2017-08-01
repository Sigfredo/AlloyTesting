package enums;

import config.Constants;

public enum TiposRelacoesEnum {
	ACCESS("access",Constants.ACCESS),
	DECLARE("declare",Constants.DECLARE),
	CREATE("create",Constants.CREATE),
	EXTEND("extend",Constants.EXTEND),
	IMPLEMENT("implement",Constants.IMPLEMENT),
	THROW("throw",Constants.THROW),
	USEANNOTATION("useannotation",Constants.USEANNOTATION),
	OUTROS("outros",Constants.OUTROS);
	
	private final String key;
	private final Integer codigo;
	
	private TiposRelacoesEnum(String key, Integer codigo){
		this.key = key;
		this.codigo = codigo;
	}

	public String getKey() {
		return key;
	}

	public Integer getCodigo() {
		return codigo;
	}
	
	public static Integer getCodigo(String tipo){

		if (tipo.equals("access"))
			return ACCESS.codigo;
		if (tipo.equals("declare"))
			return DECLARE.codigo;
		if (tipo.equals("create"))
			return CREATE.codigo;
		if (tipo.equals("extend"))
			return EXTEND.codigo;
		if (tipo.equals("implement"))
			return IMPLEMENT.codigo;
		if (tipo.equals("throw"))
			return THROW.codigo;
		if (tipo.equals("useannotation"))
			return USEANNOTATION.codigo;
		return OUTROS.codigo;
		
	}

}