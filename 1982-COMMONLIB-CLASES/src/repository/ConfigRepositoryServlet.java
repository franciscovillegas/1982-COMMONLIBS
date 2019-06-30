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

public class ConfigRepositoryServlet extends MyHttpServlet	implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Usuario user;
	
	public ConfigRepositoryServlet() {
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
				ConfigManager cmng = new ConfigManager();
				String html = "repository/config.htm";
				String accion = request.getParameter("accion") == null ? "0" : request.getParameter("accion");
				if(accion.equals("save")) {
					String url = request.getParameter("url");
					String cliente = request.getParameter("cliente");
					String usuario = request.getParameter("usuario");
					String clave = request.getParameter("clave");
					if(cmng.updateConfiguracion(url,cliente,usuario,clave,Conexion)) {
						System.out.println("Configuracion actualizado");
					}
					else {
						System.out.println("Configuracion no actualizado");
					}
				}
				Map<?, ?> mv = cmng.getConfiguracion(Conexion);
				modelRoot.put("url",mv.get("url").toString());
				modelRoot.put("cliente",mv.get("cliente").toString());
				modelRoot.put("usuario",mv.get("usuario").toString());
				modelRoot.put("clave",mv.get("clave").toString());
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