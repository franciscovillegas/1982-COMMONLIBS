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
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.efemerides:
//            Efemerides_Manager

public class S_Nacimientos extends MyHttpServlet
{

    public S_Nacimientos()
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
        if(Conexion != null)
        {
            user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
                MuestraDatos(user, req, resp, Conexion);
            else
                super.mensaje.devolverPaginaSinSesion(resp, "E-Mail", "Tiempo de Sesi\363n expirado...");
            insTracking(req, "Nacimiento".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void MuestraDatos(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        GregorianCalendar Fecha = new GregorianCalendar();
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        proper = ResourceBundle.getBundle("db");
        String dominio = proper.getString("portal.domain");
        Consulta consul = null;
        SimpleHash simplehash1;
        for(consul = (new Efemerides_Manager(Conexion)).getNacimientos(mes, ano); consul.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("rut", consul.getString("rut"));
            simplehash1.put("ruthijo", consul.getString("rut"));
            simplehash1.put("nombre", consul.getString("nombre"));
            simplehash1.put("rutcarga", consul.getString("rut_carga"));
            simplehash1.put("fec", consul.getString("fecha_nacim"));
            simplehash1.put("emple", consul.getString("empleado"));
            if(!"".equals(consul.getString("e_mail")) && consul.getString("e_mail") != null)
                simplehash1.put("mail", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consul.getString("e_mail"))))).append("@").append(dominio))));
        }

        modelRoot.put("detalle", simplelist);
        consul.close();
        super.retTemplate(resp,"efemerides/nacimiemtos.htm",modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private ResourceBundle proper;
}