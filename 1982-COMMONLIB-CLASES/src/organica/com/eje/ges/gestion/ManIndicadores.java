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
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class ManIndicadores extends MyHttpServlet
{

    public ManIndicadores()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        String modo = req.getParameter("modo");
        if(Conexion != null)
            Actualiza(req, resp, Conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Conexion != null)
            EditaDatos(req, resp, Conexion);
        else
            devolverPaginaError(resp, "Error de conexi\363n a la BD");
        connMgr.freeConnection("portal", Conexion);
    }

    public void EditaDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String consulta = null;
        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            Validar valida = new Validar();
            String item = req.getParameter("item");
            String desde = req.getParameter("desde");
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo_desde, periodo_hasta, grupo, grupo_desc, item,desc_ind, formula, desc_item, indicador, tabla, tramo_inf1,tramo_inf2, tramo_medio2, tramo_sup1, tramo_sup2,tramo_medio1, imag_inf, imag_medio, imag_sup, glosa,desc_tramo_inf, desc_tramo_med, desc_tramo_sup,nombre FROM view_def_indicadores WHERE (periodo_desde = ")).append(desde).append(") AND (item = '").append(item).append("') ").append("order by item")));
            OutMessage.OutMessagePrint("*****---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
            info.exec(consulta);
            if(info.next())
            {
                modelRoot.put("item", info.getString("item"));
                modelRoot.put("desde", info.getString("periodo_desde"));
                modelRoot.put("grupo", info.getString("grupo"));
                modelRoot.put("nom", info.getString("nombre"));
                modelRoot.put("glosa", valida.validarDato(info.getString("glosa"), "N/R"));
                modelRoot.put("politica", valida.validarDato(info.getString("desc_ind"), "N/R"));
                modelRoot.put("formula", valida.validarDato(info.getString("formula")));
                modelRoot.put("descf", valida.validarDato(info.getString("desc_tramo_inf"), "N/R"));
                modelRoot.put("inf1", valida.validarDato(info.getString("tramo_inf1"), "0"));
                modelRoot.put("inf2", valida.validarDato(info.getString("tramo_inf2"), "0"));
                modelRoot.put("descm", valida.validarDato(info.getString("desc_tramo_med"), "N/R"));
                modelRoot.put("med1", valida.validarDato(info.getString("tramo_medio1"), "0"));
                modelRoot.put("med2", valida.validarDato(info.getString("tramo_medio2")));
                modelRoot.put("descs", valida.validarDato(info.getString("desc_tramo_sup"), "N/R"));
                modelRoot.put("sup1", valida.validarDato(info.getString("tramo_sup1"), "0"));
                modelRoot.put("sup2", valida.validarDato(info.getString("tramo_sup2"), "0"));
            }
            info.close();
            modelRoot.put("gral_id", req.getParameter("gral_id"));
            super.retTemplate(resp,"Gestion/ManIndicadores/Edita.htm",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void Actualiza(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            Consulta info = new Consulta(Conexion);
            Validar valida = new Validar();
            Periodo peri = new Periodo(Conexion);
            String item = "";
            String glosa = "";
            String formula = "";
            String desc_ind = "";
            String inf_desc = "";
            String med_desc = "";
            String sup_desc = "";
            String inf1 = "";
            String inf2 = "";
            String med1 = "";
            String med2 = "";
            String sup1 = "";
            String sup2 = "";
            String queActualiza = req.getParameter("queAct");
            if(queActualiza == null || queActualiza.trim().equals(""))
                queActualiza = "R";
            item = req.getParameter("item");
            inf1 = req.getParameter("inf1");
            inf1 = Tools.remplazaTodos(inf1, ",", ".");
            inf2 = req.getParameter("inf2");
            inf2 = Tools.remplazaTodos(inf2, ",", ".");
            med1 = req.getParameter("med1");
            med1 = Tools.remplazaTodos(med1, ",", ".");
            med2 = req.getParameter("med2");
            med2 = Tools.remplazaTodos(med2, ",", ".");
            sup1 = req.getParameter("sup1");
            sup1 = Tools.remplazaTodos(sup1, ",", ".");
            sup2 = req.getParameter("sup2");
            sup2 = Tools.remplazaTodos(sup2, ",", ".");
            glosa = req.getParameter("glosa");
            formula = req.getParameter("formula");
            desc_ind = req.getParameter("politica");
            inf_desc = req.getParameter("descf");
            med_desc = req.getParameter("descm");
            sup_desc = req.getParameter("descs");
            int actual = Integer.parseInt(peri.getPeriodo());
            int desde = Integer.parseInt(req.getParameter("desde"));
            String consulta = "";
            if(desde == actual)
            {
                consulta = "UPDATE eje_ges_def_indica_histo SET ";
                if(queActualiza.equals("R"))
                    consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consulta)))).append("tramo_inf1 = ").append(inf1).append(",").append("tramo_inf2 = ").append(inf2).append(",").append("tramo_medio1 = ").append(med1).append(",").append("tramo_medio2 = ").append(med2).append(",").append("tramo_sup1 = ").append(sup1).append(",").append("tramo_sup2 = ").append(sup2)));
                else
                    consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consulta)))).append("formula = '").append(valida.validarDato(formula, "N/R")).append("',").append("glosa = '").append(glosa).append("',").append("desc_tramo_inf = '").append(inf_desc).append("',").append("desc_tramo_med = '").append(med_desc).append("',").append("desc_tramo_sup = '").append(sup_desc).append("',").append("desc_ind = '").append(desc_ind).append("'")));
                consulta = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consulta)))).append(" WHERE (item = '").append(item).append("') AND ").append("(periodo_desde = ").append(desde).append(")")));
                OutMessage.OutMessagePrint("-->mismo periodo: ".concat(String.valueOf(String.valueOf(consulta))));
                info.insert(consulta);
            }
            if(desde < actual)
            {
                consulta = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_def_indica_histo SET periodo_hasta = ")).append(actual - 1).append(" WHERE (item = '").append(item).append("') AND ").append("(periodo_desde = ").append(desde).append(") AND (periodo_hasta is null)")));
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("-->actualizar periodo anterior;")).append(consulta).append("\n"))));
                info.insert(consulta);
                consulta = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_def_indica_histo (periodo_desde, periodo_hasta,item, formula, glosa, desc_ind, desc_tramo_inf, tramo_inf1, tramo_inf2, desc_tramo_med, tramo_medio1, tramo_medio2, desc_tramo_sup, tramo_sup1, tramo_sup2) VALUES (")).append(actual).append(",").append("NULL,").append("'").append(item).append("',").append("'").append(valida.validarDato(formula, "N/R")).append("',").append("'").append(valida.validarDato(glosa, "N/R")).append("',").append("'").append(valida.validarDato(desc_ind, "N/R")).append("',").append("'").append(inf_desc).append("',").append(inf1).append(",").append(inf2).append(",").append("'").append(med_desc).append("',").append(med1).append(",").append(med2).append(",").append("'").append(sup_desc).append("',").append(sup1).append(",").append(sup2).append(")")));
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("-->Insertar data nuevo periodo(actual);")).append(consulta).append("\n"))));
                info.insert(consulta);
            }
            info.close();
            resp.sendRedirect("ListaIndicadores?gral_id=".concat(String.valueOf(String.valueOf(req.getParameter("gral_id")))));
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
        super.retTemplate(resp,"Gestion/ManIndicadores/mensaje.htm",modelRoot);
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