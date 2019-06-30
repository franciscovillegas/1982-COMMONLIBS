package organica.com.eje.ges.organigrama;



import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ConsultaData;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import graficos.mng.GraficosMng;
import mis.MisMng;
import organica.com.eje.ges.usuario.Usuario;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.ExtrasOrganica;
import portal.com.eje.tools.Validar;

public class GestionReportes extends MyHttpServlet
{

    public GestionReportes()
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
        DespResultado(req, resp);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
    	
        Connection Conexion 		= connMgr.getConnection("portal");
        Connection ConexionMisJPG 	= connMgr.getConnection("misjpg"); 
        
        Usuario user = Usuario.rescatarUsuario(req);
        if(user.esValido()) {
        	ResourceBundle proper = ResourceBundle.getBundle("db");
        	String MIS_IDCliente = proper.getString("mis.IDCliente_de_este_portal");
        	insTracking(req, "MIS".intern(),null );
        	
        	Validar vld = new Validar();
            SimpleHash modelRoot = new SimpleHash();
            SimpleList sl = new SimpleList();
            SimpleList slc = new SimpleList();
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            modelRoot.put("empActual", empresa);
            modelRoot.put("uniActual", unidad);
            modelRoot.put("unid_desc", ExtrasOrganica.UnidadDesc(unidad,Conexion) );
            
            String periodo = vld.validarDato(req.getParameter("periodo"),ExtrasOrganica.ultimoPeriodo(Conexion));
            String esArea = vld.validarDato(req.getParameter("tipo"),"0");
            String tema = vld.validarDato(req.getParameter("tema"), "Reportes");
            
            //String periodo_desde = vld.validarDato(req.getParameter("periodo_desde"),ExtrasOrganica.ultimoPeriodo(Conexion));
            //String periodo_hasta = vld.validarDato(req.getParameter("periodo_hasta"),ExtrasOrganica.ultimoPeriodo(Conexion));

            System.out.println("Tema : " + tema);
            modelRoot.put("periodo", periodo);
            modelRoot.put("esArea", esArea);
            
            if("Reportes".equals(tema)){
            	modelRoot.put("zorder_rep" ,"3");
    			modelRoot.put("zorder_repv","visible");
            }
            else{
            	modelRoot.put("zorder_rep","2");
            	modelRoot.put("zorder_repv","hidden");
            }
           
            if("Graficos".equals(tema)){
            	modelRoot.put("zorder_gra", "3");
            	modelRoot.put("zorder_grav", "visible");
            }
            else{
            	modelRoot.put("zorder_gra", "2");
            	modelRoot.put("zorder_grav", "hidden");
            }
            
            if("Semaforos".equals(tema)){
        		modelRoot.put("zorder_sem", "3");
        		modelRoot.put("zorder_semv", "visible");
            }
        	else{
        		modelRoot.put("zorder_sem", "2");
        		modelRoot.put("zorder_semv", "hidden");
        	}

            
            modelRoot.put("empActual", empresa);
            modelRoot.put("uniActual", unidad);
            
            MisMng misDataResumen = new MisMng(ConexionMisJPG);
            
            GraficosMng gm = new GraficosMng();
            ConsultaData existeTabla = gm.existeTabla("eje_ggd_perfiles");
            String existe = "0";
            if(existeTabla.next()){
            	existe = "1";
 	            ConsultaData perfiles = gm.getPerfiles();
	            Integer id_perfil;
	            while(perfiles.next()){
	            	SimpleHash sh = new SimpleHash();
	            	id_perfil =  perfiles.getInt("id_perfil");
	            	sh.put("idPerfil",id_perfil.toString());
	            	sh.put("perfil",perfiles.getString("perfil"));
	            	sl.add(sh);
	            }
	            
	            modelRoot.put("perfiles", sl);
            }
            modelRoot.put("existe", existe);
            Consulta consTmp = null;
            consTmp = misDataResumen.getReportes(MIS_IDCliente);
            modelRoot.put("tipoReporte", misDataResumen.getTipoReportes(consTmp));
            consTmp.close();

            consTmp = misDataResumen.JPGgetGrafosVisibles(MIS_IDCliente);
            modelRoot.put("tipoGraficos",misDataResumen.getTipoGrafosFormateados(consTmp));
            consTmp.close();
            
            consTmp = misDataResumen.JPGperiodosDisponibles(MIS_IDCliente);
            modelRoot.put("periodosMis_Desde",misDataResumen.getPeriodosFormateados(consTmp));
            consTmp.close();
            
            consTmp = misDataResumen.JPGperiodosDisponibles(MIS_IDCliente);
            modelRoot.put("periodosMis_Hasta",misDataResumen.getPeriodosFormateados(consTmp));
            consTmp.close();

            consTmp = misDataResumen.getPeriodosReporte(MIS_IDCliente);
            modelRoot.put("periodosReporte", misDataResumen.GetPeriodosFormateadosAAA(consTmp));
            consTmp.close();
            
            String anio;
            ConsultaData periodos = null;
            
            periodos = gm.periodosCostosCursos();
            if (periodos != null){
                while(periodos.next()){
                	SimpleHash sh = new SimpleHash();
                	anio = Integer.toString(periodos.getInt("periodo_accion"));
                	sh.put("periodo_accion",anio.toString());
                	slc.add(sh);
                }
                
                modelRoot.put("periodosCursos", slc);
            	
            }else{
                modelRoot.put("periodosCursos", "0");
            }
            	
            

            //modelRoot.put("periodo_desde", periodo_desde);
            //modelRoot.put("periodo_hasta", periodo_hasta);

            //variables de la direccio del Grafico

            modelRoot.put("Grafo_cliente",MIS_IDCliente);
            modelRoot.put("Grafo_unidad",unidad);
            modelRoot.put("Grafo_rut", user.getRutUsuario());
            modelRoot.put("Repo_cliente",MIS_IDCliente);
            modelRoot.put("Repo_unidad",unidad);
            modelRoot.put("Repo_rut", user.getRutUsuario());
            
            super.retTemplate(resp,"Gestion/InfoUsuario/Jerarquia/reports.htm",modelRoot);
        } 
        else {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("misjpg", ConexionMisJPG);
        connMgr.freeConnection("portal", Conexion);
    }
 
}