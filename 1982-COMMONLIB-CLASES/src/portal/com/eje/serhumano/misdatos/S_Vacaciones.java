package portal.com.eje.serhumano.misdatos;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;

public class S_Vacaciones extends MyHttpServlet {

    public S_Vacaciones() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
//    	MMA 20170111
//    	Connection Conexion = connMgr.getConnection("portal");
        Connection Conexion =  getConnMgr().getConnection("portal");
        if(Conexion != null) {
            generaTab(req, resp, Conexion);
            insTracking(req, "Cartola de Vacaciones".intern(), null);
        } 
        else {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        
//    	MMA 20170111
//      super.connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("porral", Conexion);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException {
        OutMessage.OutMessagePrint("\n**** Inicio generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(!user.esValido()) {
            super.mensaje.devolverPaginaSinSesion(resp, "Cartola Vacaciones", "Tiempo de Sesi\363n expirado...");
        } 
        else {
            if(rut == null) {
            	rut = user.getRutId();
            }
            FichaPersonalBean fp = FichaPersonalBean.getInstance();
            SimpleHash modelRoot = fp.getVacaciones(Conexion,user,rut);
            IOClaseWeb io = new IOClaseWeb(this, req, resp);
            super.retTemplate(io,"misdatos/vacaciones.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("\n**** Fin generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
}