package organica.com.eje.ges.organigrama;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JavascriptArrayDataOut;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.datos.ConsultaTool;
import organica.DatosRut.Rut;
import organica.com.eje.ges.Buscar.EncargadoUnidad;
import organica.com.eje.ges.Buscar.FotosPersonalUnidad;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.Tools;
import organica.tools.Validar;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.tracking.trackingManager;
import freemarker.template.SimpleHash;

public class Organizacion extends MyHttpServlet {

    public Organizacion() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        DespResultado(req, resp);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp){
    	SimpleHash ModelRoot = new SimpleHash();
    	String unidad = req.getParameter("unidad");
        String empresa = req.getParameter("empresa");
        insTracking(req, "Dotación".intern(),null );
            
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.rut, t.nombres, t.ape_paterno, t.ape_materno, c.descrip ");
        sql.append("from eje_ges_trabajador as t left outer join eje_ges_cargos as c ");
        sql.append("on t.cargo = c.cargo and t.empresa = c.empresa ");
        sql.append("where t.unidad=?");

        Object[] params = { unidad };
        
        try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			JavascriptArrayDataOut out = new JavascriptArrayDataOut(data);
			
			String[] order = {"rut", "nombres", "ape_paterno", "ape_materno", "descrip"};
			
			
			SimpleHash modelRoot = new SimpleHash();
			modelRoot.put("dataTracking", out.getListData(order));
			
			super.retTemplate(resp, "Gestion/InfoUsuario/Jerarquia/datos.html", modelRoot);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}    	
    }
    
    /*private void DespResultado(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        portal.com.eje.serhumano.user.Usuario userPortal =  portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);
        
        if(user.esValido()) {
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("FNum", new FormatoNumero());
            valida = new Validar();
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            fotos = new FotosPersonalUnidad();
            modelRoot.put("varios", fotos.GetFotosPersonalUnidad(Conexion, unidad, empresa));
            EU = new EncargadoUnidad(Conexion, unidad, empresa);
            userRut = new Rut(Conexion, EU.RutEnc);
            Consulta Buscar = new Consulta(Conexion);
            String consul = String.valueOf(String.valueOf((new StringBuilder("SELECT sum(costo) as total FROM view_dotacion_directa WHERE (empresa = '")).append(empresa).append("') AND (tipo = 'U') AND   (unidad = '").append(unidad).append("')")));
            Buscar.exec(consul);
            tool = new Tools();
            if(Buscar.next()) {
            	modelRoot.put("total", Buscar.getString("total"));
            }
            else {
            	modelRoot.put("total", "0");
            }
            Buscar.close();
            modelRoot.put("unidad", userRut.Unidad);
            modelRoot.put("unid_id", unidad);
            modelRoot.put("empresa", empresa);
            modelRoot.put("mision", EU.MisionUni);
            modelRoot.put("periodo", EU.Periodo);
            modelRoot.put("foto", userRut.Foto);
            UE = new EncargadoUnidad();
            UE.DotacionTotalUnidad(Conexion, unidad, empresa);
            UE.DotacionTotalUnidadRama(Conexion, unidad, empresa);
            modelRoot.put("ddr", (new Integer(UE.TotalUniReal)).toString());
            modelRoot.put("dda", (new Integer(UE.TotalUniAut)).toString());
            modelRoot.put("ddd", (new Integer(UE.TotalUniDif)).toString());
            if(UE.TotalUniRealR > 0) {
            	modelRoot.put("dir", (new Integer(UE.TotalUniRealR - UE.TotalUniReal)).toString());
            }
            else {
            	modelRoot.put("dir", "0");
            }
            if(UE.TotalUniAutR > 0) {
            	modelRoot.put("dia", (new Integer(UE.TotalUniAutR - UE.TotalUniAut)).toString());
            }
            else {
            	modelRoot.put("dia", "0");
            }
            if(UE.TotalUniAutR > 0 && UE.TotalUniRealR > 0) {
            	modelRoot.put("did", (new Integer(UE.TotalUniAutR - UE.TotalUniAut - (UE.TotalUniRealR - UE.TotalUniReal))).toString());
            }
            else {
            	modelRoot.put("did", "0");
            }

            boolean isJefe = organica.com.eje.ges.gestion.dimension.ExtrasOrganica.isEncargadoUnidad(user.getRutUsuario(), Conexion);
            boolean isGerente = organica.com.eje.ges.gestion.dimension.ExtrasOrganica.isGerente(user.getRutUsuario(), Conexion);
            if(isJefe && isGerente) {
            	modelRoot.put("activaEnlaces","1");
            }
            
            super.retTemplate(resp,"Gestion/InfoUsuario/Jerarquia/datos.html",modelRoot);
            
            // TRACKING ORGANICA
            if (unidad != null) {
            	insTracking(req, "Jerarquia.Organica".intern(),null );
            }
            // FIN 
              
        } 
        else {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }*/


    private Usuario user;
    private Rut userRut;
    private EncargadoUnidad EU;
    private EncargadoUnidad UE;
    private FotosPersonalUnidad fotos;
    private Tools tool;
    private Validar valida;
    private Mensaje mensaje;
}