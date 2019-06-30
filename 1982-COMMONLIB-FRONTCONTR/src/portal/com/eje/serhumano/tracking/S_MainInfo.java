package portal.com.eje.serhumano.tracking;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.FormatoFecha;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

public class S_MainInfo extends MyHttpServlet
{

    public S_MainInfo()
    {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Conexion != null)
        {
            Usuario user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
            {
                String accion = req.getParameter("accion");
                String tipo = req.getParameter("tipo");
                retPaginaUpdate(user, req, resp, Conexion, null, null, tipo);
            } else
            {
                mensaje.devolverPaginaMensage(resp, "", "Usuario no V\341lido.");
            }
        } else
        {
            mensaje.devolverPaginaMensage(resp, "", "Errores en la Conexion.");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void retPaginaMsg(HttpServletRequest req, HttpServletResponse resp, String titulo, String msg, boolean resulOk)
        throws IOException, ServletException
    {
        try
        {
            OutMessage.OutMessagePrint("******* Inicio *******" + getClass().getName());
            String pHtm = "usermgr/mensaje.htm";
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("GetParam", new GetParam(req));
            modelRoot.put("mensaje", msg);
            modelRoot.put("titulo", titulo);
            modelRoot.put("ok", resulOk);
            retTemplate(resp, pHtm, modelRoot);
            OutMessage.OutMessagePrint("******* Fin *******" + getClass().getName());
        }
        catch(Exception e)
        {
            getServletContext().log(" Error Metodo retPaginaMsg en : " + getClass().getName(), e);
        }
    }

    private void retPaginaUpdate(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection conexion, String msg, String pLogin, String tipo)
        throws IOException, ServletException
    {
        try
        {
            OutMessage.OutMessagePrint("******* Inicio *******" + getClass().getName());
            SimpleHash modelRoot = new SimpleHash();
            String pHtm = "track/mainInfo.htm";
            modelRoot.put("GetParam", new GetParam(req));
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("msg", msg);
            retTemplate(resp, pHtm, modelRoot);
            OutMessage.OutMessagePrint("******* Fin *******" + getClass().getName());
        }
        catch(Exception e)
        {
            getServletContext().log(" Error Metodo retPaginaUpdate; en : " + getClass().getName(), e);
        }
    }
}