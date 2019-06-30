package portal.com.eje.serhumano.user;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpSession;

import portal.com.eje.cache2.Cache;
import portal.com.eje.portal.generico_appid.EnumAppId;
import portal.com.eje.serhumano.user.jndimanager.IJndiManager;

public class Usuario extends UsuarioBase {
	
 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Usuario(HttpSession session, IJndiManager iJndiManager) {
		super(session, iJndiManager);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnumAppId> getAppIds() {
		Class<?>[] def = {};
		Object[] params = {};
		return Cache.weak(this, "getAppIds", def, params, List.class, true);
	}
	
	@Override
	public String getEmailFromTablaConfirmacion() {
		Class<?>[] def = {};
		Object[] params = {};
		return Cache.weak(this, "getEmailFromTablaConfirmacion", def, params, String.class, true);
	}
	
	@Override
	public String getEmailFromTrabajador() {
		Class<?>[] def = {};
		Object[] params = {};
		return Cache.weak(this, "getEmailFromTrabajador", def, params, String.class, true);
	}
	
	@Override
	public boolean tieneAccesoGestion(Connection conn, String rut) {
		Class<?>[] def = {Connection.class, String.class};
		Object[] params = {conn, rut};
		return Cache.weak(this, "tieneAccesoGestion", def, params, boolean.class, true);
	}
	
	@Override
	public boolean usuarioVigente(Connection conn, Rut rut) {
		Class<?>[] def = {Connection.class, Rut.class};
		Object[] params = {conn, rut};
		return Cache.weak(this, "usuarioVigente", def, params, boolean.class, true);
	}
}