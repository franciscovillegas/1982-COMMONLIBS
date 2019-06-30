package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.arbol.Nodo;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class GeneraArbol extends MyHttpServlet
{

    public GeneraArbol()
    {
        corre = 1;
        vecPadre = new Vector();
        vecHijo = new Vector();
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
        Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        SimpleList simplelist = new SimpleList();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar Fecha = new GregorianCalendar();
        java.util.Date date2 = Fecha.getTime();
        String strFechaHoy = simpledateformat.format(date2);
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        resp.setHeader("Expires", "0");
        resp.setHeader("Pragma", "no-cache");
        Template template = getTemplate("Gestion/InfoUsuario/Vacaciones.htm");
        String consulta = null;
        String rut = null;
        String nombre = null;
        String idPadre = null;
        String descPadre = null;
        int nivelPadre = 0;
        String id_nodo = null;
        int cont = 0;
        int corr = 0;
        SimpleHash modelRoot = new SimpleHash();
        OutMessage.OutMessagePrint("Entro a Generar Arbol!!!!");
        Consulta padres = new Consulta(Conexion);
        Validar valida = new Validar();
        consulta = "SELECT eje_ges_jerarquia.nodo_id AS padre,eje_ges_jerarquia.nodo_padre as id,eje_ges_jerarquia.nodo_nivel AS nivel,eje_ges_estamentos.esta_desc AS descrip FROM eje_ges_jerarquia INNER JOIN eje_ges_estamentos ON eje_ges_jerarquia.nodo_id = eje_ges_estamentos.esta_id WHERE (eje_ges_jerarquia.nodo_nivel = 1)";
        padres.exec(consulta);
        padres.next();
        OutMessage.OutMessagePrint("Padre:".concat(String.valueOf(String.valueOf(padres.getString("padre")))));
        vecHijo = CreaHijo(padres.getString("id"), padres.getString("padre"), padres.getInt("nivel"), Conexion);
        idPadre = padres.getString("padre");
        descPadre = padres.getString("descrip");
        nivelPadre = padres.getInt("nivel");
        Nodo padre = new Nodo("0", idPadre, descPadre, nivelPadre);
        padre.Agrega_Hijo(vecHijo);
        vecPadre.add(padre);
        padres.close();
        out.flush();
        out.close();
        OutMessage.OutMessagePrint("Fin de JeneraArbol");
        connMgr.freeConnection("portal", Conexion);
    }

    private Vector CreaHijo(String Xid, String padre, int niv, Connection Conexion)
    {
        String consu = null;
        String id = null;
        String desc = null;
        int nivel = 0;
        Consulta actualiza = new Consulta(Conexion);
        nivel = niv + 1;
        actualiza.close();
        corre++;
        Vector ElHijo = new Vector();
        Consulta hijos = new Consulta(Conexion);
        consu = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_jerarquia INNER JOIN eje_ges_estamentos ON eje_ges_jerarquia.nodo_id = eje_ges_estamentos.esta_id WHERE eje_ges_jerarquia.nodo_padre ='")).append(padre).append("'")));
        hijos.exec(consu);
        Nodo hijo;
        for(; hijos.next(); ElHijo.addElement(hijo))
        {
            OutMessage.OutMessagePrint("--->Tiene hijos*********");
            id = hijos.getString("nodo_id");
            desc = hijos.getString("esta_desc");
            nivel = niv + 1;
            hijo = new Nodo(padre, id, desc, nivel);
            hijo.Agrega_Hijo(CreaHijo(hijos.getString("nodo_padre"), hijos.getString("nodo_id"), nivel, Conexion));
        }

        hijos.close();
        return ElHijo;
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
    private int corre;
    private Vector vecPadre;
    private Vector vecHijo;
}