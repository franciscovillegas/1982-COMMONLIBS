package organica.com.eje.ges.expediente;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.PropertiesManipulateByRequest;
import freemarker.template.SimpleHash;

public class TopUtilitarios extends MyHttpServlet
{

    public TopUtilitarios()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170112
//	    java.sql.Connection conexion = connMgr.getConnection("portal");
	    Connection conexion = getConnMgr().getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Utilitarios", "Tiempo de Sesi\363n expirado...");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            PropertiesManipulateByRequest prop = new PropertiesManipulateByRequest(modelRoot);
            prop.manipulate(req);
            
            modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
            super.retTemplate(resp,"Gestion/Expediente/top_herramientas.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
//    	MMA 20170112
//      connMgr.freeConnection("portal", conexion);
        getConnMgr().freeConnection("portal", conexion);
    }
    

    private Usuario user;
}