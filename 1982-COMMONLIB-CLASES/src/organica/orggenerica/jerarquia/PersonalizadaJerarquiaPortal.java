package organica.orggenerica.jerarquia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import portal.com.eje.serhumano.user.Usuario;




public class PersonalizadaJerarquiaPortal implements IJerarquia {

	public IJerarquiaNodo getJerarquia(Usuario user) {
		IDBConnectionManager dbConn = DBConnectionManager.getInstance();
		Connection conn = dbConn.getConnection("portal");
		
		
		IJerarquiaNodo nodo = getNodoPadre(user,conn);
		loadTree(conn, nodo, user);
		
		dbConn.freeConnection("portal",conn);
		
		return nodo;
	}
	
	private IJerarquiaNodo getNodoPadre(Usuario user,Connection conn) {
		
		String str = " SELECT TOP 1 J.NODO_PADRE, ISNULL(U.UNID_DESC,'Empresa') as unid_desc";
		str += " FROM EJE_GES_JERARQUIA J LEFT OUTER JOIN EJE_GES_UNIDADES U ON U.UNID_ID = J.NODO_PADRE ";
		
		if(user.getEmpresa() != null && !"0".equals(user.getEmpresa())) {
			str += " WHERE J.NODO_ID IN (select distinct unidad from eje_ges_trabajador where wp_cod_empresa="+user.getEmpresa()+") ";
		}
		
		str += " ORDER BY J.NODO_NIVEL ";
		IJerarquiaNodo nodo = null ;
		
		PreparedStatement pst;
		try {
			pst = conn.prepareStatement(str);
			ResultSet rs = pst.executeQuery();
			
			
			while(rs.next()) {
				nodo = new JerarquiaNodo(rs.getString("unid_desc"),rs.getString("nodo_padre"),"filetree",true);
			}
			rs.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nodo;
		

	}
	
	private int loadTree(Connection conn, IJerarquiaNodo nodeParent,Usuario user) {
		int cantHijos = 0;
		
		if(nodeParent == null) {
			return cantHijos;
		}
		
		String str = "SELECT j.nodo_id , u.unid_desc ";
		str += " FROM dbo.eje_ges_jerarquia j left outer join eje_ges_unidades u on j.nodo_id = u.unid_id ";
		str += " where j.nodo_padre = '"+nodeParent.getId()+"'";
		
		if(user.getEmpresa() != null) {
			str += " and J.NODO_ID IN (select distinct unidad from eje_ges_trabajador where wp_cod_empresa="+user.getEmpresa()+") ";
		}
		
		try {
			PreparedStatement pst = conn.prepareStatement(str);
			ResultSet rs = pst.executeQuery();
			
			
			while(rs.next()) {
				cantHijos++;
				IJerarquiaNodo nodo = new JerarquiaNodo(rs.getString("unid_desc"),rs.getString("nodo_id"),"folder",false);
				nodeParent.addHijo(nodo);
				int cantNietos = loadTree(conn,nodo,user);
				
				if(cantNietos == 0) {
					nodo.setClassHtml("file");
				}
				
			}
			
			rs.close();
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cantHijos;
		
	}

}
