package portal.com.eje.serhumano.search;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.search:
//            Welcome

public class S_Welcome extends MyHttpServlet
{

    public S_Welcome()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection conexion = super.connMgr.getConnection("portal");
        SimpleHash modelRoot = new SimpleHash();
        user = SessionMgr.rescatarUsuario(req);
        Date fecha_actual = new Date();
        SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
        SimpleDateFormat agnoFormat = new SimpleDateFormat("yyyy");
        String mes = mesFormat.format(fecha_actual);
        String agno = agnoFormat.format(fecha_actual);
        int mes_int = Integer.parseInt(mes);
        int agno_int = Integer.parseInt(agno);
        if(mes_int == 1)
        {
            mes_int = 12;
            agno_int--;
        } else
        {
            mes_int--;
        }
        mes = String.valueOf(mes_int);
        if(mes.length() == 1)
            mes = "0" + mes;
        if(conexion != null)
        {
        	insTracking(req, "Bienvenido".intern(), null);
            if(!user.esValido())
            {
                super.mensaje.devolverPaginaSinSesion(resp, "Nuevos Colaboradores", "Tiempo de Sesi\363n expirado...");
            } else
            {
                Consulta consul = null;
                Welcome wel = new Welcome(conexion);
                consul = wel.ConsulWelcome();
                modelRoot.put("varios", consul.getSimpleList());
                consul.close();
            }
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "problemas T\351nicos", "Error en la Conexi\363n.");
        }
        try
        {
        	super.retTemplate(resp,"bienvenida/bienvenida.htm",modelRoot);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        super.connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("\n**** Fin doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    private Usuario user;
}