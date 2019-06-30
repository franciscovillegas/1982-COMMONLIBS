package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class UnidSinAsignar extends MyHttpServlet
{

    public UnidSinAsignar()
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
        if(Usuario.rescatarUsuario(req).esValido())
        {
            String consulta = null;
            String modo = null;
            SimpleHash modelRoot = new SimpleHash();
            Consulta cco = new Consulta(Conexion);
            modo = req.getParameter("mision");
            consulta = "SELECT eje_ges_unidades.unid_id as unid_id,eje_ges_unidades.unid_desc as unid_desc,eje_ges_unidad_encargado.rut_encargado FROM eje_ges_unidad_encargado RIGHT OUTER JOIN eje_ges_unidades ON eje_ges_unidad_encargado.unid_id = eje_ges_unidades.unid_id AND eje_ges_unidad_encargado.unid_empresa = eje_ges_unidades.unid_empresa and vigente='S'";
            if(modo != null)
                consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consulta)))).append("WHERE (eje_ges_unidad_encargado.rut_encargado IS NOT NULL) ").append("AND (eje_ges_unidad_encargado.mision IS NULL)")));
            else
                consulta = String.valueOf(String.valueOf(consulta)).concat("WHERE (eje_ges_unidad_encargado.rut_encargado IS NULL) ");
            consulta = String.valueOf(String.valueOf(consulta)).concat("order by unid_id");
            OutMessage.OutMessagePrint("UnidSinAsig --> ".concat(String.valueOf(String.valueOf(consulta))));
            cco.exec(consulta);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1 = new SimpleHash();
            for(; cco.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", cco.getString("unid_id"));
                simplehash1.put("desc", cco.getString("unid_desc").trim());
            }
            modelRoot.put("detalle", simplelist);
            super.retTemplate(resp,"Gestion/AsignaEncargado/asignar.htm",modelRoot);
            
        } else
        {
            mensaje.devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
        connMgr.freeConnection("portal", Conexion);
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
    private Mensaje mensaje;
}