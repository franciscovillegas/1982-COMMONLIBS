package portal.com.eje.tools.javabeans;

import portal.com.eje.portal.factory.Util;

/**
 * Devuelve solo los objetos buscados en dichos paquetes pero los filtra según los parámetros que hayan sido seteados.
 * 
 *  
 * */
public class JavaBeanConfTool extends JavaBeanTool {
	
	public static JavaBeanConfTool getInstance() {
		return Util.getInstance(JavaBeanConfTool.class);
	}
//	
//	public <T> T getOne(String paquete, Class<T> iface) {
//		List<Class<?>> finded =  ClaseDetector.getInstance().getClassInPackage(paquete, iface);
//	}
//	
//	public <T> T getOne(Object objeto) {
//		
//	}
//	
//	public <T> T getOnde(Collection<T> col) {
//		
//	}
//	
//	private <T> T getOneSelected(List<Class<?>> finded) {
//		
//	}
}
