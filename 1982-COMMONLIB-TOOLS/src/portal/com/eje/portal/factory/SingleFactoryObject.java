package portal.com.eje.portal.factory;

import cl.ejedigital.tool.misc.Cronometro;

public class SingleFactoryObject {
	private Cronometro cro;
	private Object o;
	private int cantVecesLlamada;

	public SingleFactoryObject(Object o) {
		this.o = o;
		cro=new Cronometro();
		cro.start();
	}
	
	public void countVecesLlamada() {
		cantVecesLlamada++;
	}
	
	public int getCantVecesLlamada() {
		return cantVecesLlamada;
	}



	public Object getObject() {
		return this.o;
	}
	
	public Cronometro getCronometro() {
		return this.cro;
	}
	
	public String toString() {
		return String.valueOf(o);
	}
	
	 
}
