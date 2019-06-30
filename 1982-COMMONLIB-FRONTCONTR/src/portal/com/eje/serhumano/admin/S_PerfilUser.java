package portal.com.eje.serhumano.admin;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.admin:
//            CapManager

public class S_PerfilUser extends MyHttpServlet
{

    public S_PerfilUser()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170112
//      Connection ConDf = super.connMgr.getConnection("portal");
//      Connection ConWf = super.connMgr.getConnection("portal");
//      Connection ConSh = super.connMgr.getConnection("portal");
	    Connection ConDf = getConnMgr().getConnection("portal");
	    Connection ConWf = getConnMgr().getConnection("portal");
	    Connection ConSh = getConnMgr().getConnection("portal");
        if(ConDf != null && ConSh != null)
        {
            user = SessionMgr.rescatarUsuario(req);
            if(!user.esValido())
                super.mensaje.devolverPaginaSinSesion(resp, "Cap", "Tiempo de Sesi\363n expirado...");
            else
            if(!user.tieneApp("adm"))
                super.mensaje.devolverPaginaMensage(resp, "", "No tiene permiso...");
            else
                MuestraDatos(user, req, resp, ConDf, ConSh, ConWf);
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
//    	MMA 20170112
//      super.connMgr.freeConnection("portal", ConDf);
//      super.connMgr.freeConnection("portal", ConSh);
//      super.connMgr.freeConnection("portal", ConWf);
        getConnMgr().freeConnection("portal", ConDf);
        getConnMgr().freeConnection("portal", ConSh);
        getConnMgr().freeConnection("portal", ConWf);
    }

    public void MuestraDatos(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection ConDf, Connection Consh, Connection ConWf)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n************Entro al doPost de CAP");
        if(SessionMgr.rescatarUsuario(req).esValido())
        {
            CapManager cm = new CapManager(Consh);
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("GetParam", new GetParam(req));
            String pRut = req.getParameter("pRut");
            String grabar = req.getParameter("grabar");
            System.err.println(String.valueOf(String.valueOf((new StringBuilder("-->pRut: ")).append(pRut).append("   --->grabar: ").append(grabar))));
            datosRut dataRut = new datosRut(ConDf, pRut);
            String app_id = "sh";
            modelRoot.put("nombre", dataRut.Nombres);
            dataRut = new datosRut(ConDf, user.getRutId());
            modelRoot.put("nom_adm", dataRut.Nombres);
            if(req.getParameter("app_id") != null && !"".equals(req.getParameter("app_id")))
            {
                app_id = req.getParameter("app_id");
                modelRoot.put("lista", cm.getRoles(app_id).getSimpleList());
                if(pRut != null && !"".equals(pRut))
                {
                    if(grabar != null && !"".equals(grabar) && "si".equals(grabar))
                    {
                        int x = 9999;
                        if(req.getParameter("estado") != null && !"".equals(req.getParameter("estado")))
                            x = Integer.parseInt(req.getParameter("estado"));
                        cm.UpdateRolUserApp(Integer.parseInt(pRut), x, req.getParameter("perfil"), app_id);
                        if("wfv".equals(app_id))
                            cm.UpdateRolUserWorkFlow(Integer.parseInt(pRut), app_id, ConWf);
                    }
                    Consulta nn = null;
                    nn = cm.getRolUser(pRut, app_id);
                    if(nn.next())
                    {
                        modelRoot.put("rol_id", nn.getString("rol_id"));
                        modelRoot.put("vigente", nn.getString("vigente"));
                    }
                    nn.close();
                }
            }
            super.retTemplate(resp,"admin/perfil_sh.htm",modelRoot);
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost Cap");
    }

    private Usuario user;
    private ResourceBundle proper;
}