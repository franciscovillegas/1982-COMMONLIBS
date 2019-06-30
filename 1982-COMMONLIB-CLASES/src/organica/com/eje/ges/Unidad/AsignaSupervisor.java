package organica.com.eje.ges.Unidad;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class AsignaSupervisor extends MyHttpServlet
{

    public AsignaSupervisor()
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
        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido())
        {
            String unidad = req.getParameter("unidad");
            modelRoot.put("cod_uni", unidad);
            super.retTemplate(resp,"Gestion/AsignaEncargado/AsignaSupervisor.htm",modelRoot);
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
            String ruts = req.getParameter("ruts");
            String rutsup = "";
            String digsup = "";
            String nomsup = "";
            String cargosup = "";
            if(req.getParameter("rut_super") != null)
                rutsup = req.getParameter("rut_super");
            if(req.getParameter("rut_dig") != null)
                digsup = req.getParameter("rut_dig");
            if(req.getParameter("nombre_sup") != null)
                nomsup = req.getParameter("nombre_sup");
            if(req.getParameter("rut_super") != null)
                cargosup = req.getParameter("cargo_sup");
            String Query = "";
            String lrut = "";
            Consulta R = new Consulta(Conexion);
            Consulta Buscar = new Consulta(Conexion);
            for(StringTokenizer st = new StringTokenizer(ruts, ","); st.hasMoreTokens(); Buscar.insert(consulta))
            {
                lrut = st.nextToken();
                Query = String.valueOf(String.valueOf((new StringBuilder("SELECT rut FROM eje_ges_supervisor WHERE (rut = ")).append(lrut).append(")")));
                R.exec(Query);
                if(R.next())
                    consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_supervisor SET  rut_supdirecto = ")).append(rutsup).append(", dig_supdirecto = '").append(digsup).append("',  ").append("    cargo_supdirecto = '").append(cargosup).append("', nom_supdirecto = '").append(nomsup).append("' ").append("WHERE (rut = ").append(lrut).append(")")));
                else
                    consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_supervisor     (rut, rut_supdirecto, dig_supdirecto, cargo_supdirecto,     nom_supdirecto) VALUES (")).append(lrut).append(", ").append(rutsup).append(", '").append(digsup).append("', '").append(cargosup).append("', '").append(nomsup).append("')")));
                OutMessage.OutMessagePrint(consulta);
            }

            R.close();
            Buscar.close();
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