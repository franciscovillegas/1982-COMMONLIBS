package portal.com.eje.portal.perfapp;

import java.sql.Connection;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import portal.com.eje.portal.trabajador.TrabajadorDataFiltro;
import portal.com.eje.portal.trabajador.TrabajadorDataLocator;

public class voPAPersona implements IConsultaDataRow {
	
	private int intIdPersona;
	private String strPersona;
	private String strCargo;
	private String strUnidad;
	
	public voPAPersona(int intIdPersona, String strPersona, String strCargo, String strUnidad) {
		super();
		this.intIdPersona = intIdPersona;
		this.strPersona = strPersona;
		this.strCargo = strCargo;
		this.strCargo = strCargo;
		this.strUnidad = strUnidad;
	}

	public voPAPersona(int intIdPersona) {
		load(intIdPersona);
	}

	
	public voPAPersona(Connection conn, int intIdPersona) {
		load(conn, intIdPersona);
	}
	
	public boolean load(int intIdPersona) {
		return load(null, intIdPersona);
	}

	public boolean load(Connection conn, int intIdPersona) {
		
		this.intIdPersona = intIdPersona;

		TrabajadorDataFiltro df = new TrabajadorDataFiltro();
		df.setDataPersonal(true);
		df.setDataContacto(true);
		df.setDataContactoMail(true);
		df.setPathBacks("../../../");
		ConsultaData dtaPersona = TrabajadorDataLocator.getInstance().getDataFiltrada(df, this.intIdPersona);
		while (dtaPersona!=null && dtaPersona.next()) {
			this.strPersona = dtaPersona.getString("nombre");
			this.strCargo = dtaPersona.getString("cargo");
			this.strUnidad = dtaPersona.getString("unidad");
		}
		
		return true;
	}
	
	
	public int getId() {
		return intIdPersona;
	}
	
	public String getNombre() {
		return strPersona;
	}

	public String getCargo() {
		return strCargo;
	}
	
	public String getUnidad() {
		return strUnidad;
	}

	public DataFields toDataField() {
		
		DataFields data = new DataFields();
		
		data.put("id_persona", new Field(this.intIdPersona));
		data.put("persona", new Field(this.strPersona));
		data.put("cargo", new Field(this.strCargo));
		data.put("unidad", new Field(this.strUnidad));
		
		return data;
		
	}
	
}
