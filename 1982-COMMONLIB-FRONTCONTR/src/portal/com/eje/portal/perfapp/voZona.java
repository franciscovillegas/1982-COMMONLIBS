package portal.com.eje.portal.perfapp;

import java.util.List;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voZona implements IConsultaDataRow {
	
	private int intIdZona;
	private String strCodigo;
	private String strNombre;
	private List<voGrupo> grupos;
	private List<voAplicacion> aplicaciones;

	public voZona(int intIdZona, String strCodigo, String strNombre, List<voGrupo> grupos, List<voAplicacion> aplicaciones) {
		super();
		this.intIdZona = intIdZona;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.grupos = grupos;
		this.aplicaciones = aplicaciones;
	}

	public voZona(int intIdZona, String strCodigo, String strNombre) {
		this(intIdZona, strCodigo, strNombre, null, null);
	}
	
	public voZona(int intIdZona) {
		this(intIdZona, null, null, null, null);
	}

	public int getId() {
		return intIdZona;
	}
	
	public String getCodigo() {
		return strCodigo;
	}

	public String getNombre() {
		return strNombre;
	}

	public void setId(int intIdZona) {
		this.intIdZona = intIdZona;
	}
	
	public List<voGrupo> getGrupos() {
		return grupos;
	}
	
	public void setGrupos(List<voGrupo> grupos) {
		this.grupos = grupos;
	}
	
	public List<voAplicacion> getAplicaciones() {
		return aplicaciones;
	}
	
	public void setAplicaciones(List<voAplicacion> aplicaciones) {
		this.aplicaciones = aplicaciones;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_zona", new Field(this.intIdZona));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		
		return data;
		
	}
	
}
