package portal.com.eje.portal.vo.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class Where {
	private String campo;
	private String signus;
	private List values;
	private List<Where> ors;
	private List<Where> ands;
	
	public Where(String campo, String signus, Object value) {
		this.values = new ArrayList<Object>();
		this.campo = campo;
		this.signus = signus;
		ors = null;
		
		if(value != null && List.class.isAssignableFrom(value.getClass())) {
			this.values.addAll((List)value);
		}
		else {
			addValue(value);		
		}
		
		
		
	}
	
	public Where(String campo, String signus, List values) {
		this.values = new ArrayList<Object>();
		this.campo = campo;
		this.signus = signus;
		ors = null;
		
		this.values.addAll(values);
		
		
	}

	public String getCampo() {
		return campo;
	}

	public String getSignus() {
		return signus;
	}

	public int size() {
		return values.size();
	}

	public Object getValue() {
		if (values.size() > 0) {
			return values.get(0);
		}

		return null;
	}

	public void addValue(Object value) {
		values.add(value);
	}
	public List getValues() {
		if (values.size() > 0) {
			return values;
		}

		Assert.notEmpty(values,"Este Where no puede tener size=0");
		return null;
	}
	
	public List<Where> getOr() {
		return this.ors;
	}
	
	public List<Where> getAnd() {
		return this.ands;
	}
	
	public void addOr(Where where) {
		if(this.ors == null) {
			synchronized (Where.class) {
				if(this.ors == null) {
					this.ors = new ArrayList<Where>();
				}
			}
		}
		
		if(where != null) {
			this.ors.add(where);	
		}
	}
	
	public void addAnd(Where where) {
		if(this.ands == null) {
			synchronized (Where.class) {
				if(this.ands == null) {
					this.ands = new ArrayList<Where>();
				}
			}
		}
		
		if(where != null) {
			this.ands.add(where);	
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(campo).append(" ");
		str.append(signus).append(" {");
		boolean first = true;
		for(Object o : values) {
			if(first){
				first= false;
			}
			else {
				str.append(",");
			}
			str.append(o);	
		}
		str.append("}");
		return str.toString();
	}

	/**
	 * retorna un new where, es un shortcut
	 * 
	 * @author Pancho
	 * @since 28-09-2018
	 * */
	public static List<Where> buildList(String campo, String signus, Object value) {
		List<Where> lista = new ArrayList<Where>();
		lista.add(new Where(campo, signus, value));
		return lista;
	}

}
