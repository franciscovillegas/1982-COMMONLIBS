package mis;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;

public class ServletCallMis extends MyHttpServlet {	

	private static final long serialVersionUID = -2814971494101720323L;
	private Usuario user;
	
	public ServletCallMis() {
		super();
	}

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		OutMessage.OutMessagePrint("Inicio doGet ServletCallReports: ");		
        user = SessionMgr.rescatarUsuario(request);
		if(user.esValido()) {
			String tipo = request.getParameter("tipo");
		    ResourceBundle proper = ResourceBundle.getBundle("db");
	        String url = proper.getString("mis.url") + request.getParameter("url");
	        if("excel".equals(tipo)) {
	        	response.setContentType("application/vnd.ms-excel");
	        }
	        MyProxyDemo pd = new MyProxyDemo();
	        OutputStream out = response.getOutputStream();
	        out = pd.doURLRequest(url,out);
	       	System.out.println(url);
	       	out.flush();
	       	out.close();
		}
		else {
            super.mensaje.devolverPaginaSinSesion(response, "Call Miss", "Tiempo de Sesi\363n expirado...");
		}
	}	
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}