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
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class BuscaUnidades extends MyHttpServlet
{

    public BuscaUnidades()
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
            GeneraDatos(req, resp, conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        connMgr.freeConnection("portal", conexion);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            String parte1 = null;
            String parte2 = " ";
            String todo = " ";
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            String mensaje = "";
            int largo = 0;
            int x = 0;
            String modo = req.getParameter("modo");
            Consulta info = new Consulta(Conexion);
            String unidad = req.getParameter("unidad");
            String emp = req.getParameter("empresa");
            if(modo == null)
            {
                String riesgoC[] = req.getParameterValues("nivelC");
                String todosC = req.getParameter("allc");
                String riesgoO[] = req.getParameterValues("nivelO");
                String todosO = req.getParameter("allo");
                String conec = "";
                int si = 0;
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("todosC: ")).append(todosC).append("\n").append("todosO: ").append(todosO))));
                parte1 = "SELECT unid_desc, rut, fecha, comercial, operativo,empresa,unidad, id_c, id_o FROM vista_contraloria WHERE 1=1 ";
                if(todosC == null)
                {
                    if(riesgoC == null)
                        largo = 0;
                    else
                        largo = riesgoC.length;
                    for(x = 0; x < largo; x++)
                    {
                        if(x == 0)
                            conec = " AND";
                        else
                            conec = " OR";
                        parte2 = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(parte2)))).append(conec).append(" (id_c = '").append(riesgoC[x]).append("') ")));
                    }

                    si = 1;
                }
                if(todosO == null)
                {
                    if(riesgoO == null)
                        largo = 0;
                    else
                        largo = riesgoO.length;
                    for(x = 0; x < largo; x++)
                    {
                        if(x == 0 && si == 1)
                            conec = " AND";
                        else
                            conec = " OR";
                        parte2 = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(parte2)))).append(conec).append(" (id_o = '").append(riesgoO[x]).append("') ")));
                    }

                }
                todo = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(parte1)))).append(parte2).append(" ORDER BY unidad")));
                mensaje = "No se encontraron Unidades que cumplan con este criterio";
            } else
            {
                todo = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc, rut, fecha, comercial, operativo, empresa,unidad, id_c, id_o FROM vista_contraloria WHERE (unidad = '")).append(unidad).append("') and (empresa='").append(emp).append("')")));
                OutMessage.OutMessagePrint("Busca Valores para; ".concat(String.valueOf(String.valueOf(todo))));
                mensaje = "Esta Unidad no registra datos de Contralor\355a.";
            }
            OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(todo))));
            info.exec(todo);
            if(info.next())
            {
                SimpleHash simplehash1 = new SimpleHash();
                simplehash1.put("emp", emp);
                simplehash1.put("id", info.getString("unidad"));
                simplehash1.put("desc", info.getString("unid_desc"));
                simplehash1.put("va", info.getString("comercial"));
                simplehash1.put("vb", info.getString("operativo"));
                simplelist.add(simplehash1);
                for(; info.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("emp", emp);
                    simplehash1.put("id", info.getString("unidad"));
                    simplehash1.put("desc", info.getString("unid_desc"));
                    simplehash1.put("va", info.getString("comercial"));
                    simplehash1.put("vb", info.getString("operativo"));
                }

            } else
            {
                SimpleHash simplehash1 = new SimpleHash();
                simplehash1.put("emp", emp);
                simplehash1.put("id", unidad);
                simplehash1.put("desc", RescataDescU(unidad, emp, Conexion));
                simplehash1.put("va", "N/R");
                simplehash1.put("vb", "N/R");
                simplelist.add(simplehash1);
            }
            modelRoot.put("detalle", simplelist);
            modelRoot.put("mensaje", mensaje);
            info.close();
            super.retTemplate(resp,"Contraloria/resultado.htm",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    private String RescataDescU(String id, String empresa, Connection Conexion)
        throws IOException, ServletException
    {
        Consulta info = new Consulta(Conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc, unid_id FROM eje_ges_unidades WHERE (unid_id = '")).append(id).append("') and (unid_empresa='").append(empresa).append("')")));
        OutMessage.OutMessagePrint("--------->desc unidad: ".concat(String.valueOf(String.valueOf(sql))));
        info.exec(sql);
        if(info.next())
            return info.getString("unid_desc");
        else
            return "S/I";
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
    private Mensaje mensaje;
}