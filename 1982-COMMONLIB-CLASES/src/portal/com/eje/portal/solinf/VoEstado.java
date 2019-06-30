package portal.com.eje.portal.solinf;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;

public class VoEstado implements IConsultaDataRow {
	
	private int intIdEstado;
	private String strDescripcion;
	private String strIcono;
	private boolean bolVigente;
	
	private boolean existOnBD;
	
	public VoEstado(int intIdEstado, String strDescripcion, String strIcono, boolean bolVigente) {
		super();
		this.intIdEstado = intIdEstado;
		this.strDescripcion = strDescripcion;
		this.bolVigente = bolVigente;
		this.strIcono = strIcono;
	}
	
	public VoEstado(EEstado eestado) {
		load(eestado.getValor());
		this.existOnBD = true;
	}
	
	public VoEstado(int intIdEstado) {
		load(intIdEstado);
		this.existOnBD = true;
	}
	
	public VoEstado(Connection conn, int intIdEstado) {
		load(conn, intIdEstado);
		this.existOnBD = true;
	}
	
	public boolean load(int intIdEstado) {
		return load(null, intIdEstado);
	}

	public boolean load(Connection conn, int intIdEstado) {
		
		this.intIdEstado = intIdEstado;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select e.descripcion, e.icono, e.vigente \n") 
		.append("from eje_solinf_estado e \n");
		if (intIdEstado!=0) {
			strSQL.append("where e.id_estado=? \n");
		}else {
			strSQL.append("where e.id_estado<>? \n");
		}
		
		Object[] params = {intIdEstado};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while(data != null && data.next()) {
				this.strDescripcion = data.getString("descripcion");
				this.strIcono = data.getString("icono");
				this.bolVigente = (data.getInt("vigente")==1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public int getId() {
		return intIdEstado;
	}
	
	public String getDescripcion() {
		return strDescripcion;
	}
	
	public String getIcono() {
		return strIcono;
	}
	
	public boolean getVigencia() {
		return bolVigente;
	}
	
	public DataFields toDataField() {
		
		DataFields data = new DataFields();

		data.put("id_estado", new Field(this.intIdEstado));
		data.put("descripcion", new Field(this.strDescripcion));
		data.put("icono", new Field(this.strIcono));
		data.put("vigente", new Field(this.bolVigente));
		
		return data;
		
	}
	
}
