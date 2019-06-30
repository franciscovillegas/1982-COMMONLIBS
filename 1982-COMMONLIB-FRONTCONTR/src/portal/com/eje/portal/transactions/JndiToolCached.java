package portal.com.eje.portal.transactions;

import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;

public class JndiToolCached extends JndiTool {

	public static JndiToolCached getInstance() {
		return Util.getInstance(JndiToolCached.class);
	}
	
	@Override
	public String getUrlConnection(EModulos modulo, String jndi) {
		
		String retorno = null;
		try {
			Class<?>[] def = {EModulos.class, String.class};
			Object[] params = {modulo, jndi};
			
			retorno = Cache.soft(this, "getUrlConnection", def, params, String.class, true);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
}
