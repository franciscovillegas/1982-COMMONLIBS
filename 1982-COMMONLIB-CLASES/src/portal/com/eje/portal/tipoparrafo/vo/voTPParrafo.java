package portal.com.eje.portal.tipoparrafo.vo;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;

public class voTPParrafo implements IConsultaDataRow {
	
	private int intId;
	private int intIdTipoParrafo;
	private String strContenido;
	private boolean bolVigente;

	public voTPParrafo(int intIdTipoParrafo, int intId, String strContenido, boolean bolVigente) {
		super();
		this.intIdTipoParrafo = intIdTipoParrafo;
		this.intId = intId;
		this.strContenido = strContenido;
		this.bolVigente = bolVigente;
	}

	public voTPParrafo(int intId) {
		load(null, intId);
	}

	public voTPParrafo(Connection conn, int intId) {
		load(conn, intId);
	}
	
	public boolean load(Connection conn, int intId) {
		
		this.intId = intId;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select p.id_tipoparrafo, p.id_parrafo, p.contenido, p.vigente \n"); 
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
				this.intIdTipoParrafo=data.getInt("id_tipoparrafo");
				this.strContenido = data.getString("contenido");
				this.bolVigente = (data.getInt("vigente"))==1;
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
	public int getIdTipoParrafo() {
		return intIdTipoParrafo;
	}
	public String getContenido() {
		return strContenido;
	}
	public boolean getVigente() {
		return bolVigente;
	}
	public int getVigenteInt() {
		int intVigente=0;
		if (bolVigente) {
			intVigente=1;
		}
		return intVigente;
	}
	
	public void setId(int intId) {
		this.intId = intId;
	}
	public void setIdTipoParrafo(int intIdTipoParrafo) {
		this.intIdTipoParrafo = intIdTipoParrafo;
	}
	public void setContenido(String strContenido) {
		this.strContenido = strContenido;
	}

	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_tipoparrafo", this.intIdTipoParrafo);
		data.put("id_parrafo", this.intId);
		data.put("contenido", this.strContenido);
		data.put("vigente", this.bolVigente);
		
		return data;
		
	}
	
}
