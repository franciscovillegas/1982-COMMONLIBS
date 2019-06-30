package cl.ejedigital.consultor;

public class Field {

	private Object	o;

	public Field(Object o) {
		this.o = o;
	}

	public Object getObject() {
		return o;
	}
	
	public String toString() {
		return String.valueOf(o);
	}

}
