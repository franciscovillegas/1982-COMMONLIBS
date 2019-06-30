package cl.ejedigital.web.frontcontroller.io;

import javax.servlet.http.HttpServletResponse;

public interface IWebOutputText {

	public boolean retJavascript(HttpServletResponse resp, String texto);
	
	public boolean retJson(HttpServletResponse resp, String json);
	
	public boolean retTexto(HttpServletResponse resp, String texto);
	
	
}
