package portal.com.eje.frontcontroller.iface;

import java.lang.reflect.Method;

import cl.ejedigital.tool.misc.Cronometro;

public interface IServicioListener {

	
	public void onShow(Object servicio, Method metodo, Object[] params, Cronometro cro);
	
	public void onNotFound(Object servicio, String methodName, String msgRespuesta);
}
