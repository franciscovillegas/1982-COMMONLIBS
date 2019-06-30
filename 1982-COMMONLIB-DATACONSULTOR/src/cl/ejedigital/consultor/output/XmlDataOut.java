package cl.ejedigital.consultor.output;

import java.util.ArrayList;
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

public class XmlDataOut implements IDataOut {
	private List<String> filtro;
	private String openLinea;
	private String closeLinea;
	private String openParam;
	private String closeParam;
	private String quotes;
	private IFieldManipulator iManipulator;
	private DataList	data;
	private IEscape escape;
	
	private XmlDataOut(DataList data, String openLinea, String closeLinea, String openParam, String closeParam, String quotes) {
		this.openLinea = openLinea;
		this.closeLinea = closeLinea;
		this.openParam = openParam;
		this.closeParam = closeParam;
		this.quotes = quotes;
		this.data = data;
		iManipulator = new ManipulatorSqlServer2000();
		
		this.escape = new Escape();
	}
	

	public XmlDataOut(DataList data) {
		this(data," "," ","<registro>","</registro>","");
	}
	
	public XmlDataOut(ConsultaData data) {
		this(data.getData());
	}
	
	public XmlDataOut(HashMap<String,Object> data) {
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
		Agrupador<XmlDataOut.Dato> linea = null;
		Agrupador<XmlDataOut.DatoSimple> lineas = new Agrupador<XmlDataOut.DatoSimple>(this.openLinea,this.closeLinea);
		
		Field f = null;
		Object o = null;
		String value = null;
		
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<XmlDataOut.Dato>(this.openParam,this.closeParam );
			
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
					f = new Field("");
				}
					
				if(f.getObject() instanceof IDataOut) {
					value = ((IDataOut) f.getObject()).getListData();
				}
				else if (f.getObject() != null && f.getObject() instanceof ReservedWord) {
					value = escape.escape(f.getObject().toString());
				}
				else if (f.getObject() != null) {
					value = iManipulator.getForcedString(f);
					value = "".intern().concat(escape.escape(value.toString())).concat("".intern()) ;
				}
				else {
					value = "".intern();
				}
					
				linea.addDato(new Dato("".intern().concat(key.intern()).concat("".intern()), value));
			}				


			lineas.addDato(new DatoSimple(linea));
		}

		return "<data>".concat(lineas.toString()).concat("</data>");
	}
	
	
	/**
	 * Usa el método Filtro
	 * 
	 * @deprecated
	 * */
	public String getListData(String[] order) {
		Set<String> set = null;
		Agrupador<XmlDataOut.Dato> linea = null;
		Agrupador<XmlDataOut.DatoSimple> lineas = new Agrupador<XmlDataOut.DatoSimple>(this.openLinea,this.closeLinea);
		
		Field f = null;
		Object o = null;
		String value = null;
		
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<XmlDataOut.Dato>(this.openParam,this.closeParam );
			
			for(String key : order) {
				f = (Field) map.get(key);
				
				if(f.getObject() instanceof IDataOut) {
					value = ((IDataOut) f.getObject()).getListData();
				}
				else {
					value = iManipulator.getForcedString(f);
					
					if(value != null) {
						value = "".intern().concat(escape.escape(value.toString())).concat("".intern()) ;
					}
					else {
						value = "".intern();
					}
					
				}
					
				linea.addDato(new Dato("".intern().concat(key.intern()).concat("".intern()), value));
			}

			lineas.addDato(new DatoSimple(linea));
		}

		return "<data>".concat(lineas.toString()).concat("</data>");
	}
	
	public void setEscape(IEscape escape) {
		if(escape != null)
			this.escape = escape;
	}
	
	private class Agrupador<E> {
		List<E> list;
		private String inicio;
		private String termino;
		
		Agrupador(String inicio, String termino) {
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
					str.append("");
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
			return new StringBuilder("<field>").append("<name>").append(key).append("</name><value>").append(value).append("</value></field>").toString();
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
		Agrupador<XmlDataOut.Dato> linea = null;
		Agrupador<XmlDataOut.DatoSimple> lineas = new Agrupador<XmlDataOut.DatoSimple>(this.openLinea,this.closeLinea);
		
		List<String> lista = new ArrayList<String>();
		for(String o : only) {
			lista.add(o);
		}
		
		Field f = null;
		Object o = null;
		String value = null;
		
		for(HashMap<String, ?> map : data ) {
			linea = new Agrupador<XmlDataOut.Dato>(this.openParam,this.closeParam );
			
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
						value = "".intern().concat(iManipulator.getForcedString(f).toString()).concat("".intern()) ;
					}
					else {
						value = "".intern();
					}
					
				}
					
				linea.addDato(new Dato("".intern().concat(key.intern()).concat("".intern()), value));
			}

			lineas.addDato(new DatoSimple(linea));
		}

		return "<data>".concat(lineas.toString()).concat("</data>");
	}
	
	public void setFilter(List<String> l) {
		this.filtro = l;
	}
}
