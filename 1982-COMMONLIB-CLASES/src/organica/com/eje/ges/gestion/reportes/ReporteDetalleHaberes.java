package organica.com.eje.ges.gestion.reportes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.gestion.reportes.bean.TrabajadorHaberes;
import organica.com.eje.ges.usuario.Usuario;

import portal.com.eje.tools.Formatear;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReporteDetalleHaberes extends MyHttpServlet {

	public ReporteDetalleHaberes() { }
	
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    }

    public void destroy() {
    	super.destroy();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doGet(req, resp);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String excel = req.getParameter("excel")==null ? "0" : "1" ;
    	if("1".equals(excel)) {
    		resp.setContentType("application/vnd.ms-excel");
    		resp.setContentType("application/force-download");
    		resp.setHeader("Content-Transfer-Encoding","binary");
    		resp.setHeader("Cache-Control", "public");
    		resp.setHeader("Content-Disposition","attachment; filename=archivo.xls");
    		resp.setHeader("pragma", "");    			
		}
		else {
			resp.setContentType("text/html");
			resp.setHeader("Expires", "0");
			resp.setHeader("Pragma", "no-cache");
			resp.setContentType("text/html; charset=iso-8859-1");
		}
       	PrintWriter out = resp.getWriter();
    	Template template = getTemplate("Gestion/Reportes/DetalleHaberes.htm");
    	SimpleHash modelRoot = new SimpleHash();
    	Usuario user = Usuario.rescatarUsuario(req);
    	String periodo = req.getParameter("periodo");
    	String unidad = req.getParameter("unidad");
    	String unidad_desc = req.getParameter("unidad_desc");
    	String clienteBd = req.getParameter("bd");
    	String tipo = req.getParameter("tipo");
//    	Connection conn = super.connMgr.getConnection(user.getjndiConnection());
    	Connection conn = super.connMgr.getConnection("portal");
    	
    	
    	modelRoot.put("periodo",periodo);
    	modelRoot.put("unidad",unidad);
    	modelRoot.put("unidad_desc",unidad_desc);
    	modelRoot.put("tipo",tipo);
    	modelRoot.put("bd",clienteBd);
    	modelRoot.put("excel",excel);
    	
    	if(tipo.equals("FV")) {
    		modelRoot.put("titulo","FIJO / VARIABLE");
    	}
    	else if(tipo.equals("IM")) {
    		modelRoot.put("titulo","IMPONIBLE / NO IMPONIBLE");
    	}
    	else if(tipo.equals("TR")) {
    		modelRoot.put("titulo","TRIBUTABLE / NO TRIBUTABLE");
    	}
    	
    	Consulta consulta = getHaberes(conn,periodo,clienteBd,unidad,tipo);
    	modelRoot.put("haberes",consulta.getSimpleList());
    	modelRoot.put("trabajadores",getTrabajadores(conn,periodo,clienteBd,unidad,tipo));
    	
    	super.connMgr.freeConnection("portal",conn);
    	try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.flush();
    }    
	
	public static int getTotalHaberes(Connection conn, String periodo, String tipo)  {
		try {
			StringBuilder sql = new StringBuilder("select count(distinct glosa_haber) total from ")
							.append("eje_ges_trabajador_detalle_haberes where periodo=").append(periodo);
			if(tipo.equals("FV")) { 
				sql = sql.append(" and haber_variable in ('S','N')");
			}
			else if(tipo.equals("IM")) {
				sql = sql.append(" and haber_imponible in ('0','1')"); 
			}
		   	else if(tipo.equals("TR")) {
		   		sql = sql.append(" and haber_tributable in ('0','1')"); 
			}
			Consulta consulta = new Consulta(conn);
			consulta.exec(sql.toString()); consulta.next();
			return consulta.getInt("total");
		}
		catch(Exception e) { }
		return 0;
	}
	
	
	public static Consulta getHaberes(Connection conn, String periodo, String clienteBd, String unidad, String tipo)  {
		try {
			String campo1 = null, campo2 = null;
			if(tipo.equals("FV")) { 
				campo1 = "haber_variable";
				campo2 = "haber_variable in ('S','N')";
			}
			else if(tipo.equals("IM")) {
				campo1 = "haber_imponible";
				campo2 = "haber_imponible in ('0','1')"; 
			}
		   	else if(tipo.equals("TR")) {
		   		campo1 = "haber_tributable";
				campo2 = "haber_tributable in ('0','1')"; 
			}
			StringBuilder sql = new StringBuilder("select distinct glosa_haber,").append(campo1).append(" from ")
						.append("eje_ges_trabajador_detalle_haberes where periodo=").append(periodo)
						.append(" and ").append(campo2).append(" order by ").append(campo1).append(",glosa_haber");
			Consulta consulta = new Consulta(conn);
			consulta.exec(sql.toString());
			return consulta;
		}
		catch(Exception e) { }
		return null;
	}
	
	public static SimpleList getTrabajadores(Connection conn, String periodo, String clienteBd, String unidad, String tipo)  {
		try {
			String campo1 = null, campo2 = null;
			if(tipo.equals("FV")) { 
				campo1 = "haber_variable";
				campo2 = "haber_variable in ('S','N')";
			}
			else if(tipo.equals("IM")) {
				campo1 = "haber_imponible";
				campo2 = "haber_imponible in ('0','1')"; 
			}
		   	else if(tipo.equals("TR")) {
		   		campo1 = "haber_tributable";
				campo2 = "haber_tributable in ('0','1')"; 
			}
			StringBuilder sql = new StringBuilder("select distinct rut,dv,nombre,glosa_haber,val_haber,")
			                 .append(campo1).append(" from eje_ges_trabajador_detalle_haberes where ")
							.append("rut in ( select rut from ").append(clienteBd).append(".dbo.eje_resumen_trabajador ") 
			                .append("where periodo=").append(periodo).append(" and unidad='").append(unidad)
			                .append("') and periodo=").append(periodo).append(" and ").append(campo2)
			                .append(" order by nombre,").append(campo1).append(",glosa_haber");
			Consulta consulta = new Consulta(conn);
			consulta.exec(sql.toString());
			
			int i=0;
			ArrayList<TrabajadorHaberes> trabajadores = new ArrayList<TrabajadorHaberes>();
			for(; consulta.next(); ) {
				TrabajadorHaberes trabajador = new TrabajadorHaberes();
				trabajador.setRut(consulta.getString("rut"));
				trabajador.setDv(consulta.getString("dv"));
				trabajador.setNombre(consulta.getString("nombre"));
				trabajador.setGlosa(consulta.getString("glosa_haber"));
				trabajador.setCantidad(Formatear.numero(Integer.parseInt(consulta.getString("val_haber"))));
				trabajador.setTipoHaber(consulta.getString(campo1));
				trabajadores.add(i, trabajador);
				i++;
			}
			
			SimpleList trabajadorList = new SimpleList();
			SimpleHash trabajadorHash = new SimpleHash();
			int totalHaberes = getTotalHaberes(conn,periodo,tipo);
			for(int j=0;j<(trabajadores.size()/totalHaberes);j++) {
				trabajadorHash = new SimpleHash();
				int indice = j*totalHaberes;
				TrabajadorHaberes eltrabajador = trabajadores.get(indice);
				int indiceTH = (j+1)*totalHaberes;
				int z=1;
				SimpleList haberList = new SimpleList();
				SimpleHash haberHash = new SimpleHash();
				for(int k=indice;k<indiceTH;k++) {
					TrabajadorHaberes elhaber = trabajadores.get(k);
					haberHash = new SimpleHash();
					haberHash.put("valor", elhaber.getCantidad());
					haberHash.put("tipo", elhaber.getTipoHaber());
					haberList.add(haberHash);
					z++;
				}
				trabajadorHash.put("rut", eltrabajador.getRut() + "-" + eltrabajador.getDv());
				trabajadorHash.put("nombre", eltrabajador.getNombre());
				trabajadorHash.put("haberes", haberList);
				trabajadorList.add(trabajadorHash);
			}
			return trabajadorList;
		}
		catch(Exception e) { }
		return null;
	}
		
}