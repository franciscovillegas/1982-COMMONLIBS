package portal.com.eje.serhumano.unidad;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.unidad:
//            RegionUnidad

public class S_Region_Sucursal extends MyHttpServlet
{

    public S_Region_Sucursal()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection conexion = super.connMgr.getConnection("portal");
        java.sql.Connection condf = super.connMgr.getConnection("portal");
        SimpleHash modelRoot = new SimpleHash();
        user = SessionMgr.rescatarUsuario(req);
        String region = req.getParameter("region");
        String unidad = req.getParameter("unidad");
        String suc_name = req.getParameter("suc_name");
        String empresa = req.getParameter("empresa");
        String emp = req.getParameter("emp");
        if(req.getParameter("emp") != null)
            empresa = emp;
        OutMessage.OutMessagePrint("R ".concat(String.valueOf(String.valueOf(region))));
        OutMessage.OutMessagePrint("U ".concat(String.valueOf(String.valueOf(unidad))));
        OutMessage.OutMessagePrint("E ".concat(String.valueOf(String.valueOf(empresa))));
        modelRoot.put("empresa", empresa);
        if(conexion != null)
        {
            if(!user.esValido())
            {
                mensaje.devolverPaginaSinSesion(resp, "Sucursales", "Tiempo de Sesi\363n expirado...");
            } else
            {
                Consulta consul1 = null;
                Consulta consul2 = null;
                RegionUnidad ru = new RegionUnidad(conexion);
                modelRoot.put("empresas", ru.ConsulEmpresas().getSimpleList());
                if(region != null && !"".equals(region))
                {
                    ru.GetDatosRegion(region);
                    modelRoot.put("reg_nombre", ru.getRegion());
                    if(empresa != null && !"".equals(empresa))
                    {
                        consul1 = ru.ConsulUnidad(region, empresa, suc_name);
                        modelRoot.put("unidades", consul1.getSimpleList());
                        consul1.close();
                    }
                }
                modelRoot.put("GetParam", new GetParam(req));
            }
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "problemas T\351nicos", "Error en la Conexi\363n.");
        }
        try
        {
        	super.retTemplate(resp,"mapa/region_sucursal.htm",modelRoot);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        super.connMgr.freeConnection("portal", condf);
        super.connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("\n**** Fin doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    private Usuario user;
    private Mensaje mensaje;
}