package cl.ejedigital.web.usuario;

import java.lang.reflect.InvocationTargetException;

import cl.ejedigital.tool.singleton.Singleton;
import cl.ejedigital.web.parametroconfig.ConfigParametroKey;
import cl.ejedigital.web.parametroconfig.ConfigParametroManager;


public class SingletonUsuarioUtils implements Singleton {
	private static IUsuarioUtils instance;
	
	public static IUsuarioUtils getInstance() {
		if(instance == null) {
			synchronized(SingletonUsuarioUtils.class) {
				if(instance == null) {
					Class[] definicion = {};
					Object[] parametros = {};
					try {
						instance=(IUsuarioUtils)ConfigParametroManager
							.getInstance()
							.getClaseConfigParametro(ConfigParametroKey.generico_sesion_usuario_util,definicion,parametros);
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					catch (InstantiationException e) {
						e.printStackTrace();
					}
					catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return instance;
	}
	
}
