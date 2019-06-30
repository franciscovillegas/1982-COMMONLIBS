package portal.com.eje.usuario;

import java.io.IOException;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;

import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

public class IntercepUserFilter implements Filter {
	private FilterConfig config = null;
  
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
  
	public void destroy() {
		config = null;
	}
  
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
		throws IOException, ServletException {
		HttpServletRequest hrequest = (HttpServletRequest) req;
	    HttpSession session = hrequest.getSession(true);
	    Usuario user = SessionMgr.rescatarUsuario(session);
	    System.out.println("VALOR SESION : " + user.esValido());
	    resp.setContentType("text/html; charset=iso-8859-1");
	    String path = hrequest.getRequestURI();
	    Pattern p = Pattern.compile("/quicksite/sitio/rrhh/");
        Matcher m = p.matcher(path);
        if (m.find()) {
        	Connection conn;
            IDBConnectionManager connMgr;
        	System.out.println("URL FILTRO ---> " + path);
        	connMgr = DBConnectionManager.getInstance();
        	conn = connMgr.getConnection("portal");
        	//insTracking(conn, req, user, path, "/firstfactors/portalrrhh/quicksite/misionvision", "tracking vision, mision y otros");
        	connMgr.freeConnection("portal", conn);
            connMgr.release();
		}
	    if(user.esValido()) {
	    	System.out.println("LOGIN");
	    	chain.doFilter(req, resp);
	    }
	    else {
	    	System.out.println("LOGOUT");
	    	RequestDispatcher dispatcher = req.getRequestDispatcher("/logout.html");
			dispatcher.forward(req, resp);
			return;
	    }
	}


}