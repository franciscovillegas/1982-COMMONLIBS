package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class Indicadores extends MyHttpServlet
{

    public Indicadores()
    {
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
        ControlAcceso control = new ControlAcceso(user);
        String html = null;
        String consulta = null;
        String rutaResul = "Gestion/InfoUsuario/Indicadores/";
        String rutaMain = "Gestion/InfoUsuario/Indicadores/";
        String rutaResultado = "Gestion/InfoUsuario/Indicadores/";
        int cualIndicador = Integer.parseInt(req.getParameter("i"));
        switch(cualIndicador)
        {
        case 1: // '\001'
            rutaResultado = "AmericaXXI/resultado.htm";
            html = "Gestion/InfoUsuario/Indicadores/".concat(String.valueOf(String.valueOf(rutaResultado)));
            break;

        case 2: // '\002'
            rutaResultado = String.valueOf(String.valueOf(rutaResultado)).concat("BAE");
            html = "Gestion/InfoUsuario/Indicadores/BAE/Jerarquia/JerarquiaOrg.htm";
            rutaMain = String.valueOf(String.valueOf(rutaResultado)).concat("/blanco.htm");
            rutaResultado = String.valueOf(String.valueOf(rutaResultado)).concat("/resultado.htm");
            rutaResul = rutaResultado;
            break;

        case 3: // '\003'
            rutaResultado = String.valueOf(String.valueOf(rutaResultado)).concat("GestionDataFolder");
            html = "Gestion/InfoUsuario/Indicadores/GestionDataFolder/Jerarquia/JerarquiaOrg.htm";
            rutaMain = String.valueOf(String.valueOf(rutaResultado)).concat("/blanco.htm");
            rutaResultado = String.valueOf(String.valueOf(rutaResultado)).concat("/resultado.htm");
            rutaResul = rutaResultado;
            break;
        }
        String sentencia = null;
        String imagen = null;
        String idNodo = null;
        String descNodo = null;
        String Spadre = null;
        String elemento = "";
        boolean existe = false;
        int nivNodo = 0;
        int nivRaiz = 0;
        int hijos = 0;
        int niv = 0;
        int maxNivel = 0;
        int resto = 0;
        int veces = 0;
        int cont = 1;
        int g = 0;
        SimpleHash modelRoot = new SimpleHash();
        Vector estan = new Vector();
        Consulta nodos = new Consulta(Conexion);
        Consulta maximo = new Consulta(Conexion);
        consulta = "select max(nodo_nivel) maximo from eje_ges_jerarquia";
        maximo.exec(consulta);
        maximo.next();
        maxNivel = maximo.getInt("maximo");
        maximo.close();
        consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT DISTINCT nodo_id, nodo_padre, nodo_corr, nodo_hijos, nodo_imagen, nodo_nivel, unid_desc FROM view_subrama WHERE (compania = '")).append(user.getEmpresa()).append("') ")));
        if(!control.tienePermiso("df_jer_comp"))
            consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consulta)))).append(" AND (unidad = '").append(user.getUnidad()).append("') ")));
        consulta = String.valueOf(String.valueOf(consulta)).concat("ORDER BY nodo_corr");
        OutMessage.OutMessagePrint(consulta);
        nodos.exec(consulta);
        if(nodos.next())
        {
            descNodo = nodos.getString("unid_desc");
            nivNodo = nodos.getInt("nodo_nivel");
            nivRaiz = nivNodo;
            hijos = nodos.getInt("nodo_hijos");
            idNodo = nodos.getString("nodo_id");
            imagen = nodos.getString("nodo_imagen");
            sentencia = String.valueOf(String.valueOf((new StringBuilder("foldersTree=folderNode('")).append(descNodo).append("',").append(nivNodo).append(",'").append(idNodo).append("','").append(imagen).append("',").append(hijos).append(")\n")));
            while(nodos.next()) 
            {
                descNodo = nodos.getString("unid_desc");
                nivNodo = nodos.getInt("nodo_nivel");
                hijos = nodos.getInt("nodo_hijos");
                idNodo = nodos.getString("nodo_id");
                imagen = nodos.getString("nodo_imagen");
                Spadre = nodos.getString("nodo_padre");
                niv = RescataNivelPadre(Spadre, Conexion);
                resto = nivNodo - niv;
                veces = Revisa(idNodo, user, Conexion);
                if(veces == 1)
                {
                    if(nivNodo == maxNivel)
                        sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append("appendChild(a").append(Spadre).append(",folderNode('").append(descNodo).append("',").append(resto).append(",'").append(idNodo).append("','").append(imagen).append("',").append(hijos).append("))\n")));
                    else
                    if(niv == nivRaiz)
                        sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append("a").append(idNodo).append("= appendChild(foldersTree,folderNode('").append(descNodo).append("',").append(resto).append(",'").append(idNodo).append("','").append(imagen).append("',").append(hijos).append("))\n")));
                    else
                        sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append("a").append(idNodo).append("= appendChild(a").append(Spadre).append(",folderNode('").append(descNodo).append("',").append(resto).append(",'").append(idNodo).append("','").append(imagen).append("',").append(hijos).append("))\n")));
                } else
                {
                    if(cont == 1)
                    {
                        sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append("UNAS=").append("appendChild(foldersTree,folderNode('UNIDADES/REVISAR',1,'UNAS','").append(imagen).append("',").append(hijos).append("))\n")));
                        cont++;
                        OutMessage.OutMessagePrint("====> Unidades con problemas(mas de un padre)<======");
                    }
                    g = 0;
                    do
                    {
                        if(g >= estan.size())
                            break;
                        elemento = (String)estan.elementAt(g);
                        if(elemento.equals(idNodo))
                        {
                            existe = true;
                            break;
                        }
                        g++;
                    } while(true);
                    if(!existe)
                    {
                        sentencia = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sentencia)))).append("a").append(idNodo).append("= appendChild(UNAS,folderNode('").append(descNodo).append("',").append(resto).append(",'").append(idNodo).append("','").append(imagen).append("',").append(hijos).append("))\n")));
                        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("==> ")).append(idNodo).append("  ").append(descNodo))));
                        estan.addElement(idNodo);
                    }
                }
                existe = false;
                veces = 0;
            }
        } else
        {
            OutMessage.OutMessagePrint("No hay datos en tabla eje_ges_jerarquia");
        }
        if(cualIndicador != 1)
        {
            modelRoot.put("sentencia", sentencia);
            modelRoot.put("ruta", rutaMain);
            modelRoot.put("rutaResult", rutaResul);
            modelRoot.put("cual", String.valueOf(cualIndicador));
        }
        nodos.close();
        super.retTemplate(resp,html,modelRoot);
        connMgr.freeConnection("portal", Conexion);
    }

    private int Revisa(String id, Usuario u, Connection Conexion)
    {
        ControlAcceso control = new ControlAcceso(u);
        String sql = null;
        int x = 0;
        Consulta cuantas = new Consulta(Conexion);
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT COUNT(DISTINCT nodo_padre) AS num FROM view_subrama WHERE (nodo_id = '")).append(id).append("') AND (compania = '").append(u.getEmpresa()).append("') ")));
        if(!control.tienePermiso("df_jer_comp"))
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" AND (unidad = '").append(u.getUnidad()).append("') ")));
        cuantas.exec(sql);
        cuantas.next();
        x = cuantas.getInt("num");
        cuantas.close();
        return x;
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
    private Vector vecPadre;
    private Vector vecHijo;
}