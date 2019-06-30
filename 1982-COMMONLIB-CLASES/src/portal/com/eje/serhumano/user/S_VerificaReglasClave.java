package portal.com.eje.serhumano.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class S_VerificaReglasClave extends MyHttpServlet {

    public S_VerificaReglasClave() {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
    	throws IOException, ServletException {
    	doPost(req,resp);            
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    	throws ServletException, IOException {
    	
    	Connection conn = connMgr.getConnection("portal");
    	response.setContentType("text/json");
    	PrintWriter out = response.getWriter();
    	String callback = request.getParameter("jsoncallback");
    	
    	proper = ResourceBundle.getBundle("db");
    	
    	Usuario user = SessionMgr.rescatarUsuario(request);
    	UserMgr usermgr = new UserMgr(conn);
    	
    	String rut = user.getRut().getRut();
    	String ejecuta = "NO";
    	String regla = "0";
   	
    	String cotaCaducacionClave = proper.getString("clave.tiempocaducacion");
    	
    	if(usermgr.primerIngresoPortal(rut)) {
    		ejecuta = "SI";
    		regla = "1";
    	}
    	else if(usermgr.tieneClaveCaducada(rut,Integer.parseInt(cotaCaducacionClave))) {
    		ejecuta = "SI";
    		regla = "2";
    	}

    	out.println(callback + "({ \"ejecuta\":\"" + ejecuta + "\", \"regla\":\"" + regla + "\"})");
    	out.flush();
    	out.close();
    	
    	connMgr.freeConnection("portal",conn);
    }

    private ResourceBundle proper;
}