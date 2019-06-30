package organica.com.eje.ges.contraloria;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class EmpresaUnidad extends MyHttpServlet
{

    public EmpresaUnidad()
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
        Connection conexion = connMgr.getConnection("portal");
        if(conexion != null)
        {
            if(Usuario.rescatarUsuario(req).esValido())
                CargaPagina(req, resp, conexion);
            else
                devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        } else
        {
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        }
        connMgr.freeConnection("portal", conexion);
    }

    public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = "";
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        Consulta combo = new Consulta(Conexion);
        Consulta unidades = new Consulta(Conexion);
        consulta = "SELECT empresa AS id,descrip AS empresa FROM eje_ges_empresa";
        combo.exec(consulta);
        SimpleHash simplehash1;
        for(; combo.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("id", combo.getString("id"));
            simplehash1.put("desc", combo.getString("empresa"));
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc AS unidad, unid_empresa AS empresa,unid_id AS id FROM eje_ges_unidades WHERE (unid_empresa = '")).append(combo.getString("id")).append("') and vigente='S' ").append("order by unidad")));
            unidades.exec(consulta);
            SimpleList datos = new SimpleList();
            SimpleHash simplehash2;
            for(; unidades.next(); datos.add(simplehash2))
            {
                simplehash2 = new SimpleHash();
                simplehash2.put("id_u", unidades.getString("id"));
                simplehash2.put("desc_u", unidades.getString("unidad"));
            }

            simplehash1.put("datos", datos);
        }

        modelRoot.put("detalle", simplelist);
        combo.close();
        unidades.close();
        super.retTemplate(resp,"Contraloria/Busca_UEmpresa.html",modelRoot);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Contraloria/mensaje.htm",modelRoot);
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