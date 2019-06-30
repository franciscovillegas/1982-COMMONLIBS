package portal.com.eje.portal.udimension;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;

public class VoUTipoDimension implements IConsultaDataRow{

	private int intIdTipo;
	private String strDescripcion;
	
	private boolean existOnBD;
	
	public VoUTipoDimension(int intIdTipo, String strDescripcion){
		super();
		this.intIdTipo = intIdTipo;
		this.strDescripcion = strDescripcion;
	}
	
	public VoUTipoDimension() {
		load(0, null);
		this.existOnBD = true;
	}
	
	public VoUTipoDimension(String strFiltro) {
		load(0, strFiltro);
		this.existOnBD = true;
	}
	
	public VoUTipoDimension(int intIdTipo) {
		load(intIdTipo, null);
		this.existOnBD = true;
	}
	
	public VoUTipoDimension(Connection conn, int intIdTipo) {
		load(conn, intIdTipo, null);
		this.existOnBD = true;
	}
	
	public VoUTipoDimension(Connection conn, int intIdTipo, String strFiltro) {
		load(conn, intIdTipo, strFiltro);
		this.existOnBD = true;
	}
	
	public boolean load(int intIdTipo, String strFiltro) {
		return load(null, intIdTipo, strFiltro);
	}

	public boolean load(Connection conn, int intIdTipo, String strFiltro) {
		
		this.intIdTipo = intIdTipo;
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select dimension_tipo, descripcion \n")
		.append("from eje_ges_dimension_tipo \n");
		if (intIdTipo!=0) {
			strSQL.append("where dimension_tipo=? \n");
		}else {
			strSQL.append("where dimension_tipo<>? \n");
		}
    	if (strFiltro!=null){
    		strSQL.append("and descripcion like '%").append(strFiltro.replaceAll(" ", "%")).append("%' ");
    	}
		strSQL.append("order by descripcion \n");
		
		Object[] params = {intIdTipo};
		try {
			ConsultaData data;
			if (conn!=null){
				data = ConsultaTool.getInstance().getData(conn, strSQL.toString() , params);
			}else{
				data = ConsultaTool.getInstance().getData("portal", strSQL.toString() , params);
			}
			while(data != null && data.next()) {
				this.intIdTipo = data.getInt("dimension_tipo");
				this.strDescripcion = data.getString("descripcion");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public int getIdDimension(){
		return intIdTipo;
	}
	
	public String getDescripcion(){
		return strDescripcion;
	}
	
	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();

		data.put("id_tipodimension", new Field(this.getIdDimension()));
		data.put("tipodimension", new Field(this.getDescripcion()));
		
		return data;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return strDescripcion;
	}
}
