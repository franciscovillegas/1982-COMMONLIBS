package portal.com.eje.serhumano.rrhh;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.rrhh:
//            publicador_Manager

public class S_MantPolitica extends MyHttpServlet
{

    public S_MantPolitica()
    {
        nueva_raiz = null;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        Connection conexion = super.connMgr.getConnection("portal");
        if(conexion != null)
        {
            if("1".equals(req.getParameter("modo")))
                editaPubli(req, resp, conexion);
            else
                mantePubli(req, resp, conexion);
            insTracking(req, "Mant. Politica".intern(), null);
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

    private void mantePubli(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio mantePubli: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        SimpleHash modelRoot = new SimpleHash();
        String nombre_imagen = "";
        String id_publi = req.getParameter("id");
        String titulo = req.getParameter("titulo");
        String contenido = req.getParameter("contenido");
        String url = req.getParameter("url");
        Validar valida = new Validar();
        if(contenido != null && !"".equals(contenido))
            contenido = req.getParameter("contenido");
        System.err.println(String.valueOf(String.valueOf((new StringBuilder("--->Titulo: ")).append(titulo).append("\n--->contenido: ").append(contenido).append("\n--->url: ").append(url))));
        publicador_Manager publica = new publicador_Manager(Conexion);
        String alert = "Publicaci\363n registrada exit\363samente.";
        if(!publica.updatePublicacion(Double.parseDouble(id_publi), titulo, contenido, nombre_imagen, url))
        {
            modelRoot.put("getError", publica.getError());
            getServletContext().log("Error de SQL".concat(String.valueOf(String.valueOf(getClass().getName()))), publica.getExcepcion());
        }
        modelRoot.put("mensaje", alert);
        Consulta info = publica.getDataPubli(id_publi);
        if(info.next())
        {
            modelRoot.put("id", info.getString("id_publi"));
            modelRoot.put("titulo", info.getString("titulo"));
            modelRoot.put("contenido", valida.validarDato(info.getString("contenido")));
            modelRoot.put("url", valida.validarDato(info.getString("url")));
            modelRoot.put("fecha", valida.validarFecha(info.getValor("fec_actualiza")));
        }
        super.retTemplate(resp,"publicador/mant_politica.htm",modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin mantePubli: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void editaPubli(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        user = SessionMgr.rescatarUsuario(req);
        if(user.esValido())
        {
            OutMessage.OutMessagePrint("\n**** Inicio editaPubli: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
            String id_publi = req.getParameter("id");
            publicador_Manager publica = new publicador_Manager(Conexion);
            Consulta info = publica.getDataPubli(id_publi);
            if(info.next())
            {
                modelRoot.put("id", info.getString("id_publi"));
                modelRoot.put("titulo", info.getString("titulo"));
                modelRoot.put("contenido", valida.validarDato(info.getString("contenido")));
                modelRoot.put("url", valida.validarDato(info.getString("url")));
                modelRoot.put("fecha", valida.validarFecha(info.getValor("fec_actualiza")));
            }
            super.retTemplate(resp,"publicador/mant_politica.htm",modelRoot);
            OutMessage.OutMessagePrint("\n**** Fin editaPubli: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Publicaciones", "Tiempo de Sesi\363n expirado...");
        }
    }

    private String nueva_raiz;
    private static int TAMANO = 0xc3500;
    private ResourceBundle proper;
    private Usuario user;

}