package organica.com.eje.ges.organigrama;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.ges.Buscar.EncargadoUnidad;
import organica.com.eje.ges.Buscar.FotosPersonalUnidad;
import organica.com.eje.ges.gestion.dimension.ExtrasOrganica;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class GestionMenu extends MyHttpServlet {

    public GestionMenu() {
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
        portal.com.eje.serhumano.user.Usuario userPortal =  portal.com.eje.serhumano.user.SessionMgr.rescatarUsuario(req);
        
        if(user.esValido()) {
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
            String consul = String.valueOf((new StringBuilder("SELECT COUNT(DISTINCT A.rut) AS dotacion, COUNT(DISTINCT A.cargo) AS total, B.unid_desc")
            		.append(" FROM view_dotacion_directa_unidad_pertenencia A")
            		.append(" INNER JOIN eje_ges_unidades B ON A.unidad = B.unid_id")
            		.append(" WHERE (A.empresa='"))
            		.append(empresa).append("') AND (tipo = 'U') AND (unidad = '")
            		.append(unidad).append("')").append(" GROUP BY B.unid_desc"));
            Buscar.exec(consul);
            if (Buscar.next()) {
                modelRoot.put("unid_desc", Buscar.getString("unid_desc"));
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
            String sql = String.valueOf(new StringBuilder("select a.rut_encargado,a.nombre,a.cargo_desc,a.anexo,a.e_mail, cast(a.rut_encargado as varchar) +'.jpg' as foto,a.mision from view_unidad_encargado a where unid_id='" + unidad +"' and estado=1")); 
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
            
            String area = req.getParameter("area");
            System.out.println("EL AREA : --------> " + area);
            modelRoot.put("area", area);
            if(area.equals("Gestion")) {
            	modelRoot.put("Gestion","1");
            }
            else if(area.equals("Dotacion")) {
            	modelRoot.put("Dotacion","1");
            }
            else if(area.equals("Capacitacion")) {
            	modelRoot.put("Capacitacion","1");
            }
            else if(area.equals("Reportes")) {
            	modelRoot.put("Reportes","1");
            }

            boolean isJefe = ExtrasOrganica.isEncargadoUnidad(user.getRutUsuario(), Conexion);
            boolean isGerente = ExtrasOrganica.isGerente(user.getRutUsuario(), Conexion);
            if(isJefe && isGerente) {
            	modelRoot.put("activaMis","1");
            }
            
            // TRACKING ORGANICA
            if (unidad != null) {
            	insTracking(req, "Organica".intern() ,null);
            }
            // FIN 
            
            consul2.close();
            super.retTemplate(resp,"Gestion/InfoUsuario/Jerarquia/menu.htm",modelRoot);
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
}