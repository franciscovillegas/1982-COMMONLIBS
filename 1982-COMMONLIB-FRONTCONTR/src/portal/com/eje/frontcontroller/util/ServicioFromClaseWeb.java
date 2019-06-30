package portal.com.eje.frontcontroller.util;

import java.lang.reflect.InvocationTargetException;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.ClaseGenerica;

public class ServicioFromClaseWeb implements IServicioGetter {
	private static String PARAM_CLASE = "claseweb";
	
	public static  ServicioFromClaseWeb getInstance() {
		return Util.getInstance(ServicioFromClaseWeb.class);
	}
	
	private ServicioFromClaseWeb() {
		
	}
	
	@Override
	public Object getServicio(IOClaseWeb io) {
		String clase = io.getParamString(PARAM_CLASE, null);
		
		Class<?>[] consDefClass= {IOClaseWeb.class };
    	Object[] consParams = {io };	
    	
    	Object objeto = null;
    	
		try {
			objeto= ClaseGenerica.getInstance().getNew(clase,consDefClass,consParams);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return objeto;
	}

	
}
