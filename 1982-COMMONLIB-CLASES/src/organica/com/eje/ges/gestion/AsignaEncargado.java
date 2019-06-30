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
import freemarker.template.SimpleList;

public class AsignaEncargado extends MyHttpServlet
{

    public AsignaEncargado()
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
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT rut_encargado, nombre, mision FROM view_unidad_encargado WHERE (periodo = ")).append(peri.getPeriodo()).append(") AND (unid_id = '").append(unidad).append("') AND (unid_empresa = '").append(empresa).append("')")));
            OutMessage.OutMessagePrint("---->consulta****: ".concat(String.valueOf(String.valueOf(consulta))));
            info.exec(consulta);
            if(info.next())
            {
                rutResponsable = info.getString("rut_encargado");
                antiguo = rutResponsable;
                OutMessage.OutMessagePrint("---->Responsable: ".concat(String.valueOf(String.valueOf(rutResponsable))));
                modelRoot.put("rut_resp", rutResponsable);
                modelRoot.put("nombre", info.getString("nombre"));
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
            SimpleList simplelist = new SimpleList();
            Consulta info2 = new Consulta(Conexion);
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver, nombre, sueldo, unidad FROM eje_ges_trabajador WHERE unidad = '")).append(unidad).append("' AND (empresa = '").append(empresa).append("')")));
            OutMessage.OutMessagePrint("---->Responsable: ".concat(String.valueOf(String.valueOf(rutResponsable))));
            if(rutResponsable != "-1")
                consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consulta)))).append(" AND rut <> ").append(rutResponsable)));
            consulta = String.valueOf(String.valueOf(consulta)).concat(" ORDER BY sueldo DESC");
            OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
            info2.exec(consulta);
            SimpleHash simplehash1;
            for(; info2.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("nombre", info2.getString("nombre"));
                simplehash1.put("rut", info2.getString("rut"));
            }

            modelRoot.put("detalle", simplelist);
            info2.close();
            super.retTemplate(resp,"Gestion/AsignaEncargado/resultado.htm",modelRoot);
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
            String responsable = req.getParameter("rut_resp");
            String mision = req.getParameter("mision");
            String empresa = req.getParameter("empresa");
            String accion = req.getParameter("accion");
            if("M".equalsIgnoreCase(accion))
                consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_unidad_encargado SET mision = '")).append(mision).append("', ").append("rut_encargado = ").append(responsable).append(",").append("periodo= ").append(peri.getPeriodo()).append(" ").append("WHERE (unid_id = '").append(unidad).append("') ").append("and (rut_encargado=").append(antiguo).append(") ").append("and (unid_empresa='").append(empresa).append("')")));
            else
            if("A".equalsIgnoreCase(accion))
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_unidad_encargado  (unid_empresa, unid_id, periodo, rut_encargado, mision)VALUES ('")).append(empresa).append("', '").append(unidad).append("', ").append(peri.getPeriodo()).append(", ").append(responsable).append(", '").append(mision).append("')")));
            OutMessage.OutMessagePrint("---->Actualizar U/E: ".concat(String.valueOf(String.valueOf(consulta))));
            info.insert(consulta);
            consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_trabajador SET rut_supdirecto = (SELECT rut FROM eje_ges_trabajador WHERE (rut = ")).append(responsable).append(") AND (empresa = '").append(empresa).append("')), ").append("dig_supdirecto = (SELECT digito_ver FROM eje_ges_trabajador WHERE (rut = ").append(responsable).append(") AND (empresa = '").append(empresa).append("')) ").append(", nom_supdirecto = (SELECT nombre FROM eje_ges_trabajador WHERE (rut = ").append(responsable).append(") AND (empresa = '").append(empresa).append("')) ").append(", cargo_supdirecto = (SELECT cargo FROM eje_ges_trabajador WHERE (rut = ").append(responsable).append(") AND (empresa = '").append(empresa).append("')) ").append("WHERE (rut <> ").append(responsable).append(") AND (empresa = '").append(empresa).append("') AND ").append("(unidad = '").append(unidad).append("')")));
            OutMessage.OutMessagePrint("---->Actualizar Trab: ".concat(String.valueOf(String.valueOf(consulta))));
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