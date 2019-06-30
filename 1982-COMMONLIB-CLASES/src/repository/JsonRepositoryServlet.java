package repository;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

import com.google.gson.JsonObject;

public class JsonRepositoryServlet extends MyHttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Usuario user;
	
	public JsonRepositoryServlet() {
		super();
	}   	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			user = SessionMgr.rescatarUsuario(request);
			if (!user.esValido()) {
				super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
				return;
			}
			else {
				response.setContentType("application/json");
				System.out.println(user.getJndi());
//		    	MMA 20170112
//			    java.sql.Connection Conexion = connMgr.getConnection(user.getJndi());
			    Connection Conexion = getConnMgr().getConnection(user.getJndi());
				String accion = request.getParameter("accion");
				if(accion.equals("trabajador")) {
					JsonObject jObject = new JsonObject();
					RepositoryManager rmng = new RepositoryManager();
					jObject = rmng.GetTrabajadores(Conexion,jObject);
					response.getOutputStream().print(jObject.toString());
				}
//		    	MMA 20170112
//		        connMgr.freeConnection(user.getJndi(), Conexion);
		        getConnMgr().freeConnection(user.getJndi(), Conexion);
				response.getOutputStream().flush();
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