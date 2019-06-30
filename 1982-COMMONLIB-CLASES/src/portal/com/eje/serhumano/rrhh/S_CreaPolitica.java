package portal.com.eje.serhumano.rrhh;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.rrhh:
//            publicador_Manager

public class S_CreaPolitica extends MyHttpServlet
{

    public S_CreaPolitica()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        Connection conexion = super.connMgr.getConnection("portal");
        if(conexion != null)
        {
            generaPag(req, resp, conexion);
            insTracking(req, "Creación Politica".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("\n**** Fin doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    private void generaPag(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        user = SessionMgr.rescatarUsuario(req);
        if(user.esValido())
        {
            OutMessage.OutMessagePrint("\n**** Inicio generaPag: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
            String nombre_imagen = "";
            String titulo = req.getParameter("titulo");
            String contenido = req.getParameter("contenido");
            String url = req.getParameter("url");
            System.err.println(String.valueOf(String.valueOf((new StringBuilder("--->Titulo: ")).append(titulo).append("\n--->contenido: ").append(contenido).append("\n--->url: ").append(url))));
            publicador_Manager publica = new publicador_Manager(Conexion);
            String alert = "Publicaci\363n registrada exit\363samente.";
            if(!publica.AddPublicacion(titulo, contenido, nombre_imagen, url))
            {
                modelRoot.put("getError", publica.getError());
                getServletContext().log("Error de SQL".concat(String.valueOf(String.valueOf(getClass().getName()))), publica.getExcepcion());
            }
            modelRoot.put("mensaje", alert);
            super.retTemplate(resp,"publicador/crear_politica.htm",modelRoot);
            OutMessage.OutMessagePrint("\n**** Fin generaPag: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Publicaciones", "Tiempo de Sesi\363n expirado...");
        }
    }

    private Usuario user;
}