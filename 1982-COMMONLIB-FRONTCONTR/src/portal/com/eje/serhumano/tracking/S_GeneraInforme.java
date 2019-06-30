package portal.com.eje.serhumano.tracking;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.servlet.FormatoFecha;
import portal.com.eje.tools.servlet.FormatoNumero;
import portal.com.eje.tools.servlet.GetDatos;
import portal.com.eje.tools.servlet.GetParam;
import portal.com.eje.tools.servlet.MesesToYM;
import portal.com.eje.tools.servlet.SubQuery;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.TemplateModelException;

public class S_GeneraInforme extends MyHttpServlet
{

    public S_GeneraInforme()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        Connection conexion = connMgr.getConnection("portal");
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(user.esValido())
            generaDatos(req, resp, conexion);
        else
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        connMgr.freeConnection("portal", conexion);
    }

    private void generaDatos(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\nEntro al doPost de GeneraInforme");
        Validar valida = new Validar();
        Tools tool = new Tools();
        String consulta = null;
        String cualSql = req.getParameter("cual");
        String strCriterio = req.getParameter("criterio");
        String infoTitle = req.getParameter("title");
        consulta = "select * from wsg_inf_querys where query_id = '" + cualSql + "'";
        Consulta query = new Consulta(conexion);
        OutMessage.OutMessagePrint("--> consulta Query\n" + consulta);
        query.exec(consulta);
        query.next();
        String sql = query.getString("query_sentencia");
        String titulo = query.getString("query_titulo");
        String html = query.getString("query_template");
        int cantRows = query.getInt("query_rows");
        String order_by = query.getString("query_order_by");
        Tools _tmp = tool;
        Vector agrupar = Tools.separaLista(query.getString("query_agrupar"), ",");
        Tools _tmp1 = tool;
        Vector sumar = Tools.separaLista(query.getString("query_sum"), ",");
        query.close();
        Consulta consul = new Consulta(conexion);
        OutMessage.OutMessagePrint("Agrupar por: " + agrupar.toString());
        OutMessage.OutMessagePrint("sumar   por: " + sumar.toString());
        if(strCriterio != null)
            sql = sql + " " + strCriterio;
        if(order_by != null)
            sql = sql + " order by " + order_by;
        OutMessage.OutMessagePrint("--> consulta Informe\n" + sql);
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

        OutMessage.OutMessagePrint("---->Template= " + html);
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("FFecha", new FormatoFecha());
        modelRoot.put("FNum", new FormatoNumero());
        modelRoot.put("MTYM", new MesesToYM());
        modelRoot.put("GetParam", new GetParam(req));
        modelRoot.put("SubQuery", new SubQuery(conexion, req, getServletContext().getRealPath(getTemplatePath())));
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
            	if( simplelist == null ) {
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
                        simplehash1.put("ENC" + columnName, columnValor);
                    }
                    if(se_suma[column - 1])
                        totales[column - 1] += consul.getFloat(column);
                    simplehash1.put(columnName, columnValor);
                }

                simplehash1.put("FILATIPO", filaTipo);
                simplelist.add(simplehash1);
            } while(row != cantRows);
            boolean paginaConDatos = false;
            paginaConDatos = !(simplelist == null);
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
                modelRoot.put("T_" + consul.getColumnName(column), Integer.toString(totales[column - 1]));
                int prom = 0;
                if(cuentaReg > 0)
                    prom = totales[column - 1] / cuentaReg;
                modelRoot.put("TP_" + consul.getColumnName(column), Integer.toString(prom));
            }

        modelRoot.put("title", infoTitle);
        consul.close();
        retTemplate(resp, html, modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost GeneraInforme");
    }
}