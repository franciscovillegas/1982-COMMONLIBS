package repository;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import freemarker.template.SimpleHash;

public class ConceptRepositoryServlet extends MyHttpServlet	implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Usuario user;
	
	public ConceptRepositoryServlet() {
		super();
	}   	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SimpleHash modelRoot = new SimpleHash();
			user = SessionMgr.rescatarUsuario(request);
			if (!user.esValido()) {
				super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
				return;
			}   
			else {
				System.out.println(user.getJndi());
//		    	MMA 20170112
//		      java.sql.Connection Conexion = connMgr.getConnection(user.getJndi());
		    	Connection Conexion = getConnMgr().getConnection(user.getJndi());
				ConceptManager cmng = new ConceptManager();
				String html = null;
				String accion = request.getParameter("accion") == null ? "new" : request.getParameter("accion");
				String clasificacion = request.getParameter("clasificacion") == null ? "0" : request.getParameter("clasificacion");
				if(accion.equals("new")) {
					html="repository/concept-new.htm";
					modelRoot.put("iconos", cmng.GetIcono(Conexion) );
					modelRoot.put("visibilidad", cmng.GetVisibilidad(Conexion) );
					modelRoot.put("uploaders", cmng.GetUploader(Conexion) );
					modelRoot.put("accion", accion);
				}
				else if(accion.equals("save")) {
					html="repository/concept-new.htm";
					
					String idclasificacion = cmng.GetIdClasificacion(Conexion);
					String abrev = request.getParameter("abrev");
					String vigencia = request.getParameter("vigencia");
					String icono = request.getParameter("icono");
					String descripcion = request.getParameter("descripcion");
					if(cmng.addClasificacion(idclasificacion,clasificacion,abrev,vigencia,icono,descripcion,Conexion)) {
						String visibilidad[] = request.getParameterValues("visibilidad") == null ? null : request.getParameterValues("visibilidad");
						if(visibilidad != null) {
							for(String valor: visibilidad){
								String idcv = cmng.GetIdVisibilidadByClasificacion(Conexion);
								cmng.addClasificacionVisibilidad(idcv,idclasificacion,valor,Conexion);
							}
						}
						String uploaders[] = request.getParameterValues("uploader") == null ? null : request.getParameterValues("uploader");
						if(uploaders != null) {
							for(String valor: uploaders){
								String idcu = cmng.GetIdUploaderByClasificacion(Conexion);
								cmng.addClasificacionUploader(idcu,idclasificacion,valor,Conexion);
							}
						}
					}
					modelRoot.put("iconos", cmng.GetIcono(Conexion) );
					modelRoot.put("visibilidad", cmng.GetVisibilidad(Conexion) );
					modelRoot.put("uploaders", cmng.GetUploader(Conexion) );
					modelRoot.put("accion", "new");
				}
				else if(accion.equals("mod")) {
					html="repository/concept-mod.htm";
					modelRoot.put("clasificaciones", cmng.GetClasificaciones(Conexion) );
					if(!clasificacion.equals("0")) {
						Map<?, ?> mp =  cmng.getClasificacion(clasificacion,Conexion);
						modelRoot.put("c_clasificacion", mp.get("clasificacion").toString());
						modelRoot.put("c_abrev", mp.get("abrev").toString());
						modelRoot.put("c_vigencia", mp.get("vigencia").toString());
						modelRoot.put("c_icono", mp.get("icono").toString());
						modelRoot.put("c_descripcion", mp.get("descripcion").toString());
						modelRoot.put("iconos", cmng.GetIconoByClasificacion(clasificacion, Conexion) );
						modelRoot.put("visibilidad", cmng.GetVisibilidadByClasificacion(clasificacion, Conexion) );
						modelRoot.put("uploaders", cmng.GetUploaderByClasificacion(clasificacion, Conexion) );
					}
					modelRoot.put("accion", accion);
					modelRoot.put("clasificacion", clasificacion);
				}
				else if(accion.equals("update")) {
					html="repository/concept-mod.htm";
					if(!clasificacion.equals("0")) {
						String nombre = request.getParameter("nombre");
						String abrev = request.getParameter("abrev");
						String vigencia = request.getParameter("vigencia");
						String icono = request.getParameter("icono");
						String descripcion = request.getParameter("descripcion");
						if(cmng.updateClasificacion(clasificacion, nombre, abrev, vigencia, icono, descripcion, Conexion)) {
							String visibilidad[] = request.getParameterValues("visibilidad") == null ? null : request.getParameterValues("visibilidad");
							if(visibilidad != null) {
								cmng.deleteVisibilidadByClasificacion(clasificacion,Conexion);
								for(String valor: visibilidad){
									String idcv = cmng.GetIdVisibilidadByClasificacion(Conexion);
									cmng.addClasificacionVisibilidad(idcv,clasificacion,valor,Conexion);
								}
							}
							String uploaders[] = request.getParameterValues("uploader") == null ? null : request.getParameterValues("uploader");
							if(uploaders != null) {
								cmng.deleteUploaderByClasificacion(clasificacion, Conexion);
								for(String valor: uploaders){
									String idcu = cmng.GetIdUploaderByClasificacion(Conexion);
									cmng.addClasificacionUploader(idcu,clasificacion,valor,Conexion);
								}
							}
						}
						modelRoot.put("clasificaciones", cmng.GetClasificaciones(Conexion) );		
						Map<?, ?> mp =  cmng.getClasificacion(clasificacion,Conexion);
						modelRoot.put("c_clasificacion", mp.get("clasificacion").toString());
						modelRoot.put("c_abrev", mp.get("abrev").toString());
						modelRoot.put("c_vigencia", mp.get("vigencia").toString());
						modelRoot.put("c_icono", mp.get("icono").toString());
						modelRoot.put("c_descripcion", mp.get("descripcion").toString());
						modelRoot.put("iconos", cmng.GetIconoByClasificacion(clasificacion, Conexion) );
						modelRoot.put("visibilidad", cmng.GetVisibilidadByClasificacion(clasificacion, Conexion) );
						modelRoot.put("uploaders", cmng.GetUploaderByClasificacion(clasificacion, Conexion) );
					}
					modelRoot.put("accion", "mod");
					modelRoot.put("clasificacion", clasificacion);
				}
//		    	MMA 20170112
//		        connMgr.freeConnection(user.getJndi(), Conexion);
		        getConnMgr().freeConnection(user.getJndi(), Conexion);
				super.retTemplate(response,html,modelRoot);
			}
		}
		catch (Exception ex) {      
			ex.printStackTrace();
			throw new ServletException(ex.toString(), ex);
		}
	}  	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}   	  	    
}