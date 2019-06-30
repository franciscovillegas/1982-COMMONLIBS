package cl.intercept;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Intercept implements Filter {
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
//	    HttpSession session = hrequest.getSession(true);
//	    Usuario user = SessionMgr.rescatarUsuario(session);
//	    
//	    HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

        //response.setHeader("Access-Control-Max-Age", "3600");
        chain.doFilter(req, resp);
        
        /*
	    resp.setContentType("text/html; charset=iso-8859-1");
        resp.setCharacterEncoding("ISO-8859-1");
	    if(user.esValido()) {
	    	System.out.println("FILTRO - LOGIN");
	    
	    	String servletPath = hrequest.getServletPath();
	        ArrayList<String> servPermitidos = new ArrayList<String>();
	        if(servletPath.indexOf("/sitio") != -1) {
	            try {
	            	DBConnectionManager connMgr = DBConnectionManager.getInstance();
	            	Connection Conexion = connMgr.getConnection("qs");
	                ManagerTracking mgrTracking = new ManagerTracking(Conexion);
	                WsgTracking wsg = new WsgTracking();
	                Date dateActual = new Date(Varios.getInstanceCalendar().getTimeInMillis());
	                String url = hrequest.getRequestURI().replaceAll(hrequest.getContextPath() + "/sitio", "");
	                String tipo = url.substring(url.lastIndexOf("/") + 4, url.length() - 4);
	                String container = url.substring(0, url.lastIndexOf("/"));
	                wsg.setRut(Integer.parseInt(user.getRutId()));
	                wsg.setCod_empresa(Integer.parseInt(user.getEmpresa()));
	                wsg.setFecha(dateActual);
	                wsg.setIp(hrequest.getRemoteAddr());
	                wsg.setBrowser(hrequest.getHeader("User-Agent"));
	                wsg.setDescripcion(tipo);
	                wsg.setHora(dateActual);
	                wsg.setDirec_rel("load_page");
	                wsg.setQuery(container);
	                wsg.setDatos(url);
	                wsg.setInitdate(dateActual);
	                wsg.setServer(InetAddress.getLocalHost().getHostName());
	                mgrTracking.addTracking(wsg);
	                connMgr.freeConnection("qs", Conexion);
	            }
	            catch(Exception e) {
	                System.out.println("Error al agregar tracking");
	            }
	        }
	    	chain.doFilter(req, resp);
	    }
	    else {
	    	ResourceBundle rb = ResourceBundle.getBundle("intercept");
			String intercepta = rb.getString("intercept.autenticacion");
	    	if(intercepta.equals("1")) {
	    		System.out.println("FILTRO - LOGOUT");
	    		RequestDispatcher dispatcher = req.getRequestDispatcher("/servlet/EjeCoreI?claseweb=cl.intercept.Redirect");
	    		dispatcher.forward(req, resp);
	    		return;
	    	}
	    	else {
	    		chain.doFilter(req, resp);
	    	}
	    }
	    */
	    
	}

}