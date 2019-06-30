package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class MuestraRutsCcosto extends MyHttpServlet
{

    public MuestraRutsCcosto()
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
        String ccosto = null;
        SimpleHash modelRoot = new SimpleHash();
        OutMessage.OutMessagePrint("Entro a Mostrar datos!!!!");
        Consulta trabajadores = new Consulta(Conexion);
        ccosto = req.getParameter("ccosto");
        consulta = "SELECT eje_ges_ccosto.descrip as ccosto, eje_ges_trabajadores.rut as elrut,eje_ges_trabajadores.digito_ver as digito, eje_ges_trabajadores.nombre,eje_ges_trabajadores.fecha_ingreso,eje_ges_cargos.descrip AS cargo FROM eje_ges_ccosto RIGHT OUTER JOIN eje_ges_trabajador ON eje_ges_ccosto.ccosto = eje_ges_trabajadores.ccosto LEFT OUTER JOIN eje_ges_cargos ON eje_ges_trabajadores.cargo = eje_ges_cargos.cargo eje_ges_trabajadores.empresa = eje_ges_cargos.empresa where eje_ges_trabajadores.ccosto = ".concat(String.valueOf(String.valueOf(ccosto)));
        OutMessage.OutMessagePrint(consulta);
        trabajadores.exec(consulta);
        SimpleList simplelist = new SimpleList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleHash simplehash1;
        for(; trabajadores.next(); simplelist.add(simplehash1))
        {
            modelRoot.put("ccosto", trabajadores.getString("ccosto"));
            simplehash1 = new SimpleHash();
            simplehash1.put("rut", trabajadores.getString("elrut"));
            simplehash1.put("digito", trabajadores.getString("digito"));
            simplehash1.put("nombre", trabajadores.getString("nombre"));
            simplehash1.put("fec_ingreso", dateFormat.format(trabajadores.getValor("fecha_ingreso")));
            simplehash1.put("cargo", trabajadores.getString("cargo"));
        }

        modelRoot.put("ccos", simplelist);
        trabajadores.close();
        super.retTemplate(resp,"Gestion/InfoUsuario/RutsCcosto.htm",modelRoot);
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