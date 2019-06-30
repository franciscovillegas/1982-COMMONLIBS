package portal.com.eje.serhumano.user;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.permiso.PermisoPortal;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class BloqueoUsuarios extends MyHttpServlet {
	
	public BloqueoUsuarios() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException, ServletException {
    	
//    	MMA 20170112
//	    java.sql.Connection conexion = connMgr.getConnection("portal");
	    Connection conexion = getConnMgr().getConnection("portal");
    	String actualiza = req.getParameter("op") == null ? "0" : req.getParameter("op");
    	if(actualiza.equals("Y")) {
    		UserMgr usermgr = new UserMgr(conexion);
    		String usuarios[] = req.getParameterValues("usuario");
    		if(usuarios != null) {
    			for(int i=0;i<usuarios.length;i++) {
    				usermgr.resetBloqueos(usuarios[i].toString()); 
    			}
    		}
    	}
    	
        MuestraDatos(req,resp,conexion);
//    	MMA 20170112
//      connMgr.freeConnection("portal", conexion);
        getConnMgr().freeConnection("portal", conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doPost(req,resp);
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Consh)
        throws ServletException, IOException {
    	
        Usuario user = SessionMgr.rescatarUsuario(req);
     
        UserMgr usermgr = new UserMgr(Consh);
        
        SimpleList lista = new SimpleList();
        
        SimpleHash modelRoot = new SimpleHash();

        if(user.esValido()) {
            if( user.tieneApp(PermisoPortal.ADMIN_MOD_USUARIOS_BLOQUEADOS) ) {
                lista = usermgr.getListaUsuariosBloqueados();
                modelRoot.put("lista", lista);
            } 
            super.retTemplate(resp,"user/listaUsuariosBloqueados.htm",modelRoot);
        } 
        else {
            mensaje.devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

}