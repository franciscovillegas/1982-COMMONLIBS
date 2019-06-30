package cl.ejedigital.web.usuario;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.parametroconfig.ConfigParametro;
import cl.ejedigital.web.parametroconfig.ConfigParametroKey;
import cl.ejedigital.web.parametroconfig.ConfigParametroManager;


public class SessionManagerBase implements ISessionManager {
	private static SessionManagerBase instance;
	private int timeOut;
	
	public SessionManagerBase() {
		
	}
	
	public IUsuario rescatarUsuario(HttpServletRequest req) {
		IUsuario user;
		HttpSession sesion = req.getSession(false);

		if(sesion == null) {
			user = newUsuario();

		}
		else {
			try {
				user = (IUsuario) sesion.getAttribute(sesion.getId());
			}
			catch (Exception e) {
				user = newUsuario();
			}
			if(user == null) {
				user = newUsuario();
			}
		}

		return user;

	}
	
	private IUsuario newUsuario() {
		Class[] definicion = {};
		Class[] parametros = {};
		
		try {
			return (IUsuario) ConfigParametroManager.getInstance().getClaseConfigParametro(ConfigParametroKey.generico_session_usuario,definicion,parametros);
		}
		catch (ClassNotFoundException e) {
			return null;
		}
		catch (NoSuchMethodException e) {
			return null;
		}
		catch (InstantiationException e) {
			return null;
		}
		catch (IllegalAccessException e) {
			return null;
		}
		catch (InvocationTargetException e) {
			return null;
		}
	}

	public void guardarUsuario(HttpServletRequest req, IUsuario usuario) {
		HttpSession miSesion = req.getSession(true);
        miSesion.setAttribute(miSesion.getId(), usuario);
        
        ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.generico_session_segundoscaducidad);
        Validar val = new Validar();
        int segundos = val.validarInt(p.getSelectedKey(), 1500 );
        
        
        miSesion.setMaxInactiveInterval(segundos);
        System.out.println("[SESSION] guardando Usuario (".concat(String.valueOf(segundos)).concat(")--> ".concat(String.valueOf(String.valueOf(usuario.toString())))));
	}

}
