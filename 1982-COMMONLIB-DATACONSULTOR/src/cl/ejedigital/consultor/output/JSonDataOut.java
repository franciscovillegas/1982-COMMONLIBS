package cl.ejedigital.consultor.output;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IFieldManipulator;
import cl.ejedigital.consultor.ManipulatorSqlServer2000;


/**
 * Retorna JSON del tipo Hash {param1:valor1, param2: valor2}
 * 
 * */
public class JSonDataOut implements IDataOut {
	private List<String> filtro;
	private char openLinea;
	private char closeLinea;
	private char openParam;
	private char closeParam;
	private char quotes;
	private IFieldManipulator iManipulator;
	protected DataList	data;
	private IEscape escape;
	private String formatDate;
	
	protected JSonDataOut() {
		
	}
	private JSonDataOut(DataList data, char openLinea, char closeLinea, char openParam, char closeParam, char quotes) {
		this.openLinea = openLinea;
		this.closeLinea = closeLinea;
		this.openParam = openParam;
		this.closeParam = closeParam;
		this.quotes = quotes;
		this.data = data;
		iManipulator = new ManipulatorSqlServer2000();
		
		this.escape = new EscapeJavascript();
		this.formatDate = "yyyy-MM-dd HH:mm:ss";
	}
	

	public JSonDataOut(DataList data) {
		this(data,' ',' ','{','}','\"');
	}
	
	public JSonDataOut(ConsultaData data) {
		this(data.getData());
	}
	
	public JSonDataOut(HashMap<String,Object> data) {
		this((DataList)null);
		
		DataList lista = new DataList();
		DataFields fields = new DataFields();
		
		Set<String> set = data.keySet();
		
		for(String k : set) {
			fields.put(k, new Field(data.get(k)));
		}
		
		lista.add(fields);
		this.data = lista;
	}

	
	public void setIFieldManipuldor(IFieldManipulator iManipulator) {
		if(iManipulator != null) {
			this.iManipulator = iManipulator;
		}
	}

	public String getListData() {
		Set<String> set = null;
		Agrupador<JSonDataOut.Dato> linea = null;
		Agrupador<JSonDataOut.DatoSimple> lineas = new Agrupador<JSonDataOut.DatoSimple>(this.openLinea,this.closeLinea);
		
		Field f = null;
		Object o = null;
		String value = null;
		
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<JSonDataOut.Dato>(this.openParam,this.closeParam );
			
			if(filtro != null ) {
				set = new LinkedHashSet<String>(filtro);
			}
			else {
				set = map.keySet();
			}
			
			for(String key : set) {
				f = (Field) map.get(key);
				if(f == null && key != null) {
					f = (Field) map.get(key.toLowerCase());
				}

				if(f == null) {
					f = new Field(null);
				}
					
				if(f.getObject() instanceof IDataOut) {
					value = ((IDataOut) f.getObject()).getListData();
				}
				else if(f.getObject() instanceof Short 		||
						   f.getObject() instanceof Integer 	||
						   f.getObject() instanceof Long 		||
						   f.getObject() instanceof Float 		||
						   f.getObject() instanceof Double 		||
						   f.getObject() instanceof Boolean		||
						   f.getObject() instanceof ReservedWord ) {
							
					value = escape.escape(f.getObject().toString());
				}
				else if(f.getObject() instanceof GsonObject) {
					value = f.getObject().toString();
				}
				else if (f.getObject() instanceof Date) {
					value = "\"".concat(toDate((Date) f.getObject(), this.formatDate)).concat("\"") ;	
				}
				else if (f.getObject() != null) {
					value = iManipulator.getForcedString(f);
					if(value != null) {
						value = "\"".concat(escape.escape(value.toString())).concat("\"") ;	
					}
					else {
						value = "\"\"" ;
					}
					
				}
				else {
					value = "null";
				}
					
				linea.addDato(new Dato("\"".concat(key.intern()).concat("\""), value));
			}				


			lineas.addDato(new DatoSimple(linea));
		}

		return lineas.toString();
	}
	
	
	/**
	 * Usa el método Filtro
	 * 
	 * @deprecated
	 * */
	public String getListData(String[] order) {
		Set<String> set = null;
		Agrupador<JSonDataOut.Dato> linea = null;
		Agrupador<JSonDataOut.DatoSimple> lineas = new Agrupador<JSonDataOut.DatoSimple>(this.openLinea,this.closeLinea);
		
		Field f = null;
		Object o = null;
		String value = null;
		
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<JSonDataOut.Dato>(this.openParam,this.closeParam );
			
			for(String key : order) {
				f = (Field) map.get(key);
				
				if(f.getObject() instanceof IDataOut) {
					value = ((IDataOut) f.getObject()).getListData();
				}
				else {
					value = iManipulator.getForcedString(f);
					
					if(value != null) {
						value = "\"".intern().concat(escape.escape(value.toString())).concat("\"".intern()) ;
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
	
	public void setEscape(IEscape escape) {
		if(escape != null)
			this.escape = escape;
	}
	
	public void setFormatDate(String formatDate) {
		if(formatDate != null) {
			this.formatDate = formatDate;
		}
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
		Set<String> set = null;
		Agrupador<JSonDataOut.Dato> linea = null;
		Agrupador<JSonDataOut.DatoSimple> lineas = new Agrupador<JSonDataOut.DatoSimple>(this.openLinea,this.closeLinea);
		
		List<String> lista = new ArrayList<String>();
		for(String o : only) {
			lista.add(o);
		}
		
		Field f = null;
		Object o = null;
		String value = null;
		
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<JSonDataOut.Dato>(this.openParam,this.closeParam );
			
			set = map.keySet();
			
			for(String key : set) {
				if(lista.indexOf(key) == -1)
					continue;
				
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
	
	public void setFilter(List<String> l) {
		this.filtro = l;
	}
	
    public String toDate(Date fecha, String formatoDestino) {
    	if(fecha != null && formatoDestino != null) {
	    	SimpleDateFormat fDestino = new SimpleDateFormat(formatoDestino);
			
			String fechaDate;
			fechaDate = fDestino.format(fecha); 
	    	
			return fechaDate;
    	}
    	
    	return null;
    }

}
