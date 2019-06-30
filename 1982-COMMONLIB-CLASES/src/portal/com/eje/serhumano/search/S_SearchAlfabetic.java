package portal.com.eje.serhumano.search;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.ControlAcceso;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.search:
//            ArmaQueryBusquedaAlfabetica, EjecutaBusquedaAlfabetica

public class S_SearchAlfabetic extends MyHttpServlet
{

    public S_SearchAlfabetic()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        ArmaConsulta(req, resp, Conexion);
        super.connMgr.freeConnection("portal", Conexion);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        ArmaConsulta(req, resp, Conexion);
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void ArmaConsulta(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        if(Conexion != null)
        {
            user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
            {
                int cual = 0;
                if(req.getParameter("Operacion") != null)
                    cual = Integer.parseInt(req.getParameter("Operacion"));
                else
                    cual = 1;
                OutMessage.OutMessagePrint("---->Inicio cual: ".concat(String.valueOf(String.valueOf(cual))));
                switch(cual)
                {
                case 1: // '\001'
                    PagIngreso(req, resp, Conexion);
                    break;

                case 2: // '\002'
                    DespResultado(req, resp, Conexion);
                    break;
                }
            } else
            {
                super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
            }
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "problemas T\351nicos", "Error en la Conexi\363n.");
        }
    }

    private void PagIngreso(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio PagIngreso: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String htmls = null;
        if(req.getParameter("pagina") != null)
            htmls = req.getParameter("pagina");
        else
            htmls = "buscar/buscar.html";
        SimpleHash modelRoot = new SimpleHash();
        super.retTemplate(resp,htmls,modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin PagIngreso: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio DespResultado: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String htmls = null;
        if(req.getParameter("pagina") != null)
            htmls = req.getParameter("pagina");
        else
            htmls = "buscar/buscar.html";
        SimpleHash modelRoot = new SimpleHash();
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Busqueda Alfab\351tica", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String Bus = "";
            Bus = "SELECT distinct rut, digito_ver AS dig, ape_paterno AS apa, ape_materno AS ama, nombres AS nom FROM   dbo.eje_ges_trabajador tr where";
            ArmaQueryBusquedaAlfabetica aq = new ArmaQueryBusquedaAlfabetica(req);
            EjecutaBusquedaAlfabetica exec = new EjecutaBusquedaAlfabetica(Conexion, Bus + aq.query);
            modelRoot.put("varios", exec.EjecutaBusquedaAlfabetica(Conexion, String.valueOf(Bus) + String.valueOf(aq.query)));
        }
        super.retTemplate(resp,htmls,modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin DespResultado: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private ControlAcceso control;
}