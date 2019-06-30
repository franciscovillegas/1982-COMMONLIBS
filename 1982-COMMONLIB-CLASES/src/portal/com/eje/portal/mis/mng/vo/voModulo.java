package portal.com.eje.portal.mis.mng.vo;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;

public class voModulo implements IConsultaDataRow {

	int intModulo;
	String strModulo;
	boolean bolActivo;
	
	public voModulo(int intModulo, String strModulo) {
		super();
		this.intModulo = intModulo;
		this.strModulo = strModulo;
	}
	
	public voModulo(int intModulo) {
		load(intModulo);
	}

	boolean load(int intModulo) {

		StringBuilder strSQL = new StringBuilder();
		
		ConsultaTool ctool = Mng.getInstance(ConsultaTool.class);

		strSQL.append("select id_modulo, modulo \n")
		.append("from eje_mis_modulo \n")
		.append("where id_modulo=").append(intModulo);
		try {
			ConsultaData data = ctool.getData("mis", strSQL.toString());
			while (data!=null && data.next()) {
				this.intModulo = data.getInt("id_modulo");
				this.strModulo = new String(data.getString("modulo"));
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
		
	}

	public int getId(){
		return intModulo; 
	}

	public String getNombre(){
		return strModulo; 
	}

	public boolean getActivo() {
		return bolActivo;
	}
	
	public void setActivo(boolean bolActivo) {
		this.bolActivo = bolActivo;
	}
	
	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();
		
		data.put("id_modulo", this.intModulo);
		data.put("modulo", this.strModulo);
		if (this.bolActivo) {
			data.put("activo", 1);
		}else{
			data.put("activo", 0);
		}
		
		return data;
		
	}

}
