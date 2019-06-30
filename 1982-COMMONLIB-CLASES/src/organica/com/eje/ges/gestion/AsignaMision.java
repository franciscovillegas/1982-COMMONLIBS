package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class AsignaMision extends MyHttpServlet
{

    public AsignaMision()
    {
        antiguo = null;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Conexion != null)
            Mantiene(req, resp, Conexion);
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

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = null;
        String rutResponsable = "";
        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            Periodo peri = new Periodo(Conexion);
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT mision FROM view_unidad_encargado WHERE (periodo = ")).append(peri.getPeriodo()).append(") AND (unid_id = '").append(unidad).append("') AND (unid_empresa = '").append(empresa).append("')")));
            OutMessage.OutMessagePrint("---->consulta****: ".concat(String.valueOf(String.valueOf(consulta))));
            info.exec(consulta);
            if(info.next())
            {
                rutResponsable = info.getString("rut_encargado");
                antiguo = rutResponsable;
                OutMessage.OutMessagePrint("---->Responsable: ".concat(String.valueOf(String.valueOf(rutResponsable))));
                modelRoot.put("mision", info.getString("mision"));
                modelRoot.put("accion", "M");
            } else
            {
                rutResponsable = "-1";
                OutMessage.OutMessagePrint("---->Sin Responsable");
                modelRoot.put("accion", "A");
            }
            modelRoot.put("unidad", unidad);
            modelRoot.put("empresa", empresa);
            info.close();
            super.retTemplate(resp,"Gestion/AsignaEncargado/AsignaMision.htm",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void Mantiene(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = null;
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            Periodo peri = new Periodo(Conexion);
            String unidad = req.getParameter("unidad");
            String mision = req.getParameter("mision");
            String empresa = req.getParameter("empresa");
            String accion = req.getParameter("accion");
            if("M".equalsIgnoreCase(accion))
                consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET mision = '")).append(mision).append("', ").append("periodo= ").append(peri.getPeriodo()).append(" ").append("WHERE (unid_id = '").append(unidad).append("') ").append("and (periodo = ").append(peri.getPeriodo()).append(") ").append("and (unid_empresa='").append(empresa).append("')")));
            else
            if("A".equalsIgnoreCase(accion))
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_encargado  (unid_empresa, unid_id, periodo, mision)VALUES ('")).append(empresa).append("', '").append(unidad).append("', ").append(peri.getPeriodo()).append(", '").append(mision).append("')")));
            OutMessage.OutMessagePrint("---->Actualizar Mision: ".concat(String.valueOf(String.valueOf(consulta))));
            info.insert(consulta);
            info.close();
            GeneraDatos(req, resp, Conexion);
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
        super.retTemplate(resp,"Gestion/AsignaEncargado/mensaje.htm",modelRoot);
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
    private String antiguo;
}