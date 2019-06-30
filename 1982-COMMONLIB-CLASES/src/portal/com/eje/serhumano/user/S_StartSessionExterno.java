package portal.com.eje.serhumano.user;

import cl.ejedigital.tool.properties.PropertiesTools;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class S_StartSessionExterno extends MyHttpServlet {
  
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		sendToStartSession(req, resp);
	}
  
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		sendToStartSession(req, resp);
	}
  
	private void sendToStartSession(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PropertiesTools pTools = new PropertiesTools();
		String cWeb = "portal.com.eje.serhumano.user.startsession.StartSessionPerExterno";
		RequestDispatcher dispatcher = req.getRequestDispatcher("EjeCoreI?claseweb=".concat(cWeb));
		dispatcher.forward(req, resp);
	}
}
