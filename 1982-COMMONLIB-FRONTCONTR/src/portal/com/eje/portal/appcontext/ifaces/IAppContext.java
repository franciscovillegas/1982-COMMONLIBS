package portal.com.eje.portal.appcontext.ifaces;

import org.springframework.context.ApplicationContext;

public interface IAppContext {

	public ApplicationContext getApplicationContext(Class<?> classe);
	
}
