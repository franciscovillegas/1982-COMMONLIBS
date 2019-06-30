package organica.com.eje.ges.usuario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.datos.Consulta;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.usuario:
//            Usuario, ControlAcceso

public class Inicio extends MyHttpServlet
{

    public Inicio()
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
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutConsultado();
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Bienvenida", "Tiempo de Sesi\363n expirado...");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            String passw = user.getRutUsuario().substring(user.getRutUsuario().length() - 4, user.getRutUsuario().length());
            int cam = 0;
            if(passw.equals(user.getPassWord()))
            {
                cam = 1;
            } else
            {
                Consulta consul = new Consulta(Conexion);
                Consulta consul2 = new Consulta(Conexion);
                String sql = "SELECT id, valor FROM eje_ges_parametros WHERE (id = 1)";
                consul2.exec(sql);
                if(consul2.next())
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT login_usuario, error, passw_cambiar, DATEDIFF(dd,     passw_ult_cambio, GETDATE()) AS dias FROM eje_ges_usuario WHERE (login_usuario = '")).append(user.getRutUsuario()).append("')")));
                    consul.exec(sql);
                    if(consul.next())
                        if(consul.getInt("dias") >= consul2.getInt("valor"))
                        {
                            modelRoot.put("expi", "1");
                            cam = 1;
                        } else
                        if("S".equals(consul.getString("passw_cambiar")))
                            cam = 1;
                }
                consul.close();
                consul2.close();
            }
            modelRoot.put("cambio", (new Integer(cam)).toString());
            super.retTemplate(resp,"Gestion/main_bienvenida.html",modelRoot);
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}