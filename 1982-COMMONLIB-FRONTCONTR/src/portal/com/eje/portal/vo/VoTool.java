package portal.com.eje.portal.vo;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.IMetable;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.annotations.ForeignKey;
import portal.com.eje.portal.vo.annotations.ForeignKeys;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.errors.MethodNotFoundException;
import portal.com.eje.portal.vo.iface.IFieldIterable;
import portal.com.eje.portal.vo.iface.IVoFind;
import portal.com.eje.portal.vo.iface.IVoTool;
import portal.com.eje.portal.vo.iface.IVoValueChange;
import portal.com.eje.portal.vo.vo.ListPaginada;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.ListUtils;
import portal.com.eje.tools.deprecates.AssertWarn;

class DefinitionToInsert {
	private Method method;
	private Class[] classToSet;

	public Class[] getClassToSet() {
		return classToSet;
	}

	public Method getMethod() {
		return method;
	}

	public void setClassToSet(Class[] classToSet) {
		this.classToSet = classToSet;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}

public class VoTool implements IVoTool {
	protected final Logger logger = Logger.getLogger(getClass());
	
	enum VoToolSetMethod {
		SIMPLE_ABBREVIATON, // ONLY FIRST
		COMPLETELY_ABBREVIATON // ALL _
	}
	
	private Map<Class, Class> buildMap;
	private Map<Class,List<Class>> buildMapObject;

	private ClaseGenerica cg;

	private IParamMapping paramMapping;

	public VoTool() {
		cg = Util.getInstance(ClaseGenerica.class);
		buildMap = new HashMap<Class, Class>();
		buildMap.put(String.class, String.class);
		buildMap.put(Integer.class, int.class);
		buildMap.put(Long.class, long.class);
		buildMap.put(Double.class, double.class);
		buildMap.put(Float.class, float.class);
		buildMap.put(Boolean.class, boolean.class);
		buildMap.put(Byte.class, byte.class);
		buildMap.put(Void.class, void.class);
		buildMap.put(Short.class, short.class);

		paramMapping = Util.getInstance(ClaseConversor.class);
		
		buildMapObject = new HashMap<Class, List<Class>>();
		List<Class> listaClasses = new ArrayList<Class>();
		listaClasses.add(Date.class);
		buildMapObject.put(Timestamp.class, listaClasses);
	}
	
	public static IVoTool getInstance() {
		return  VoToolCached.getInstance();
	}

	@Override
	public <T> ConsultaData buildConsultaData(Collection<? extends Vo> collection) {
		return buildConsultaData(collection, false);
	}

	/**
	 * Construye un ConsultaData a partir de un Collection de Vo, se puede incluir en cada fila todas las referencias del vo.<br/>
	 * Las columnas referencias si ya existen se les cambiará el nombre agregándole "_<numero>" por cada repetición que tengan
	 * 
	 * @author Pancho
	 * @since 25-07-2018
	 * */
	@Override
	public <T> ConsultaData buildConsultaData(Collection<? extends Vo> collection , boolean incluyeReferencias) {
		ConsultaData data = null;
		Class[] nullDef = {};
		Object[] nullObj = {};

		if (collection != null) {
			
			
			if(collection.size() == 0) {
				List<String> cols = new ArrayList<String>();
				cols.add("vacio");
				data = new ConsultaData(cols);
			}
			else {
				Iterator iterator = collection.iterator();
				Object o = null;
				Class classDef = null;
				List<Method> listaMethods = null;
				Map<Method, String> mapNames = null;
	 
				checkColumnName(collection);
				
				while (iterator.hasNext()) {
					o = iterator.next();
										
					Vo vo = (Vo) o;
					
					DataFields fields = new DataFields();

					if (data == null) {
						listaMethods = getGetsMethodsWithNoParameters(o.getClass());
						mapNames = getColumnsMappingFromMethods(listaMethods);
						List<String> cols = getListFromValues(mapNames);
						
						data = new ConsultaData(cols);
						
						if(IMetable.class.isAssignableFrom(collection.getClass())) {
							data.setMetaData(((IMetable)collection).getMetaData());
						}
					}

					for (Method m : listaMethods) {
						fields.put(mapNames.get(m), getReturnFromMethod(o, m.getName(), nullDef, nullObj));
					}
					
					if(incluyeReferencias) {
						buildConsultaDataReferencias(fields, data.getNombreColumnas(), vo, "");
					}

					
					data.add(fields);
				}
			}
			
		}

		return data;
	}
	
	/**
	 * Los fields, o los nombres de columnas deben estar siempre en minuculas, si no, el vo y la consultaData quedan distintos
	 * @author Pancho
	 * @since 22-05-2019
	 * */
	private void checkColumnName(Collection<? extends Vo> collection) {
		@SuppressWarnings("rawtypes")
		Iterator iterator = collection.iterator();
		List<Method> listaMethods = null;
		Map<Method, String> mapNames = null;
		
		boolean estaEnNorma = true;
		while (iterator.hasNext()) {
			Object o = iterator.next();
			
			Vo vo = (Vo) o;
			
			listaMethods = getGetsMethodsWithNoParameters(o.getClass());
			mapNames = getColumnsMappingFromMethods(listaMethods);
			
			for( Entry<Method, String>  entry : mapNames.entrySet()) {
				estaEnNorma = entry.getValue().toLowerCase().equals(entry.getValue());
				
				if(!estaEnNorma) {
					break;
				}
			}
		}
		
		AssertWarn.isTrue(getClass(), estaEnNorma, "El vo debe siempre tener los fields en minusculas, y por end lo métodos quedarán bien formados.");
	}
	
	/**
	 * Agrega a las datafiels de un consultaData (por cada fila) todos los valores de los objetos referenciados. <br/>
	 * también agrega simultaneamente los nombre de columnas necesarios
	 * 
	 * @author Pancho
	 * @since 25-07-2018
	 * 
	 * 	 * */
	@Override
	public DataFields buildConsultaDataReferencias(DataFields df , List<String> colsName, Vo vo, String prefijoColName) {
		if(vo != null) {
			Class clazzToSearchReference = vo.getClass();
			 
			TableReference[] tableReferences = getTableReference(clazzToSearchReference);
			
			if(tableReferences != null) {
				for(TableReference tr : tableReferences) {
					String field = tr.field();
					Method metodo = getGetMethod(vo.getClass(), field);
					
					if(metodo != null) {
						Object voHijo = getReturnFromMethod(vo, metodo);
						
						if(!(voHijo instanceof Vo)) {
							continue;
						}
						
						Vo voHijoVo = (Vo) voHijo;
						
						List<Method> listaMethods = getGetsMethodsWithNoParameters(voHijo.getClass());
						Map<Method, String> mapNames = getColumnsMappingFromMethods(listaMethods);
					 
						
						for (Method m : listaMethods) {
							String colName = prefijoColName + tr.field()+"_"+ mapNames.get(m);
							 
							
							Object valor = getReturnFromMethod(voHijo, m);
							
							df.put(colName, valor);
							colsName.add(colName);
						}
						
						buildConsultaDataReferencias(df, colsName, voHijoVo, tr.field()+"_");
					}
				}
			}
			
			 
		}

		return df;
	}

	@Override
	public <T> Collection<T> buildVo(ConsultaData data, Class<T> clazz) {
		try {
			return buildVoPrivate_fromVO(data, clazz);	
			
			/*
			if(data != null && clazz != null) {
				List cols =data.getNombreColumnas();
				List metodos = getSetsFieldsWithNoParameters(clazz);
				
				if(metodos.size() < cols.size()) {
					return buildVoPrivate_fromVO(data, clazz);	
				}
				else {
					return buildVoPrivate_fromConsultaData(data, clazz);
				}
				
			}
			*/
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (NoSuchMethodException e) {
			System.out.println(e);
		} catch (InstantiationException e) {
			System.out.println(e);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (InvocationTargetException e) {
			System.out.println(e);
		}

		return new ArrayList<T>();
	}
	
	/**
	 * @deprecated no funcionó siempre
	 * @author Pancho
	 * @since 17-07-2018
	 * */
	private <T> Collection<T> buildVoPrivate_fromConsultaData(ConsultaData data, Class<T> clazzVO) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Collection<T> lista = new ArrayList<T>();

		List<Class> defsMethodsAIntentar = new LinkedList<Class>();
		defsMethodsAIntentar.add(String.class);
		defsMethodsAIntentar.add(double.class);
		defsMethodsAIntentar.add(float.class);
		defsMethodsAIntentar.add(long.class);
		defsMethodsAIntentar.add(int.class);
		defsMethodsAIntentar.add(boolean.class);
		defsMethodsAIntentar.add(short.class);
		defsMethodsAIntentar.add(byte.class);
		defsMethodsAIntentar.add(char.class);

		Map<String, DefinitionToInsert> definiciones = new HashMap<String, DefinitionToInsert>();

		if (data != null) {
			int pos = data.getPosition();
			data.toStart();
			List<String> cols = data.getNombreColumnas();

			while (data.next()) {

				Object objetoVO = null;
				{
					Class[] clazz2 = {};
					Object[] objects2 = {};
					objetoVO = cg.getNewFromClass(clazzVO, clazz2, objects2);
				}

				for (String columnaName : cols) {
					if (columnaName != null && !"".equals(columnaName)) {
						if(!data.existField(columnaName)) {
							continue;
						}
						Object oFromData = data.getObject(columnaName);
					
						if(oFromData == null) {
							continue;
						}
						
						if (definiciones.get(columnaName) == null) {
							String[] metodoNames = getSetMethodsFromColName(VoToolSetMethod.SIMPLE_ABBREVIATON, columnaName);
							Method method = null;

							for (String nombreMetodo : metodoNames) {
								/* este es el set buscado */
								List<Class> trying = new ArrayList<Class>();
								
								if(oFromData.getClass() != null) {
									/*SU PRIMERA FORMA*/
									trying.add(oFromData.getClass());
									Class[] defToSet = { oFromData.getClass() };
									method = getMethod(objetoVO.getClass(), nombreMetodo, defToSet);
								}
								if (method == null && buildMap.get(oFromData.getClass()) != null) {
									/*SU FORMA MINIMA*/
									trying.add(buildMap.get(oFromData.getClass()) );
									Class[] defToSet = { buildMap.get(oFromData.getClass()) };
									method = getMethod(objetoVO.getClass(), nombreMetodo, defToSet);
								}
								if (method == null && buildMapObject.get(oFromData.getClass()) != null) {
									/*SU FORMA TIMESTAMP*/
									List<Class> listaToClass = buildMapObject.get(oFromData.getClass());
									for(Class c : listaToClass) {
										trying.add(c);
										Class[] defToSet = { c };
										method = getMethod(objetoVO.getClass(), nombreMetodo, defToSet);
										if(method != null) {
											break;
										}
									}
								}
								if(method == null) {
									/*CUALQUIER OTRA FORMA*/
									Set<Class> clases = buildMap.keySet();
									for(Class c : clases) {
										if(trying.indexOf(c) == -1) {
											trying.add(c);
											Class[] defToSet = { c };
											method = getMethod(objetoVO.getClass(), nombreMetodo, defToSet);
											if(method != null) {
												break;
											}
										}
										if(trying.indexOf(buildMap.get(c)) == -1) {
											trying.add(buildMap.get(c));
											Class[] defToSet = { buildMap.get(c) };
											method = getMethod(objetoVO.getClass(), nombreMetodo, defToSet);
											if(method != null) {
												break;
											}
										}
									}
								}
								
								DefinitionToInsert def = new DefinitionToInsert();
								if (method != null) {
									Class[] classToSet = method.getParameterTypes();
									def.setClassToSet(classToSet);
									def.setMethod(method);
								}

								definiciones.put(columnaName, def);
							}
						}

						if (definiciones.get(columnaName) != null && definiciones.get(columnaName).getMethod() != null) {
							Object[] oConverted = { this.paramMapping.getObject(oFromData, definiciones.get(columnaName).getClassToSet()[0]) };
							intentaEjecutarElMetodo(objetoVO, definiciones.get(columnaName).getMethod(), definiciones.get(columnaName).getClassToSet(), oConverted);
						}

					}
				}

				lista.add((T) objetoVO);
			}

			data.setPosition(pos);
		}

		return lista;
	}
	
	
	@SuppressWarnings("rawtypes")
	private <T> Collection<T> buildVoPrivate_fromVO(ConsultaData data, Class<T> clazzVO) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Collection<T> lista = new ListPaginada<T>();
		
		if (data != null && data.size() > 0) {
			((ListPaginada)lista).setMetaData(data.getMetaData());
			
			int pos = data.getPosition();
			data.toStart();
			List<String> cols = data.getNombreColumnas();

			List<Method> methods = getSetsMethodsWithOneParameter(clazzVO);
			while (data.next()) {
				Object objetoVO = cg.getNewFromClass(clazzVO);
				
				if(objetoVO != null) {
					
					for(Method metodo : methods) {
						String fieldName = getMetodNameWithoutSetOrGetOrIsLowerCase(metodo);
						if(data.existField(fieldName)) {
							Class classToSet = metodo.getParameterTypes()[0];
							Object objectToSet = data.getObject(fieldName);
							
							if(objectToSet != null) {
								Object[] params = {this.paramMapping.getObject(objectToSet, classToSet)};
								Object retorno = getReturnFromMethod(objetoVO, metodo, params);	
							}
						}
					}
				}
				
				lista.add((T)objetoVO);
			}
			
			data.setPosition(pos);
		}
		
		return lista;
	}

	@Override
	public <T> T buildVoSimple(ConsultaData data, Class<T> clazz) {
 
		Collection cols = buildVo(data, clazz);
		if(cols != null) {
			Iterator ite = cols.iterator();
			if(ite.hasNext()) {
				return (T)ite.next();	
			}
		}
		 

		return null;
	}

	/**
	 * Concatena los valores de una collection, omitiendo el null
	 * 
	 * @author Pancho
	 * @since 25-06-2018
	 * */
	@Override
	public String concatenateValues(Collection vos, String campo) {
		StringBuilder str =new StringBuilder();
		if(vos != null && campo != null && vos.size() > 0) {
			DefinitionToInsert toGet = null;
			
			for(Object object : vos) {
				if(toGet == null) {
					Method m = getGetMethod(object.getClass(), campo);
					toGet = new DefinitionToInsert();
					
					toGet.setMethod(m);
				}
				
				if(toGet != null && toGet.getMethod() != null) {
					Object retorno = getReturnFromMethod(object, toGet.getMethod());
					
					String string = paramMapping.getObject(retorno, String.class);
					
					if(string != null) {
						if(!"".equals(str.toString())) {
							str.append(",");
						}
						
						str.append(string.toString());
					}
					
					
				}
			}
		}
		return str.toString();
	}
	/**

	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	@Override
	public <T> Collection<T> copy(Collection<? extends Vo> vos, Class<T> clazzNewObject) {
		return copy(vos, clazzNewObject, null);
	}

	/**
	 * Copia una collection de objetos en otra segunda
	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	@Override
	public <T> Collection<T> copy(Collection<? extends Vo> vos, Class<T> clazzNewObject, IVoValueChange iVoValueChange) {
		Collection<T> newVos = null;
		
		if(vos != null && clazzNewObject != null) {
			newVos = cg.getNewFromClass(vos.getClass());
			
			for(Vo vo : vos) {
				T t = cg.getNewFromClass(clazzNewObject);
				copy(vo, (Vo)t, iVoValueChange);
				
				newVos.add(t);
			}
		}
		return newVos;
	}
	
	/**

	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	@Override
	public <T> T copy(Vo vo, Class<? extends Vo> clazz) {
		Vo copy = null;
		
		if(vo != null && clazz != null){
			{
				Class[] defs = {};
				Object[] params = {};
				copy = cg.getNewFromClass(clazz, defs, params);
			}
			
			return copy(vo, copy, null);
		}
		
		return (T)copy;
	}
	
	/**
	 * Copia una clase
	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public <T> T copy(Vo vo1, Vo copy) {
		return copy(vo1, copy, null);
	}

	/**
	 * Copia una clase
	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public <T> T copy(Vo vo1, Vo copy, IVoValueChange iVoValueChange) {
		
		if(vo1!=null && copy != null){
 
			
			//copy.getClass().getm
			List<Method> metodosSet= getSetsMethodsWithOneParameter(copy.getClass());
			List<Method> metodosGet= getGetsMethodsWithNoParameters(vo1.getClass());
			/**/
			Map<String,Method> mapGet = new HashMap<String, Method>();
			for(Method mGet : metodosGet) {
				String fieldName = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(mGet);
				mapGet.put(fieldName, mGet);
			}
			
			for(Method mSet : metodosSet) {
				String fieldName = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(mSet);
				
			 	Method mGet = mapGet.get(fieldName);
				if(mGet == null) {
					continue;
				}
				
				String fieldNameGet = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(mGet);
				boolean isTheMethod = fieldName.equals(fieldNameGet);
					
				if(isTheMethod) {
					Object retornoMetodoGet = this.getReturnFromMethod(vo1, mGet );
					
					if(iVoValueChange != null) {
						retornoMetodoGet = iVoValueChange.changeAfterGet(fieldName, retornoMetodoGet);
					}
					
					Class clazzTo = mSet.getParameterTypes()[0];
					Object convertidoToSet = paramMapping.getObject(retornoMetodoGet, clazzTo);
					
					if(iVoValueChange != null){
						convertidoToSet = iVoValueChange.changeBeforeSetToCopy(fieldName, convertidoToSet);
					}
					
					Object[] params = {convertidoToSet};
					
					this.getReturnFromMethod(copy, mSet , params );	
				}
				 
				
			}
		}
		
		return (T)copy;
	}

	/**
	 * Siempre retorna una Collection con valores de un metodo del vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	
	/**
	 * Siempre retorna una Collection , si collectionClass es una List, entonces retorna una lista con los valores del fieldName<br/>
	 * También el valor de los fieldName los puedes convertir en otro objeto para eso {@link #getCollection(Collection, Class, String, Class)}<br/>
	 * Si es un Map. entonces el key será el fieldName, solo soporta un Vo por cada key, si necesitas una lista de valores en el Map utilza {@link #getMapList(List, Class, Class, String)}
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	@Override
	public <T> T getCollection(Collection<? extends Vo> vos, Class<T> collectionClass, String fieldName) {
		return getCollection(vos, collectionClass, fieldName, null );
	}
	
	/**
	 * Siempre retorna una Collection con valores de un metodo del vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	@Override
	public <T> T getCollection(Collection<? extends Vo> vos, Class<T> collectionClass, String fieldName, Class fieldNameClass) {
		T collection = null;
		if(vos != null &&  collectionClass != null && fieldName != null) {
			Class[] def = {};
			Object[] obj = {};
			collection = Util.getInstance(ClaseGenerica.class).getNewFromClass(collectionClass, def, obj);
			
			if(vos.size() > 0) {
				Object firstObject = vos.iterator().next();
				
				Method metodoGet = getGetMethod( firstObject.getClass() , fieldName);
				
				if(metodoGet != null) {
					Iterator i = vos.iterator();
					while(i.hasNext()) {
						Object vo = i.next();
						Object o = getReturnFromMethod(vo, metodoGet);
						 
						if(fieldNameClass != null) {
							ClaseConversor.getInstance().getObject(o, fieldNameClass);
						}
		 
						Collection.class.isAssignableFrom(collection.getClass());

						if(List.class.isAssignableFrom(collection.getClass()) || Collection.class.isAssignableFrom(collection.getClass())) {
							((Collection)collection).add(o);
						}
						else if(Map.class.isAssignableFrom(collection.getClass())  || collection instanceof Map) {
							((Map) collection).put(o, vo);
						} else {
							Assert.isTrue(false, "No se pudo definir paternidad reconocible de "+collection.getClass().getCanonicalName()); 
						}
					}
				}

			}

		}
		return (T) collection;
	}

	/**
	 * Este método devuelve un Map&lt;Object,List&lt;Vo&gt;&gt; de una lista de Vos<br/>
	 * 
	 * Para el siguiente ejemplo de Collection<Vo>, que tiene 4 objetos (Vo) y cada Vo tiene los métodos getA(), getB(), getC() y getD()<br/>
	 *  cols = {<br/>
	 * a b c d <br/>
	 * 1 2 3 5 <br/>
	 * 1 1 2 5 <br/>
	 * 2 2 5 5 <br/>
	 * 6 6 1 5 <br/>
	 * }<br/>
	 * 
	 * Si se llama al método getMapList(cols, HashMap.class, ArrayList.class, "a") el retorno será un Map con tres keys, 1,2 y 6, 1={{1,2,3,4},{1,1,2,2}},2={2,2,5,5},6={6,6,1,2}} <br/>
	 * Si se llama al método getMapList(cols, HashMap.class, ArrayList.class, "d") el retorno será un Map con una key 5, 5={{1,2,3,5},{1,1,2,5},{2,2,5,5},{6,6,1,5}} <br/>
	 * Si se llama al método getMapList(cols, HashMap.class, ArrayList.class, "c") el retorno será un Map con cuatro keys 3,2,5, y 1, 3={1,2,3,5},2={1,1,2,5},5={2,2,5,5},1={6,6,1,5} <br/>
	 * 
	 * @author Pancho
	 * @since 28-08-2018
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public <T>Map<Object, List<T>> getMapList(List<T> vos, Class<? extends Map> collectionClass, Class<? extends List> listClass, String fieldName) {
		Map<Object,List<T>> map = null;
		if(vos != null &&  collectionClass != null && fieldName != null) {

			map = Util.getInstance(ClaseGenerica.class).getNewFromClass(collectionClass);
			
			if(vos.size() > 0) {
				Object firstObject = vos.iterator().next();
				
				Method metodoGet = getGetMethod( firstObject.getClass() , fieldName);
				
				if(metodoGet != null) {
					Iterator<Vo> i = (Iterator<Vo>) vos.iterator();
					while(i.hasNext()) {
						Object vo = i.next();
						Object o = getReturnFromMethod(vo, metodoGet);
		 
						if((map).get(o) == null) {
							List<Object> list = Util.getInstance(ClaseGenerica.class).getNewFromClass(listClass);;
							(map).put(o, (List<T>) list);
						}
						
						((List<Object>)(map).get(o)).add(vo);
						 
					}
				}

			}

		}
		return map;
	}
	
	/**
	 * Retorna un vo a partir de la comparacion de un objeto con el retorno del método indicado
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */

	@Override
	public <T>Collection<T> getCollection(Collection<T> vos, IVoFind voFind) {
		Collection<T> colls = new ArrayList<T>();
		if(vos != null && vos.size() >= 0 && voFind != null) {
 
			Iterator i = vos.iterator();
			while(i.hasNext()) {
				Object vo = i.next();
				
				Collection<T> col = new ArrayList<T>();
				boolean push = voFind.filter(col);
				
				if(push) {
					colls.addAll(col);
				}
			 
			}
		}
		return colls;
	}


	private Map<Method, String> getColumnsMappingFromMethods(List<Method> lista) {
		Map<Method, String> colsName = new HashMap<Method, String>();
		for (Method m : lista) {

			StringBuilder bdFieldFinal = new StringBuilder();
			String bdField = getMetodNameWithoutSetOrGetOrIsLowerCase(m);

			char[] chars = bdField.toCharArray();
			for (int i = 0; i < bdField.length(); i++) {
				if (Character.isUpperCase(chars[i])) {
					bdFieldFinal.append("_");
				}

				bdFieldFinal.append(chars[i]);
			}

			colsName.put(m, bdFieldFinal.toString());
		}

		return colsName;
	}
	
	/**
	 * Retorna el método del field
	 * 
	 * @since 03-07-2018
	 * @author Pancho
	 * */
	@Override
	public Method getGetMethod(Class clazz, String campo) {
		Class[] defsNoClass= {};
		Method metodo = null;
		
		Field field = getField(clazz, campo);
		 
		if(field != null) {
			if(field.getType().equals(boolean.class)) {
				String mName = "is"+campo.substring(0, 1).toUpperCase()+campo.substring(1, campo.length());
				
				metodo = getMethod(clazz, mName, defsNoClass);
			}
			else {
				String mName = "get"+campo.substring(0, 1).toUpperCase()+campo.substring(1, campo.length());
				metodo = getMethod(clazz, mName, defsNoClass);
			}
		}
		return metodo;
	}
	
	/**
	 * Retorna el método del field
	 * 
	 * @since 03-07-2018
	 * @author Pancho
	 * */
	@Override
	public Method getSetMethod(Class clazz, String campo, Class[] defsNoClass) {
		Method metodo = null;
		
		Field field = getField(clazz, campo);
		 
		if(field != null) {
	 		String mName = "set"+campo.substring(0, 1).toUpperCase()+campo.substring(1, campo.length());
			metodo = getMethod(clazz, mName, defsNoClass);
			 
		}
		return metodo;
	}
	
	/**
	 * Retorna el método del field
	 * 
	 * @since 03-07-2018
	 * @author Pancho
	 * */
	@Override
	public Method getSetMethod(Class clazz, String campo, int cantParams) {
		Method metodo = null;
		
		Field field = getField(clazz, campo);
		 
		if(field != null) {
			List<Method> methods = VoTool.getInstance().getSetsMethodsWithOneParameter(clazz);
			for(Method mSet : methods) {
				String fieldToFind = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(mSet);
				
				if(fieldToFind != null && fieldToFind.equals(campo)) {
					
					Class<?>[] paramTypes = mSet.getParameterTypes();
					if(paramTypes != null && paramTypes.length == cantParams) {
						
						metodo = mSet;
						
						break;
					}
				}
			}
		}
		
		return metodo;
	}
	
	@Override
	public List<Method> getNoVoidMethod_noParams(Class clazz) {
		List<Method> retorno = new ArrayList<Method>();
		Method[] methods = clazz.getMethods();
		for(Method m : methods) {
			if(!(void.class == m.getGenericReturnType()) && m.getParameterTypes().length == 0) {
				retorno.add(m);
			}
		}
	 
		return retorno;
	}
	
	/**
	 * Retorna un field de un Clase
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	@Override
	public Field getField(Class clazz, String campo ) {
		String key = "getField_"+clazz.getCanonicalName()+"_"+campo;
		Field field =  (Field) WeakCacheLocator.getInstance(getClass()).get(key);
		
		if(field == null) {
			try {
				field = clazz.getDeclaredField(campo);
			} catch (SecurityException e) {
				//System.out.println(e);
			} catch (NoSuchFieldException e) {
				if(clazz.getSuperclass() != null) {
					field = getField(clazz.getSuperclass(), campo);
				}
			}
			
			WeakCacheLocator.getInstance(getClass()).put(key, field);
		}

		
		return field;
	}
	
	/**
	 * Retorna true si existe el campo como un Miembro de la clase
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	@Override
	public boolean existeField(Class clazz, String campo) {
		return getField(clazz, campo) != null;
	}
	
	/**
	 * 
	 * Retorno la lista de fields de un VO desde el punto de vista de los GETS(get e is)
	 * @author Pancho
	 * @since 20-06-2018
	 * */
	@Override
	public List<String> getGetsFieldsWithNoParameters(Class c) {
	 
		List<String> fields =  new ArrayList<String>();
			if(c != null) {
				List<Method> metodos = getGetsMethodsWithNoParameters(c);
				for(Method m :  metodos) {
					fields.add(getMetodNameWithoutSetOrGetOrIsLowerCase(m));
				}
			}
		 
			return fields;
	}
	
	/**
	 * Retorna un Map con todos los métodos de una clase que empiezan con get, sin contar getClass
	 * 
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	@Override
	public Map<String, Method> getGetMethodsWithNoParameters_map(Class c) {
		List<Method> metodos = getGetsMethodsWithNoParameters(c);
		Map<String, Method> map = new HashMap<String, Method>();
		
		for(Method m : metodos) {
			map.put(getMetodNameWithoutSetOrGetOrIsLowerCase(m), m);
		}
		
		return map;
	}
	
	/**
	 * Retorna todos los métodos, sin excepción, y su field respectivo como KEY, cuando el métdoo no tiene parámetro y a su vez retorna algo distinto de void
	 * retornará field <enum, Method> si es que es un Enum
	 * 
	 * @author Pancho
	 * */
	
	@Override
	public Map<String, Method> getAllGetMethodsWithNoParameters_map(Class<?> c) {
		Map<String, Method> map = new HashMap<>();
		
		if(c != null) {
			Method[] metodos = c.getMethods();
			for(Method m : metodos) {
				boolean retornaAlgo = m.getGenericReturnType() != Void.class && m.getGenericReturnType() != void.class;
				boolean tieneParametro = m.getParameterCount() > 0;
				
				if(retornaAlgo && !tieneParametro) {
					String fieldName = getMetodNameWithoutSetOrGetOrIsLowerCase(m);
					
					if(fieldName == null) {
						map.put(m.getName(), m);	
					}
					else {
						map.put(fieldName, m);	
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * retorna todos los métodos de una clase que empiezan con get o is, sin contar getClass
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	@Override
	public List<Method> getGetsMethodsWithNoParameters(Class c) {
	 
		List<Method> lista =   new LinkedList<Method>();
			
			List<String> tableFields = getTableFields(c);
			
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				String fieldName = getMetodNameWithoutSetOrGetOrIsLowerCase(m);
				if(tableFields.contains(fieldName)) {
					continue;
				}
				
				if (m.getName().length() >= 3 && m.getName().substring(0, 3).equals("get") && !m.getName().equals("getClass")) {
					Class<?>[] definicionMethodo = m.getParameterTypes();

					if (definicionMethodo.length == 0) {
						lista.add(m);
					}
				}
				else if (m.getName().length() >= 2 && m.getName().substring(0, 2).equals("is") ) {
					Class<?>[] definicionMethodo = m.getParameterTypes();

					if (definicionMethodo.length == 0) {
						lista.add(m);
					}
				}
			}
			 


		return new ArrayList<Method>(lista);
	}

	/**
	 * Retorna una lista de String que contiene los Miembros del método que hacen referencia a otras tablas
	 * 
	 * @since 29-08-2018
	 * */
	@Override
	public List<String> getTableFields(Class c) {
		List<String> tableFields = new ArrayList<String>();
		
		TableReference[] tableReferences = getTableReference(c);
		
		if(tableReferences != null) {
			for(TableReference tb : tableReferences) {
				tableFields.add(tb.field());
			}
		} 
	 
		return tableFields;
	}
	
	@Override
	public TableReference[] getTableReference(Class c) {
		if(c != null) {
			Annotation ano = c.getAnnotation(TableReferences.class);
			if(ano instanceof TableReferences) {
				TableReference[] tableReferences = ((TableReferences) ano).value();
				return tableReferences;
			}
		}
		
		return null;
	}
	
	@Override
	public List<String> getTablePks(Class<? extends Vo> vo) {
		List<String> pks = new ArrayList<String>();
			
			if(vo != null) {
				TableDefinition td = VoTool.getInstance().getTableDefinition(vo);
				if(td != null) {
					for(PrimaryKeyDefinition pk : td.pks()) {
						pks.add(pk.field());
					}
				}
			}
			
		if(pks != null &&  (vo.getClass().isAssignableFrom(vo))) {
			logger.debug("cantidad de pks:"+pks.size());
		}
		 
		return pks;
	}
	
	@Override
	public Map<String,List<PrimaryKeyDefinition>> getTablePksMap(Class<? extends Vo> vo) {
 
		Map<String,List<PrimaryKeyDefinition>> pks =  new HashMap<String, List<PrimaryKeyDefinition>>();
			
			if(vo != null) {
				TableDefinition td = VoTool.getInstance().getTableDefinition(vo);
				if(td != null) {
					for(PrimaryKeyDefinition pk : td.pks()) {
						
						if(pks.get(pk.field()) == null) {
							pks.put(pk.field(), new ArrayList<PrimaryKeyDefinition>());
						}
						
						pks.get(pk.field()).add(pk);
					}
				}
			}
			
		 
		
		return pks;
	}
	
	@Override
	public TableDefinition getTableDefinition(Class<?> c) {
		if(c != null) {
			Annotation ano = c.getAnnotation(TableDefinition.class);
			if(ano instanceof TableDefinition) {
				return ((TableDefinition) ano);
			}
		}
		
		return null;
	}
	
	@Override
	public ForeignKeys getTableForeignKeysDefinition(Class<? extends Vo> c) {
		if(c != null) {
			Annotation ano = c.getAnnotation(ForeignKeys.class);
			if(ano instanceof ForeignKeys) {
				return ((ForeignKeys) ano);
			}
		}
		
		return null;
	}
	
	
	private List<String> getListFromValues(Map<Method, String> colsName) {
		Set<Method> sets = colsName.keySet();
		List<String> lista = new ArrayList<String>();
		for (Method m : sets) {
			lista.add(colsName.get(m));
		}

		return lista;

	}



	@Override
	public Method getMethod(Class clazz, String metodoName, Class[] def) {
		try {
			Method metodo = clazz.getDeclaredMethod(metodoName, def);
		 
			if (!metodo.isAccessible()) {
				metodo.setAccessible(true);
			}
			
			return metodo;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	@Override
	public Method getMethodOrSuperMethod(Class clazz, String metodoName, Class[] def) {
	 
			Method metodo = getMethod(clazz, metodoName, def);
			while(metodo == null && clazz.getSuperclass() != null) {
				clazz = clazz.getSuperclass();
				
				metodo = getMethod( clazz,  metodoName, def);
			}
			 
			if(metodo != null && !metodo.isAccessible()) {
				metodo.setAccessible(true);
			}
			
			return metodo;
		 
	}
	
	@Override
	public Method getMethodThrowError(Class clazz, String metodoName, Class[] def) throws SecurityException, NoSuchMethodException {
 
		Method metodo = clazz.getDeclaredMethod(metodoName, def);
	 
		if(metodo != null && !metodo.isAccessible()) {
			metodo.setAccessible(true);
		}
		
		return metodo;
		 
	}
	
	@Override
	public Method getSuperMethod(Class clazz, String metodoName, Class[] def) throws SecurityException, NoSuchMethodException {
		 
			Method metodo = clazz.getSuperclass().getMethod(metodoName, def);
			
			if(metodo != null && !metodo.isAccessible()) {
				metodo.setAccessible(true);
			}
			
			return metodo;
		 
	}

	@Override
	public String getMetodNameWithoutSetOrGetOrIsLowerCase(Method m) {
	 
		String retorno = null;
		
			String mName = m.getName();
			int start=0;
			if(mName.length() > 2 && mName.substring(0, 2).equals("is")) {
				start = 2;
			}
			if(mName.length() > 3 && (mName.substring(0, 3).equals("get") || mName.substring(0, 3).equals("set")) ) {
				start = 3;
			}
			
			if(start > 0) {
				String bdField = m.getName().substring(start, m.getName().length());
				bdField = bdField.substring(0, 1).toLowerCase() + bdField.substring(1, bdField.length());
				retorno= bdField;
			}
			
		//logger.debug("getMetodNameWithoutSetOrGetOrIsLowerCase:"+m.getName()+" = "+retorno);
		 
		return retorno;
	}

	@Override
	public Object getReturnFromMethod(Object objeto, Method metodo) {
		Object[] params = {};
		return getReturnFromMethod(objeto, metodo, params);
	}
	@Override
	public Object getReturnFromMethod(Object objeto, Method metodo, Object[] params) {
		
		try {
			return getReturnFromMethodThrowErrors(objeto, metodo, params);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Object getReturnFromMethodThrowErrors(Object objeto, Method metodo, Object[] params) throws Throwable {

		try {

			if (metodo != null && !metodo.isAccessible()) {
				metodo.setAccessible(true);
			}

			return metodo.invoke(objeto, params);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	@Override
	public Object getReturnFromMethod(Object objetoVO, String metodoName, Class[] defParams, Object[] params) {
		try {
			return cg.ejecutaMetodo(objetoVO, metodoName, defParams, params);

		} catch (NoSuchMethodException e) {
			System.out.println(e);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (InvocationTargetException e) {
			System.out.println(e);
		}

		return null;
	}
	
	@Override
	public Object getReturnFromMethodThrowError(Object objetoVO, String metodoName, Class[] defParams, Object[] params) throws NoSuchMethodException {
		try {
			return cg.ejecutaMetodo(objetoVO, metodoName, defParams, params);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private String[] getSetMethodsFromColName(VoToolSetMethod type, String c) {
		List<String> lista = new ArrayList<String>();
		if (c != null) {
			lista.add("set".concat(WordUtils.capitalize(c)));

			if (type == VoToolSetMethod.COMPLETELY_ABBREVIATON) {
				StringBuilder cn2 = new StringBuilder("set");
				String[] partes = c.split("_");
				for (String p : partes) {
					cn2.append(WordUtils.capitalize(p));
				}
			}

		}

		String[] ret = new String[lista.size()];
		lista.toArray(ret);
		return ret;
	}
	
	/**
	 * 
	 * Retorno la lista de fields de un VO desde el punto de vista de los SETS(set)
	 * @author Pancho
	 * @since 20-06-2018
	 * */
	@Override
	public List<String> getSetsFieldsWithNoParameters(Class c) {
		List<String> fields = new ArrayList<String>();
		if(c != null) {
			List<Method> metodos = getSetsMethodsWithOneParameter(c);
			for(Method m :  metodos) {
				fields.add(getMetodNameWithoutSetOrGetOrIsLowerCase(m));
			}
		}
		
		return fields;
	}
	/**
	 * retorna todos los métodos de una clase que empiezan con set
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	@Override
	public List<Method> getSetsMethodsWithOneParameter(Class c) {
		String key = "getSetsMethodsWithOneParameter_"+c;
		List<Method> lista = (List<Method>) WeakCacheLocator.getInstance(this.getClass()).get(key);
		
		if(lista == null) {
			lista = new LinkedList<Method>();
			
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (m.getName().substring(0, 3).equals("set") ) {
					Class[] definicionMethodo = m.getParameterTypes();

					if (definicionMethodo.length == 1) {
						lista.add(m);
					}
				}
			}
			
			WeakCacheLocator.getInstance(this.getClass()).put(key, lista);
		}
		 


		return lista;
	}
	
	
	private boolean intentaEjecutarElMetodo(Object objetoVO, Method metodo, Class[] posibleDefParam, Object[] oFromData) {
		try {
			cg.ejecutaMetodo(objetoVO, metodo.getName(), posibleDefParam, oFromData);
			return true;
		} catch (NoSuchMethodException e) {
			System.out.println(e);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (InvocationTargetException e) {
			System.out.println(e);
		}

		return false;
	}

	/**
	 * Imprime un vo en formato horizontal
	 * */
	@Override
	public <T>void printVo(T vo) {
		printVo(vo, System.out);
	}

	/**
	 * Imprime un vo en formato horizontal
	 * */
	@Override
	public <T>void printVo(T vo,PrintStream io) {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		if(vo != null) {
			Class classVO = vo.getClass();
			List<Method> lista = getGetsMethodsWithNoParameters(classVO);
			for(Method m : lista) {
				Class[] defMetodo = {};
				Object[] params = {};
				Object o = getReturnFromMethod(vo, m.getName(), defMetodo, params);
				if(!"".equals(str.toString())) {
					str.append(",");
				}
				str.append( getMetodNameWithoutSetOrGetOrIsLowerCase(m) ).append(":").append(o);
				
			}
		}
		
		if(io != null) {
			io.println(str.toString());
		}
	}

	/**
	 * 
	 * Retorna el valor de un field de un Vo
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	@Override
	public Object getFieldValue(Object vo, String field) {
		Object retorno = null;
		
		if(vo != null && field != null) {
			Method  m = getGetMethod(vo.getClass(), field);
			
			if(m != null && !m.isAccessible()) {
				m.setAccessible(true);
			}
			
			retorno = getReturnFromMethod(vo, m);
		}
		
		return retorno;
	}
	
	/**
	 * Retorna el valor del metodo get o is de un field (no necesita tener un field) es el valor del metodo que representa un field
	 * Puede ser includo un método de un parent
	 * @author Pancho
	 * */
	@Override
	public Object getMethodValue_byField(Object vo, String field) {
		Object retorno = null;
		
		if(vo != null && field != null) {
			Method theMethod = null;
			Map<String, Method> map = getAllGetMethodsWithNoParameters_map(vo.getClass());
			
			for(String fieldMethod : map.keySet()) {
				
				if(fieldMethod != null && fieldMethod.equals(field) ) {
					theMethod = map.get(fieldMethod);
					break;
				}
			}
			
			if(theMethod != null && !theMethod.isAccessible()) {
				theMethod.setAccessible(true);
			}
			
			retorno = getReturnFromMethod(vo, theMethod);
		}
		
		return retorno;
	}
	
	@Override
	public <T> T getFieldValue_fromField(Object object, String field, Class<T> type) {
		 
		try {
			return (T) FieldUtils.readDeclaredField(object, field, true);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void eachField(Vo grupo, IFieldIterable iFieldIterable) {
		if(grupo != null) {
			List<Method> metodos = getGetsMethodsWithNoParameters(grupo.getClass());
			for(Method m : metodos) {
				String field = getMetodNameWithoutSetOrGetOrIsLowerCase(m);
				
				if(field != null) {
					Object retorno = getReturnFromMethod(grupo, m);
					iFieldIterable.iterate(field, retorno);	
				}
			}
		}
		
	}
	
	@Override
	public void eachFieldFromVo(Object grupo, IFieldIterable iFieldIterable) {
		if(grupo != null) {
			List<Method> metodos = getGetsMethodsWithNoParameters(grupo.getClass());
			for(Method m : metodos) {
				String field = getMetodNameWithoutSetOrGetOrIsLowerCase(m);
				
				if(field != null) {
					Object retorno = getReturnFromMethod(grupo, m);
					iFieldIterable.iterate(field, retorno);	
				}
			}
		}
		
	}

	/**
	 * Retorna un lista de Objetos a partis de una lista de Vos, se usa para batchs SQLs
	 * @throws MethodNotFoundException 
	 * */
	@Override
	public List<Object[]> getObjectArray(List<? extends Vo> listaVos, String[] fieldsOrden) throws MethodNotFoundException  {
		List<Object[]> listParams = new LinkedList<Object[]>();
		
		if(listaVos != null && fieldsOrden != null && listaVos.size() > 0 && fieldsOrden.length > 0) {
			for(Vo vo : listaVos) {
				Map<String,Method> metodos = getGetMethodsWithNoParameters_map(vo.getClass());
				
				List<Object> params = new LinkedList<Object>();
				for(String pos : fieldsOrden) {
					Method m = metodos.get(pos);
					
					if(m != null) {
						Object retorno = getReturnFromMethod(vo, m);
						params.add(retorno);
					}
					else {
						throw new MethodNotFoundException(pos);
					}
				}
				
				listParams.add(params.toArray());
			}
		}
		
		return listParams;
	}
	
	@Override
	public boolean existMethod(String metodoName, Object o) {
		
		boolean existe = false;
		
		Map<String, Method> map = VoTool.getInstance().getAllGetMethodsWithNoParameters_map(o.getClass());
		for(Entry<String, Method> entry : map.entrySet()) {
			boolean tiene = entry.getValue().getName().equals(metodoName);
			if(tiene) {
				existe = true;
				break;
			}
		}
		
		//logger.debug("existMethod "+metodoName+" "+existe);
		return existe;
	}

	@Override
	public boolean containValue(Collection<?> listObjects, String fieldName, Object valurToFind) {
		boolean siLoContiene = false;
		
		if(listObjects != null && fieldName != null && valurToFind != null) {
			for(Object o : listObjects) {
				Object valor = getMethodValue_byField(o, fieldName);
				boolean contain = valor != null && valor.equals(valurToFind);
				if(contain) {
					siLoContiene = true;
					break;
				}
			}
		}
		
		return siLoContiene;
	}

	@Override
	public List<String> getTableForeignKeys(Class<? extends Vo> vo) {
		List<String> retorno = new ArrayList<String>();

		if (vo != null) {
			ForeignKeys fks = getTableForeignKeysDefinition(vo);
			if (fks != null) {
				for (ForeignKey fk : fks.foreignkeys()) {
					retorno.add(fk.fk());
				}
			}
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	public List<String> getPrimaryAndForeignkeys(Class<? extends Vo>[] clases) {
		List<String> retorno = new ArrayList<>();
		
		for(Class<?> classVo : clases) {
			List<String> pks = VoTool.getInstance().getTablePks((Class<? extends Vo>) classVo);
			List<String> fks = VoTool.getInstance().getTableForeignKeys((Class<? extends Vo>) classVo);
			
			retorno.addAll(pks);
			retorno.addAll(fks);
		}
		
		return retorno;
	}

	@Override
	public List<String> getPrimaryAndForeignkeys(Class<? extends Vo> clase) {
		List<Class<? extends Vo>> lista = new ArrayList<>();
		lista.add(clase);
		
		Class<? extends Vo>[] array = new Class[lista.size()];
		 lista.toArray(array);
		
		return getPrimaryAndForeignkeys( array);
	}

	/**
	 * Permite setear valores a una collection de javaBeans
	 * @author Pancho
	 * */
	@Override
	public <T> void setValue(Collection<T> vos, String fieldToSet, Object valor) {
		 
			if(vos != null && fieldToSet != null ) {
				for(T p : vos) {
					setValue(p, fieldToSet, valor);
				}
			}
	}
	
	@Override
	public <T>void setValue(T t, String fieldToSet, Object valor) {
		if(t != null && fieldToSet != null) {
			Method m = VoTool.getInstance().getSetMethod(t.getClass(), fieldToSet, 1);
			
			Object[] params = {ClaseConversor.getInstance().getObject(valor , m.getParameterTypes()[0])};
			VoTool.getInstance().getReturnFromMethod(t, m, params);
		}
		
	}
	

	@Override
	public <T> void capitalizeFully(Collection<T> vos, String[] fields) {
		if(vos != null && fields != null) {
			for(T t : vos) {
				List<String> fieldsList = ListUtils.getList(fields);
				if(t != null) {
					eachFieldFromVo(t , new IFieldIterable() {
						
						@Override
						public void iterate(String field, Object value) {
							if(fieldsList.contains(field)) {
								setValue(t, field, value);
							}
							
						}
					});
				}
			}
		}
		
	}
	
}