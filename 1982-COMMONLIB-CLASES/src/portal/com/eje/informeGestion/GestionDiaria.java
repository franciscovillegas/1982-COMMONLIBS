package portal.com.eje.informeGestion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.datos.ConsultaTool;
import organica.DatosRut.Rut;
import organica.com.eje.ges.Buscar.EncargadoUnidad;
import organica.com.eje.ges.Buscar.FotosPersonalUnidad;

import organica.com.eje.ges.usuario.Usuario;

import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.frontcontroller.IOClaseWeb;
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
            
            Consulta Buscar = new Consulta(Conexion);
            String qryEmpresa = "select top 1 empresa from eje_ges_empresa where padre_empresa is null";
            Buscar.exec(qryEmpresa);
            Buscar.next();
            String empresa = Buscar.getString("empresa");
            
            String unidad = req.getParameter("unidad");
            String tipoOrganica = req.getParameter("tipoOrganica");

            
            portal.com.eje.serhumano.user.Usuario portalUser = (portal.com.eje.serhumano.user.Usuario)req.getSession(false).getAttribute(req.getSession(false).getId());

            String strPersonas = "";
            Boolean userPerfilado = false;
            if ("-1".equals(portalUser.getPerfil().getIdPerfil())) {
                List orgUnidades = new ArrayList();
                List lstPersonas = new ArrayList();
                orgUnidades = ExtrasOrganica.NodosHijosRecursivos(user.getUnidad(),empresa,Conexion,orgUnidades); 
                orgUnidades.add(user.getUnidad()); 
                
                lstPersonas = portal.com.eje.carpelect.mgr.ManagerTrabajador.getInstance().getTrabajadoresUnidades(Conexion, orgUnidades);
                strPersonas = lstPersonas.toString().replace("[", "").replace("]", "");

                userPerfilado = true;
            }

            System.out.println("LA UNIDAD ------> " + unidad);
            System.out.println("LA EMPRESA ------> " + empresa);
            
            fotos = new FotosPersonalUnidad();
            modelRoot.put("varios", fotos.GetFotosPersonalUnidad(Conexion, unidad, empresa));
            EU = new EncargadoUnidad(Conexion, unidad, empresa);
            userRut = new Rut(Conexion, EU.RutEnc);
            StringBuilder str = new StringBuilder();
            str.append(" select sum(lc.tot_haberes) as total \n");
            str.append("	from	eje_ges_Trabajador_unidad a \n ");
            str.append("    inner join eje_ges_trabajador t on a.rut = t.rut \n ");
            str.append("	inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut").append(" \n");
            str.append(" where 	a.unidad='").append(unidad).append("' \n");
            str.append("		and lc.periodo = (select MAX(peri_id) from eje_ges_periodo) \n");
            str.append("		and isnull(a.tipo_jerarquia,0) = ").append(tipoOrganica);
          
            String consul = str.toString();
            if (userPerfilado){
            	consul = consul + " and a.rut in ("+ strPersonas +")";
            }
            
            Buscar.exec(consul);
            if (Buscar.next()) {
                modelRoot.put("total", String.valueOf(new StringBuilder("$")
                		.append(Formatear.numero(Buscar.getInt("total")))));
            } 
            else {
                modelRoot.put("total", "0");
            }
            
            String consulCA = "select sum(lc.tot_haberes) as total from eje_ges_Trabajador_unidad a inner join eje_ges_trabajador t on a.rut = t.rut inner join eje_ges_certif_histo_liquidacion_cabecera lc on t.rut=lc.rut where a.unidad in (";
            
            List lista = new ArrayList();
            lista = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,Conexion,lista); 
            lista.add(unidad); 
       	    for (int i=0; i < lista.size(); i++) {
       	    	System.out.println(lista.get(i));
       	    	consulCA+= "'" + lista.get(i).toString() + "'";
       	    	if(i<lista.size()-1) {
       	    		consulCA+=",";
       	    	}
       	    }
       	    consulCA+= ") and lc.periodo = (select MAX(peri_id) from eje_ges_periodo) and isnull(a.tipo_jerarquia,0) = " + tipoOrganica;

            if (userPerfilado){
            	consulCA = consulCA + " and a.rut in ("+ strPersonas  +") ";
            }

       	    Buscar.exec(consulCA);
            if (Buscar.next()) { 
            	modelRoot.put("totalCA", String.valueOf(new StringBuilder("$").append(Formatear.numero(Buscar.getInt("total"))))); 
            } 
            else { 
            	modelRoot.put("totalCA", "0"); 
            }
            
            consul = String.valueOf((new StringBuilder("select COUNT(DISTINCT b.rut)dotacion, COUNT(DISTINCT b.cargo)total, c.unid_desc ")
            		.append("from	eje_ges_trabajador_unidad a ")
            		.append("		inner join eje_ges_Trabajador	b on a.rut = b.rut ")
            		.append("		inner join eje_ges_unidades		c on a.unidad = c.unid_id ")
            		.append(" WHERE (a.unidad = '").append(unidad).append("')").append("and isnull(a.tipo_jerarquia,0) = ").append(tipoOrganica)));

            if (userPerfilado){
            	consul = consul + " and a.rut in ("+ strPersonas  +") ";
            }
            		
            consul = consul + " GROUP BY c.unid_desc";
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
            
            String consulDA = "select count(distinct b.rut)trut, count(distinct b.cargo)tcargo from eje_ges_trabajador_unidad a inner join eje_ges_trabajador b on a.rut = b.rut where a.unidad in (";
            List lista2 = new ArrayList(); lista2 = ExtrasOrganica.NodosHijosRecursivos(unidad,empresa,Conexion,lista2); lista2.add(unidad); 
    	    for(int i=0;i<lista2.size();i++) {  
    	    	consulDA+= "'" + lista2.get(i).toString() + "'";
    	       if(i<lista2.size()-1) {
    	    	   consulDA+=",";
    	       }
            }
       	    consulDA+= ") and isnull(a.tipo_jerarquia,0) = " + tipoOrganica;

       	    if (userPerfilado){
            	consulDA = consulDA + " and a.rut in ("+ strPersonas  +") ";
            }
       	    
       	    Buscar.exec(consulDA);
            if (Buscar.next()) {
                modelRoot.put("dotacionDA", Buscar.getString("trut"));
                modelRoot.put("num_cargosDA", Buscar.getString("tcargo"));
            } 
            else {
                modelRoot.put("dotacionDA", "0");
                modelRoot.put("num_cargosDA", "0");
            }

            consul = String.valueOf(new StringBuilder("SELECT COUNT(DISTINCT A.rut)total")
            		.append(" FROM eje_ges_trabajador_unidad A INNER JOIN eje_ges_vacaciones_det C ")
            		.append(" ON A.rut = C.rut AND A.wp_cod_empresa = C.empresa")
            		.append(" WHERE	A.unidad IN ")
            		.append(" (SELECT nodo_id FROM eje_ges_jerarquia WHERE nodo_padre='")
            		.append(unidad).append("' OR nodo_id='").append(unidad)
            		.append("') AND YEAR(desde)=YEAR(DATEADD(mm, -2, getdate())) AND MONTH(desde)=MONTH(DATEADD(mm,-2,getdate())) ")
            		.append(" and isnull(a.tipo_jerarquia,0) =").append(tipoOrganica));

            		if (userPerfilado){
            			consul = consul + " and a.rut in ("+ strPersonas  +") ";
		            }
            		consul = consul + " GROUP BY YEAR(desde), MONTH(desde)";
            
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
            String sql = String.valueOf(new StringBuilder("select distinct a.rut_encargado,a.nombre,a.cargo_desc,a.anexo,isnull(a.e_mail,'No Definido') as e_mail, cast(a.rut_encargado as varchar) +'.jpg' as foto,a.mision from view_unidad_encargado a where unid_id='" + unidad +"' and estado=1")); 

            modelRoot.put("unid_desc", ExtrasOrganica.UnidadDesc(unidad,Conexion) );
            
           
            //OutMessage.OutMessagePrint("UEn --> ".concat(String.valueOf(String.valueOf(sql))));
            //consul2.exec(sql);
            try {
				ConsultaData data = ConsultaTool.getInstance().getData(Conexion, sql);
				
				if(data != null) {
					while(data.next()) {
		                modelRoot.put("tiene", "SI");
		                modelRoot.put("rut", 	data.getForcedString("rut_encargado"));
		                modelRoot.put("nombre", data.getForcedString("nombre"));
		                modelRoot.put("cargo", 	data.getForcedString("cargo_desc"));
		                modelRoot.put("anexo", 	data.getForcedString("anexo"));
		                modelRoot.put("e_mail", data.getForcedString("e_mail"));
		                modelRoot.put("foto", 	data.getForcedString("foto"));
		                modelRoot.put("mision", data.getForcedString("mision"));
					}
					
					modelRoot.put("cantJefes", String.valueOf(data.size()));
					
					if(data.size() > 0 ){
						FreemakerTool free = new FreemakerTool();
						data.toStart();
						modelRoot.put("jefes", free.getListData(data));
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            Consulta consul2 = new Consulta(Conexion);
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
            super.retTemplate(resp,"informegestion/gestion_diaria.htm",modelRoot);
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