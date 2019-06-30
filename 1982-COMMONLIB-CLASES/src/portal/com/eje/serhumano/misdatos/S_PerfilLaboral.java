package portal.com.eje.serhumano.misdatos;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

public class S_PerfilLaboral extends MyHttpServlet {

	public S_PerfilLaboral() {
	}

    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    	throws IOException, ServletException {
    	doGet(httpservletrequest, httpservletresponse);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException, ServletException {
    	
        Connection connection = connMgr.getConnection("portal");
        if(connection != null)
        {
            String rutOrg = req.getParameter("rut_org")==null?"false":req.getParameter("rut_org");
        	PerfilLaboral(req, resp, connection,rutOrg);
            insTracking(req, "Perfil Laboral".intern(), null);
        } else
        {
            mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        connMgr.freeConnection("portal", connection);
    }
    
    public void PerfilLaboral (HttpServletRequest req, HttpServletResponse resp,Connection connection,String rutOrg)
	throws IOException, ServletException {
        String periodo = "";
        user = SessionMgr.rescatarUsuario(req);
        String rut = ( !rutOrg.equals("false") )? rutOrg : user.getRutId();
        
        String empresa = "-1";
        String sql = "SELECT wp_cod_empresa FROM eje_ges_trabajador WHERE rut = " + rut;
        Consulta consul = new Consulta(connection);
        consul.exec(sql);
        if( consul.next() )
        	empresa = consul.getString("wp_cod_empresa");
        consul.close();
        
        System.out.println("RUT------------>" + user.getRutId());
        String template = "menu/PerfilLaboral.htm";
        if(req.getParameter("htm") != null)
            template = req.getParameter("htm");

    	FichaPersonalBean fp = FichaPersonalBean.getInstance();
    	SimpleHash datosPersonales = fp.getMuestraInfoRut(connection,rut);
    	SimpleHash datosContrato = fp.getDatosContratos(connection,rut);
    	SimpleHash grupoFamiliar = fp.getGrupoFamiliar(connection,rut);
    	SimpleHash licencia = fp.getLicenciasMedicas(connection,rut);
    	SimpleHash datosPrev = fp.getDatosPrev(connection,rut);
    	SimpleHash datosVac = fp.getVacaciones(connection,user,rut);
    	
        sql = "SELECT max(periodo) as periodo FROM eje_ges_certif_histo_liquidacion_cabecera WHERE rut =" + rut; 
        consul = new Consulta(connection);
        consul.exec(sql);
        if( consul.next() )
        	periodo = consul.getString("periodo");
        consul.close();
        fp = FichaPersonalBean.getInstance();
    	SimpleHash liquida = fp.getDatosLiquidacion(connection,getServletContext().getRealPath(getTemplatePath()),periodo,18,rut, req);
    	
    	SimpleHash contenedor = new SimpleHash();
    	contenedor.put("ir",datosPersonales);
    	contenedor.put("dc",datosContrato);
    	contenedor.put("gf",grupoFamiliar);
    	contenedor.put("lm",licencia);
    	contenedor.put("dp",datosPrev);
    	contenedor.put("dv",datosVac);
    	contenedor.put("li",liquida);
    	super.retTemplate(resp,template,contenedor);
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
}
