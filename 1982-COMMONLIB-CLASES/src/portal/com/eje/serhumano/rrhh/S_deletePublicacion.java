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
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.rrhh:
//            publicador_Manager

public class S_deletePublicacion extends MyHttpServlet
{

    public S_deletePublicacion()
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
            MuestraDatos(req, resp, Conexion);
            insTracking(req, "Borrar Publicación".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        String alert = "Publicaci\363n registrada exit\363samente.";
        Validar valida = new Validar();
        if(user.esValido())
        {
            String id_publi = req.getParameter("id");
            publicador_Manager publica = new publicador_Manager(Conexion);
            if(!publica.delPublicacion(Double.parseDouble(id_publi)))
            {
                modelRoot.put("getError", publica.getError());
                getServletContext().log("Error de SQL".concat(String.valueOf(String.valueOf(getClass().getName()))), publica.getExcepcion());
            }
            modelRoot.put("mensaje", alert);
            SimpleHash simplehash1;
            Consulta info;
            for(info = publica.getPublicaciones(); info.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", info.getString("id_publi"));
                simplehash1.put("titulo", info.getString("titulo"));
                simplehash1.put("contenido", valida.validarDato(info.getString("contenido")));
                simplehash1.put("url", valida.validarDato(info.getString("url")));
                simplehash1.put("fecha", valida.validarFecha(info.getValor("fec_actualiza")));
            }

            modelRoot.put("detalle", simplelist);
            info.close();
            super.retTemplate(resp,"publicador/lista_publicaciones.htm",modelRoot);
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Publicaciones", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private ResourceBundle proper;
}