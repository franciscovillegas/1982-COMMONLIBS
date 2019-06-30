package portal.com.eje.cache;

import cl.ejedigital.tool.misc.Cronometro;

public class ObjectConAntiguedad {
	private Object o;
	private Cronometro cro;

	public ObjectConAntiguedad(Object o) {
		super();
		this.o = o;
		cro = new Cronometro();
		cro.start();
	}

	public Object getObject() {
		return o;
	}

	public Cronometro getCro() {
		return cro;
	}

}
