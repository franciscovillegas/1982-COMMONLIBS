package portal.com.eje.consolidadorems;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.consolidadorems:
//            DataInformeMgr

public class ServletGeneraInformeRems extends MyHttpServlet
{

    public ServletGeneraInformeRems()
    {
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "ERROR de Sesi&oacute;n", "Su sesi&oacute;n ha expirado o no se ha logeado.");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            conexion = super.connMgr.getConnection("portal");
            if(conexion == null)
            {
                super.mensaje.devolverPaginaMensage(resp, "mensaje.htm", "Problemas T\351cnicos", "Errores en la Conexi\363n.");
            } else
            {
                periodo = req.getParameter("periodo");
                DataInformeMgr obj = new DataInformeMgr(conexion);
                modelRoot = obj.generaInformeConsolidado(periodo);
                super.retTemplate(resp,"consolidrems/consolidadorems.htm",modelRoot);
            }
            super.connMgr.freeConnection("portal", conexion);
            OutMessage.OutMessagePrint("\n**** FIN doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
    }

    private void jbInit()
        throws Exception
    {
    }

    private Usuario user;
    private Connection conexion;
    private String periodo;
}