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

public class S_DatosPrev extends MyHttpServlet {

    public S_DatosPrev() {
    }

    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException {
        doGet(httpservletrequest, httpservletresponse);
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException {
    	
        Usuario user = SessionMgr.rescatarUsuario(httpservletrequest); 
//    	MMA 20170111
//    	Connection connection =  connMgr.getConnection(user.getJndi());
    	Connection connection =  getConnMgr().getConnection(user.getJndi());
        
        if(connection != null) {
            String rutOrg = httpservletrequest.getParameter("rut_org")==null ? "false" : httpservletrequest.getParameter("rut_org");
            MuestraDatos(httpservletrequest, httpservletresponse, connection,rutOrg);
            insTracking(httpservletrequest, "Datos Previsionales".intern(), null);
        } 
        else {
            mensaje.devolverPaginaMensage(httpservletresponse, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        
//    	MMA 20170111
//      connMgr.freeConnection(user.getJndi(), connection);
        getConnMgr().freeConnection(user.getJndi(), connection);

    }

    public void MuestraDatos(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Connection connection,String rutOrg)
        throws ServletException, IOException {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(httpservletrequest);
        String rut = ( !rutOrg.equals("false") )? rutOrg : user.getRutId();
        System.out.println("RUT------------>" + user.getRutId());
        String template = "menu/DatosPrev.htm";
        if(httpservletrequest.getParameter("htm") != null) {
        	template = httpservletrequest.getParameter("htm");
        }
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash simplehash = fp.getDatosPrev(connection,rut);
        simplehash.put("ir", fp.getMuestraInfoRut(connection, rut));
        
        IOClaseWeb io = new IOClaseWeb(this, httpservletrequest, httpservletresponse);
        
        super.retTemplate(io,  template,simplehash);
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
}