package portal.com.eje.permiso;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JavascriptArrayDataOut;
import cl.ejedigital.consultor.output.JqueryDataOut;
import cl.ejedigital.web.datos.ConsultaTool;




public class PerfilMngr {

	
	public String jQueryGetListPerfiles() throws SQLException {
		String strConsulta = " SELECT id,id_perfil_creo,nombre,estado,fecha_creacion FROM eje_generico_perfil ";
		String[] params = {};
				
		ConsultaData data =  ConsultaTool.getInstance().getData("portal",strConsulta,params);
		IDataOut tool = new JqueryDataOut(data);
		
		return tool.getListData();
	}
	
	public String jQueryGetListPrivilegios(Connection conn,String idPerfil) throws SQLException {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" SELECT a.app_id, a.app_desc, a.orden,");
		strConsulta.append(" 	isnull(papp.vigente,0) as vigente, ");
		strConsulta.append(" 	isnull(papp.permiso_heredable,0) as permiso_heredable ");
		strConsulta.append(" FROM EJE_GES_APLICACION a ");
		strConsulta.append(" LEFT OUTER JOIN eje_generico_perfil_app papp ");
		strConsulta.append("  	ON papp.app_id = a.app_id and papp.id_perfil = ? ");
		String[] params = { idPerfil };
		
		ConsultaData data =  ConsultaTool.getInstance().getData("portal",strConsulta.toString(),params);
		IDataOut tool = new JavascriptArrayDataOut(data);
		
		String[] order = {"app_id","app_desc", "orden" , "vigente", "permiso_heredable"};
		
		return tool.getListData(order);
	}
	
}
