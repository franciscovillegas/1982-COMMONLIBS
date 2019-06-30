package portal.com.eje.portal.tipoparrafo.vo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;

public class voTipoParrafo implements IConsultaDataRow {
	
	private int intId;
	private String strNombre;
	private String strUso;
	private boolean bolVigente;
	List<voTPParrafo> parrafos;
	
	public voTipoParrafo(int intId, String strNombre, String strUso, boolean bolVigente) {
		super();
		this.intId = intId;
		this.strNombre = strNombre;
		this.strUso = strUso;
		this.bolVigente = bolVigente;
	}
	
	public voTipoParrafo(int intId, String strNombre, boolean bolVigente) {
		this(intId, strNombre, null, bolVigente);
	}

	public voTipoParrafo(int intId, String strNombre) {
		this(intId, strNombre, null, false);
	}
	
	public voTipoParrafo(int intId) {
		load(null, intId);
	}

	public voTipoParrafo(Connection conn, int intId) {
		load(conn, intId);
	}
	
	public boolean load(Connection conn, int intId) {
		
		this.intId = intId;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select tp.id_tipoparrafo, tp.tipoparrafo, tp.uso, tp.vigente \n"); 
		strSQL.append("from eje_generico_tipoparrafo tp \n");
		strSQL.append("where tp.id_tipoparrafo=? \n");
		
		Object[] params = {intId};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while (data!=null && data.next()) {
				this.strNombre = data.getString("tipoparrafo");
				this.strUso = data.getString("uso");
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
	public String getNombre() {
		return strNombre;
	}
	public String getUso() {
		return strUso;
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
	public void setNombre(String strNombre) {
		this.strNombre = strNombre;
	}
	public void setUso(String strUso) {
		this.strUso = strUso;
	}
	public void setParrafos(List<voTPParrafo> parrafos) {
		this.parrafos=parrafos;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_tipoparrafo", this.intId);
		data.put("tipoparrafo", this.strNombre);
		data.put("uso", this.strUso);
		data.put("vigente", this.bolVigente);
		
		return data;
		
	}
	
}
