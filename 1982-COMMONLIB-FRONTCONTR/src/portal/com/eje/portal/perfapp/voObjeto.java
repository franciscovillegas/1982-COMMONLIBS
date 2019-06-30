package portal.com.eje.portal.perfapp;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voObjeto implements IConsultaDataRow {
	
	private int intIdObjeto;
	private voGrupo grupo;
	private String strCodigo;
	private String strNombre;
	private boolean bolAccede;

	public voObjeto(int intIdObjeto, voGrupo grupo, String strCodigo, String strNombre, boolean bolAccede) {
		super();
		this.intIdObjeto = intIdObjeto;
		this.grupo= grupo;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.bolAccede = bolAccede;
	}
	
	public voObjeto(int intIdObjeto, String strCodigo, String strNombre) {
		this(intIdObjeto, null, strCodigo, strNombre, true);
	}
	
	public voObjeto(int intIdObjeto, boolean bolAccede) {
		this(intIdObjeto, null, null, null, bolAccede);
	}

	public int getId() {
		return intIdObjeto;
	}
	
	public voGrupo getgrupo() {
		return grupo;
	}
	
	public String getCodigo() {
		return strCodigo;
	}

	public String getNombre() {
		return strNombre;
	}
	
	public boolean getAccede() {
		return bolAccede;
	}
	
	public void setgrupo(voGrupo grupo) {
		this.grupo = grupo;
	}

	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_objeto", new Field(this.intIdObjeto));
		data.put("id_zona", new Field(this.grupo.getZona().getId()));
		data.put("id_grupo", new Field(this.grupo.getId()));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		data.put("accede", new Field(this.bolAccede));
		
		return data;
		
	}
	
}
