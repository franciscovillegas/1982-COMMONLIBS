package portal.com.eje.serhumano.informes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JavascriptArrayDataOut;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
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
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170111
//    	Connection conexion = connMgr.getConnection("portal");
        Connection conexion =  getConnMgr().getConnection("portal");
        if(SessionMgr.rescatarUsuario(req).esValido()) {
        	String cualSql = req.getParameter("cual");
        	String benef_id =req.getParameter("benef_id");
            if("SOLTRACK".equals(cualSql)){
            	generaDatosTracking(req, resp,benef_id);
            }
            else {
            	generaDatos(req, resp, conexion);
            }
        }  
        else {
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
//    	MMA 20170111
//      super.connMgr.freeConnection("portal", conexion);
        getConnMgr().freeConnection("porral", conexion);
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
        String fechaIni = req.getParameter("desde");
        String fechaTer = req.getParameter("hasta");
        consulta = String.valueOf(String.valueOf((new StringBuilder("select * from eje_ges_querys where query_id = '")).append(cualSql).append("'")));
        Consulta query = new Consulta(conexion);
        OutMessage.OutMessagePrint("--> consulta Query\n".concat(String.valueOf(String.valueOf(consulta))));
        query.exec(consulta);
        query.next();
        
        String titulo = query.getString("query_titulo");
        String html = query.getString("query_template");
        int cantRows = query.getInt("query_rows");
        String order_by = query.getString("query_order_by");
        Vector agrupar = Tools.separaLista(query.getString("query_agrupar"), ",");
        Vector sumar = Tools.separaLista(query.getString("query_sum"), ",");
        Consulta consul = new Consulta(conexion);
        String sql="".intern();

    	sql= query.getString("query_sentencia");
        if(strCriterio != null)
             sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" ").append(strCriterio)));
        if(order_by != null)
             sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" order by ").append(order_by)));
         OutMessage.OutMessagePrint("--> consulta Informe\n".concat(String.valueOf(String.valueOf(sql))));
             
        query.close();
        consul.exec(sql);
        OutMessage.OutMessagePrint("Agrupar por: ".concat(String.valueOf(String.valueOf(agrupar.toString()))));
        OutMessage.OutMessagePrint("sumar   por: ".concat(String.valueOf(String.valueOf(sumar.toString()))));
       
        boolean se_agrupa[] = new boolean[consul.getColumnCount()];
        boolean se_suma[] = new boolean[consul.getColumnCount()];
label0:
        for(int col = 1; col <= consul.getColumnCount(); col++)
        {
            se_agrupa[col - 1] = false;
            int x = 0;
            do {
                if(x >= agrupar.size())
                    break;
                if(consul.getColumnName(col).equals(agrupar.get(x).toString())) {
                    se_agrupa[col - 1] = true;
                    break;
                }
                x++;
            } while(true);
            se_suma[col - 1] = false;
            x = 0;
            do {
                if(x >= sumar.size())
                    continue label0;
                if(consul.getColumnName(col).equals(sumar.get(x).toString())) {
                    se_suma[col - 1] = true;
                    continue label0;
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
        modelRoot.put("SubQuery", new SubQuery(conexion, req, getServletContext().getRealPath(super.getTemplatePath())));
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
        {
            if(!se_suma[column - 1])
                continue;
            modelRoot.put("T_".concat(String.valueOf(String.valueOf(consul.getColumnName(column)))), Integer.toString(totales[column - 1]));
            int columnName = 0;
            if(cuentaReg > 0)
                columnName = totales[column - 1] / cuentaReg;
            modelRoot.put("TP_".concat(String.valueOf(String.valueOf(consul.getColumnName(column)))), Integer.toString(columnName));
        }

        modelRoot.put("title", infoTitle);
        consul.close();
        super.retTemplate(resp,html,modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost GeneraInforme");
    }
    
    
    private void generaDatosTracking(HttpServletRequest req, HttpServletResponse resp, String url) {
        String fechaIni = req.getParameter("desde");
        String fechaTer = req.getParameter("hasta");
        
    	fechaIni = Formatear.getInstance().toAnotherDate(fechaIni, "dd/MM/yyyy", "yyyyMMdd");
     	fechaTer = Formatear.getInstance().toAnotherDate(fechaTer, "dd/MM/yyyy", "yyyyMMdd");           
        StringBuilder sql2 = new StringBuilder();
        ConsultaData data = null;
        
        try {
        	
	        if("".equals(url)){
	        	sql2.append(" select CONVERT(varchar,l.fecha,103) as fecha, CONVERT(varchar,l.fecha,108) as hora, l.descripcion, t.rut, t.digito_ver, t.nombres, t.ape_materno, t.ape_paterno ");
	        	sql2.append(" from eje_ges_tracking l inner join eje_ges_trabajador t on l.rut =  t.rut ");
	        	sql2.append(" where (l.fecha > ? or l.fecha = ? ) and (l.fecha < DATEADD(day,1,?))");
	        
	        	Object[] params= { fechaIni , fechaIni , fechaTer };
	        	data =  ConsultaTool.getInstance().getData("portal", sql2.toString(), params);
	        	
	        }
	        else{
	        	
	        	sql2.append("select CONVERT(varchar,l.fecha,103) as fecha, CONVERT(varchar,l.fecha,108) "); 
	        	sql2.append("as hora, l.descripcion, t.rut, t.digito_ver, t.nombres, t.ape_materno, t.ape_paterno ");
	        	sql2.append("from eje_ges_tracking l inner join eje_ges_trabajador t on l.rut =  t.rut ");
	        	sql2.append("inner join eje_ges_tracking_func tf on l.direc_rel = tf.url ");      	
	        	sql2.append("where (l.fecha > ? or l.fecha = ? ) and (l.fecha < DATEADD(day,1,?)) ");
	        	sql2.append("and tf.url = ?");
	        	
	        	Object[] params= { fechaIni , fechaIni , fechaTer , url};        	
	        	data =  ConsultaTool.getInstance().getData("portal", sql2.toString(), params);
	        }

	        SimpleHash modelRoot = new SimpleHash();
	        if( data != null) {
	        	JavascriptArrayDataOut out = new JavascriptArrayDataOut(data);
				String[] order = {"fecha", "hora", "descripcion", "rut", "digito_ver", "nombres", "ape_paterno", "ape_materno"};
				modelRoot.put("dataTracking", out.getListData(order));
	
	        }
			
	        super.retTemplate(resp, "informes/informes_tracking.html", modelRoot);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
        
        
    }
}