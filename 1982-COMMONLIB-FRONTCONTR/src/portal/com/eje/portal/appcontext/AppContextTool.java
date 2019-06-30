package portal.com.eje.portal.appcontext;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import portal.com.eje.portal.factory.Util;

public class AppContextTool {
	private Map<Class<?>, ApplicationContext> map;
	
	public static AppContextTool getInstance() {
		return Util.getInstance(AppContextTool.class);
	}
	
	private AppContextTool() {
		map = new HashMap<Class<?>, ApplicationContext>();
	}
	
	private ApplicationContext getAppContextFromClassNameXml(Class<?> clase) {
		if(map.get(clase) == null) {
			synchronized (AppContextTool.class) {
				if(map.get(clase) == null) {
					String cn = clase.getCanonicalName().replace('.', '/');
					ApplicationContext ctx =  new ClassPathXmlApplicationContext("masterbeans/"+cn+".xml");
					map.put(clase, ctx);
				}
			}
		}
		
		return map.get(clase);
	}
	
	public static ApplicationContext getAppContext(Class<?> clase) {
		return getInstance().getAppContextFromClassNameXml(clase);
	}
	
	
}
