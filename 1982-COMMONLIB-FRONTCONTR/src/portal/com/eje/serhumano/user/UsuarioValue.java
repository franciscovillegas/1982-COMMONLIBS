package portal.com.eje.serhumano.user;

import java.io.Serializable;

import cl.ejedigital.tool.misc.Cronometro;

public class UsuarioValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7539601413100356166L;
	private Object object;
	private Cronometro cro;
	private double max;
	
	
	public UsuarioValue(Object o) {
		this(o, 0);
	}
	
	public UsuarioValue(Object o, double millisecondsToLive) {
		cro = new Cronometro();
		cro.start();
		
		this.object = o;
		this.max = millisecondsToLive;
	}
	
	
	public double getMillisecondsOld() {
		return cro.GetMilliseconds();
	}
	
	public Object getObject() {
		return this.object;
	}
	
	public Object getObject(double millisecondsMaxOld) {
		if( millisecondsMaxOld == 0 || 	this.max >= getMillisecondsOld() ) {
			
			return this.object;	
		}
		else {
			return null;
		}
	}
}
