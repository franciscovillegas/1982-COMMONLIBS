package portal.com.eje.serhumano.user;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class S_EndSession extends MyHttpServlet {

    public S_EndSession() {
    }

    public void init(ServletConfig config)
        throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
    	HttpSession sesion = request.getSession(false);
        if(sesion != null) {
        	sesion.invalidate();
        }
        String htm = request.getParameter("htm");
        if(htm != null) {
        	response.sendRedirect(htm);
        }
        else {
        	response.sendRedirect(request.getContextPath());
        }

    }

    public void destroy() {
    }

}