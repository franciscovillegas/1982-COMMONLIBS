package portal.com.eje.serhumano.menu;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.menu:
//            menuManager, Tools

public class S_Admtiempo extends MyHttpServlet
{

    public S_Admtiempo()
    {
    }

    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doGet(httpservletrequest, httpservletresponse);
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
//    	MMA 20170111
//    	Connection connection = connMgr.getConnection("portal");
        Connection connection =  getConnMgr().getConnection("portal");
        if(connection != null)
        {
            String rutOrg = httpservletrequest.getParameter("rut_org")==null?"false":httpservletrequest.getParameter("rut_org");
        	MuestraDatos(httpservletrequest, httpservletresponse, connection,rutOrg);
            insTracking(httpservletrequest, "Horas Extras".intern(), null);
        } else
        {
            mensaje.devolverPaginaMensage(httpservletresponse, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
//    	MMA 20170111
//      connMgr.freeConnection("portal", connection);
        getConnMgr().freeConnection("portal", connection);
    }

    public void MuestraDatos(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Connection connection,String rutOrg)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(httpservletrequest);
        String rut = ( !rutOrg.equals("false") )? rutOrg : user.getRutId();
        System.out.println("RUT------------>" + user.getRutId());
        String template = "menu/CartolaHorasExtras.htm";
        if(httpservletrequest.getParameter("htm") != null)
            template = httpservletrequest.getParameter("htm");
        FichaPersonalBean fp = FichaPersonalBean.getInstance();
        SimpleHash simplehash = fp.getAdmTiempo(connection,rut);
        super.retTemplate(httpservletresponse,template,simplehash);
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Tools tool;
    private Usuario user;
    private ResourceBundle proper;
}