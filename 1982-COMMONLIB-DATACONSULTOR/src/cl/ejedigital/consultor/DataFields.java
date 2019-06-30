package cl.ejedigital.consultor;

import java.util.LinkedHashMap;
import java.util.Set;


public class DataFields extends LinkedHashMap<String,Field> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	
	public Field put(String key, Object value) {
		return super.put(key, new Field(value));
	}

	@Override
	public Field put(String key, Field value) {
		// TODO Auto-generated method stub
		return super.put(key, value);
	}

	public void printKeysOverConsole() {
		Set<String> sets = keySet();
		StringBuilder str = new StringBuilder();
		for(String s:sets) {
			if(str.length() != 0) {
				str.append(",");
			}
			
			str.append(s);
		}
		
		System.out.println(str.toString());
		
	}
	
	@Override
	public Object clone() {
		DataFields df = new DataFields();
		Set<String> set = this.keySet();
		
		for(String k : set) {
			df.put(k, get(k) );
		}
		
		return df;
	}
}
