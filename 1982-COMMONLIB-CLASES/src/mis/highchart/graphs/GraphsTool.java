package mis.highchart.graphs;

import highchart.ifaces.AbsGraph;

import java.lang.reflect.InvocationTargetException;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.ClaseGenerica;

 

public class GraphsTool {

	
	public Object getImplementacion(IOClaseWeb cWeb, Class<?> clase) {
		
		ClaseGenerica cGenerica = new ClaseGenerica();
		String path = "";
		if(cWeb.getParamString("path", null) != null) {
			path = cWeb.getParamString("path", "");
		}

		Class<?>[] defs= {IOClaseWeb.class};
		Object[] params = {cWeb};
		try {
			Object o = cGenerica.getNew(path, defs, params);
			
			if( o != null ) {
				if(o instanceof AbsGraph){
					return o;
				}
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("No existe definición de gráfico -->(type:"
					.concat(cWeb.getParamString("gtype", null))
					.concat("   path:")
					.concat(path).concat(")"));
			
		} catch (NoSuchMethodException e) {
			System.out.println("No existe definición de gráfico -->(type:"
					.concat(cWeb.getParamString("gtype", null))
					.concat("   path:")
					.concat(path).concat(")"));
			
		} catch (InstantiationException e) {
			System.out.println("No existe definición de gráfico -->(type:"
					.concat(cWeb.getParamString("gtype", null))
					.concat("   path:")
					.concat(path).concat(")"));
			
		} catch (IllegalAccessException e) {
			System.out.println("No existe definición de gráfico -->(type:"
					.concat(cWeb.getParamString("gtype", null))
					.concat("   path:")
					.concat(path).concat(")"));
			
		} catch (InvocationTargetException e) {
			System.out.println("No existe definición de gráfico -->(type:"
					.concat(cWeb.getParamString("gtype", null))
					.concat("   path:")
					.concat(path).concat(")"));
			
		}
		
		return null;
	}


}
