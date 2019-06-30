package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.gestion.dimension.ExtrasOrganica;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class UnidadRelativa extends MyHttpServlet {
	
    public UnidadRelativa() { 
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
            valida = new Validar();
            insTracking(req, "Unidad Relativa".intern(),null );
            SimpleHash modelRoot = new SimpleHash();
            String unidad = valida.validarDato(req.getParameter("unidad"),"0");
            String empresa = valida.validarDato(req.getParameter("empresa"),ExtrasOrganica.EmpresaPadre(Conexion));
            String rut = valida.validarDato(req.getParameter("trabajador"),"-1");
            String unidadRel = valida.validarDato(req.getParameter("unidadR"),"-1");
            String unidadBR = valida.validarDato(req.getParameter("unidadBR"),"-1");
            String tipo = valida.validarDato(req.getParameter("tipo"),"U");
            String operacion = valida.validarDato(req.getParameter("operacion"),"NULA");
            UnidadRelativa_Manager umgr = new UnidadRelativa_Manager(Conexion);
            if(operacion.equals("GR")) {
            	umgr.grabaUnidadRelativa(rut,unidadRel,ExtrasOrganica.EmpresaPadre(Conexion),ExtrasOrganica.PlantaTrabajador(rut,Conexion),tipo);	
            }
            else if(operacion.equals("BR")) {
            	umgr.borraUnidadRelativa(rut,unidadBR);	
            }
            modelRoot.put("unidad", unidad);
            modelRoot.put("empresa", empresa);
            modelRoot.put("rut", rut);
            modelRoot.put("unidadrel", unidadRel);
            modelRoot.put("trabajador", umgr.getSimpleSecuences(umgr.GetTrabajadores(unidad)));
            modelRoot.put("unidades", umgr.getSimpleSecuences(umgr.GetUnidadesxRut(rut)));
            modelRoot.put("unidadesR", umgr.getSimpleSecuences(umgr.GetUnidadesRelativasRut(rut)));
            super.retTemplate(resp,"Gestion/Asignar_UniRel/UnidadRelativa.htm",modelRoot);
        } 
        else {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Validar valida;
    private Mensaje mensaje;
}