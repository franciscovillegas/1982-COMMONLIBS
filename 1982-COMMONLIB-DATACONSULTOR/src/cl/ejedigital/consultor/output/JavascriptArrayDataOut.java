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
public class JavascriptArrayDataOut implements IDataOut {
	private char openLinea;
	private char closeLinea;
	private char openParam;
	private char closeParam;
	private char quotes;
	private DataList	data;
	private IFieldManipulator iManipulator;

	public JavascriptArrayDataOut(DataList data, char openLinea, char closeLinea, char openParam, char closeParam, char quotes) {
		this.openLinea = openLinea;
		this.closeLinea = closeLinea;
		this.openParam = openParam;
		this.closeParam = closeParam;
		this.quotes = quotes;
		this.data = data;
		iManipulator = new ManipulatorSqlServer2000();
	}
	
	public JavascriptArrayDataOut(DataList data) {
		this(data,'[',']','[',']','\'');
	}

	public JavascriptArrayDataOut(ConsultaData data) {
		this(data.getData());
	}
	
	public void setIFieldManipuldor(IFieldManipulator iManipulator) {
		if(iManipulator != null) {
			this.iManipulator = iManipulator;
		}
	}

	public String getListData() {
		Set<String> set = null;
		Agrupador<String> linea = null;
		Agrupador<Agrupador<String>> lineas =  new Agrupador<Agrupador<String>>(this.openLinea,this.closeLinea );
		
		Field f = null;
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<String>(this.openParam,this.closeParam);
			
			set = map.keySet();
			
			for(String key : set) {
				f = (Field) map.get(key);
				
				if(f.getObject() instanceof IDataOut) {
					linea.addDato( ((IDataOut) f.getObject()).getListData() );
				}
				else {
					if(iManipulator.getForcedString(f) != null) {
						linea.addDato(new StringBuilder("").append(quotes).append(iManipulator.getForcedString(f)).append(quotes).toString());
					}
					else {
						linea.addDato(new StringBuilder("").append(quotes).append(quotes).toString());
					}
				}
			}

			lineas.addDato(linea);
		}

		return lineas.toString();
	}
	
	public String getListData(String[] order) {
		Agrupador<String> linea = null;
		Agrupador<Agrupador<String>> lineas =  new Agrupador<Agrupador<String>>(this.openLinea,this.closeLinea);
		
		Field f = null;
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<String>(this.openParam,this.closeParam);
			
			for(Object key : order) {
				f = (Field) map.get(key);
				
				if(f.getObject() instanceof IDataOut) {
					linea.addDato( ((IDataOut) f.getObject()).getListData() );
				}
				else {
					if(iManipulator.getForcedString(f) != null) {
						linea.addDato(new StringBuilder("").append(quotes).append(iManipulator.getForcedString(f)).append(quotes).toString());
					}
					else {
						linea.addDato(new StringBuilder("").append(quotes).append(quotes).toString());
					}
				}
			}

			lineas.addDato(linea);
		}

		return lineas.toString();
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
