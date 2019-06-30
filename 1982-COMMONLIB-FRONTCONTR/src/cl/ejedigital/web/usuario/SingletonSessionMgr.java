package cl.ejedigital.web.usuario;

import java.lang.reflect.InvocationTargetException;

import cl.ejedigital.tool.singleton.Singleton;
import cl.ejedigital.web.parametroconfig.ConfigParametroKey;
import cl.ejedigital.web.parametroconfig.ConfigParametroManager;
import cl.ejedigital.web.usuario.ISessionManager;


public class SingletonSessionMgr implements Singleton {
	private static ISessionManager instance;
	
	public static ISessionManager getInstance() {
		if(instance== null) {
			synchronized(SingletonSessionMgr.class) {				
				if(instance== null) {
					Class[] definicion = {};
					Object[] parametros = {};
					try {
						instance=(ISessionManager)ConfigParametroManager
							.getInstance()
							.getClaseConfigParametro(ConfigParametroKey.generico_session_manager,definicion,parametros);
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