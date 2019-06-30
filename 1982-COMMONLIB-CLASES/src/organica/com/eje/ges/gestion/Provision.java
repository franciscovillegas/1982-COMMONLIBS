package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class Provision extends MyHttpServlet
{

    public Provision()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutConsultado();
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Provisi\363n de Vacaciones", "Tiempo de Sesi\363n expirado...");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            Consulta vacaciones = new Consulta(Conexion);
            Consulta info = new Consulta(Conexion);
            String sql = "";
            OutMessage.OutMessagePrint("consulta: ".concat(String.valueOf(String.valueOf(sql))));
            info.exec(sql);
            info.next();
            info.close();
            modelRoot.put("provision", "");
            vacaciones.close();
            super.retTemplate(resp,"Gestion/InfoUsuario/provision.htm",modelRoot);
            connMgr.freeConnection("portal", Conexion);
        }
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
    private Tools tool;
    private Mensaje mensaje;
}