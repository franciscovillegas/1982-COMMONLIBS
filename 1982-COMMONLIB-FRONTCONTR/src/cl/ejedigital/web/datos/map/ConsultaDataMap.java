package cl.ejedigital.web.datos.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.consultor.DataFields;

public class ConsultaDataMap<T> extends ConsultaData {
	private Map<T, List<DataFields>> dataMap;
	/**
	 * 
	 */

	
	public ConsultaDataMap(List<String> nombreColumnas) {
		super(nombreColumnas);
		
		dataMap = new HashMap<T, List<DataFields>>();
	}
	
	public ConsultaDataMap(ConsultaData data) {
		super(data.getNombreColumnas());
		this.data.addAll(data.getData());
		
		setMode(ConsultaDataMode.CONVERSION);
		
		dataMap = new HashMap<T, List<DataFields>>();
	}

	public Map<T, List<DataFields>> getMap() {
		return dataMap;
	}
	
	public boolean containsKey(Object key) {
		return dataMap.containsKey(key);
	}
	
	public DataFields put(T key , DataFields value, boolean onlyOneTime) {
		
		if(!dataMap.containsKey(key)) {
			dataMap.put(key, new ArrayList<DataFields>());
		}
		
		boolean deboIngresar = !onlyOneTime  || onlyOneTime && dataMap.get(key).size() == 0; 
		if(deboIngresar) {
			dataMap.get(key).add(value);
		}
		
		return value;
	}
	
	
	/**
	 * Retorna una consultaData a partir de una agrupación selecionada al hora de construir el map<br/>
	 * Nunca retorna null
	 * 
	 * @author Pancho
	 * @since 26-09-2018
	 * */
	
	public ConsultaData getConsultaData() {
		return getConsultaData(null);
	}
	
	/**
	 * Retorna una consultaData a partir de una agrupación selecionada al hora de construir el map<br/>
	 * Nunca retorna null
	 * 
	 * @author Pancho
	 * @since 26-09-2018
	 * */
	
	public ConsultaData getConsultaData(Object key) {
		List<DataFields> dataFields = null;
		
		if(key == null) {
			dataFields = new ArrayList<DataFields>();
			
			Set<T> set = dataMap.keySet();
			for(T t : set) {
				dataFields.addAll(dataMap.get(t));
			}
			
		}
		else {
			dataFields = (dataMap.get(key));
		}
		
		if(dataFields != null && dataFields.size() > 0) {
			ConsultaData newData = new ConsultaData(new ArrayList<String>(getNombreColumnas()));
			newData.setMode(ConsultaDataMode.CONVERSION);
			
			for(DataFields df : dataFields) {
				newData.add(df);
			}
			
			return newData;
		}
		else {
			return null;
		}
	}
	
	public Set<T> keySet() {
		return dataMap.keySet();
	}
 
}
