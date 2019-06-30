package cl.ejedigital.web.usuario;

import javax.servlet.http.HttpServletRequest;

public interface ISessionManager {

	public IUsuario rescatarUsuario(HttpServletRequest req);
		
	public void guardarUsuario(HttpServletRequest req, IUsuario usuario);
	
}
