package portal.com.eje.portal.mis.mng.vo;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;

public class voEmpresa implements IConsultaDataRow {

	String strIdEmpresa;
	String strEmpresa;

	public voEmpresa(String strIdEmpresa, String strEmpresa) {
		super();
		this.strIdEmpresa = strIdEmpresa;
		this.strEmpresa = strEmpresa;
	}
	
	public voEmpresa(String strIdEmpresa) {
		load(strIdEmpresa);
	}
	
	boolean load(String strIdEmpresa) {

		StringBuilder strSQL = new StringBuilder();
		
		ConsultaTool ctool = Mng.getInstance(ConsultaTool.class);

		this.strIdEmpresa = strIdEmpresa;
		
		strSQL.append("select id_empresa=empresa, empresa=rtrim(ltrim(descrip)) \n")
		.append("from eje_ges_empresa \n")
		.append("where empresa=? ");
		
		Object[] params = {strIdEmpresa};

		try {
			ConsultaData data = ctool.getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				this.strEmpresa = data.getString("empresa");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
		
	}

	public String getId(){
		return strIdEmpresa; 
	}
	
	public String getNombre(){
		return strEmpresa; 
	}

	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();
		
		data.put("id_empresa", this.strIdEmpresa);
		data.put("empresa", this.strEmpresa);

		return data;
		
	}

}
