package portal.com.eje.serhumano.menu;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.menu:
//            menuManager, Tools

public class S_LicenciasMedicas extends MyHttpServlet {

    public S_LicenciasMedicas() {
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException {
//    	MMA 20170111
//    	Connection connection = connMgr.getConnection("portal");
        Connection connection =  getConnMgr().getConnection("portal");
        if(connection != null) {
            String rutOrg = httpservletrequest.getParameter("rut_org")==null?"false":httpservletrequest.getParameter("rut_org");
            MuestraDatos(httpservletrequest, httpservletresponse, connection,rutOrg);
            insTracking(httpservletrequest, "Licencias Medicas".intern(), null);
        } 
        else
            mensaje.devolverPaginaMensage(httpservletresponse, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
//    	MMA 20170111
//      connMgr.freeConnection("portal", connection);
        getConnMgr().freeConnection("portal", connection);
    }

    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException {
        doGet(httpservletrequest, httpservletresponse);
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection connection,String rutOrg)
        throws ServletException, IOException {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        String rut = ( !rutOrg.equals("false") )? rutOrg : user.getRutId();
        System.out.println("RUT------------>" + user.getRutId());
        String template = "menu/LicMedicas.htm";
        if(req.getParameter("htm") != null)
            template = req.getParameter("htm");
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash simplehash = fp.getLicenciasMedicas(connection,rut);
        simplehash.put("dc", fp.getDatosContratos(connection,rut));
        
        IOClaseWeb io = new IOClaseWeb(this, req, resp);
        super.retTemplate(io,template,simplehash);
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
}