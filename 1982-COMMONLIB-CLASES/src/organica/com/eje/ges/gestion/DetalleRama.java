package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class DetalleRama extends MyHttpServlet
{

    public DetalleRama()
    {
        vecPadre = new Vector();
        vecHijo = new Vector();
        numP = 0;
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
        String consulta = null;
        Consulta nodos = new Consulta(Conexion);
        String padre = req.getParameter("padre");
        String indicador = req.getParameter("indic");
        String cual = req.getParameter("ind");
        String formato = req.getParameter("formato");
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("padre: ")).append(padre).append("\nindicador: ").append(indicador).append("\ncual: ").append(cual).append("\nformato: ").append(formato))));
        consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT DISTINCT nodo_id, nodo_padre, nodo_corr, nodo_hijos, nodo_imagen, nodo_nivel, unid_desc FROM view_subrama WHERE (compania = '")).append(user.getEmpresa()).append("') ").append("and (unidad='").append(padre).append("') ORDER BY nodo_corr")));
        OutMessage.OutMessagePrint(consulta);
        nodos.exec(consulta);
        IndicBAE(nodos, cual, "U", indicador, formato, resp, req, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    private void IndicBAE(Consulta nodo, String indicador, String tipo, String indi, String forma, HttpServletResponse resp, HttpServletRequest req, 
            Connection Conexion)
        throws ServletException, IOException
    {
        String consulta = null;
        String unid = "";
        user = Usuario.rescatarUsuario(req);
        SimpleList simplelist = new SimpleList();
        SimpleList datos = new SimpleList();
        SimpleHash modelRoot = new SimpleHash();
        Consulta valores = new Consulta(Conexion);
        OutMessage.OutMessagePrint("---->Generando Indicadores Individuales BAE");
        SimpleHash simplehash1;
        for(; nodo.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            unid = nodo.getString("nodo_id");
            int ind = Integer.parseInt(indicador);
            switch(ind)
            {
            case 0: // '\0'
                OutMessage.OutMessagePrint("---->Capacitacion");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_capacitacion.empresa,eje_ges_indic_bae_capacitacion.unidad,eje_ges_indic_bae_capacitacion.periodo,eje_ges_indic_bae_capacitacion.uni_rama,ROUND(eje_ges_indic_bae_capacitacion.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_indic_bae_capacitacion INNER JOIN ").append("eje_ges_periodo ON ").append("eje_ges_indic_bae_capacitacion.periodo = eje_ges_periodo.peri_id ").append("WHERE (eje_ges_indic_bae_capacitacion.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_capacitacion.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_capacitacion.uni_rama = '").append(tipo).append("') AND ").append("(eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) })")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_capacitacion", unid, Conexion);
                break;

            case 1: // '\001'
                OutMessage.OutMessagePrint("---->Evaluacion desempe\361o---->formato= ".concat(String.valueOf(String.valueOf(forma))));
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_desemp.empresa,eje_ges_indic_bae_desemp.unidad,eje_ges_indic_bae_desemp.periodo,eje_ges_indic_bae_desemp.uni_rama,eje_ges_indic_bae_desemp.formato,ROUND(eje_ges_indic_bae_desemp.")).append(indi).append(",2) AS ").append(indi).append(" ").append("eje_ges_periodo.peri_ano,eje_ges_periodo.peri_mes ").append("FROM eje_ges_indic_bae_desemp LEFT OUTER JOIN ").append("eje_ges_periodo ON ").append("eje_ges_indic_bae_desemp.periodo = eje_ges_periodo.peri_id ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_desemp.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_desemp.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_desemp.formato = ").append(forma).append(") AND ").append("(eje_ges_indic_bae_desemp.uni_rama = '").append(tipo).append("')")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_desemp", unid, Conexion);
                break;

            case 2: // '\002'
                OutMessage.OutMessagePrint("---->Dotacion del personal");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_dotacion.empresa,eje_ges_indic_bae_dotacion.unidad,eje_ges_indic_bae_dotacion.periodo,eje_ges_indic_bae_dotacion.uni_rama,eje_ges_indic_bae_dotacion.")).append(indi).append(" ").append("eje_ges_periodo.peri_mes, eje_ges_periodo.peri_ano ").append("FROM eje_ges_indic_bae_dotacion LEFT OUTER JOIN ").append("eje_ges_periodo ON ").append("eje_ges_indic_bae_dotacion.periodo = eje_ges_periodo.peri_id ").append("WHERE (eje_ges_indic_bae_dotacion.unidad = '").append(unid).append("') AND ").append(" (eje_ges_indic_bae_dotacion.uni_rama = '").append(tipo).append("') AND ").append(" (eje_ges_indic_bae_dotacion.empresa = '").append(user.getEmpresa()).append("') AND ").append(" (eje_ges_periodo.peri_mes = MONTH (GETDATE())) AND ").append(" (eje_ges_periodo.peri_ano = YEAR (GETDATE()))")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_dotacion", unid, Conexion);
                break;

            case 3: // '\003'
                OutMessage.OutMessagePrint("---->Costos");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_costos.empresa,eje_ges_indic_bae_costos.unidad,eje_ges_indic_bae_costos.periodo,eje_ges_indic_bae_costos.uni_rama,ROUND(eje_ges_indic_bae_costos.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_periodo RIGHT OUTER JOIN ").append("eje_ges_indic_bae_costos ON ").append("eje_ges_periodo.peri_id = eje_ges_indic_bae_costos.periodo ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_costos.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_costos.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_costos.uni_rama = '").append(tipo).append("')")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_costos", unid, Conexion);
                break;

            case 4: // '\004'
                OutMessage.OutMessagePrint("---->Antiguedad");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_antig.empresa,eje_ges_indic_bae_antig.unidad,eje_ges_indic_bae_antig.periodo,eje_ges_indic_bae_antig.uni_rama,ROUND(eje_ges_indic_bae_antig.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_periodo RIGHT OUTER JOIN ").append("eje_ges_indic_bae_antig ON ").append("eje_ges_periodo.peri_id = eje_ges_indic_bae_antig.periodo ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_antig.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_antig.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_antig.uni_rama = '").append(tipo).append("')")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_antig", unid, Conexion);
                break;

            case 5: // '\005'
                OutMessage.OutMessagePrint("---->Ausentismo");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_ausentismo.empresa,eje_ges_indic_bae_ausentismo.unidad,eje_ges_indic_bae_ausentismo.periodo,eje_ges_indic_bae_ausentismo.uni_rama,ROUND(eje_ges_indic_bae_ausentismo.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_periodo RIGHT OUTER JOIN ").append("eje_ges_indic_bae_ausentismo ON ").append("eje_ges_periodo.peri_id = eje_ges_indic_bae_ausentismo.periodo ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_ausentismo.uni_rama = '").append(tipo).append("') AND ").append("(eje_ges_indic_bae_ausentismo.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_ausentismo.empresa = '").append(user.getEmpresa()).append("')")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_ausentismo", unid, Conexion);
                break;

            case 6: // '\006'
                OutMessage.OutMessagePrint("---->Licencias medicas");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_licencias.empresa,eje_ges_indic_bae_licencias.unidad,eje_ges_indic_bae_licencias.periodo,eje_ges_indic_bae_licencias.uni_rama,ROUND(eje_ges_indic_bae_licencias.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_periodo INNER JOIN ").append("eje_ges_indic_bae_licencias ON ").append("eje_ges_periodo.peri_id = eje_ges_indic_bae_licencias.periodo ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_licencias.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_licencias.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_licencias.uni_rama = '").append(tipo).append("')")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_licencias", unid, Conexion);
                break;

            case 7: // '\007'
                OutMessage.OutMessagePrint("---->Nivel Academicol");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_academ.empresa,eje_ges_indic_bae_academ.unidad,eje_ges_indic_bae_academ.periodo,eje_ges_indic_bae_academ.uni_rama,ROUND(eje_ges_indic_bae_academ.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_periodo RIGHT OUTER JOIN ").append("eje_ges_indic_bae_academ ON ").append("eje_ges_periodo.peri_id = eje_ges_indic_bae_academ.periodo ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_academ.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_academ.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_academ.uni_rama = '").append(tipo).append("')")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_academ", unid, Conexion);
                break;

            case 8: // '\b'
                OutMessage.OutMessagePrint("---->Participacion Sindical");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_sindical.empresa,eje_ges_indic_bae_sindical.unidad,eje_ges_indic_bae_sindical.periodo,eje_ges_indic_bae_sindical.uni_rama,ROUND(eje_ges_indic_bae_sindical.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_periodo RIGHT OUTER JOIN ").append("eje_ges_indic_bae_sindical ON ").append("eje_ges_periodo.peri_id = eje_ges_indic_bae_sindical.periodo ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_sindical.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_sindical.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_sindical.uni_rama = '").append(tipo).append("') ")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_sindical", unid, Conexion);
                break;

            case 9: // '\t'
                OutMessage.OutMessagePrint("---->Sobretiempo");
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_indic_bae_sobretiempo.empresa,eje_ges_indic_bae_sobretiempo.unidad,eje_ges_indic_bae_sobretiempo.periodo,eje_ges_indic_bae_sobretiempo.uni_rama,ROUND(eje_ges_indic_bae_sobretiempo.")).append(indi).append(",2) AS ").append(indi).append(" ").append("FROM eje_ges_periodo RIGHT OUTER JOIN ").append("eje_ges_indic_bae_sobretiempo ON ").append("eje_ges_periodo.peri_id = eje_ges_indic_bae_sobretiempo.periodo ").append("WHERE (eje_ges_periodo.peri_ano = { fn YEAR(GETDATE()) }) AND ").append("(eje_ges_periodo.peri_mes = { fn MONTH(GETDATE()) }) AND ").append("(eje_ges_indic_bae_sobretiempo.empresa = '").append(user.getEmpresa()).append("') AND ").append("(eje_ges_indic_bae_sobretiempo.unidad = '").append(unid).append("') AND ").append("(eje_ges_indic_bae_sobretiempo.uni_rama = '").append(tipo).append("') ")));
                valores.exec(consulta);
                datos = RescataDatos(valores, "eje_ges_indic_bae_sobretiempo", unid, Conexion);
                break;
            }
            simplehash1.put("datos", datos);
        }

        valores.close();
        modelRoot.put("unidad", unid);
        modelRoot.put("Lista", simplelist);
        super.retTemplate(resp,"Gestion/InfoUsuario/Indicadores/Bae/DetalleRama.htm",modelRoot);
    }

    private SimpleList RescataDatos(Consulta info, String tabla, String unidad, Connection Conexion)
    {
        SimpleList simplelist = new SimpleList();
        String rutaImagen = null;
        String consulta = null;
        while(info.next()) 
        {
            numP++;
            for(int column = 4; column <= info.getColumnCount(); column++)
            {
                SimpleHash simplehash1 = new SimpleHash();
                String columnName = info.getColumnName(column);
                Object columnValor = info.getValor(column);
                Consulta rangos = new Consulta(Conexion);
                consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT glosa,indicador, tabla, tramo_inf1 AS inf1, tramo_inf2 AS inf2,tramo_medio1 AS med1, tramo_medio2 AS med2,tramo_sup1 AS sup1, tramo_sup2 AS sup2,imag_inf AS imag_inf, imag_medio AS imag_med,imag_sup AS imag_sup FROM eje_ges_def_indicadores WHERE indicador = '")).append(columnName).append("' ").append("and tabla='").append(tabla).append("'")));
                rangos.exec(consulta);
                if(rangos.next())
                {
                    float inf1 = rangos.getFloat("inf1");
                    float inf2 = rangos.getFloat("inf2");
                    float med1 = rangos.getFloat("med1");
                    float med2 = rangos.getFloat("med2");
                    float sup1 = rangos.getFloat("sup1");
                    float sup2 = rangos.getFloat("sup2");
                    float valor;
                    if(columnValor == null)
                        valor = 0.0F;
                    else
                        valor = Float.parseFloat(columnValor.toString());
                    if(valor >= inf1 && valor <= inf2)
                        rutaImagen = rangos.getString("imag_inf").trim();
                    if(valor >= med1 && valor <= med2)
                        rutaImagen = rangos.getString("imag_med").trim();
                    if(valor >= sup1 && valor <= sup2)
                        rutaImagen = rangos.getString("imag_sup").trim();
                    simplehash1.put("unidad", unidad);
                    simplehash1.put("indic", rangos.getString("glosa"));
                    OutMessage.OutMessagePrint("NumLayer: ".concat(String.valueOf(String.valueOf(numP))));
                    simplehash1.put("NumInd", String.valueOf(numP));
                    simplehash1.put("imagen", rutaImagen);
                    simplehash1.put("valor", String.valueOf(columnValor));
                    simplehash1.put("inf2", String.valueOf(inf2));
                    simplehash1.put("sup1", String.valueOf(sup1));
                    rangos.close();
                    simplelist.add(simplehash1);
                }
            }

        }
        return simplelist;
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
    private Tools tool;
    private Vector vecPadre;
    private Vector vecHijo;
    private int numP;
}