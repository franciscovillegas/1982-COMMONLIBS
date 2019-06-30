package organica.com.eje.ges.contraloria;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Contraloria extends MyHttpServlet
{

    public Contraloria()
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
        String valor = req.getParameter("val1");
        OutMessage.OutMessagePrint("*valor: ".concat(String.valueOf(String.valueOf(valor))));
        if(conexion != null)
        {
            if(valor == null)
            {
                CargaPagina(req, resp, conexion);
            } else
            {
                OutMessage.OutMessagePrint("*******Actualizar....criterios***********");
                GeneraDatos(req, resp, conexion);
            }
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
        String rut = "";
        String modo = req.getParameter("modo");
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        if(user.esValido())
        {
            ControlAcceso control = new ControlAcceso(user);
            if(control.tienePermiso("df_contraloria_ver"))
            {
                String id_unidad = req.getParameter("unidad");
                String empresa = req.getParameter("empresa");
                String unidad = req.getParameter("descrip");
                String boton = req.getParameter("boton");
                OutMessage.OutMessagePrint("********boton= ".concat(String.valueOf(String.valueOf(boton))));
                if(boton == "1")
                    modelRoot.put("boton", "1");
                Consulta valores = new Consulta(Conexion);
                Consulta combo = new Consulta(Conexion);
                consulta = "SELECT niv_id AS id, niv_glosa AS glosa FROM eje_ges_contraloria_niveles order by id";
                combo.exec(consulta);
                SimpleHash simplehash1;
                for(; combo.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("id", combo.getString("id"));
                    simplehash1.put("desc", combo.getString("glosa"));
                }

                modelRoot.put("detalle", simplelist);
                combo.close();
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc, rut,nombre,fecha,comercial, operativo, empresa,unidad,id_c,id_o FROM vista_contraloria WHERE (unidad = '")).append(id_unidad).append("')").append("AND (empresa = '").append(empresa).append("')")));
                OutMessage.OutMessagePrint("consulta!!!: ".concat(String.valueOf(String.valueOf(consulta))));
                valores.exec(consulta);
                modelRoot.put("unidad", unidad);
                modelRoot.put("empresa", empresa);
                modelRoot.put("id_unidad", id_unidad);
                if(valores.next())
                {
                    modelRoot.put("valA", valores.getString("id_c"));
                    modelRoot.put("valB", valores.getString("id_o"));
                    Rut datosRut = new Rut(Conexion, valores.getString("rut"));
                    rut = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(valores.getString("rut")))))).append("-").append(datosRut.Digito_Ver)));
                    modelRoot.put("rutDig", rut);
                    modelRoot.put("modo", modo);
                    modelRoot.put("rut", valores.getString("rut"));
                    modelRoot.put("nom", valores.getString("nombre"));
                    modelRoot.put("fecha", Tools.RescataFecha(valores.getValor("fecha")));
                }
                valores.close();
                if(control.tienePermiso("df_contraloria_mant"))
                    modelRoot.put("mantener", "1");
            } else
            {
                modelRoot.put("permiso", "Usted no tiene permiso para ver esta informaci\363n...");
            }
            super.retTemplate(resp,"Contraloria/Criterios.html",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        OutMessage.OutMessagePrint("---->Generar detalle*********");
        String modo = req.getParameter("modo");
        GregorianCalendar Fecha = new GregorianCalendar();
        OutMessage.OutMessagePrint("modo= ".concat(String.valueOf(String.valueOf(modo))));
        String html = null;
        if(modo.equals("1"))
        	html = "Contraloria/mensaje2.html";
        else
        	html = "Contraloria/mensaje1.html";
        String consulta = null;
        SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        String unidad = req.getParameter("unidad");
        String empresa = req.getParameter("empresa");
        String descrip = req.getParameter("descrip");
        String valA = req.getParameter("val1");
        String valB = req.getParameter("val2");
        Consulta info = new Consulta(Conexion);
        Consulta control = new Consulta(Conexion);
        consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_contraloria WHERE (empresa = '")).append(empresa).append("') ").append("AND (unidad = '").append(unidad).append("') ")));
        OutMessage.OutMessagePrint("---Buscar registro: ".concat(String.valueOf(String.valueOf(consulta))));
        info.exec(consulta);
        valida.setFormatoFecha("MM/dd/yyyy");
        if(!info.next())
            consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_contraloria (empresa, unidad, rut, riesgo_c, riesgo_o, fecha)VALUES ('")).append(empresa).append("', '").append(unidad).append("',").append(user.getRutConsultado()).append(",'").append(valA).append("','").append(valB).append("',").append("CONVERT(DATETIME,'").append(valida.validarFecha(Fecha.getTime())).append("', 102))")));
        else
            consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_contraloria SET riesgo_c = '")).append(valA).append("', riesgo_o = '").append(valB).append("',").append("rut = ").append(user.getRutConsultado()).append(",fecha = CONVERT(DATETIME,'").append(valida.validarFecha(Fecha.getTime())).append("', 102) ").append("WHERE (empresa = '").append(empresa).append("') ").append("AND (unidad = '").append(unidad).append("') ")));
        OutMessage.OutMessagePrint("==>SQL: ".concat(String.valueOf(String.valueOf(consulta))));
        control.insert(consulta);
        control.close();
        info.close();
        modelRoot.put("unidad", unidad);
        modelRoot.put("empresa", empresa);
        modelRoot.put("descrip", descrip);
        super.retTemplate(resp,html,modelRoot);
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