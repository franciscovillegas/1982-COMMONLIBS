package organica.arbol.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.web.datos.DBConnectionManager;
import freemarker.template.SimpleHash;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class Jerarquia extends MyHttpServlet
{

    public Jerarquia()
    {
        vecPadre = new Vector();
        vecHijo = new Vector();
    }


    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        user = Usuario.rescatarUsuario(req);
        String html = null;

        int cual = Integer.parseInt(req.getParameter("cual"));
        switch(cual)
        {
        case 1: // '\001'
            html = "Gestion/InfoUsuario/Jerarquia/JerarquiaOrg.htm";
            break;

        case 2: // '\002'
            html = "Gestion/InfoUsuario/BusquedaGrafica/JerarquiaOrg.htm";
            break;

        case 3: // '\003'
            html = "Gestion/AsignaEncargado/JerarquiaOrg.htm";
            break;

        case 4: // '\004'
            html = "Gestion/Usuarios/JerarquiaOrg.htm";
            break;

        case 5: // '\005'
            html = "Contraloria/Jerarquia/JerarquiaOrg.htm";
            break;

        case 6: // '\006'
            html = "Gestion/Jerarquia.htm";
            break;
        }
        OutMessage.OutMessagePrint("---->ruta html;".concat(String.valueOf(String.valueOf(html))));
        SimpleHash modelRoot = new SimpleHash();
        
        super.retTemplate(resp,html,modelRoot);
    }

    private int RescataNivelPadre(String padre, Connection Conexion)
    {
        String consulta = null;
        int niv = 0;
        Consulta nivel = new Consulta(Conexion);
        consulta = String.valueOf(String.valueOf((new StringBuilder("select nodo_nivel from eje_ges_jerarquia where nodo_id='")).append(padre).append("'")));
        nivel.exec(consulta);
        if(nivel.next())
            niv = nivel.getInt("nodo_nivel");
        nivel.close();
        return niv;
    }

    private int Revisa(String id, Usuario u, boolean puedeVer, Connection Conexion)
    {
        String sql = null;
        int x = 0;
        Consulta cuantas = new Consulta(Conexion);
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT COUNT(DISTINCT nodo_padre) AS num FROM view_subrama WHERE (nodo_id = '")).append(id).append("') AND (compania = '").append(u.getEmpresa()).append("') ")));
        if(!puedeVer)
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" AND (unidad = '").append(u.getUnidad()).append("') ")));
        cuantas.exec(sql);
        cuantas.next();
        x = cuantas.getInt("num");
        cuantas.close();
        return x;
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

    private DBConnectionManager connMgr;
    private Usuario user;
    private Vector vecPadre;
    private Vector vecHijo;
}