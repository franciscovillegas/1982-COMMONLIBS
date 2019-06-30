package organica.com.eje.ges.Buscar;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.datos.Unidades;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.Buscar:
//            Listas, ArmaQueryBusquedaAlfabetica, EjecutaBusaquedaAlfabetica

public class Buscar_por_Nombre2 extends MyHttpServlet
{

    public Buscar_por_Nombre2()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        ArmaConsulta(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void ArmaConsulta(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            control = new ControlAcceso(user);
            x = new Unidades();
            int cual = Integer.parseInt(req.getParameter("Operacion"));
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
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("---->Termino!!!!!!!!!!*****************");
    }

    private void PagIngreso(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        String htmls = null;
        if(req.getParameter("pagina") != null)
            htmls = req.getParameter("pagina");
        else
            htmls = "Gestion/Buscar/form_busqueda.html";
        SimpleHash modelRoot = new SimpleHash();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Busqueda", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String Query = "";
            Listas Uni = new Listas();
            Listas Emp = new Listas();
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("FNum", new FormatoNumero());
            Query = "SELECT empresa AS id,descrip AS empresa FROM eje_ges_empresa order by id";
            modelRoot.put("emp", Emp.getSimpleList(Conexion, Query));
            Query = "SELECT unid_empresa AS empresa, unid_id, unid_desc FROM eje_ges_unidades where vigente='S'  ORDER BY unid_empresa, unid_desc";
            modelRoot.put("uni", Uni.getSimpleList(Conexion, Query));
            Query = "SELECT empresa, cargo, RTRIM(descrip) as descrip FROM eje_ges_cargos where vigente='S' ORDER BY empresa, descrip";
            modelRoot.put("car", Uni.getSimpleList(Conexion, Query));
        }
        super.retTemplate(resp,htmls,modelRoot);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        String htmls = null;
        if(req.getParameter("pagina") != null)
            htmls = req.getParameter("pagina");
        else
            htmls = "Gestion/Buscar/Desp_busqueda.html";
        SimpleHash modelRoot = new SimpleHash();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Busqueda Alfab\351tica", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String Query = "";
            String Bus = "";
            String nombre = "";
            String empresa = "";
            Listas Uni = new Listas();
            Listas Emp = new Listas();
            Query = "SELECT empresa AS id,descrip AS empresa FROM eje_ges_empresa order by id";
            modelRoot.put("emp", Emp.getSimpleList(Conexion, Query));
            Query = "SELECT unid_empresa AS empresa, unid_id, unid_desc FROM eje_ges_unidades where vigente='S'  ORDER BY unid_empresa, unid_desc";
            modelRoot.put("uni", Uni.getSimpleList(Conexion, Query));
            Query = "SELECT empresa, cargo, RTRIM(descrip) as descrip FROM eje_ges_cargos where vigente='S' ORDER BY empresa, descrip";
            modelRoot.put("car", Uni.getSimpleList(Conexion, Query));
            nombre = req.getParameter("nombre");
            modelRoot.put("unidad", req.getParameter("unidad"));
            modelRoot.put("empresa", req.getParameter("empresa"));
            modelRoot.put("cargo", req.getParameter("cargo"));
            if(req.getParameter("empresa") != null)
                empresa = req.getParameter("empresa");
            String tipo = null;
            Bus = "SELECT Distinct rut,digito,rtrim(cargo) as cargo, rtrim(nombres) as nombres,empresa_trab,rtrim(ape_paterno) as ape_paterno, rtrim(ape_materno) as ape_materno, rtrim(unid_desc) as unid_desc, unid_id,foto FROM view_ges_busqueda where tipo='R' ";
            System.err.println("---->busqueda Query: ".concat(String.valueOf(String.valueOf(Bus))));
            if(!"".equals(empresa))
                Bus = String.valueOf(Bus) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" and (empresa = '")).append(empresa).append("') "))));
            ArmaQueryBusquedaAlfabetica aq = new ArmaQueryBusquedaAlfabetica(req);
            EjecutaBusaquedaAlfabetica exec = new EjecutaBusaquedaAlfabetica(Conexion, String.valueOf(Bus) + String.valueOf(aq.query));
            modelRoot.put("varios", exec.getSimpleList());
        }
        super.retTemplate(resp,htmls,modelRoot);
    }

    private Usuario user;
    private ControlAcceso control;
    private Unidades x;
    private Mensaje mensaje;
}