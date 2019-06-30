package organica.com.eje.ges.usuario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.usuario:
//            Usuario, ControlAcceso

public class AuditoriaUsuario extends MyHttpServlet
{

    public AuditoriaUsuario()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\n>>---> Entro al doGet de AuditoriaUsuario");
        user = Usuario.rescatarUsuario(req);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        else
        if(!(new ControlAcceso(user)).tienePermiso("df_audit_usu"))
        {
            mensaje.devolverPaginaMensage(resp, "", "Usted no tiene permiso para realizar esta acci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            SimpleHash modelRoot = new SimpleHash();
            String sql = "SELECT id, nombre, valor FROM eje_ges_parametros";
            consul.exec(sql);
            for(; consul.next(); modelRoot.put("param_".concat(String.valueOf(String.valueOf(consul.getInt("id")))), consul.getString("valor")));
            consul.close();
            super.retTemplate(resp,"Gestion/Usuarios/AuditoriaUsuario.htm",modelRoot);
        }
        connMgr.freeConnection("portal", conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\n>>---> Entro al doPost de AuditoriaUsuario");
        user = Usuario.rescatarUsuario(req);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        else
        if(!(new ControlAcceso(user)).tienePermiso("df_audit_usu"))
        {
            mensaje.devolverPaginaMensage(resp, "", "Usted no tiene permiso para realizar esta acci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_parametros SET valor = '")).append(req.getParameter("rotacion")).append("' WHERE (id = 1)")));
            consul.insert(sql);
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_parametros SET valor = '")).append(req.getParameter("intentos")).append("' WHERE (id = 2)")));
            consul.insert(sql);
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_parametros SET valor = '")).append(req.getParameter("deshabilitar") != null ? "S" : "N").append("' WHERE (id = 3)")));
            consul.insert(sql);
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_parametros SET valor = '")).append(req.getParameter("mail") != null ? "S" : "N").append("' WHERE (id = 4)")));
            consul.insert(sql);
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_parametros SET valor = '")).append(req.getParameter("email")).append("' WHERE (id = 5)")));
            consul.insert(sql);
            consul.close();
        }
        doGet(req, resp);
        connMgr.freeConnection("portal", conexion);
    }


    private Usuario user;
    private Mensaje mensaje;
}