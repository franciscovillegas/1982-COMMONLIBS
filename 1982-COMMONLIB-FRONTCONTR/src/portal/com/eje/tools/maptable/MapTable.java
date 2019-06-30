package portal.com.eje.tools.maptable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.util.Assert;

import cl.ejedigital.web.datos.Order;
import portal.com.eje.portal.liquidacion.vo.TrabLiqPosibilidad;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.iface.IFieldIterable;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.ListUtils;
import portal.com.eje.tools.maptable.error.FieldDoesntHaveHash;
import portal.com.eje.tools.sortcollection.VoSort;
 

/**
 * Genera busquedas rápidas en una List<Vos>, esto lo hace generando maps por lo grupos indicados <br/>

 * @author Pancho
 * @since 12-04-2019
 * */
public class MapTable<T extends Vo> {
	private List<T> vos;
	private List<String> fields;
	private Map<String, Map<Object, List<T>>> maps;
	
	/**
	 * si fields == null entonces se generarán hash por todos los fields de un objeto <br/>
	 * Cuidado!!, es CASE SENITIVE en los nombres de Camos
	 * */
	public MapTable(List<T> vos, String[] campos) {
		this.vos = vos;
		
		
		Assert.notNull(vos, "El arreglo no puede ser null");
		addCampos(campos);
		
		
		buildMaps();
	}
	
	/**
	 * si fields == null entonces se generarán hash por todos los fields de un objeto <br/>
	 * Cuidado!!, es CASE SENITIVE en los nombres de Camos
	 * */
	public MapTable(List<T> vos, List<String> campos) {
		this.vos = vos;
		
		
		Assert.notNull(vos);
		this.fields = campos;
		
		buildMaps();
	}
	
	private void addCampos(String[] campos) {
		this.fields = new ArrayList<String>();
		
		for(String c : campos) {
			fields.add(c);	
		}
	}
	
	private void buildMaps() {
		maps = new HashMap<String, Map<Object,List<T>>>();
			
		for(final T vo : vos) {
			VoTool.getInstance().eachField(vo, new IFieldIterable() {
				
				@Override
				public void iterate(String field, Object value) {
					if(fields.contains(field)) {
						getListForValue(field, value).add(vo);
					}
					
				}
			});
		}
	}
	
	private Map<Object, List<T>> getMapForField(String campo) {
		if(maps.get(campo) == null) {
			maps.put(campo, new HashMap<Object, List<T>>());
		}
		
		return maps.get(campo);
	}
	

	@SuppressWarnings("unchecked")
	private List<Vo> getListForValue(String campo, Object value) {
		Map<Object, List<T>> mapValores = getMapForField(campo);
		if(mapValores.get(value) == null) {
			mapValores.put(value, new ArrayList<T>());
		}
		
		return (List<Vo>) mapValores.get(value);
	}
	
	/**
	 * Retorna una lista de Objetos filtrados por un valor de un campo
	 * 
	 * @throws FieldDoesntHaveHash
	 * */
	
	public List<? extends Vo> getFiltrados(String campo, Object value) {
		List<Object> valores = new ArrayList<Object>();
		valores.add(value);
		return getFiltradosIn(campo, valores);
	}
	
	/**
	 * Retorna una lista de Objetos filtrados por un conjunto de valores que pueda tener un campo
	 * 
	 * @throws FieldDoesntHaveHash
	 * */
	public List<? extends Vo> getFiltradosIn(String campo, List<Object> valores) {
		List<Vo> retorno = new ArrayList<Vo>();
		
		if(!fields.contains(campo)) {
			throw new FieldDoesntHaveHash();
		}
		
		if(maps.get(campo) != null) {
			for( Object valor : valores) {
				retorno.addAll(maps.get(campo).get(valor));
			}
		}
		
		return retorno;
	}

	public Collection<Vo> sortVo(List<Vo> collection, String campo,  Order order, Class<?> tipoDelCampo) {
		VoSort.getInstance().sort(collection, campo, tipoDelCampo , Order.Ascendente);
		
		return collection;
	}

	/**
	 * Retorna grupos de valores para un campo determinado
	 * 
	 * @author Pancho
	 * @since 12-04-2019
	 * */
	public Map<Object, List<T>> getGroupBy(String campo) {
		
		Map<Object, List<T>> retorno = maps.get(campo);
		if(retorno == null) {
			retorno = new HashMap<Object, List<T>>();
		}
		return retorno;
	}
	/**
	 * Retorna
	 * 
	 * @author Pancho
	 * @since 26-04-2019
	 * */
	public Map<Map<Object, Object>, List<T>> getGoupBy(String[] campos) {
		checkFields(campos);
		
		Map<String,Map<Object, List<T>>> grupos = new HashMap<String, Map<Object,List<T>>>();
		
		for(String campo : ListUtils.getList(campos)) {
			Map<Object, List<T>> grupoPorValores = getGroupBy(campo);
			grupos.put(campo, grupoPorValores);
		}
		
		Map<String, Object> keys = new HashMap<String, Object>();
		
		for( String campo : grupos.keySet()) {
			for(Object valorDelCampo : grupos.get(campo).keySet()) {
				keys.put(campo, valorDelCampo);
			}
		}
		
		throw new NotImplementedException();
	}
	
	private void checkFields(String[] campos) {
		
		List<String> toCheck = ListUtils.getList(campos);
		for(String campo : toCheck) {
			checkField(campo);
		}
		
	}
	
	private void checkField(String campo) {
		if(!fields.contains(campo)) {
			throw new FieldDoesntHaveHash();
		}
	}
	
 
	@SuppressWarnings("unchecked")
	public static <T>Map<Object, List<T>> groupBy(List<T> vos, String campo) {
		@SuppressWarnings("rawtypes")
		MapTable mapPorPeriodo = new MapTable(vos, new String[] {campo});
		return mapPorPeriodo.getGroupBy(campo);
	}
 
}
