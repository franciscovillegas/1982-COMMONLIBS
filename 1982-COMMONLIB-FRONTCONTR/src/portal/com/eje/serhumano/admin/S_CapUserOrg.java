package portal.com.eje.serhumano.admin;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.permiso.PermisoPortal;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.FormatoFecha;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.admin:
//            CapManager

public class S_CapUserOrg extends MyHttpServlet
{

    public S_CapUserOrg()
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
//	    Connection connPortal = super.connMgr.getConnection("portal");
	    Connection connPortal = getConnMgr().getConnection("portal");

        if(connPortal != null )
        {
            user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
            {
                if(user.tieneApp(PermisoPortal.ORG_TOOL_ASIGNAR_TRABAJADOR_UNIDAD))
                    MuestraDatos(user, req, resp, connPortal );
                else
                    super.mensaje.devolverPaginaMensage(resp, "Cap", "No tiene permiso...");
            } else
            {
                super.mensaje.devolverPaginaSinSesion(resp, "Cap", "Tiempo de Sesi\363n expirado...");
            }
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }

//    	MMA 20170112
//      super.connMgr.freeConnection("portal", connPortal);
        getConnMgr().freeConnection("portal", connPortal);
    }

    public void MuestraDatos(Usuario user, HttpServletRequest req, HttpServletResponse resp, Connection connPortal)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n************Entro al doPost de CAP");
        if(SessionMgr.rescatarUsuario(req).esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("GetParam", new GetParam(req));
            modelRoot.put("FF", new FormatoFecha());
            String pRut = req.getParameter("pRut");
            String pUnidadNueva = req.getParameter("selUnidad");
            
            CapManager cm = new CapManager(connPortal);
            String grabar = req.getParameter("grabar");
            if(pRut != null && !"".equals(pRut))
            {
                if(grabar != null && !"".equals(grabar) && "si".equals(grabar))
                {
                	cm.UpdateTrabajador(pRut, user.getEmpresa(), pUnidadNueva);
                }
            }
           
            datosRut userRut = new datosRut(connPortal, pRut);
                       
            modelRoot.put("nuevo", "si");
            modelRoot.put("user", pRut);
            modelRoot.put("dig", cm.getDigRut(pRut, connPortal));
            modelRoot.put("nombre", cm.getNombregRut(pRut, connPortal));
            modelRoot.put("cargo",  userRut.Cargo);
            modelRoot.put("unidad", cm.getUnidadRut(pRut, connPortal, user.getEmpresa()));

            SimpleList unidadesList = new SimpleList(); 
            SimpleHash unidadesIter = new SimpleHash();
            Consulta conUnidades = new Consulta(connPortal);
            //String sqlUnidades = "SELECT distinct unid_id, unid_desc FROM dbo.eje_ges_unidades WHERE unid_id in (select distinct unidad from eje_ges_trabajador where wp_cod_empresa=" + user.getEmpresa() + ") ORDER by unid_desc";
            String sqlUnidades = "SELECT distinct unid_id, unid_desc FROM dbo.eje_ges_unidades ORDER by unid_id";
            conUnidades.exec(sqlUnidades);
            for(; conUnidades.next();unidadesList.add(unidadesIter))
            {
                unidadesIter = new SimpleHash();
                unidadesIter.put("codigo", conUnidades.getString("unid_id"));
                unidadesIter.put("nombre", conUnidades.getString("unid_id") + "-" + conUnidades.getString("unid_desc"));
            }    
            conUnidades.close();            
            modelRoot.put("unidadedes", unidadesList);
            super.retTemplate(resp,"admin/cap_usuario_organica.htm",modelRoot);
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost Cap");
    }

    private Usuario user;
    private ResourceBundle proper;
}