package portal.com.eje.portal.mis.mng.vo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;
import portal.com.eje.portal.mis.mng.enums.EnumTipoConexion;

public class voConexion implements IConsultaDataRow {

	EnumTipoConexion tipoconexion;
	int intConexion;
	String strConexion;
	String strUrl;
	String strServidor;
	String strBaseDato;
	String strUsuario;
	String strPassword;
	voEmpresa empresa;

	public voConexion(EnumTipoConexion tipoconexion, int intConexion) {
		super();
		this.tipoconexion = tipoconexion;
		this.intConexion = intConexion;
	}
	
	public voConexion() {
		load(null, 0, null);
	}
	
	public voConexion(Connection conn) {
		load(conn, 0, null);
	}

	
	public voConexion(int intConexion) {
		load(null, intConexion, null);
	}
	
	public voConexion(Connection conn, int intConexion) {
		load(conn, intConexion, null);
	}
	
	public voConexion(String empresa) {
		load(null, 0, empresa);
	}
	
	public voConexion(Connection conn, String empresa) {
		load(conn, 0, empresa);
	}
	
	boolean load(Connection conn, int intConexion, String empresa) {

		StringBuilder strSQL = new StringBuilder();
		
		ConsultaTool ctool = Mng.getInstance(ConsultaTool.class);

		Object[] objParam = new Object[] {};
		ArrayList<Object> params = new ArrayList<Object>(Arrays.asList(objParam));
		
		strSQL.append("select id_conexion, id_cnxtipo, conexion, url, servidor, basedato, usuario, password, id_empresa \n")
		.append("from eje_mis_conexion \n")
		.append("where id_conexion<>0 ");
		if(intConexion!=0) {
			strSQL.append("and id_conexion=? ");
			params.add(intConexion);
		}else if (empresa!=null) {
			strSQL.append("and id_empresa=? ");
			params.add(empresa);
		}
		strSQL.append("order by conexion ");

		try {
			ConsultaData data;
			if (conn!=null) {
				data = ctool.getData(conn, strSQL.toString(), params.toArray());
			}else {
				data = ctool.getData("mis", strSQL.toString(), params.toArray());
			}
			while (data!=null && data.next()) {
				
				this.intConexion = data.getInt("id_conexion");
				this.empresa = new voEmpresa(data.getString("id_empresa"));;
				this.tipoconexion = EnumTipoConexion.fromInteger(data.getInt("id_cnxtipo"));
				this.strUrl = data.getString("url");
				this.strServidor = data.getString("servidor");
				this.strBaseDato = data.getString("basedato");
				this.strUsuario = data.getString("usuario");
				this.strPassword = data.getString("password");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
		
	}

	public int getId(){
		return intConexion; 
	}
	
	public EnumTipoConexion getTipoConexion(){
		return tipoconexion; 
	}

	public voEmpresa getEmpresa() {
		return empresa;
	}

	public String getUrl() {
		return strUrl;
	}

	public String getServidor() {
		return strServidor;
	}
	
	public String getBaseDato() {
		return strBaseDato;
	}

	public String getUsuario() {
		return strUsuario;
	}

	public String getPassword() {
		return strPassword;
	}
	
	@Override
	public DataFields toDataField() {

		DataFields data = new DataFields();
		
		data.put("id_conexion", this.intConexion);
		data.put("id_empresa", this.empresa.getId());
		data.put("empresa", this.empresa.getNombre());
		data.put("id_cnxtipo", this.tipoconexion.getId());
		data.put("url", this.strUrl);
		data.put("servidor", this.strServidor);
		data.put("basedato", this.strUsuario);
		data.put("password", this.strPassword);

		return data;
		
	}

}
