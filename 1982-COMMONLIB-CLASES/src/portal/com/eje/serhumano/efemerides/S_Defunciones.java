package portal.com.eje.serhumano.efemerides;

import java.io.IOException;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.efemerides:
//            Efemerides_Manager

public class S_Defunciones extends MyHttpServlet
{

    public S_Defunciones()
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
        Connection Conexion = super.connMgr.getConnection("portal");
        Connection ConSh = super.connMgr.getConnection("portal");
        if(Conexion != null && ConSh != null)
        {
            user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
                MuestraDatos(user, req, resp, Conexion, ConSh);
            else
                super.mensaje.devolverPaginaSinSesion(resp, "E-Mail", "Tiempo de Sesi\363n expirado...");
            insTracking(req, "Defunción".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
        super.connMgr.freeConnection("portal", ConSh);
    }

    public void MuestraDatos(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection Conexion, Connection conSh)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        SimpleHash modelRoot = new SimpleHash();
        GregorianCalendar Fecha = new GregorianCalendar();
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        proper = ResourceBundle.getBundle("db");
        Consulta consul = null;
        consul = (new Efemerides_Manager(conSh)).getDefunciones(mes, ano);
        modelRoot.put("detalle", consul.getSimpleList());
        consul.close();
        super.retTemplate(resp,"efemerides/defunciones.htm",modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private ResourceBundle proper;
}