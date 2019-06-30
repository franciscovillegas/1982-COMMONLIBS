package portal.com.eje.serhumano.datosdf;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.efemerides.Efemerides_Manager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

public class S_InfoRutCarga extends MyHttpServlet
{

    public S_InfoRutCarga()
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
            insTracking(req, "Información Cargas".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
        super.connMgr.freeConnection("portal", ConSh);
    }

    public void MuestraDatos(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection Conexion, Connection ConSh)
        throws ServletException, IOException
    {
        try
        {
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("******* Inicio *******")).append(getClass().getName()).append(" Metodo : MuestraDatos"))));
            SimpleHash modelRoot = new SimpleHash();
            proper = ResourceBundle.getBundle("db");
            Consulta consul = null;
            Consulta consul2 = null;
            String rutcarga = req.getParameter("rutcarga");
            String rut = req.getParameter("rut");
            consul = (new Efemerides_Manager(Conexion)).getNacimientos(rut, rutcarga);
            if(consul.next())
            {
                modelRoot.put("rut", rut);
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("rutcarga", rutcarga);
                modelRoot.put("fec", consul.getString("fecha_nacim"));
                modelRoot.put("emple", consul.getString("empleado"));
            }
            consul2 = (new Efemerides_Manager(Conexion)).getInfoFotoCarga(ConSh, rutcarga);
            if(consul2.next())
                modelRoot.put("foto", consul2.getString("foto"));
            consul.close();
            super.retTemplate(resp,"fichapersonal/fichacarga.htm",modelRoot);
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("******* Fin *******")).append(getClass().getName()).append(" Metodo : MuestraDatos"))));
        }
        catch(Exception exception) { }
    }

    private Usuario user;
    private ResourceBundle proper;
}