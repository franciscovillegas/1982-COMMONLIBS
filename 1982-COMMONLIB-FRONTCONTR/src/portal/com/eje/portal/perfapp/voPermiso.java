package portal.com.eje.portal.perfapp;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voPermiso implements IConsultaDataRow {
	
	private int intIdPermiso;
	private String strCodigo;
	private String strNombre;
	private boolean bolAccede;
	
	
	public voPermiso(int intIdPermiso, String strCodigo, String strNombre, boolean bolAccede) {
		super();
		this.intIdPermiso = intIdPermiso;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.bolAccede = bolAccede;
	}

	public int getId() {
		return intIdPermiso;
	}
	
	public String strGetCodigo() {
		return strCodigo;
	}

	public String strGetNombre() {
		return strNombre;
	}

	public boolean getAccede() {
		return bolAccede;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_permiso", new Field(this.intIdPermiso));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		data.put("accede", new Field(this.bolAccede));
		
		return data;
		
	}
	
}
