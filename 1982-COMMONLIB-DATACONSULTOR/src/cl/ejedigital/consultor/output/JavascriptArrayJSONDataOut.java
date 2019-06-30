package cl.ejedigital.consultor.output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IFieldManipulator;
import cl.ejedigital.consultor.ManipulatorSqlServer2000;

/**
 * @deprecated
 * */
public class JavascriptArrayJSONDataOut implements IDataOut {
	private IFieldManipulator iManipulator;
	private DataList	data;

	public JavascriptArrayJSONDataOut(DataList data) {
		this.data = data;
		iManipulator = new ManipulatorSqlServer2000();
	}

	public JavascriptArrayJSONDataOut(ConsultaData data) {
		this(data.getData());
	}
	
	public void setIFieldManipuldor(IFieldManipulator iManipulator) {
		if(iManipulator != null) {
			this.iManipulator = iManipulator;
		}
	}

	public String getListData() {
		Set<String> set = null;
		Agrupador<JavascriptArrayJSONDataOut.Dato> linea = null;
		Agrupador<JavascriptArrayJSONDataOut.DatoSimple> lineas = new Agrupador<JavascriptArrayJSONDataOut.DatoSimple>('[',']');
		
		Field f = null;
		Object o = null;
		String value = null;
		
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<JavascriptArrayJSONDataOut.Dato>('{','}');
			
			set = map.keySet();
			
			for(String key : set) {
				f = (Field) map.get(key);
				
				if(f.getObject() instanceof IDataOut) {
					value = ((IDataOut) f.getObject()).getListData();
				}
				else {
					value = iManipulator.getForcedString(f);
					
					if(value != null) {
						value = "\"".intern().concat(iManipulator.getForcedString(f).toString()).concat("\"".intern()) ;
					}
					else {
						value = "\"\"".intern();
					}
					
				}
					
				linea.addDato(new Dato("\"".intern().concat(key.intern()).concat("\"".intern()), value));
			}

			lineas.addDato(new DatoSimple(linea));
		}

		return lineas.toString();
	}
	
	public String getListData(String[] order) {
		throw new NotImplementedException();
	}
	
	private class Agrupador<E> {
		List<E> list;
		private char inicio;
		private char termino;
		
		Agrupador(char inicio, char termino) {
			this.list = new ArrayList<E>();
			this.inicio = inicio;
			this.termino = termino;
		}
		
		void addDato(E d) {
			list.add(d);
		}
		
		public String toString() {
			boolean primero = true;
			StringBuilder str = new StringBuilder();
			str.append(inicio);
			
			for(E d : list) {
				if(!primero) {
					str.append(",");
				}
				else {
					primero = false;
				}
				str.append(d.toString());
			}
			
			return str.append(termino).toString();
		}
		
	}
	
	private class Dato {
		private String key;
		private Object value;
		
		public Dato(String key, Object value) {
			super();
			this.key = key.intern();
			this.value = value;		
		}
		
		public String toString() {
			return new StringBuilder().append(key).append(":").append(value).toString();
		}
		
	}
	private class DatoSimple {
		private Object value;
		
		public DatoSimple(Object value) {
			if (value != null) {
				this.value = value;	
			}
			else {
				this.value = "".intern();
			}
		}
		
		public String toString() {
			return value.toString();
		}
		
	}
	public int getSize() {
		return this.data.size();
	}

	public String getListDataFiltered(String[] only) {
		throw new NotImplementedException();
	}
	
	public void setFilter(List<String> l) {
		throw new NotImplementedException();
	}
}
