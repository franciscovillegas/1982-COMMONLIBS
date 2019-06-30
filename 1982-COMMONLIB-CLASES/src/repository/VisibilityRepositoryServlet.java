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

public class VisibilityRepositoryServlet extends MyHttpServlet	implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Usuario user;
	
	public VisibilityRepositoryServlet() {
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
//			    java.sql.Connection Conexion = connMgr.getConnection(user.getJndi());
			    Connection Conexion = getConnMgr().getConnection(user.getJndi());
				VisibilityManager vmng = new VisibilityManager();
				String html = null;
				String accion = request.getParameter("accion") == null ? "new" : request.getParameter("accion");
				String grupo = request.getParameter("grupo") == null ? "0" : request.getParameter("grupo");
				if(accion.equals("new")) {
					html="repository/visibility-new.htm";
					modelRoot.put("accion", "new");
				}
				else if(accion.equals("save")) {
					html="repository/visibility-new.htm";
					String idVisibility = vmng.GetIdVisibilidad(Conexion);
					String nombre = request.getParameter("nombre");
					String universo = request.getParameter("universo");
					if(vmng.addGrupoVisibilidad(idVisibility, nombre, universo, Conexion)) {
						System.out.println("Grupo de Visibilidad agregado");
					}
					else {
						System.out.println("Grupo de Visibilidad no agregado");
					}
					modelRoot.put("accion", "new");
				}
				else if(accion.equals("mod")) {
					html="repository/visibility-mod.htm";
					modelRoot.put("grupos", vmng.GetVisibilidad(Conexion));
					if(!grupo.equals("0")) {
						Map<?, ?> mv = vmng.getGrupoVisibilidad(grupo,Conexion);
						modelRoot.put("c_grupo",mv.get("nombre").toString());
						modelRoot.put("c_universo",mv.get("universo").toString());
					}
					modelRoot.put("grupo",grupo);
					modelRoot.put("accion","mod");
				}
				else if(accion.equals("update")) {
					html="repository/visibility-mod.htm";
					if(!grupo.equals("0")) {
						String nombre = request.getParameter("nombre");
						String universo = request.getParameter("universo");
						if(vmng.updateGrupoVisibilidad(grupo, nombre, universo, Conexion)) {
							System.out.println("Grupo de Visibilidad actualizado");
						}
						else {
							System.out.println("Grupo de Visibilidad no actualizado");
						}
						Map<?, ?> mv = vmng.getGrupoVisibilidad(grupo,Conexion);
						modelRoot.put("c_grupo",mv.get("nombre").toString());
						modelRoot.put("c_universo",mv.get("universo").toString());
					}
					modelRoot.put("grupos", vmng.GetVisibilidad(Conexion));
					modelRoot.put("grupo",grupo);
					modelRoot.put("accion","mod");
				}
				else if(accion.equals("del")) {
					html="repository/visibility-delete.htm";
					String grupos[] = request.getParameterValues("grupos") == null ? null : request.getParameterValues("grupos");
					if(grupos != null) {
						for(String valor: grupos){
							System.out.println(valor);
							vmng.deleteGrupo(valor,Conexion);
						}
					}
					modelRoot.put("grupos", vmng.GetVisibilidad(Conexion));
					modelRoot.put("accion", "del");
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