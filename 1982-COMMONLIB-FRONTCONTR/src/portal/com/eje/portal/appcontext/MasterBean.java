package portal.com.eje.portal.appcontext;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.portal.factory.Util;

public class MasterBean {
	private Logger logger = Logger.getLogger(MasterBean.class);
	private Map<Class<?>, Object> map;
	
	public static MasterBean getInstance() {
		return Util.getInstance(MasterBean.class);
	}
	
	private MasterBean() {
		map = new HashMap<Class<?>, Object>();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getMBean(Class<T> clase) {
		
		if(map.get(clase)==null) {
			synchronized (MasterBean.class) {
				if(map.get(clase)==null) {
					Cronometro cro = new Cronometro();
					cro.start();
					
					try {
						T t= (T) AppContextTool.getAppContext(clase).getBean(clase);
						map.put(clase, t);
						
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					logger.debug(new StringBuilder().append(clase.getCanonicalName())
							.append(" Load time:")
							.append(cro.getTimeHHMMSS_milli()));
				}
			}
		}
		
		
		
		return (T) map.get(clase);
	}
	
	public static <T> T getMasterBean(Class<T> clase) {
		return getInstance().getMBean(clase);
	}
	
	 
}
