package organica.com.eje.ges.usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class CerrarSesion extends MyHttpServlet
{

    public CerrarSesion()
    {
    }

   

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        Usuario.SuperNodo = null;
        HttpSession sesion = request.getSession(false);
        if(sesion != null)
            sesion.removeAttribute(sesion.getId() + "o");
        String htm = request.getParameter("htm");
        String inicio = request.getParameter("inicio");
        if(inicio == null) {
            if(htm != null)
                response.sendRedirect(htm);
        }
        else {
            response.sendRedirect(inicio);
        }
    }


    private static final String CONTENT_TYPE = "text/html";
}