package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.UserManager;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class CambioClave extends MyHttpServlet
{

    public CambioClave()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Conexion != null)
            Actualiza(req, resp, Conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Conexion != null)
            GeneraDatos(req, resp, Conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        connMgr.freeConnection("portal", Conexion);
    }

    public void Actualiza(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            String nueva = req.getParameter("nClave");
            String rut = req.getParameter("x1");
            UserManager usermanager = new UserManager(Conexion);
            usermanager.cambiarClave(rut, nueva, 'N');
            usermanager.close();
            modelRoot.put("nueva", nueva);
            modelRoot.put("rut", rut);
            if("1".equals(req.getParameter("inicio")))
                modelRoot.put("ok", "1");
            if("1".equals(req.getParameter("expi")))
            {
                OutMessage.OutMessagePrint("exp : ".concat(String.valueOf(String.valueOf(req.getParameter("expi")))));
                modelRoot.put("ex", "1");
            }
            info.close();
            super.retTemplate(resp,"Gestion/CambioClave/mensaje.htm",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = null;
        String rut = "";
        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            if("1".equals(req.getParameter("inicio")))
                modelRoot.put("mensaje", "0");
            rut = user.getRutConsultado();
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_usuario,login_usuario,password_usuario FROM vista_usuario WHERE (rut_usuario = ")).append(rut).append(")")));
            OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
            info.exec(consulta);
            if(info.next())
            {
                modelRoot.put("x1", info.getString("rut_usuario").trim());
                modelRoot.put("x2", info.getString("password_usuario").trim());
            } else
            {
                modelRoot.put("error", "0");
                OutMessage.OutMessagePrint("---->Rut no encontrado!!!!");
            }
            info.close();
            if("1".equals(req.getParameter("expi")))
                modelRoot.put("ex", "1");
            super.retTemplate(resp,"Gestion/CambioClave/cambio.htm",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/CambioClave/mensaje.htm",modelRoot);
    }

    private void devolverPaginaError(HttpServletResponse resp, String msg)
    {
        try
        {
            PrintWriter printwriter = resp.getWriter();
            resp.setContentType("text/html");
            printwriter.println("<html>");
            printwriter.println("<head>");
            printwriter.println("<title>Valores recogidos en el formulario</title>");
            printwriter.println("</head>");
            printwriter.println("<body>");
            printwriter.println(msg);
            printwriter.println("</body>");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
        }
        catch(IOException e)
        {
            OutMessage.OutMessagePrint("Error al botar la pagina");
        }
    }

    private Usuario user;
}