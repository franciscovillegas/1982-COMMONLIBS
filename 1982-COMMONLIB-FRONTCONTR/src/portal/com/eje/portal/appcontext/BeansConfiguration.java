package portal.com.eje.portal.appcontext;

 
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import portal.com.eje.portal.appcontext.enums.EnumBeansConfigurationType;
import portal.com.eje.portal.appcontext.error.NoBeanConfigurationSetted;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.VoTool;

/**
 * Maneja la configuración de un conjunto de bean <br/>
 * Luego de ser creado el bean, en un objeto bean donde se guardan un conjunto de Bean como propiedad lo llamamos MasterBean <br/> 
 * De ese Arreglo este objeto retornará un bean o varios según la estategia de seleccion
 * @author Pancho
 * @since 17-04-2019
 * */
public class BeansConfiguration<T> extends ArrayList<T> implements List<T>  {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	//Evita que maneje muchas veces el mismo conjunto de Array
	private Map<String, BeansConfiguration> mapBeansOptions = new HashMap<String, BeansConfiguration>();
	
	private static final long serialVersionUID = 1L;

	
	@SuppressWarnings("rawtypes")
	public static BeansConfiguration getInstance() {
		return Util.getInstance(BeansConfiguration.class);
	}
		
	public enum EnumBeanConfigurationEstrategy {
		SI_NO_HAY_SELECCIONADO_RETORNA_TODOS_LOS_POSIBLES,
		SI_NO_HAY_SELECCIONADO_RETORNA_ERROR
	}
	
	/**
	 * Retorno un BeansConfiguration aunque se llame muchas veces <br/>
	 * Es la única forma de construir este Objecto para usarlo como BeansConfiguration, si se 
	 * construye cono una Collection solo funcionará como Collection.
	 * 
	 * @author Pancho
	 * @since 16-04-2019
	 * */
	@SuppressWarnings("unchecked")
	public static <E>BeansConfiguration<E> manageOptions(List<E> options) {
		if(options == null) {
			throw new NullPointerException("Las beans a manejar no pueden ser null ");
		}
		
		BeansConfiguration<E> singleton = BeansConfiguration.getInstance();
		@SuppressWarnings("rawtypes")
		Map<String, BeansConfiguration> singletonMapBeanOptions = singleton.mapBeansOptions;
		String key = options.toString();
		
		if(singletonMapBeanOptions.get(key) == null) {
			BeansConfiguration<E> bConf = new BeansConfiguration<E>();
			bConf.addAll(options);
			
			singletonMapBeanOptions.put(key, bConf);
		}
		
		return singletonMapBeanOptions.get(key);
	}
	
	
	@SuppressWarnings("unchecked")
	public static <E> E getBeanSeteado(Object masterBean , String fieldName, EnumBeanConfigurationEstrategy estrategy) {
		 Field field = VoTool.getInstance().getField(masterBean.getClass(), fieldName);
		 Collection<?> beans = (Collection<?>) VoTool.getInstance().getFieldValue_fromField(masterBean, fieldName, Collection.class);
		 
		 ParameterizedType type = (ParameterizedType) field.getGenericType();
		 Class<?> classeDelBeans = (Class<?>)type.getActualTypeArguments()[0];
		 
		 BeansConfiguration<Object> beansConf = new BeansConfiguration<Object>();
		 beansConf.addAll(beans);
		 
		 return (E) beansConf.getBeanSeteado(masterBean.getClass(), fieldName, classeDelBeans, estrategy);
	}
	/**
	 * Obtiene desde la BD el bean que a sido seteado como la IMPLEMENTACIÓN SELECCIONADA
	 * @author Pancho
	 * @since 16-04-2019
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public <E> E getBeanSeteado(Class<?> masterBeanClass, String varName, Class<E> typeOfConfBeans, EnumBeanConfigurationEstrategy estrategy) {
		BeansConfigurationRecognize.getInstance().registraClasesDeConfiguracion(this, masterBeanClass, varName, EnumBeansConfigurationType.SINGLE);
		
		List<E> e = (List<E>) BeansConfigurationRecognize.getInstance().getClaseImplementacion(this, masterBeanClass, varName, EnumBeansConfigurationType.SINGLE);
		boolean vacio = (e == null || e.size() == 0);
		E retorno = null;
		
		if( vacio && estrategy == EnumBeanConfigurationEstrategy.SI_NO_HAY_SELECCIONADO_RETORNA_ERROR) {
			throw new NoBeanConfigurationSetted("La variable \"".concat(varName).concat("\"")
												.concat(" de la clase \"".concat(masterBeanClass.getCanonicalName())
														.concat("\" no tiene configuración en BD ")));
		}
		else if(vacio && estrategy == EnumBeanConfigurationEstrategy.SI_NO_HAY_SELECCIONADO_RETORNA_TODOS_LOS_POSIBLES) {
			
			retorno = (E) this.iterator().next();
		}
		else {
			retorno = (E) this.iterator().next();
		}
		
		return retorno;
	}
	
	/**
	 * Obtiene desde la BD los beans que han sido seteados como IMPLEMENTACIONES VÁLIDAS
	 * @author Pancho
	 * @param <E>
	 * @since 16-04-2019
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public <E> List<E> getBeansSeteados(Class<?> masterBeanClass, String varName, Class<E> typeOfConfBeans, EnumBeanConfigurationEstrategy estrategy) {
		BeansConfigurationRecognize.getInstance().registraClasesDeConfiguracion(this, masterBeanClass, varName, EnumBeansConfigurationType.MUTISELECT);
		
		List<E> e = (List<E>) BeansConfigurationRecognize.getInstance().getClaseImplementacion(this, masterBeanClass, varName, EnumBeansConfigurationType.MUTISELECT);
		boolean vacio = (e == null || e.size() == 0);
		List<E> retorno = null;
		
		if(vacio && estrategy == EnumBeanConfigurationEstrategy.SI_NO_HAY_SELECCIONADO_RETORNA_ERROR) {
			throw new NoBeanConfigurationSetted("La variable \"".concat(varName).concat("\"")
												.concat(" de la clase \"".concat(masterBeanClass.getCanonicalName())
														.concat("\" no tiene configuración en BD ")));
		}
		else if(vacio && estrategy == EnumBeanConfigurationEstrategy.SI_NO_HAY_SELECCIONADO_RETORNA_TODOS_LOS_POSIBLES) {
			
			retorno = (List<E>) this;
		}
		else {
			retorno = (List<E>) this;
		}
		
		return retorno;
	}
	
	
}
