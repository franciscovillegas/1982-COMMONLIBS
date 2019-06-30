package portal.com.eje.serhumano.unidad;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.unidad:
//            RegionUnidad

public class S_Region_Unidades extends MyHttpServlet
{

    public S_Region_Unidades()
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
        String empresa = req.getParameter("empresa");
        OutMessage.OutMessagePrint("R ".concat(String.valueOf(String.valueOf(region))));
        OutMessage.OutMessagePrint("U ".concat(String.valueOf(String.valueOf(unidad))));
        OutMessage.OutMessagePrint("E ".concat(String.valueOf(String.valueOf(empresa))));
        if(conexion != null)
        {
            if(!user.esValido())
            {
                super.mensaje.devolverPaginaSinSesion(resp, "Detalle de Mapa", "Tiempo de Sesi\363n expirado...");
            } else
            {
                Consulta consul1 = null;
                Consulta consul2 = null;
                RegionUnidad ru = new RegionUnidad(conexion);
                ru.GetDatosRegion(region);
                modelRoot.put("uni", ru.getTotalUnidades());
                modelRoot.put("emp", ru.getTotalEmpleaos());
                modelRoot.put("cos", ru.getTotalCosto());
                modelRoot.put("empresas", ru.ConsulEmpresas().getSimpleList());
                if(region != null && !"".equals(region) && empresa != null && !"".equals(empresa))
                {
                    consul1 = ru.ConsulUnidad(region, empresa);
                    modelRoot.put("unidades", consul1.getSimpleList());
                    consul1.close();
                    if(unidad != null && !"".equals(unidad))
                    {
                        consul2 = ru.ConsulColaboradores(condf, unidad);
                        modelRoot.put("rut", consul2.getSimpleList());
                        consul2.close();
                    }
                }
                modelRoot.put("getParam", new GetParam(req));
            }
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "problemas T\351nicos", "Error en la Conexi\363n.");
        }
        try
        {
        	super.retTemplate(resp,"user/detallemapa.htm",modelRoot);
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
}