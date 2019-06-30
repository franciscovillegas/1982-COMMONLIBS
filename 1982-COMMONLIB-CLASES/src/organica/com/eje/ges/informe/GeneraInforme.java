package organica.com.eje.ges.informe;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import organica.tools.servlet.GetDatos;
import organica.tools.servlet.GetParam;
import organica.tools.servlet.MesesToYM;
import organica.tools.servlet.SubQuery;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.TemplateModelException;

public class GeneraInforme extends MyHttpServlet
{

    public GeneraInforme()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
            generaDatos(req, resp, conexion);
        else
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        connMgr.freeConnection("portal", conexion);
    }

    private void generaDatos(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro al doPost de GeneraInforme");
        Validar valida = new Validar();
        String consulta = null;
        String cualSql = req.getParameter("cual");
        String strCriterio = req.getParameter("criterio");
        String infoTitle = req.getParameter("title");
        consulta = String.valueOf(String.valueOf((new StringBuilder("select * from eje_ges_querys where query_id = '")).append(cualSql).append("'")));
        Consulta query = new Consulta(conexion);
        OutMessage.OutMessagePrint("--> consulta Query\n".concat(String.valueOf(String.valueOf(consulta))));
        query.exec(consulta);
        query.next();
        String sql = query.getString("query_sentencia");
        String titulo = query.getString("query_titulo");
        String html = query.getString("query_template");
        int cantRows = query.getInt("query_rows");
        String order_by = query.getString("query_order_by");
        Vector agrupar = Tools.separaLista(query.getString("query_agrupar"), ",");
        Vector sumar = Tools.separaLista(query.getString("query_sum"), ",");
        query.close();
        Consulta consul = new Consulta(conexion);
        OutMessage.OutMessagePrint("Agrupar por: ".concat(String.valueOf(String.valueOf(agrupar.toString()))));
        OutMessage.OutMessagePrint("sumar   por: ".concat(String.valueOf(String.valueOf(sumar.toString()))));
        if(strCriterio != null)
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" ").append(strCriterio)));
        if(order_by != null)
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" order by ").append(order_by)));
        OutMessage.OutMessagePrint("--> consulta Informe\n".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        boolean se_agrupa[] = new boolean[consul.getColumnCount()];
        boolean se_suma[] = new boolean[consul.getColumnCount()];
        for(int col = 1; col <= consul.getColumnCount(); col++)
        {
            se_agrupa[col - 1] = false;
            int x = 0;
            do
            {
                if(x >= agrupar.size())
                    break;
                if(consul.getColumnName(col).equals(agrupar.get(x).toString()))
                {
                    se_agrupa[col - 1] = true;
                    break;
                }
                x++;
            } while(true);
            se_suma[col - 1] = false;
            x = 0;
            do
            {
                if(x >= sumar.size())
                    break;
                if(consul.getColumnName(col).equals(sumar.get(x).toString()))
                {
                    se_suma[col - 1] = true;
                    break;
                }
                x++;
            } while(true);
        }

        OutMessage.OutMessagePrint("---->Template= ".concat(String.valueOf(String.valueOf(html))));
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("FFecha", new FormatoFecha());
        modelRoot.put("FNum", new FormatoNumero());
        modelRoot.put("MTYM", new MesesToYM());
        modelRoot.put("GetParam", new GetParam(req));
        modelRoot.put("SubQuery", new SubQuery(conexion, getServletContext().getRealPath("")));
        modelRoot.put("GetDatos", new GetDatos(conexion));
        SimpleList listaPaginas = new SimpleList();
        boolean seguir = true;
        int cuentaPag = 0;
        int cuentaReg = 0;
        int totales[] = new int[consul.getColumnCount()];
        for(int col = 1; col <= consul.getColumnCount(); col++)
            totales[col - 1] = 0;

        while(seguir) 
        {
            cuentaPag++;
            SimpleHash simplehashPag = new SimpleHash();
            SimpleList simplelist = null;
            simplehashPag.put("info_titulo", titulo);
            String filaTipo = "";
            int row = 0;
            String valorAnterior[] = new String[consul.getColumnCount()];
            for(int col = 1; col <= consul.getColumnCount(); col++)
                valorAnterior[col - 1] = "&@&";

            do
            {
                if(simplelist == null) {
                	simplelist = new SimpleList();
                }
            	
            	
                if(!(seguir = consul.next()))
                    break;
                row++;
                cuentaReg++;
                filaTipo = "DET";
                SimpleHash simplehash1 = new SimpleHash();
                for(int column = 1; column <= consul.getColumnCount(); column++)
                {
                    String columnName = consul.getColumnName(column);
                    String columnValor = valida.validarDato(consul.getString(column));
                    if(se_agrupa[column - 1] && !valorAnterior[column - 1].equals(columnValor))
                    {
                        filaTipo = "ENC";
                        valorAnterior[column - 1] = columnValor;
                        simplehash1.put("ENC".concat(String.valueOf(String.valueOf(columnName))), columnValor);
                    }
                    if(se_suma[column - 1])
                        totales[column - 1] += consul.getFloat(column);
                    simplehash1.put(columnName, columnValor);
                }

                simplehash1.put("FILATIPO", filaTipo);
                simplelist.add(simplehash1);
            } while(row != cantRows);
            boolean paginaConDatos = false;
            try
            {
                paginaConDatos = !(simplelist == null);
            }
            catch(Exception e)
            {
                OutMessage.OutMessagePrint(e.getMessage());
            }
            if(paginaConDatos || cuentaPag == 1)
            {
                simplehashPag.put("p_ant", String.valueOf(cuentaPag - 1));
                simplehashPag.put("p_sig", String.valueOf(cuentaPag + 1));
                simplehashPag.put("p_act", String.valueOf(cuentaPag));
                simplehashPag.put("datos", simplelist);
                listaPaginas.add(simplehashPag);
            } else
            {
                cuentaPag--;
            }
        }
        modelRoot.put("p_tot", String.valueOf(cuentaPag));
        modelRoot.put("cant_reg", String.valueOf(cuentaReg));
        modelRoot.put("paginas", listaPaginas);
        for(int column = 1; column <= consul.getColumnCount(); column++)
            if(se_suma[column - 1])
            {
                modelRoot.put("T_".concat(String.valueOf(String.valueOf(consul.getColumnName(column)))), Integer.toString(totales[column - 1]));
                String columnName = "0";
                if(cuentaReg > 0)
                    columnName = String.valueOf(totales[column - 1] / cuentaReg);
                modelRoot.put("TP_".concat(String.valueOf(String.valueOf(consul.getColumnName(column)))), columnName);
            }

        modelRoot.put("title", infoTitle);
        consul.close();
        super.retTemplate(resp,html,modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost GeneraInforme");
    }

    private Mensaje mensaje;
}