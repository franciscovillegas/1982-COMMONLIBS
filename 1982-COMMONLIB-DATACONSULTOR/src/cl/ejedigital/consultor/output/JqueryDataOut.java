package cl.ejedigital.consultor.output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;

/**
 * @deprecated
 * 
 * */
public class JqueryDataOut implements IDataOut {

	private DataList	data;

	public JqueryDataOut(DataList data) {
		this.data = data;
	}

	public JqueryDataOut(ConsultaData data) {
		this(data.getData());
	}

	public String getListData() {
		Set<String> set = null;
		Agrupador<JqueryDataOut.Dato> linea = null;
		Agrupador<JqueryDataOut.Dato> lineas = new Agrupador<JqueryDataOut.Dato>('{','}');
		
		int num=1;
		Field f = null;
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<JqueryDataOut.Dato>('{','}');
			
			set = map.keySet();
			
			for(String key : set) {
				f = (Field) map.get(key);

				linea.addDato(new Dato("'".intern().concat(key.intern()).concat("'".intern()), "'".intern().concat(f.toString()).concat("'".intern()) ));
			}

			lineas.addDato(new Dato("'linea_".concat(String.valueOf(num++)).concat("'".intern()), linea));
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
