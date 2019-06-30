package portal.com.eje.portal.parametro;

import portal.com.eje.portal.factory.Static;

public class ParametroLocator {
	
	public static IParametro getInstance() {
		return Static.getInstance(ParametroWithCache.class);
	}
	
	
}
