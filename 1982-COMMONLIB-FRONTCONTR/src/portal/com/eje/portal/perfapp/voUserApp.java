package portal.com.eje.portal.perfapp;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voUserApp implements IConsultaDataRow {
	
	private int intIdUserApp;
	private String strCodigo;
	private String strNombre;
	private boolean bolActivo;

	public voUserApp(int intIdUserApp, String strCodigo, String strNombre, boolean bolActivo) {
		super();
		this.intIdUserApp = intIdUserApp;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.bolActivo = bolActivo;
	}

	public voUserApp(int intIdUserApp, String strCodigo, String strNombre) {
		this(intIdUserApp, strCodigo, strNombre, false);
	}
	
	public voUserApp(int intIdUserApp, boolean bolActivo) {
		this(intIdUserApp, null, null, bolActivo);
	}
	
	public int getId() {
		return intIdUserApp;
	}
	
	public String getCodigo() {
		return strCodigo;
	}

	public String getNombre() {
		return strNombre;
	}
	
	public boolean getActivo() {
		return bolActivo;
	}

	public void setId(int intIdUserApp) {
		this.intIdUserApp = intIdUserApp;
	}
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_userapp", new Field(this.intIdUserApp));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		data.put("activo", new Field(this.bolActivo));
		
		return data;
		
	}
	
}
