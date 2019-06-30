package portal.com.eje.permiso;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaPreparada;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import freemarker.template.SimpleHash;


public class ServletModPerfil extends AbsClaseWeb {

	public ServletModPerfil(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	public void doPost() throws Exception {
		
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","permiso/ModPerfil.htm");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			super.getIoClaseWeb().retTemplate(htm,modelRoot);			
		}
		else if("select".equals(accion)) {
			if("perfiles".equals(thing)) {
				super.getIoClaseWeb().retTexto(perfil.jQueryGetListPerfiles());
			}
			else if("privilegios".equals(thing)) {
				super.getIoClaseWeb().retTexto(perfil.jQueryGetListPrivilegios(super.getIoClaseWeb().getConnection("portal"),
																				super.getIoClaseWeb().getParamString("idPerfil","-1")));
			}
			
			
		}
		else if("insert".equals(accion)) {
			if("perfil".equals(thing)) {
				super.getIoClaseWeb().retTexto(String.valueOf(insertPerfil(super.getIoClaseWeb().getParamString("perfilName","[error]"))));
			}
			else if ("privilegios".equals(thing)) {
				super.getIoClaseWeb().retTexto(String.valueOf(
						insertPrivilegios(
								super.getIoClaseWeb().getParamString("idPerfil","-1"),
								super.getIoClaseWeb().getParamString("vigentes",""),
								super.getIoClaseWeb().getParamString("heredables","")
								)));
			}
		}
		else if("update".equals(accion)) {
			
		}
		else if("delete".equals(accion)) {
			if("perfil".equals(thing)) {
				super.getIoClaseWeb().retTexto(String.valueOf(delPerfil(super.getIoClaseWeb().getParamString("idPerfil","-1"))));
			}
		}


	}

	public void doGet() throws Exception {
		doPost();
		
	}
	

	
	
	private boolean insertPerfil(String key) throws SQLException {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" INSERT INTO eje_generico_perfil ");
		strConsulta.append("  (id,id_perfil_creo,nombre,estado,fecha_creacion)");
		strConsulta.append(" VALUES ");
		strConsulta.append(" (?,?,?,?,GETDATE()) ");
		String[] params = {String.valueOf(getNewIdPerfil()),
							super.getIoClaseWeb().getUsuario().getPerfil().getIdPerfil() ,
							key,
							"1"};
		
		ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
		return consulta.insert(strConsulta.toString(),params);
	}
	
	private int getNewIdPerfil() throws SQLException {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" SELECT isnull(MAX(id),0) as id FROM eje_generico_perfil ");
		String[] params = {};
		
		ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
		ConsultaData data = consulta.getData(strConsulta.toString(),params);
		if(data.next()) {
			return   data.getInt("id") +1;
		}
		else {
			return 1;
		}			
	}
	
	private boolean delPerfil(String id) throws SQLException {
		if(id.equals(String.valueOf(super.getIoClaseWeb().getUsuario().getPerfil().getIdPerfil()))) {
			return false;
		}
		else  {
			delPerfilPrivilegios(id);
			delPerfilUsuario(id);
			
			String strConsulta = " DELETE FROM eje_generico_perfil WHERE id = ? ";
			String[] params = {id};
			
			ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
			return consulta.update(strConsulta,params) > 0;
		}
		
	}
	
	private boolean delPerfilPrivilegios(String id) throws SQLException {
		if(id.equals(String.valueOf(super.getIoClaseWeb().getUsuario().getPerfil().getIdPerfil()))) {
			return false;
		}
		else  {
			String strConsulta = " DELETE FROM eje_generico_perfil_app WHERE id_perfil = ? ";
			String[] params = {id};
			
			ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
			return consulta.update(strConsulta,params) > 0;
		}
	}
	
	private boolean delPerfilUsuario(String id) throws SQLException {
		if(id.equals(String.valueOf(super.getIoClaseWeb().getUsuario().getPerfil().getIdPerfil()))) {
			return false;
		}
		else  {
			String strConsulta = " DELETE FROM eje_generico_perfil_usuario WHERE id_perfil = ? ";
			String[] params = {id};
			
			ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
			return consulta.update(strConsulta,params) > 0;
		}
		
	}
	
	private boolean insertPrivilegios(String idPerfil, String privilegios, String heredables) throws SQLException {
		boolean ok = true;
		
		ok = ok && invalidaPermisosActuales(idPerfil);
		
		String[] arrayPrivilegios = privilegios.split("\\,");
		for(String appId :  arrayPrivilegios) {
			if(!existePermiso(idPerfil,appId)) {
				ok = ok && insertPermisoNullo(idPerfil,appId);
			}	
			
			ok = ok && activaPermiso(idPerfil,appId);
			
		}
		
		String[] arrayHeredables = heredables.split("\\,");
		for(String appId :  arrayHeredables) {
			if(!existePermiso(idPerfil,appId)) {
				ok = ok && insertPermisoNullo(idPerfil,appId);
			}	
			
			ok = ok && activaHerencia(idPerfil,appId);
			
		}
		
		return ok;
	}
	
	private boolean invalidaPermisosActuales(String idPerfil) throws SQLException {
		String strConsulta = " UPDATE eje_generico_perfil_app SET vigente = ?, permiso_heredable = ? WHERE id_perfil = ? ";
		String[] params = {"0","0",idPerfil};
		
		ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
		return consulta.update(strConsulta,params) != -1;
	}
	
	private boolean existePermiso(String idPerfil, String appId) throws SQLException {
		String strConsulta = " SELECT 1 FROM eje_generico_perfil_app WHERE app_id = ? and id_perfil = ? ";
		String[] params = {appId,idPerfil};
		
		ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
		return consulta.getData(strConsulta,params).getData().size() > 0;
	}
	
	private boolean insertPermisoNullo(String idPerfil, String appId) throws SQLException {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" INSERT INTO eje_generico_perfil_app ");
		strConsulta.append(" (Id_perfil,app_id,permiso_heredable,vigente) ");
		strConsulta.append(" VALUES (?,?,?,?) "); 
		String[] params = {idPerfil,appId,"0","0"};
		
		ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
		return consulta.insert(strConsulta.toString(),params);
	}
	
	private boolean activaPermiso(String idPerfil, String appId) throws SQLException {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" UPDATE eje_generico_perfil_app ");
		strConsulta.append(" set vigente = 1 ");
		strConsulta.append(" WHERE app_id = ? and id_perfil = ?  "); 
		String[] params = {appId,idPerfil};
		
		ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
		return consulta.update(strConsulta.toString(),params) > 0;
	}
	
	private boolean activaHerencia(String idPerfil, String appId) throws SQLException {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" UPDATE eje_generico_perfil_app ");
		strConsulta.append(" set permiso_heredable = 1 ");
		strConsulta.append(" WHERE app_id = ? and id_perfil = ?  "); 
		String[] params = {appId,idPerfil};
		
		ConsultaPreparada consulta = new ConsultaPreparada(super.getIoClaseWeb().getConnection("portal"));
		return consulta.update(strConsulta.toString(),params) > 0;
	}


	
}
