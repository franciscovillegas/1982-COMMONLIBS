package portal.com.eje.serhumano.user;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Util;

public class UsuarioPerfilTool {

	public static UsuarioPerfilTool getInstance() {
		return Util.getInstance(UsuarioPerfilTool.class);
	}
	
	/**
	 * Determina si existe el perfil id
	 * */
	public boolean existPefil(int perfilId) throws SQLException {
		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" select * ");
		sql.appendLine(" from eje_generico_perfiles_webmatico p ");
		sql.appendLine(" 	inner join eje_generico_perfiles_rut_webmatico r on r.perfil = p.id ");
		sql.appendLine(" 	inner join eje_ges_usuario u on r.rut = u.login_usuario ");
		sql.appendLine(" 	inner join eje_ges_trabajador t on t.rut = r.rut ");
		
		ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
		return data != null && data.next();
	}
	
 
}
