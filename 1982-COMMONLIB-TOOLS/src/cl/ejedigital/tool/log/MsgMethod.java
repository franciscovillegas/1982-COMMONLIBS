package cl.ejedigital.tool.log;

import java.lang.reflect.Method;

import cl.ejedigital.tool.votools.IVoBase;
import cl.ejedigital.tool.votools.VoError;

class MsgMethod implements IVoBase {
	private Method metodo;
	private String mensaje;
	private VoError	error;
	
	@SuppressWarnings("unused")
	private MsgMethod() {
		super();
	}

	public MsgMethod(Method metodo) {
		this.metodo = metodo;
	}

	public MsgMethod(Class<?> clazz, String metodo, Class<?>[] parametros) {
		super();

		try {
			this.metodo = clazz.getDeclaredMethod(metodo, parametros);

		} catch (SecurityException e) {
			System.out.println("Clase:"+clazz+"\nMetodo:"+metodo+"\nParametros:"+parametros);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("Clase:"+clazz+"\nMetodo:"+metodo+"\nParametros:"+parametros);
			e.printStackTrace();
		}
	}

	public Method getMetodo() {
		return metodo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}






	public VoError getError() {
		return error;
	}



	public void setError(VoError error) {
		this.error = error;

	}

	public Double getVoCode() {
		// TODO Auto-generated method stub
		return 0d;
	}

}
