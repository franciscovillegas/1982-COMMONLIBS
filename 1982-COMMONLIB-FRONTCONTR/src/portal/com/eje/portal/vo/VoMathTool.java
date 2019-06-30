package portal.com.eje.portal.vo;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.WordUtils;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.vo.Vo;

public class VoMathTool {

	public static VoMathTool getInstance() {
		return Util.getInstance(VoMathTool.class);
	}
	
	/**
	 * Obtiene la suma de los valores de un Map
	 * @author Pancho
	 * @since 06-09-2018
	 * @see #getSuma(List, Collection)
	 * */
	public double getSuma(Map<String,Double> sumaVo) {
		double suma = 0 ;
		if(sumaVo != null) {
			for(Entry<String, Double> entry : sumaVo.entrySet()) {
				suma += entry.getValue();
			}
		}
		
		return suma;
	}
	
	/**
	 * Obtiene el mensaje de las sumas ingresadas en un Map<String,Double> sumaVo
	 * @author Pancho
	 * @since 06-09-2018
	 * @see #getSuma(List, Collection)
	 * */
	public String getSumaStringPorColumna(String prefijo, Map<String,Double> sumaVo, DecimalFormat decimalFormat) {
		StringBuilder str = new StringBuilder();
		
		if(sumaVo != null) {
			for(Entry<String,Double> entry : sumaVo.entrySet()) {
				if(!"".equals(str.toString())) {
					str.append(",");
				}
				str.append(prefijo != null ? prefijo : "").append(" ").append(WordUtils.capitalize(entry.getKey())).append("=").append(decimalFormat.format(entry.getValue()));
			}
		}
			
		
		return str.toString();
	}
	
	
	/**
	 * Obtiene la suma de los vos de los campos indicados en camposASumas
	 * @author Pancho
	 * @since 06-09-2018
	 * */
	@SuppressWarnings("rawtypes")
	public Double getSuma(String campoASumar, Collection cols) { 
		List<String> campo = new ArrayList<>();
		campo.add(campoASumar);
		
		return getSuma(campo, cols).get(campoASumar);
	}
	/**
	 * Obtiene la suma de los vos de los campos indicados en camposASumas
	 * @author Pancho
	 * @since 06-09-2018
	 * */
	@SuppressWarnings("rawtypes")
	public Map<String,Double> getSuma(List<String> camposASumar, Collection cols) {
		Map<String, Double> suma = new HashMap<String, Double>();
		
		if(camposASumar != null && camposASumar.size() > 0 && cols != null && cols.size() > 0) {
			Object firstVo = cols.iterator().next();
			
			List<Method> metodos = VoTool.getInstance().getGetsMethodsWithNoParameters(firstVo.getClass());
			List<Method> metodosAObtener = new ArrayList<Method>();
			
			for(Method m : metodos) {
				String field = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(m);
				if(camposASumar.contains(field)) {
					metodosAObtener.add(m);
				}
			}
			
			
			for(Object vo : cols) {
				for(Method m : metodosAObtener) {
					String field = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(m);
					
					Object o = VoTool.getInstance().getReturnFromMethod(vo, m);
					Double valor = (Double) ClaseConversor.getInstance().getObject(o, Double.class);
					
					suma.put(field, (suma.get(field) != null ? suma.get(field) : 0) + valor);
				}
			}
			
		}
		
		return suma;
		
	}
}
