package portal.com.eje.portal.vo.iface;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import portal.com.eje.portal.vo.annotations.ForeignKeys;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.errors.MethodNotFoundException;
import portal.com.eje.portal.vo.vo.Vo;

public interface IVoTool {

	public <T> ConsultaData buildConsultaData(Collection<? extends Vo> collection);
	
	public <T> ConsultaData buildConsultaData(Collection<? extends Vo> collection , boolean incluyeReferencias);
	
	 
	
	/**
	 * Agrega a las datafiels de un consultaData (por cada fila) todos los valores de los objetos referenciados. <br/>
	 * también agrega simultaneamente los nombre de columnas necesarios
	 * 
	 * @author Pancho
	 * @since 25-07-2018
	 * 
	 * 	 * */
	public DataFields buildConsultaDataReferencias(DataFields df , List<String> colsName, Vo vo, String prefijoColName);

	public <T> Collection<T> buildVo(ConsultaData data, Class<T> clazz);
	
	 
	public <T> T buildVoSimple(ConsultaData data, Class<T> clazz);

	/**
	 * Concatena los valores de una collection, omitiendo el null
	 * 
	 * @author Pancho
	 * @since 25-06-2018
	 * */
	public String concatenateValues(Collection vos, String campo);

	 
	public <T> Collection<T> copy(Collection<? extends Vo> vos, Class<T> clazzNewObject);
	 
	/**
	 * Copia una collection de objetos en otra segunda
	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public <T> Collection<T> copy(Collection<? extends Vo> vos, Class<T> clazzNewObject, IVoValueChange iVoValueChange);
	
	/**

	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public <T> T copy(Vo vo, Class<? extends Vo> clazz);
	
	/**
	 * Copia una clase
	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public <T> T copy(Vo vo1, Vo copy);

	/**
	 * Copia una clase
	 * 19-05-2018
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public <T> T copy(Vo vo1, Vo copy, IVoValueChange iVoValueChange);

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
	public <T> T getCollection(Collection<? extends Vo> vos, Class<T> collectionClass, String fieldName);
	
	/**
	 * Siempre retorna una Collection con valores de un metodo del vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	public <T> T getCollection(Collection<? extends Vo> vos, Class<T> collectionClass, String fieldName, Class fieldNameClass);

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
	public <T>Map<Object, List<T>> getMapList(List<T> vos, Class<? extends Map> collectionClass, Class<? extends List> listClass, String fieldName);
	
	/**
	 * Retorna un vo a partir de la comparacion de un objeto con el retorno del método indicado
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */


	public <T>Collection<T> getCollection(Collection<T> vos, IVoFind voFind);
 
	/**
	 * Retorna el método del field
	 * 
	 * @since 03-07-2018
	 * @author Pancho
	 * */
	public Method getGetMethod(Class clazz, String campo);
	
	/**
	 * Retorna el método del field
	 * 
	 * @since 03-07-2018
	 * @author Pancho
	 * */
	public Method getSetMethod(Class clazz, String campo, Class[] defsNoClass);
	
	/**
	 * Retorna el método del field
	 * 
	 * @since 03-07-2018
	 * @author Pancho
	 * */
	public Method getSetMethod(Class clazz, String campo, int cantParams);
	
	public List<Method> getNoVoidMethod_noParams(Class clazz);
	
	/**
	 * Retorna un field de un Clase
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	public Field getField(Class clazz, String campo ) ;
	
	/**
	 * Retorna true si existe el campo como un Miembro de la clase
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	public boolean existeField(Class clazz, String campo);
	
	/**
	 * 
	 * Retorno la lista de fields de un VO desde el punto de vista de los GETS(get e is)
	 * @author Pancho
	 * @since 20-06-2018
	 * */
	public List<String> getGetsFieldsWithNoParameters(Class c);
	
	/**
	 * Retorna un Map con todos los métodos de una clase que empiezan con get, sin contar getClass
	 * 
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public Map<String, Method> getGetMethodsWithNoParameters_map(Class c);
	
	/**
	 * Retorna todos los métodos, sin excepción, y su field respectivo como KEY, cuando el métdoo no tiene parámetro y a su vez retorna algo distinto de void
	 * retornará field <enum, Method> si es que es un Enum
	 * 
	 * @author Pancho
	 * */
	public Map<String, Method> getAllGetMethodsWithNoParameters_map(Class<?> c);
	
	/**
	 * retorna todos los métodos de una clase que empiezan con get o is, sin contar getClass
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public List<Method> getGetsMethodsWithNoParameters(Class c);;
	
	/**
	 * Retorna una lista de String que contiene los Miembros del método que hacen referencia a otras tablas
	 * 
	 * @since 29-08-2018
	 * */
	public List<String> getTableFields(Class c);
	
	public TableReference[] getTableReference(Class c);
	
	public List<String> getTablePks(Class<? extends Vo> vo);
	
	public List<String> getTableForeignKeys(Class<? extends Vo> vo);
	
	public Map<String,List<PrimaryKeyDefinition>> getTablePksMap(Class<? extends Vo> vo);
	
	public TableDefinition getTableDefinition(Class<?> c);
	
	public Method getMethod(Class clazz, String metodoName, Class[] def);
	
	public Method getMethodOrSuperMethod(Class clazz, String metodoName, Class[] def);
	
	public Method getMethodThrowError(Class clazz, String metodoName, Class[] def) throws SecurityException, NoSuchMethodException;
	
	public Method getSuperMethod(Class clazz, String metodoName, Class[] def) throws SecurityException, NoSuchMethodException;

	public String getMetodNameWithoutSetOrGetOrIsLowerCase(Method m);

	public Object getReturnFromMethod(Object objeto, Method metodo);
	
	public Object getReturnFromMethod(Object objeto, Method metodo, Object[] params);
	
	public Object getReturnFromMethodThrowErrors(Object objeto, Method metodo, Object[] params) throws Throwable;

	public Object getReturnFromMethod(Object objetoVO, String metodoName, Class[] defParams, Object[] params);
	
	public Object getReturnFromMethodThrowError(Object objetoVO, String metodoName, Class[] defParams, Object[] params) throws NoSuchMethodException;

 
	/**
	 * 
	 * Retorno la lista de fields de un VO desde el punto de vista de los SETS(set)
	 * @author Pancho
	 * @since 20-06-2018
	 * */
	public List<String> getSetsFieldsWithNoParameters(Class c);
	
	/**
	 * retorna todos los métodos de una clase que empiezan con set
	 * @author Pancho
	 * @since 19-06-2018
	 * */
	public List<Method> getSetsMethodsWithOneParameter(Class c);
	
 	/**
	 * Imprime un vo en formato horizontal
	 * */
	public <T>void printVo(T vo);

	/**
	 * Imprime un vo en formato horizontal
	 * */
	public <T>void printVo(T vo,PrintStream io);

	/**
	 * 
	 * Retorna el valor de un field de un Vo
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	public Object getFieldValue(Object vo, String field);
	
	/**
	 * Retorna el valor del metodo get o is de un field (no necesita tener un field) es el valor del metodo que representa un field
	 * @author Pancho
	 * */
	public Object getMethodValue_byField(Object vo, String field);
	
	public <T> T getFieldValue_fromField(Object object, String field, Class<T> type);

	public void eachField(Vo grupo, IFieldIterable iFieldIterable);
	
	public void eachFieldFromVo(Object grupo, IFieldIterable iFieldIterable);

	/**
	 * Retorna un lista de Objetos a partis de una lista de Vos, se usa para batchs SQLs
	 * @throws MethodNotFoundException 
	 * */
	public List<Object[]> getObjectArray(List<? extends Vo> listaVos, String[] fieldsOrden) throws MethodNotFoundException;
	
	public boolean existMethod(String metodoName, Object o);

	/**
	 * Retorna true, si algun objeto de la lista contiene el valor en el campo "fieldName" <br/>
	 * Busca en todo el objeto incluso en los metodos del super
	 * */
	public boolean containValue(Collection<?> list, String fieldName, Object valurToFind);

	public ForeignKeys getTableForeignKeysDefinition(Class<? extends Vo> c);

	public List<String> getPrimaryAndForeignkeys(Class<? extends Vo> clase);
	
	public List<String> getPrimaryAndForeignkeys(Class<? extends Vo>[] clases);

	public <T>void setValue(Collection<T> vos, String fieldToSet, Object valor);

	public <T> void capitalizeFully(Collection<T> vos, String[] strings);

	 <T>void setValue(T t, String fieldToSet, Object valor);

	


	
}
