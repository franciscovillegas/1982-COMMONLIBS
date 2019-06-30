package portal.com.eje.serhumano.search;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.ControlAcceso;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.ExtrasOrganica;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.search:
//            ArmaQueryBusquedaAlfabetica, EjecutaBusquedaAlfabetica

public class S_SearchAlfabeticOrganica extends MyHttpServlet
{

    public S_SearchAlfabeticOrganica()
    {
    	
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170112
//	    Connection Conexion = super.connMgr.getConnection("portal");
	    Connection Conexion = getConnMgr().getConnection("portal");
        ArmaConsulta(req, resp, Conexion);
//    	MMA 20170112
//      super.connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("portal", Conexion);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170112
//	    Connection Conexion = super.connMgr.getConnection("portal");
	    Connection Conexion = getConnMgr().getConnection("portal");
        ArmaConsulta(req, resp, Conexion);
//    	MMA 20170112
//      super.connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("portal", Conexion);
    }

    public void ArmaConsulta(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        if(Conexion != null)
        {
            user = SessionMgr.rescatarUsuario(req);
            if(user.esValido())
            {
                String Operacion = req.getParameter("operacion");
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
            htmls = "buscar/organica/buscar_usuario_organica.html";
        SimpleHash modelRoot = new SimpleHash();

        user = SessionMgr.rescatarUsuario(req);
        String emp = user.getEmpresa();

        SimpleList unidadesList = new SimpleList();
        SimpleHash unidadesIter = new SimpleHash();
        Consulta conUnidades = new Consulta(Conexion);
        String sqlUnidades = "SELECT unid_id, unid_desc FROM dbo.eje_ges_unidades WHERE unid_empresa=" + emp + " ORDER by unid_desc";
        
        conUnidades.exec(sqlUnidades);
        for(; conUnidades.next();unidadesList.add(unidadesIter))
        {
            unidadesIter = new SimpleHash();
            unidadesIter.put("codigo", conUnidades.getString("unid_id"));
            unidadesIter.put("nombre", conUnidades.getString("unid_id") + "-" + conUnidades.getString("unid_desc"));
        }    
        conUnidades.close();            
        modelRoot.put("unidadedes", unidadesList);

        SimpleList cargosList = new SimpleList();
        SimpleHash cargosIter = new SimpleHash();
        Consulta conCargos = new Consulta(Conexion);
        String sqlCargos = "SELECT cargo, descrip FROM dbo.eje_ges_cargos WHERE empresa=" + emp + " ORDER by descrip";
        conCargos.exec(sqlCargos);
        for(; conCargos.next();cargosList.add(cargosIter))
        {
            cargosIter = new SimpleHash();
            cargosIter.put("codigo", conCargos.getString("cargo"));
            cargosIter.put("nombre", conCargos.getString("cargo") + "-" + conCargos.getString("descrip"));
        }    
        conCargos.close();            
        modelRoot.put("cargos", cargosList);        
        
        super.retTemplate(resp,htmls,modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin PagIngreso: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        con = Conexion;
        user = SessionMgr.rescatarUsuario(req);
    	OutMessage.OutMessagePrint("\n**** Inicio DespResultado: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String htmls = null;
        if(req.getParameter("pagina") != null)
            htmls = req.getParameter("pagina");
        else
            htmls = "buscar/organica/buscar_usuario_organica.html";
        SimpleHash modelRoot = new SimpleHash();
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Busqueda Alfab\351tica", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String Bus = "";
            String emp = user.getEmpresa();

            SimpleList unidadesList = new SimpleList();
            SimpleHash unidadesIter = new SimpleHash();
            Consulta conUnidades = new Consulta(con);
            String sqlUnidades = "SELECT unid_id, unid_desc FROM dbo.eje_ges_unidades WHERE unid_empresa=" + emp + " ORDER by unid_desc";
            conUnidades.exec(sqlUnidades);
            for(; conUnidades.next();unidadesList.add(unidadesIter))
            {
                unidadesIter = new SimpleHash();
                unidadesIter.put("codigo", conUnidades.getString("unid_id"));
                unidadesIter.put("nombre", conUnidades.getString("unid_id") + "-" + conUnidades.getString("unid_desc"));
            }    
            conUnidades.close();            
            modelRoot.put("unidadedes", unidadesList);

            SimpleList cargosList = new SimpleList();
            SimpleHash cargosIter = new SimpleHash();
            Consulta conCargos = new Consulta(con);
            String sqlCargos = "SELECT cargo, descrip FROM dbo.eje_ges_cargos WHERE empresa=" + emp + " ORDER by descrip";
            conCargos.exec(sqlCargos);
            for(; conCargos.next();cargosList.add(cargosIter))
            {
                cargosIter = new SimpleHash();
                cargosIter.put("codigo", conCargos.getString("cargo"));
                cargosIter.put("nombre", conCargos.getString("cargo") + "-" + conCargos.getString("descrip"));
            }    
            conCargos.close();            
            modelRoot.put("cargos", cargosList);
           
            Bus = "SELECT distinct rut, digito_ver AS dig, ape_paterno AS apa, ape_materno AS ama, nombres AS nom, unidad AS uni FROM dbo.eje_ges_trabajador tr where";
            //para chequear si el usuario es Administrador corporativo
            ExtrasOrganica extraOrganica= new ExtrasOrganica();
            ArmaQueryBusquedaAlfabeticaOrganica aq;
            if (extraOrganica.isAdministradorCorporativo(user.getRutId(), Conexion))
            	aq = new ArmaQueryBusquedaAlfabeticaOrganica(req, "");
            else
                aq = new ArmaQueryBusquedaAlfabeticaOrganica(req, emp);
            //	
            EjecutaBusquedaAlfabeticaOrganica exec = new EjecutaBusquedaAlfabeticaOrganica(Conexion, Bus + aq.query);
            modelRoot.put("varios", exec.EjecutaBusquedaAlfabeticaOrganica(Conexion, String.valueOf(Bus) + String.valueOf(aq.query)));
        }
        super.retTemplate(resp,htmls,modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin DespResultado: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private ControlAcceso control;
    Connection con;
    
}