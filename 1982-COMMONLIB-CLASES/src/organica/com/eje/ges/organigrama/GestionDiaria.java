package organica.com.eje.ges.organigrama;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.ges.Buscar.EncargadoUnidad;
import organica.com.eje.ges.Buscar.FotosPersonalUnidad;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.ExtrasOrganica;
import portal.com.eje.tools.Formatear;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class GestionDiaria extends MyHttpServlet {

    public GestionDiaria() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        DespResultado(req, resp);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido()) {
        	insTracking(req, "Gestión Diaria".intern(), null);
            SimpleHash modelRoot = new SimpleHash();
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            
            System.out.println("LA UNIDAD ------> " + unidad);
            System.out.println("LA EMPRESA ------> " + empresa);
            
            fotos = new FotosPersonalUnidad();
            modelRoot.put("varios", fotos.GetFotosPersonalUnidad(Conexion, unidad, empresa));
            EU = new EncargadoUnidad(Conexion, unidad, empresa);
            userRut = new Rut(Conexion, EU.RutEnc);
            Consulta Buscar = new Consulta(Conexion);
            String consul = String.valueOf((new StringBuilder("select sum(lc.tot_haberes) as total from eje_ges_trabajador t inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut").append(" where t.unidad='").append(unidad).append("' and lc.periodo = (select max(periodo) from eje_ges_certif_histo_liquidacion_cabecera)")));
            
            Buscar.exec(consul);
            if (Buscar.next()) {
                modelRoot.put("total", String.valueOf(new StringBuilder("$")
                		.append(Formatear.numero(Buscar.getInt("total")))));
            } 
            else {
                modelRoot.put("total", "0");
            }
            
            String consulCA = "select sum(lc.tot_haberes) as total from eje_ges_trabajador t inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where t.unidad in (";
            List lista = new ArrayList();
            lista = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,Conexion,lista); lista.add(unidad); 
       	    for (int i=0; i < lista.size(); i++) {
       	    	System.out.println(lista.get(i));
       	    	consulCA+= "'" + lista.get(i).toString() + "'";
       	    	if(i<lista.size()-1) {
       	    		consulCA+=",";
       	    	}
       	    }
       	    consulCA+= ") and lc.periodo = (select max(periodo) from eje_ges_certif_histo_liquidacion_cabecera)";
            Buscar.exec(consulCA);
            if (Buscar.next()) { 
            	modelRoot.put("totalCA", String.valueOf(new StringBuilder("$").append(Formatear.numero(Buscar.getInt("total"))))); 
            } 
            else { 
            	modelRoot.put("totalCA", "0"); 
            }
            
            consul = String.valueOf((new StringBuilder("SELECT COUNT(DISTINCT A.rut) AS dotacion, COUNT(DISTINCT A.cargo) AS total, B.unid_desc")
            		.append(" FROM view_dotacion_directa_unidad_pertenencia A")
            		.append(" INNER JOIN eje_ges_unidades B ON A.unidad = B.unid_id")
            		.append(" WHERE (A.empresa='"))
            		.append(empresa).append("') AND (tipo = 'U') AND (unidad = '")
            		.append(unidad).append("')").append(" GROUP BY B.unid_desc"));
            Buscar.exec(consul);
            if (Buscar.next()) {
                modelRoot.put("dotacion", Buscar.getString("dotacion"));
                modelRoot.put("num_cargos", Buscar.getString("total"));
                modelRoot.put("unid_desc", Buscar.getString("unid_desc"));
            } 
            else {
                modelRoot.put("cantidad", "0");
                modelRoot.put("dotacion","0");
            }
            
            String consulDA = "select count(distinct rut) as trut, count(distinct cargo) as tcargo from eje_ges_trabajador where unidad in (";
            List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,Conexion,lista2); lista2.add(unidad); 
    	    for(int i=0;i<lista2.size();i++) {  
    	    	consulDA+= "'" + lista2.get(i).toString() + "'";
    	       if(i<lista2.size()-1) {
    	    	   consulDA+=",";
    	       }
            }
       	    consulDA+= ")";
            Buscar.exec(consulDA);
            if (Buscar.next()) {
                modelRoot.put("dotacionDA", Buscar.getString("trut"));
                modelRoot.put("num_cargosDA", Buscar.getString("tcargo"));
            } 
            else {
                modelRoot.put("dotacionDA", "0");
                modelRoot.put("num_cargosDA", "0");
            }

            consul = String.valueOf(new StringBuilder("SELECT COUNT(DISTINCT A.rut) AS total")
            		.append(" FROM eje_ges_trabajador A INNER JOIN eje_ges_unidades B")
            		.append(" ON A.unidad = B.unid_id INNER JOIN eje_ges_vacaciones_det C")
            		.append(" ON A.rut = C.rut AND A.empresa = C.empresa WHERE A.unidad IN ")
            		.append(" (SELECT nodo_id FROM eje_ges_jerarquia WHERE nodo_padre='")
            		.append(unidad).append("' OR nodo_id='").append(unidad)
            		.append("') AND YEAR(desde)=YEAR(DATEADD(mm, -2, getdate())) AND MONTH(desde)=MONTH(DATEADD(mm,-2,getdate()))")
            		.append(" GROUP BY YEAR(desde), MONTH(desde)"));
            Buscar.exec(consul);
            if (Buscar.next()) {
                modelRoot.put("vacaciones", Buscar.getString("total"));
            } 
            else {
                modelRoot.put("vacaciones", "0");
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
            modelRoot.put("empActual", empresa);
            modelRoot.put("uniActual", unidad);
            //informacion encargado unidad
            String sql = String.valueOf(new StringBuilder("select distinct a.rut_encargado,a.nombre,a.cargo_desc,a.anexo,a.e_mail, cast(a.rut_encargado as varchar) +'.jpg' as foto,a.mision from view_unidad_encargado a where unid_id='" + unidad +"' and estado=1")); 

            modelRoot.put("unid_desc", ExtrasOrganica.UnidadDesc(unidad,Conexion) );
            
            Consulta consul2 = new Consulta(Conexion);
            OutMessage.OutMessagePrint("UEn --> ".concat(String.valueOf(String.valueOf(sql))));
            consul2.exec(sql);
            if(consul2.next()) {
                modelRoot.put("tiene", "SI");
                modelRoot.put("rut", consul2.getString("rut_encargado"));
                modelRoot.put("nombre", consul2.getString("nombre"));
                modelRoot.put("cargo", consul2.getString("cargo_desc"));
                modelRoot.put("anexo", consul2.getString("anexo"));
                modelRoot.put("e_mail", consul2.getString("e_mail"));
                modelRoot.put("foto", consul2.getString("foto"));
                modelRoot.put("mision", consul2.getString("mision"));
            }
            consul2.close();
            
            //informacion periodos de remuneraciones
            sql = String.valueOf(new StringBuilder("select distinct periodo from eje_ges_certif_histo_liquidacion_cabecera order by periodo desc")); 
            consul2 = new Consulta(Conexion);
            consul2.exec(sql);
            SimpleList periodoList = new SimpleList();
        	SimpleHash periodoIter = new SimpleHash();
        	int i=0;
            for(;consul2.next();periodoList.add(periodoIter)) {
            	periodoIter = new SimpleHash();
            	periodoIter.put("periodo",consul2.getString("periodo") );
            	if(i==0) {
            		modelRoot.put("lperiodo", consul2.getString("periodo"));
            		i=1;
            	}
            }
            modelRoot.put("lperiodos",periodoList);
            
            boolean isJefe = organica.com.eje.ges.gestion.dimension.ExtrasOrganica.isEncargadoUnidad(user.getRutUsuario(), Conexion);
            boolean isGerente = organica.com.eje.ges.gestion.dimension.ExtrasOrganica.isGerente(user.getRutUsuario(), Conexion);
            if(isJefe && isGerente) {
            	modelRoot.put("activaReportes","1");
            }
            
            consul2.close();
            super.retTemplate(resp,"Gestion/InfoUsuario/Jerarquia/gestion_diaria.htm",modelRoot);
        } 
        else {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Rut userRut;
    private EncargadoUnidad EU;
    private EncargadoUnidad UE;
    private FotosPersonalUnidad fotos;
    private Mensaje mensaje;
}