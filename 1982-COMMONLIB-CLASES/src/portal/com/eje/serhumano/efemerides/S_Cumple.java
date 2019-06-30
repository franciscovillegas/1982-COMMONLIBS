package portal.com.eje.serhumano.efemerides;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.tools.Tools;
import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.portal.ToolNewOrganicaIO;
import portal.com.eje.tools.portal.ToolNewOrganicaIO.NodoUnidad;
import portal.com.eje.usuario.UsuarioImagenManager;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.efemerides:
//            Efemerides_Manager

public class S_Cumple extends MyHttpServlet {

    public S_Cumple() {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
    	
        Usuario user = SessionMgr.rescatarUsuario(req); 
        if(user.esValido()) {
        	
        	Connection Conexion = DBConnectionManager.getInstance().getConnection(user.getJndi());
        	try {
        		String htm = req.getParameter("htm"); 
            	
                if(Conexion != null) {
                	if (htm!=null){
                        MuestraDatos(req, resp, Conexion, htm);
                	}else {
                        MuestraDatos(req, resp, Conexion);
                	}
                    insTracking(req, "Cumpleaños".intern(), null);
                } 
                else {
                    super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
                }
        	}
        	catch (IOException e) {
        		throw e;
        	}
        	catch (ServletException e) {
        		throw e;
        	}
        	finally {
        		DBConnectionManager.getInstance().freeConnection(user.getJndi(), Conexion);
        	}            
        }
        else {
        	super.mensaje.devolverPaginaSinSesion(req, resp, "Sin sesión", "Sin sesión");
        }
    	
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion) throws ServletException, IOException{
    	MuestraDatos(req, resp, Conexion, null);
    }
    
    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion, String htm)
        throws ServletException, IOException {
    	
        Usuario user					= SessionMgr.rescatarUsuario(req);
        ToolNewOrganicaIO toolOrg = new ToolNewOrganicaIO();
        
        String diasRel = req.getParameter("diasRel");
        String diasRelDespues = req.getParameter("diasRelDespues");
        
        if (diasRel==null){
        	diasRel = "2";
        } 
        if (diasRelDespues==null){
        	diasRelDespues = "2";
        } 
        
        if(user.esValido()) {
        	StringBuilder sql = new StringBuilder();
        	StringBuilder sql_base = new StringBuilder();
        	sql_base.append(" SELECT rut, digito_ver, nombre, nombres, ape_materno, ");
        	sql_base.append("		ape_paterno, unidad, anexo, ");  
        	sql_base.append("		convert(datetime, convert(varchar(10),fecha_nacim , 112)) as fecha_nacim, ");
        	sql_base.append("		DATEDIFF(day,convert(datetime, convert(varchar(10),GETDATE() , 112)), ");
        	sql_base.append("		convert(datetime, convert(varchar(10), ");
        	sql_base.append("		dateadd(year, DATEDIFF(year,convert(datetime, convert(varchar(10),fecha_nacim , 112)), convert(datetime, convert(varchar(10),GETDATE() , 112)))  ,fecha_nacim ) ");
        	sql_base.append("		, 112))     ) as dias_relativos,e_mail ");
        	sql_base.append(" from 	eje_ges_trabajador");
        	
        	if (htm==null){
            	sql.append(" SELECT * FROM (");
            	sql.append(sql_base).append(") as m");
            	sql.append(" WHERE m.dias_relativos >= -2 and m.dias_relativos <= 7 ");
            	sql.append(" ORDER BY m.dias_relativos ");

        	}else {
            	sql.append(" SELECT abs(m.dias_relativos)abs_dias_relativos, * FROM (");
            	sql.append(sql_base).append(") as m");
            	sql.append(" WHERE (m.dias_relativos = 0) union ");
            	sql.append(" SELECT abs(m.dias_relativos)abs_dias_relativos, * FROM (");
            	sql.append(sql_base).append(") as m");
            	sql.append(" WHERE (m.dias_relativos between -").append(diasRel).append(" and ").append(diasRelDespues).append(" and m.dias_relativos != 0) ");
            	sql.append(" ORDER BY m.dias_relativos ");
        	}
        	
        	SimpleHash modelRoot = new SimpleHash();
        	ConsultaData data = null;
        	
        	try {
				data = ConsultaTool.getInstance().getData("portal", sql.toString());
				FreemakerTool f = new FreemakerTool();
				Calendar cal = Calendar.getInstance();
				
				UsuarioImagenManager imagen = new UsuarioImagenManager();
				
				
				SimpleList lista = new SimpleList();
				while(data != null && data.next()) {
					SimpleHash hash = f.getData(data);
					hash.put("usuarioImagen" , imagen.getImagen(data.getInt("rut")).getNameUnic());
					Date date = data.getDateJava("fecha_nacim");
					cal.setTime(date);
					int mes = cal.get(Calendar.MONTH) + 1;
					int dia = cal.get(Calendar.DAY_OF_MONTH);
					hash.put("fecha_cumpl", dia + " de " + Tools.RescataMes(mes));
					
					
					if(data.getInt("dias_relativos") < 0) {
						int can = data.getInt("dias_relativos") * -1;
						hash.put("diff", "Hace "+can+" d&#237;a" + (can > 1 ? "s":""));
					}
					else if(data.getInt("dias_relativos") > 0) {
						int can = data.getInt("dias_relativos");
						hash.put("diff", "Falta"+(can > 1 ? "n":"")+" "+can+" d&#237;a" + (can > 1 ? "s":""));
					}
					else {
						hash.put("diff", "hoy");
					}
					
					ConsultaData dataTrab = ManagerTrabajador.getInstance().getTrabajador(data.getInt("rut"));
					if(dataTrab != null && dataTrab.next()) {
						NodoUnidad unidad = toolOrg.getUnidad(dataTrab.getString("unidad"));
						
						if(unidad != null && unidad.getUnidDesc() != null) {
							hash.put("unidad", unidad.getUnidDesc());
						}
						else {
							hash.put("unidad", "No/Definida");
						}
					}
					else {
						hash.put("unidad", "No/Definida");
					}
					lista.add(hash);
				}
				
				modelRoot.put("cumples", lista);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	
        	IOClaseWeb io = new IOClaseWeb(this, req, resp);
        	
        	if (htm!=null){
        		super.retTemplate(io,htm,modelRoot);
        	}else {
            	super.retTemplate(io,"efemerides/cumple.html",modelRoot);
        	}
        	
        } 
        else {
            super.mensaje.devolverPaginaSinSesion(resp, "E-Mail", "Tiempo de Sesi\363n expirado...");
        }
    }

}