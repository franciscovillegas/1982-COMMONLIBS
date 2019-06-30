package portal.com.eje.serhumano.formularios.tracking;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.permiso.PermisoPortal;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.FormatoFecha;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.formularios.tracking:
//            TrackManager

public class S_InfoSolTrack extends MyHttpServlet
{

    public S_InfoSolTrack()
    {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170111
//    	Connection Conexion = connMgr.getConnection("portal");
        Connection Conexion =  getConnMgr().getConnection("portal");
        if(Conexion != null)
        {
            Usuario user = SessionMgr.rescatarUsuario(req);
            if(!user.esValido())
                super.mensaje.devolverPaginaSinSesion(resp, "Formulario Tracking", "Tiempo de Sesi\363n expirado...");
            else
                if(!user.tieneApp(PermisoPortal.ADMIN_GET_INFO_TRACKING) ) 
                	super.mensaje.devolverPaginaMensage(resp, "Administrar Tracking", "No tiene permiso...");
            else
                generaPagina(req, resp, Conexion, user);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
//    	MMA 20170111
//      super.connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("porral", Conexion);
    }

    protected void generaPagina(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String filtro = "";
        String pHtm = "tracking/info_main.html";
        TrackManager tMgr = new TrackManager(conexion);
        SimpleHash modelRoot = new SimpleHash();
        Consulta consul = tMgr.getTracking();
        modelRoot.put("tracking", consul.getSimpleList());
        consul.close();
        modelRoot.put("FFecha", new FormatoFecha());
        retTemplate(resp, pHtm, modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }
}