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

public class S_Empleados extends MyHttpServlet
{

    public S_Empleados()
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
        String unidad = req.getParameter("unidad");
        String empresa = req.getParameter("emp");
        String nombres = req.getParameter("nombres");
        String region = req.getParameter("region");
        OutMessage.OutMessagePrint("U ".concat(String.valueOf(String.valueOf(unidad))));
        OutMessage.OutMessagePrint("E ".concat(String.valueOf(String.valueOf(empresa))));
        OutMessage.OutMessagePrint("R ".concat(String.valueOf(String.valueOf(region))));
        OutMessage.OutMessagePrint("N ".concat(String.valueOf(String.valueOf(nombres))));
        if(condf != null)
        {
            if(!user.esValido())
            {
                super.mensaje.devolverPaginaSinSesion(resp, "Lista de Empleados", "Tiempo de Sesi\363n expirado...");
            } else
            {
                Consulta consul2 = null;
                Consulta consul3 = null;
                if(region != null && !"".equals(region))
                {
                    RegionUnidad ru = new RegionUnidad(conexion);
                    modelRoot.put("empresas", ru.ConsulEmpresas().getSimpleList());
                    ru.GetDatosRegion(region);
                    modelRoot.put("reg_nombre", ru.getRegion());
                    if(req.getParameter("emp") != null)
                        consul2 = (new RegionUnidad(conexion)).ConsulColaboradores(unidad, nombres, region, 0, empresa);
                    else
                        consul2 = (new RegionUnidad(conexion)).ConsulColaboradores(unidad, nombres, region, 0);
                    modelRoot.put("rut", consul2.getSimpleList());
                    consul2.close();
                }
                modelRoot.put("GetParam", new GetParam(req));
            }
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "problemas T\351nicos", "Error en la Conexi\363n.");
        }
        try
        {
        	super.retTemplate(resp,"mapa/empleados.htm",modelRoot);
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