package portal.com.eje.portal.tipoparrafo.vo;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;

public class voTPParametroValor implements IConsultaDataRow {
	
	private int intId;
	private String strNombre;
	private Double dblIdRef;

	public voTPParametroValor(int intId, String strNombre) {
		super();
		this.intId = intId;
		this.strNombre = strNombre;
	}

	public voTPParametroValor(int intId) {
		load(null, intId);
	}

	public voTPParametroValor(Connection conn, int intId) {
		load(conn, intId);
	}
	
	public boolean load(Connection conn, int intId) {
		
		this.intId = intId;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select p.id_tipoparrafo, p.id_parrafo, p.Nombre, p.vigente \n"); 
		strSQL.append("from eje_generico_tipoparrafo_parrafo p \n");
		strSQL.append("where p.id_parrafo=? \n");
		
		Object[] params = {intId};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while (data!=null && data.next()) {
				this.strNombre = data.getString("Nombre");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
		
	}
	
	public int getId() {
		return intId;
	}
	public String getNombre() {
		return strNombre;
	}
	public Double getIdRef() {
		return dblIdRef;
	}
	
	public void setId(int intId) {
		this.intId = intId;
	}public void setNombre(String strNombre) {
		this.strNombre = strNombre;
	}
	public void setIdRef(Double dblIdRef) {
		this.dblIdRef = dblIdRef;
	}

	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_tpvalor", this.intId);
		data.put("tpvalor", this.strNombre);
		
		return data;
		
	}
	
}
