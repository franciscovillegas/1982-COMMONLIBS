package portal.com.eje.portal.eje_generico_util;

import java.lang.reflect.InvocationTargetException;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.conf.IConfCentral;

public class ConfContext implements IConfCentral {
	
	public static ConfContext getInstance() {
		return Util.getInstance(ConfContext.class);
	}
	
	public ConfContextUrl getConfContextUrl() {
		ConfContextUrl retorno = null;
		try {
			Class[] def = {IConfCentral.class};
			Object[] params = {this};
			retorno = (ConfContextUrl) ClaseGenerica.getInstance().getNew(ConfContextUrl.class, def, params);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}
	

	
}
