package organica.com.eje.ges.usuario;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import organica.tools.OutMessage;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

// Referenced classes of package com.eje.ges.usuario:
//            Usuario, UsuariosConectados

public class SetEstado extends MyHttpServlet
{

    public SetEstado()
    {
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        boolean estado = request.isRequestedSessionIdValid();
        OutMessage.OutMessagePrint("\nSetEstado de usuario\n--> sesion valida? - ".concat(String.valueOf(String.valueOf(estado))));
        if(estado)
        {
            HttpSession sesion = request.getSession(false);
            OutMessage.OutMessagePrint("--> sesion ID: ".concat(String.valueOf(String.valueOf(sesion.getId()))));
            Usuario user = Usuario.rescatarUsuario(sesion);
            if(request.getParameter("q") != null)
            {
                OutMessage.OutMessagePrint("--> Quitar usuario de la lista.");
            } else
            {
                OutMessage.OutMessagePrint("--> Agregar usuario a la lista.");
                
            }
        }
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        out.println("<html>");
        out.println("<head></head>");
        out.println("<body onLoad='self.close()'></body></html>");
    }

    private static final String CONTENT_TYPE = "text/html";
}