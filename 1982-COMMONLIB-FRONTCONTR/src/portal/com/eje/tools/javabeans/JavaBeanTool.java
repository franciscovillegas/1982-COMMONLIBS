package portal.com.eje.tools.javabeans;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.Row;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.appcontext.ifaces.IBeansList;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.tools.ClaseDetector;
import portal.com.eje.tools.ClaseGenerica;

/**
 * Esta clase solo obtendrá los valores de los JavaBean, después crearemos otra que los relacione con parámetros
 * 
 * @author Pancho
 * @since 15-05-2019
 * */
public class JavaBeanTool {
	private final Logger logger =Logger.getLogger(getClass());
	private final String SUFIJO_ENUM = "_enum";
	private final String SUFIJO_ORDINAL = "_ordinal";
	private final String SUFIJO_TOSTRING = "_tostring";
	private final String SUFIJO_CANONICALNAME = "_canonicalName";
	private final JavaBeanColumnTool cTool = JavaBeanColumnTool.getInstance();
	private final String VACIO = "vacio";
	private final List<String> noValidFields = new ArrayList<>();
	
	public static JavaBeanTool getInstance() {
		return Util.getInstance(JavaBeanTool.class);
	}
	
	public JavaBeanTool() {
		noValidFields.add("declaringClass");
	}
	
	/**
	 * No funciona el obtener objetos cuando estos están dentro de un jar , así que hay que ocupar la abstración de MasterBean, con <br/>
	 * IBeansList o IBean
	 * @deprecated
	 * */	
	public ConsultaData getData(String pquete, Class<?> iface) {
		List<Class<?>> tools = ClaseDetector.getInstance().getClassInPackage(pquete, iface);
		return getDataFromClass(tools);
	}
	
	public ConsultaData getDataFromMasterBean(Class<? extends IBeansList> mbClass) {
		ConsultaData data = null;
		
		if(mbClass != null) {
			IBeansList mb = ClaseGenerica.getInstance().getNewFromClass(mbClass);
			List<?> tools = mb.getBeans();
			data = getDataFromObject(tools);		
		}
	
		return data;
	}
	
	private ConsultaData getDataFromClass(List<Class<?>> tools) {
		 
		List<Object> objetos = new ArrayList<>();
		for(Class<?> t : tools) {
			Object tool =  ClaseGenerica.getInstance().getNewFromClass(t);
			objetos.add(tool);
		}
		
		return getDataFromObject(objetos);
	}
	
	public ConsultaData getDataFromObject(Object objeto) {
		ConsultaData data = null;
		List<Object> objetos = new ArrayList<>();
		if(objeto != null) {
			objetos.add(objeto);
			data = getDataFromObject(objetos);
		}
		return data;
	}
	
	
	public ConsultaData getDataFromObject(List<?> objetos) {
		return getDataFromObject(objetos, false);
	}
	public ConsultaData getDataFromObject(List<?> objetos, boolean addTypeOfField) {
		ConsultaData data = ConsultaTool.getInstance().newConsultaData(new String[] {VACIO} );
		
		if(objetos != null && objetos.size()>0) {
			for(Object tool : objetos) {
				Class<?> t = tool.getClass(); 
				
				Map<String, Method> methods = VoTool.getInstance().getGetMethodsWithNoParameters_map(t);
				Row row = Row.column(VACIO, null);
				
				for(Entry<String, Method> entry : methods.entrySet()) {
					logger.trace(entry.getKey());
					if(noValidFields.contains(entry.getKey())) {
						continue;
					}
					
					Object valor = VoTool.getInstance().getReturnFromMethod(tool, entry.getValue());
					
					addEnumValues(data, row, entry, valor, addTypeOfField);
						
					if(entry.getKey() != null && entry.getKey().equals("buttonsIde")) {
						logger.trace("debug");
					}
					cTool.addColumn(data, row, entry.getKey(), valor, addTypeOfField);
				}
				
				
				cTool.addColumn(data, row , "canonicalName", t.getCanonicalName(), addTypeOfField);
				cTool.addColumn(data, row , "simpleName", t.getSimpleName(), addTypeOfField);
				cTool.addColumn(data, row, "toString", tool.toString(), addTypeOfField);
				
				data.add(row.build());
			}
		}
		
		
		return data;
	}
	
	
	@SuppressWarnings("rawtypes")
	private void addEnumValues(ConsultaData data, Row row, Entry<String, Method> entry,  Object o, boolean addTypeOfField) {
		if(o instanceof Enum) {
			Map<String, Method> map = VoTool.getInstance().getGetMethodsWithNoParameters_map(o.getClass());
			
			for( Entry<String, Method> entryEnum : map.entrySet()) {
				if(noValidFields.contains(entryEnum.getKey())) {
					continue;
				}
				
				Object valorEnumField = VoTool.getInstance().getReturnFromMethod(o, entryEnum.getValue());
				
				String fieldEnum = entry.getKey() + "_" + entryEnum.getKey();
				cTool.addColumn(data, row,  fieldEnum, valorEnumField, addTypeOfField);
 			}
			
			cTool.addColumn(data, row, entry.getKey() + SUFIJO_ENUM			, ((Enum) o).name(), addTypeOfField);
			cTool.addColumn(data, row, entry.getKey() + SUFIJO_ORDINAL		, ((Enum) o).ordinal(), addTypeOfField);
			cTool.addColumn(data, row, entry.getKey() + SUFIJO_TOSTRING		,  ((Enum) o).toString() , addTypeOfField);
			cTool.addColumn(data, row, entry.getKey() + SUFIJO_CANONICALNAME	, ((Enum) o).getClass().getCanonicalName(), addTypeOfField);
 
		}
	}
	
	

	public ConsultaData getData(Class<?> page) {
		List<Class<?>> pages = new ArrayList<>();
		pages.add(page);
		
		return getDataFromClass(pages);
	}
}
