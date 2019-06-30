package portal.com.eje.portal.correspondencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.portal.factory.Static;

public class CorrespondenciaMailLocator {
	private Map<Integer, IVoDestinatario> destinatarios;
	
	public static CorrespondenciaMailLocator getInstance() {
		return Static.getInstance(CorrespondenciaMailLocator.class);
	}
	
	public CorrespondenciaMailLocator() {
		destinatarios = new HashMap<Integer, IVoDestinatario>();
		loadCorreos(null);
	}
	
	private void loadCorreoPersona(String sqlExist, Integer rut) {
		Object[] params = {rut};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlExist, params);
			while(data != null && data.next()) {
				IVoDestinatario vo =  new CorrespondenciaVO(data.getInt("rut"), 
														    data.getString("nombre"), 
														    data.getString("mail"),
														    data.getDateJava("fecha_create"));
				destinatarios.put(data.getInt("rut"), vo);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadCorreosPrimeraVez() {
		if(destinatarios.size() == 0) {
			String sql = "INSERT INTO eje_correspondencia_contacto_repository (rut, nombre, mail, fecha_create) ";
			sql += " SELECT rut, nombre, mail, getdate() from eje_ges_trabajador where not mail is null ";
			
			try {
				ConsultaTool.getInstance().update("portal", sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadCorreos(Integer rut) {
		//String sqlExist 	= " SELECT rut, nombre, mail, fecha_create 			   FROM eje_correspondencia_contacto_repository \n";
		String sqlExist2    = " SELECT rut, nombre, mail, fecha_create = getdate() FROM eje_ges_trabajador ";
		
		if(rut != null) {
			boolean entro = false;
			/*ACTUALIZA SOLO 1 PERSONA*/
			//sqlExist += " WHERE rut = ? \n";
			sqlExist2+= " WHERE rut = ? \n";
		
			loadCorreoPersona(sqlExist2, rut);
		
			if(destinatarios.get(rut) == null){
				loadCorreoPersona(sqlExist2, rut);
				
				if(destinatarios.get(rut) == null){
						
				}
				else {
					setCorreo(destinatarios.get(rut));
				}
			}
		}
		else {
			/*ACTUALIZA TODA LA DOTACIÓN*/
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sqlExist2);
				while(data != null && data.next()) {
					IVoDestinatario vo =  new CorrespondenciaVO(data.getInt("rut"), 
						    data.getString("nombre"), 
						    data.getString("mail"));

					destinatarios.put(data.getInt("rut"), vo);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public IVoDestinatario getCorreo(int rut) {
		if(destinatarios.size() == 0) {
			loadCorreosPrimeraVez();
		}
		
		if( destinatarios.get(rut)  == null) {
			loadCorreos(rut);
		}
		 
		if( destinatarios.get(rut)  == null) {
			return new CorrespondenciaVO(rut, "", "");
		}
		else {
			return destinatarios.get(rut);	
		}
		 
	}
	
	public String getCorreo(int rut, String defecto) {
		if( destinatarios.get(rut)  == null || true /*SIEMPRE CARGARÁ EL MAIL DENUEVO*/ ) {
			loadCorreos(rut);
		}
		 
		IVoDestinatario ivo = destinatarios.get(rut);
		
		if(ivo == null) {
			return defecto;
		}
		else {
			return ivo.getEmail();
		}
	}
	
	/**
	 * @deprecated
	 * Mal construida, puede incurrir en tiempos de espera largo y resultados imprecisos
	 * */
	public IVoDestinatario getVoFromCorreo(String correo) {
		throw new NotImplementedException("Este metodo quedó como lastre, no debería existir.");
		/*
		Set<Integer> set = destinatarios.keySet();
		
		for(Integer i : set) {
			IVoDestinatario vo = destinatarios.get(i);
			if(correo != null && vo!= null && correo.equals(vo.getEmail())) {
				return vo;
			}
		}
		
		return new VoDestinatarioPortal(null, null, null);
		*/
	}
	
	private boolean respaldaCorreo_yElimina(int rut) {
		String sqlRespalda 	= " INSERT INTO eje_correspondencia_contacto_repository_history (rut, nombre, mail,fecha_create, fecha_archivado) ";
		sqlRespalda 	   += " SELECT TOP 1 rut, nombre, mail, fecha_create, getdate() from eje_correspondencia_contacto_repository where rut = ? ";
		
		String sqlElmina  	= " DELETE FROM eje_correspondencia_contacto_repository WHERE rut = ? ";
		
		Object[] params = {rut};
		Connection conn = null;
		boolean ok = true;
		
		try {
			conn = DBConnectionManager.getInstance().getConnection("portal");
			conn.setAutoCommit(false);
			
			
			ConsultaTool.getInstance().update(conn, sqlRespalda, params);
			ConsultaTool.getInstance().update(conn, sqlElmina  , params);
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
			
		} finally {
			
			try {
				if(ok) {
					conn.commit();
				}
				else {
					conn.rollback();
				}
				
				conn.setAutoCommit(true);
				DBConnectionManager.getInstance().freeConnection("portal", conn);	
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return ok;
	}
	
	
	private boolean setCorreo(IVoDestinatario vo) {
		boolean ok = false;
		
		if(vo != null) {
			String sql = "INSERT INTO eje_correspondencia_contacto_repository (RUT, NOMBRE, MAIL, FECHA_CREATE ) VALUES (?,?,?, GETDATE()) ";
			try {
				Object[] params = {vo.getRut(), vo.getNombre(), vo.getEmail()};
				ConsultaTool.getInstance().insert("portal", sql, params);
				
				loadCorreos(vo.getRut());
				ok = true;
			} catch (SQLException e) {
				e.printStackTrace();
				ok = false;
			}	
		}
	
		
		return ok;
	}
	
	public boolean updSiEsNecesario(List<IVoDestinatario> lista) {	
		boolean ok = true;
		
		if(lista != null) {
			for(IVoDestinatario vo : lista)
				if(vo != null) {
					ok &= updSiEsNecesario(vo);
			}
		}
		
		return ok;
	}
	
	public boolean updSiEsNecesario(IVoDestinatario vo) {	
		boolean ok = true;
		
		try {
			if(vo != null) {
				if(vo.getRut() == null) {
					System.out.println("@@@ ERROR : "+CorrespondenciaMailLocator.class.toString()+">>>> No puede ser null el rut");
				}
				else {
					IVoDestinatario data = getCorreo(vo.getRut() != null? vo.getRut() : -1);
					boolean doNothing = false;
					
					if(data != null) {
						if(vo.getEmail() != null && vo.getEmail().equals(data.getEmail())) {
							/*NO SE HACE NADA*/
							doNothing = true;
						}
						else if(vo.getEmail() == null) {
							/*NO SE HACE NADA*/
							doNothing = true;
						}
					}
					
					if(!doNothing) {
						respaldaCorreo_yElimina(vo.getRut() != null? vo.getRut() : -1);
						setCorreo(vo);
						ok = true;
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ok = false;
		}
		
		return ok;
		
	}
}
