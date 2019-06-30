package cl.ejedigital.tool.misc;

public class CacheObject {
	private Cronometro cro;
	private Object o;
	
	public CacheObject(Object o) {
		this.o = o;
		cro = new Cronometro();
		cro.start();
	}
	
	public Object getObject() {
		return this.o;
	}
	
	public double getSecondOld() {
		return cro.GetMilliseconds() / 1000;
	}
}
