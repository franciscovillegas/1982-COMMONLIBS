package portal.com.eje.portal.vo;

import portal.com.eje.portal.factory.Util;

public class DynamicClassLoaderLocator  {
	
	public enum Loader {
		DEFAULT(DynamicClassLoader.class);
		
		private Class<? extends DynamicClassLoader> clase;
		Loader(Class<? extends DynamicClassLoader> clase) {
			this.clase = clase;
		}
		
		public Class<? extends DynamicClassLoader> getImplementation() {
			return clase;
		}
	}
	
	private DynamicClassLoaderLocator() {
 
	}
	
	public static  DynamicClassLoader getInstance(Loader l) {
		return Util.getInstance(l.getImplementation());
	}

	 
}
