package portal.com.eje.portal.perfapp;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;

public class voAutoservicio implements IConsultaDataRow {
	
	private int intIdAutoservicio;
	private int intOrden;
	private String strCodigo;
	private String strNombre;
	private String strImagen;
	private String strBackgroundColor;
	private int intHeight;
	private int intWidth;
	private String strPath;
	private boolean bolActivo;
	
	public voAutoservicio(int intIdAutoservicio, int intOrden, String strCodigo, String strNombre, String strImagen, String strBackgroundColor, int intHeight, int intWidth, String strPath, boolean bolActivo) {
		super();
		this.intIdAutoservicio = intIdAutoservicio;
		this.intOrden = intOrden;
		this.strCodigo = strCodigo;
		this.strNombre = strNombre;
		this.strImagen = strImagen;
		this.strBackgroundColor = strBackgroundColor;
		this.intHeight = intHeight;
		this.intWidth = intWidth;
		this.strPath = strPath;
		this.bolActivo = bolActivo;
	}

	public voAutoservicio(int intIdAutoservicio, boolean bolActivo) {
		this(intIdAutoservicio, 0, null, null, null, null, 0, 0, null, bolActivo);
	}
		
	public int getId() {
		return intIdAutoservicio;
	}
	
	public int getOrden() {
		return intOrden;
	}
	
	public String getCodigo() {
		return strCodigo;
	}

	public String getNombre() {
		return strNombre;
	}

	public String getImagen() {
		return strImagen;
	}
	
	public String getBackgroundColor() {
		return strBackgroundColor;
	}
	
	public int getHeight() {
		return intHeight;
	}
	
	public int getWidth() {
		return intWidth;
	}
	
	public String getPath() {
		return strPath;
	}
	
	public boolean getActivo() {
		return bolActivo;
	}
	
	public void setId(int intIdAutoservicio) {
		this.intIdAutoservicio = intIdAutoservicio;
	}
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_autoservicio", new Field(this.intIdAutoservicio));
		data.put("orden", new Field(this.intOrden));
		data.put("codigo", new Field(this.strCodigo));
		data.put("nombre", new Field(this.strNombre));
		data.put("imagen", new Field(this.strImagen));
		data.put("backgroundcolor", new Field(this.strBackgroundColor));
		data.put("height", new Field(this.intHeight));
		data.put("width", new Field(this.intWidth));
		data.put("path", new Field(this.strPath));
		data.put("activo", new Field(this.bolActivo));
		
		return data;
		
	}
	
}
