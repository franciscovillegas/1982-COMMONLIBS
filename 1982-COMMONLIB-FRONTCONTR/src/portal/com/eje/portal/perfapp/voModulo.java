package portal.com.eje.portal.perfapp;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voModulo implements IConsultaDataRow {
	
	private int intIdModulo;
	private voAplicacion aplicacion;
	private String strCodigo;
	private String strNombre;
	private boolean bolAccede;

	public voModulo(int intIdModulo, voAplicacion aplicacion, String strCodigo, String strNombre, boolean bolAccede) {
		super();
		this.intIdModulo = intIdModulo;
		this.aplicacion= aplicacion;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.bolAccede = bolAccede;
	}

	public voModulo(int intIdModulo, String strCodigo, String strNombre) {
		this(intIdModulo, null, strCodigo, strNombre, true);
	}
	
	public int getId() {
		return intIdModulo;
	}
	
	public voAplicacion getaplicacion() {
		return aplicacion;
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
	
	public void setAplicacion(voAplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_modulo", new Field(this.intIdModulo));
		data.put("id_zona", new Field(this.aplicacion.getZona().getId()));
		data.put("id_aplicacion", new Field(this.aplicacion.getId()));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		data.put("accede", new Field(this.bolAccede));
		
		return data;
		
	}
	
}
