package portal.com.eje.serhumano.inicio;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetProp;
import freemarker.template.SimpleHash;

public class S_Inicio extends MyHttpServlet
{

    public S_Inicio()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Sucursales", "Tiempo de Sesi\363n expirado...");
        } else
        {
            Connection Conexion = super.connMgr.getConnection("portal");
            if(Conexion != null)
                MuestraDatos(user, Conexion, req, resp);
            else
                super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
            super.connMgr.freeConnection("portal", Conexion);
        }
    }

    private void MuestraDatos(Usuario user, Connection conexion, HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        SimpleHash modelRoot = new SimpleHash();
        datosRut userRut = new datosRut(conexion, user.getRutId());
        modelRoot.put("nombre", userRut.Nombres);
        modelRoot.put("foto", userRut.Foto);
        modelRoot.put("usuario", user.toHash());
        modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
        try
        {
        	super.retTemplate(resp,"user/user_bienvenido.htm",modelRoot);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }
}