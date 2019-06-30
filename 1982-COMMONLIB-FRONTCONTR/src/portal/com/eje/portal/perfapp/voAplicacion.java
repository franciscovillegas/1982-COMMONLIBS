package portal.com.eje.portal.perfapp;

import java.util.List;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voAplicacion implements IConsultaDataRow {
	
	private int intIdAplicacion;
	private voZona zona;
	private String strCodigo;
	private String strNombre;
	private List<voModulo> modulos;

	public voAplicacion(int intIdAplicacion, voZona zona, String strCodigo, String strNombre, List<voModulo> modulos) {
		super();
		this.intIdAplicacion = intIdAplicacion;
		this.zona= zona;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.modulos = modulos;
	}
	
	public voAplicacion(int intIdAplicacion, voZona zona, String strCodigo, String strNombre) {
		this(intIdAplicacion, zona, strCodigo, strNombre, null);
	}

	public voAplicacion(int intIdAplicacion, String strCodigo, String strNombre) {
		this(intIdAplicacion, null, strCodigo, strNombre, null);
	}
	
	public int getId() {
		return intIdAplicacion;
	}
	
	public void setId(int intIdAplicacion) {
		this.intIdAplicacion = intIdAplicacion;
	}

	public voZona getZona() {
		return zona;
	}
	
	public void setZona(voZona zona) {
		this.zona = zona;
	}
	
	public String strGetCodigo() {
		return strCodigo;
	}

	public String strGetNombre() {
		return strNombre;
	}
		
	public List<voModulo> getModulos(){
		return modulos;
	}

	public void setModulos(List<voModulo> modulos) {
		this.modulos = modulos;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_aplicacion", new Field(this.intIdAplicacion));
		data.put("id_zona", new Field(this.zona.getId()));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		
		return data;
		
	}
	
}
