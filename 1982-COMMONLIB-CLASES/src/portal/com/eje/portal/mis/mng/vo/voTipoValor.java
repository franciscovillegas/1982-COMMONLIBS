package portal.com.eje.portal.mis.mng.vo;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;

public class voTipoValor implements IConsultaDataRow {

	int intTipoValor;
	String strTipoValor;
	String strDescripcion;
	Double dblValor;
	
	public voTipoValor(int intTipoValor, String strTipoValor, String strDescripcion, Double dblValor) {
		super();
		this.intTipoValor = intTipoValor;
		this.strTipoValor = strTipoValor;
		this.strDescripcion = strDescripcion;
		this.dblValor = dblValor;
	}
	
	public voTipoValor(int intTipoValor) {
		load(null, intTipoValor);
	}

	public voTipoValor(Connection conn, int intTipoValor) {
		load(conn, intTipoValor);
	}

	boolean load(Connection conn, int intTipoValor) {

		StringBuilder strSQL = new StringBuilder();
		
		ConsultaTool ctool = Mng.getInstance(ConsultaTool.class);

		this.intTipoValor = intTipoValor;
		
		strSQL.append("select tipovalor, descripcion \n")
		.append("from eje_mis_mae_tipovalor \n")
		.append("where id_tipovalor=").append(intTipoValor);
		try {
			ConsultaData data;
			if (conn!=null) {
				data = ctool.getData(conn, strSQL.toString());
			}else{
				data = ctool.getData("mis", strSQL.toString());
			}
			while (data!=null && data.next()) {
				this.strTipoValor = data.getString("tipovalor");
				this.strDescripcion = data.getString("descripcion");
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
		
	}

	public int getId(){
		return intTipoValor; 
	}
	public String getNombre(){
		return strTipoValor; 
	}
	public String getDescripcion() {
		return strDescripcion;
	}
	public Double getValor() {
		return dblValor;
	}
	
	public void setValor(Double dblValor) {
		this.dblValor = dblValor;
	}
	
	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();
		
		data.put("id_tipovalor", this.intTipoValor);
		data.put("tipovalor", this.strTipoValor);
		data.put("descripcion", this.strDescripcion);
		data.put("valor", this.dblValor);
		
		return data;
		
	}

}
