package portal.com.eje.tools;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.search.ArmaQueryBusquedaAlfabetica;
import portal.com.eje.serhumano.search.EjecutaBusquedaAlfabetica;
import portal.com.eje.usuario.Usuario;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.tools:
//            OutMessage, DBConnectionManager, Mensaje, Tools

public class procesando extends MyHttpServlet {

    public procesando()
    {
    }

   
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        Connection Conexion = connMgr.getConnection("portal");
        doGet(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        Connection Conexion = connMgr.getConnection("portal");
        generaTab(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws IOException, ServletException
    {
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutConsultado();
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Datos Previsionales", "Tiempo de Sesi\363n expirado...");
        } else
        {
            OutMessage.OutMessagePrint("\nProcesando....");
            SimpleHash modelRoot = new SimpleHash();

            if(req.getParameter("buscar") != null)
            {
                String Bus = "SELECT Distinct rut,digito,rtrim(cargo) as cargo,digito, rtrim(nombres) as nombres,empresa, rtrim(ape_paterno) as ape_paterno, rtrim(ape_materno) as ape_materno, rtrim(unid_desc) as unid_desc,     unid_id FROM view_busqueda where tipo='R' and (empresa = '" + req.getParameter("empresa") + "') ";
                ArmaQueryBusquedaAlfabetica aq = new ArmaQueryBusquedaAlfabetica(req);
                if(!"".equals(aq.query))
                {
                    EjecutaBusquedaAlfabetica exec = new EjecutaBusquedaAlfabetica(Conexion, Bus + aq.query);
                    OutMessage.OutMessagePrint("query buscar: " + Bus + aq.query);
                    modelRoot.put("varios", exec.getSimpleList());
                    modelRoot.put("query", "1");
                }
            }
            super.retTemplate(resp,"procesando.html",modelRoot);
            OutMessage.OutMessagePrint("Fin de doPost");
        }
    }

    private Usuario user;
}