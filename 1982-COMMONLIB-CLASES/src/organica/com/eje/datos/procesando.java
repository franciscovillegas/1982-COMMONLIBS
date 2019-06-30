package organica.com.eje.datos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.web.datos.DBConnectionManager;

import organica.com.eje.ges.Buscar.ArmaQueryBusquedaAlfabetica;
import organica.com.eje.ges.Buscar.EjecutaBusaquedaAlfabetica;
import organica.com.eje.ges.Buscar.Listas;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import organica.tools.Tools;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

// Referenced classes of package com.eje.datos:
//            DBConnectionManager, Mensaje

public class procesando extends MyHttpServlet
{

    public procesando() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        doGet(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        generaTab(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Datos Previsionales", "Tiempo de Sesi\363n expirado...");
        } else
        {
            OutMessage.OutMessagePrint("\nProcesando....");

            resp.setContentType("text/html");
            resp.setHeader("Expires", "0");
            resp.setHeader("Pragma", "no-cache");

            SimpleHash modelRoot = new SimpleHash();
            lista = new Listas();
            if(req.getParameter("buscar") != null)
            {
                String Bus = String.valueOf(String.valueOf((new StringBuilder("SELECT Distinct rut,digito,rtrim(cargo) as cargo,digito, rtrim(nombres) as nombres,empresa, rtrim(ape_paterno) as ape_paterno, rtrim(ape_materno) as ape_materno, rtrim(unid_desc) as unid_desc,     unid_id FROM view_ges_busqueda where tipo='R' and (empresa = '")).append(req.getParameter("empresa")).append("') ")));
                ArmaQueryBusquedaAlfabetica aq = new ArmaQueryBusquedaAlfabetica(req);
                if(!"".equals(aq.query))
                {
                    EjecutaBusaquedaAlfabetica exec = new EjecutaBusaquedaAlfabetica(Conexion, String.valueOf(Bus) + String.valueOf(aq.query));
                    OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("query buscar: ")).append(Bus).append(aq.query))));
                    modelRoot.put("varios", exec.getSimpleList());
                    modelRoot.put("query", "1");
                }
            }
            super.retTemplate(resp,"Gestion/procesando.html",modelRoot);
            
            OutMessage.OutMessagePrint("Fin de doPost");
        }
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
    private Listas lista;
}