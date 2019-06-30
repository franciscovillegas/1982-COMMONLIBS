package cl.ejedigital.consultor.def;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;

public class JsonObject extends JSonDataOut implements Map  {
	private DataFields fields;
	
	public JsonObject() {
		this( null);

	}
	
	public JsonObject(DataFields data) {
		super((DataList) null);
		
		this.data = new DataList();
		if( data == null) {
			fields = new DataFields();
		}
		else {
			fields = data;
		}
		
		this.data.add(fields);
	}

	public DataFields getDataFields() {
		return fields;
	}
	/*
 
	private static final long serialVersionUID = 2078624709177500508L;

	public IDataOut getValues() {
		DataList lista = new DataList();
		DataFields fields = new DataFields();
		
		Set<String> keys = keySet();
		for(String k : keys) {
			Object o = get(k);
			
			if(o instanceof Integer) {
				fields.put(k, Integer.parseInt( (String)o ));
			}
			else if(o instanceof Double) {
				fields.put(k, Double.parseDouble( (String)o ));
			}
			else if(o instanceof Long) {
				fields.put(k, Long.parseLong( (String)o ));
			}
			else if(o instanceof Byte) {
				fields.put(k, Byte.parseByte( (String)o ));
			}
			else if(o instanceof Short) {
				fields.put(k, Short.parseShort( (String)o ));
			}
			else if(o instanceof Float) {
				fields.put(k, Float.parseFloat( (String)o ));
			}
			else if(o instanceof Boolean) {
				fields.put(k, Boolean.parseBoolean( (String)o ));
			}
			else if(o instanceof String) {
				fields.put(k,  (String)o );
			}
			else if(o instanceof Field ) {
				fields.put(k,  ((Field) o).getObject() );
			}
			else {
				fields.put(k, "Error de conversión");
			}
		}
		
		lista.add(fields);	
		
		
		this.data = lista;
	}
	 */
	public int size() {
		// TODO Auto-generated method stub
		return fields.size();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return fields.isEmpty();
	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return fields.containsKey(key);
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return fields.containsValue(value);
	}

	public Object get(Object key) {
		// TODO Auto-generated method stub
		return fields.get(key);
	}

	public Object put(Object key, Object value) {
		// TODO Auto-generated method stub
		
		if(false && value instanceof JsonObject) {
			JsonObject jsonPivot = new JsonObject();
			jsonPivot.put(key, value);
			return put("valor", jsonPivot);	
		}
		else {
			return fields.put(String.valueOf(key), value);
		}
		
	}

	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return fields.remove(key);
	}

	public void putAll(Map m) {
		// TODO Auto-generated method stub
		fields.putAll(m);
	}

	public void clear() {
		// TODO Auto-generated method stub
		fields.clear();
	}

	public Set keySet() {
		// TODO Auto-generated method stub
		return fields.keySet();
	}

	public Collection values() {
		// TODO Auto-generated method stub
		return fields.values();
	}

	public Set entrySet() {
		// TODO Auto-generated method stub
		return fields.entrySet();
	}
	
}
