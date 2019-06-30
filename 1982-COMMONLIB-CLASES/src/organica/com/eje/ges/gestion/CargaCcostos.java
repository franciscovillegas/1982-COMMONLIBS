package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class CargaCcostos extends MyHttpServlet
{

    public CargaCcostos()
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
        user = Usuario.rescatarUsuario(req);
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        String consulta = null;
        SimpleHash modelRoot = new SimpleHash();
        Consulta cco = new Consulta(Conexion);
        consulta = "select * from eje_ges_ccosto order by descrip";
        cco.exec(consulta);
        SimpleList simplelist = new SimpleList();
        SimpleHash simplehash1;
        for(; cco.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("cod", cco.getString("ccosto"));
            simplehash1.put("desc", cco.getString("descrip"));
        }

        modelRoot.put("ccos", simplelist);
        super.retTemplate(resp,"Gestion/InfoUsuario/RutPorCcosto.htm",modelRoot);
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
}