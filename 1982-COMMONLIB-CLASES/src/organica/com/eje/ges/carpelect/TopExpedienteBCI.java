package organica.com.eje.ges.carpelect;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import freemarker.template.SimpleHash;

public class TopExpedienteBCI extends MyHttpServlet
{
    private Usuario user;
    
    public TopExpedienteBCI()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    /**
     * @deprecated Ya no se usa el usuario de orgánica
     * */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170112
//    	java.sql.Connection Conexion = connMgr.getConnection("portal");
        java.sql.Connection Conexion = getConnMgr().getConnection("portal");
        SimpleHash modelRoot = new SimpleHash();
        
        portal.com.eje.serhumano.user.Usuario userPortal = portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req); 
        user = Usuario.rescatarUsuario(req);

        if (user.getDatos(Conexion, userPortal.getRut().getRutId().concat("-").concat(userPortal.getRut().getDig()), userPortal.getPassWord())) {
			SessionMgr.guardarUsuarioOrganica(req , user);
		}
        
        String strRut = req.getParameter("rut");
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Rut '")).append(strRut).append("'"))));
        if(strRut == null)
            strRut = user.getRutConsultado();
        else
        if(strRut.equals("")) {
            strRut = user.getRutConsultado();
        } 
        else {
        	modelRoot.put("brut", strRut);
        }
        
        IOClaseWeb io = new IOClaseWeb(this, req, resp);
        io.retTemplate("CarpetaElectronica/top_expedientes.html",modelRoot);
        
        OutMessage.OutMessagePrint("Fin de doPost");
//    	MMA 20170112
//        connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("portal", Conexion);
    }

}