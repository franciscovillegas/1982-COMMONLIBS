package portal.com.eje.portal.perfapp;

import java.util.List;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voGrupo implements IConsultaDataRow {
	
	private int intIdGrupo;
	private voZona zona;
	private String strCodigo;
	private String strNombre;
	private List<voObjeto> objetos; 

	public voGrupo(int intIdGrupo, voZona zona, String strCodigo, String strNombre, List<voObjeto> objetos) {
		super();
		this.intIdGrupo = intIdGrupo;
		this.zona= zona;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.objetos = objetos;
	}
	
	public voGrupo(int intIdGrupo, String strCodigo, String strNombre, List<voObjeto> objetos) {
		this(intIdGrupo, null, strCodigo, strNombre, objetos);
	}
	
	public voGrupo(int intIdGrupo, voZona zona, String strCodigo, String strNombre) {
		this(intIdGrupo, zona, strCodigo, strNombre, null);
	}
	
	public voGrupo(int intIdGrupo) {
		this(intIdGrupo, null, null, null, null);
	}

	public int getId() {
		return intIdGrupo;
	}
	
	public void setId(int intIdGrupo) {
		this.intIdGrupo = intIdGrupo;
	}
	
	public voZona getZona() {
		return zona;
	}
	
	public void setZona(voZona zona) {
		this.zona = zona;
	}
	
	public String getCodigo() {
		return strCodigo;
	}

	public String getNombre() {
		return strNombre;
	}

	public List<voObjeto> getObjetos() {
		return objetos;
	}
	
	public void setObjetos(List<voObjeto> objetos) {
		this.objetos = objetos;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_grupo", new Field(this.intIdGrupo));
		data.put("id_zona", new Field(this.zona.getId()));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		
		return data;
		
	}
	
}
