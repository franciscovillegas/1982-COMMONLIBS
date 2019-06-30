package portal.com.eje.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;

public class EnumTool {
	
	/**
	 * @deprecated
	 * */
	public static EnumTool getInstance() {
		return Util.getInstance(EnumTool.class);
	}
	
	public static <T> T getEnum(Class<? extends Enum<?>> clazz, String toString, T defaultValue) {

		if(clazz != null && toString != null) {
			defaultValue = EnumsToolsCached.getInstance().getEnum(clazz, toString, defaultValue);
		}
		
		return defaultValue;
	}
 
	
	public static <T> T getEnumByName(Class<? extends Enum<?>> clazz, String name, T defaultValue) {
		
		if(clazz != null && name != null) {
			defaultValue= EnumsToolsCached.getInstance().getEnumByName(clazz, name, defaultValue);
		}
		return defaultValue;
	}

	public static <T> T getEnumByNameIngoreCase(Class<? extends Enum<?>> clazz, String name, T defaultValue) {
		
		if(clazz != null && name != null) {
			defaultValue= EnumsToolsCached.getInstance().getEnumByNameIngoreCase(clazz, name, defaultValue);
		}
		return defaultValue;
	}
	
	public static <T> T getEnumByToString(Class<? extends Enum<?>> clazz, String name, T defaultValue) {

		if(clazz != null && name != null) {
			 defaultValue = EnumsToolsCached.getInstance().getEnumByToString(clazz, name, defaultValue);
		}
		return defaultValue;
	}
	
	 
	
	public static <T>List<T> getArrayList(Class<? extends Enum<?>> clazz) {
		List<T> lista = null;;
		
		if(clazz != null) {
			lista= EnumsToolsCached.getInstance().getArrayList(clazz);
		}
		
		
		return lista;
	}

	
	public static DataList getList(Class clazz) {
		DataList lista = null;
		
		if(clazz != null) {
			try {
				Class[] def = {Class.class};
				Object[] params = {clazz};
				DataList temporal = (DataList) Cache.soft(getInstance(), "privateGetList", def, params, Object.class);
				if(temporal != null) {
					lista = temporal;
				}
				else {
					lista = new DataList();
				}
			} catch (SecurityException e) {
				
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();
			}
		}
		
		return lista;
	}
	
	private static DataList privateGetList(Class clazz) {
		DataList lista = new DataList();
		
		if(clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					Object e = Enum.valueOf(clazz, f.getName());
					lista.add(EnumTool.getEnumFields((Enum<?>) e));
				}
			}
		}
		
		return lista;
	}
	
	public static DataFields getEnumFields(Enum<?> e) {
		DataFields df= null;
		
		if(e != null) {

			try {
				Class[] def = { Enum.class };
				Object[] params = { e };
				DataFields temporal = (DataFields) Cache.soft(getInstance(), "privateGetEnumFields", def, params, Object.class);
				if (temporal != null) {
					df = temporal;
				} else {
					df = new DataFields();
				}
			} catch (SecurityException e1) {

				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {

				e1.printStackTrace();
			}
		}
		
		return df;
	}
	
	private static DataFields privateGetEnumFields(Enum<?> e) {
		DataFields df= new DataFields();
		df.put("enum_name", e.toString());
		df.put("toString", e.toString());
		df.put("tostring", e.toString());
		df.put("name", ((Enum)e).name() ); 
		df.put("ordinal", ((Enum)e).ordinal());
		
		List<Method> metodos = VoTool.getInstance().getGetsMethodsWithNoParameters(e.getClass());
		
		for(Method m : metodos ) {
			Object o = VoTool.getInstance().getReturnFromMethod(e, m);
			
			try {
				String retorno = ClaseConversor.getInstance().getObject(o, String.class);
				df.put(VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(m), retorno);	
			}
			catch(Exception ex) {
				
			}
			
		}
		
		return df;
	}

	public static List<String> getListNames(Class<?> clazz) {
		List<String> lista = null;
		
		if(clazz != null) {
			try {
				Class[] def = {Class.class};
				Object[] params = {clazz};
				List<String> temporal = (List<String>) Cache.soft(getInstance(), "privateGetListNames", def, params, Object.class);
				if(temporal != null) {
					lista = temporal;
				}
				else {
					lista = new ArrayList<String>();
				}
			} catch (SecurityException e) {
				
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();
			}
		}
		
		return lista;
		
	}
	
	private static List<String> privateGetListNames(Class<?> clazz) {
		List<String> lista = new ArrayList<String>();
		
		if(clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					Enum e = Enum.valueOf((Class)clazz, f.getName());
					
					lista.add( e.name() );
				}
			}
		}
		
		return lista;
	}

	/**
	 * Retorna una lista de enum variables
	 * @author Pancho
	 * @since 19-12-2018
	 * 
	 * */
	public static <T> List<T> getListEnums(Class<T> clazz) {
		List<T> lista = null;
		
		if(clazz != null) {
			try {
				Class[] def = {Class.class};
				Object[] params = {clazz};
				List<T> temporal = (List<T>) Cache.soft(getInstance(), "privateGetListEnums", def, params, Object.class);
				if(temporal != null) {
					lista = temporal;
				}
				else {
					lista = new ArrayList<T>();
				}
				
			} catch (SecurityException e) {
				
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();
			}
		}
		
		return lista;
	}
	
	private static <T> List<T> privateGetListEnums(Class<T> clazz) {
		List<T> lista = new ArrayList<T>();
		
		if(clazz != null && Enum.class.isAssignableFrom(clazz)) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					Enum e = Enum.valueOf((Class)clazz, f.getName());
					
					lista.add( (T) e );
				}
			}
		}
		
		return lista;
	}

	public static ConsultaData getConsultaData(List<EModulos> modulos) {
		ConsultaData data = ConsultaTool.getInstance().newConsultaData(new String[] {"enum_name","toString", "toString", "name","ordinal"});
		
		if(modulos != null) {
			for(EModulos mod : modulos) {
				data.add( EnumTool.getEnumFields(mod) );
			}
		}
		
		return data;
	}
	
	public static ConsultaData getConsultaData(Class<?> clase) {
		ConsultaData data = ConsultaTool.getInstance().newConsultaData(new String[] {"enum_name","toString", "toString", "name","ordinal"});
		
		if(clase != null) {
			for(Object mod : getListEnums(clase)) {
				data.add( EnumTool.getEnumFields((Enum<?>) mod) );
			}
		}
		
		return data;
	}
	
	public static List<String> getListToString(Class<?> clazz) {
		List<String> lista = null;
		
		if(clazz != null) {
			try {
				Class[] def = {Class.class};
				Object[] params = {clazz};
				List<String> temporal = (List<String>) Cache.soft(getInstance(), "privateGetListToString", def, params, Object.class);
				if(temporal != null) {
					lista = temporal;
				}
				else {
					lista = new ArrayList<String>();
				}
			} catch (SecurityException e) {
				
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();
			}
		}
		
		return lista;
		
	}
	
	
	private static List<String> privateGetListToString(Class<?> clazz) {
		List<String> lista = new ArrayList<String>();
		
		if(clazz != null && Enum.class.isAssignableFrom(clazz)) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					@SuppressWarnings("unchecked")
					Enum<?> e = Enum.valueOf((Class)clazz, f.getName());
					
					lista.add( (e).toString() );
				}
			}
		}
		
		return lista;
	}

	public static <T> T getEnumByToStringIngoreCase(Class<? extends Enum<?>> clase, String valor)  {
		Enum<?> retorno = null;
		
		if(clase != null && valor != null) {
			retorno = EnumsToolsCached.getInstance().getEnumByToStringIngoreCase(clase, valor);	
		}
		
		return  (T) retorno;
	}

	
}
