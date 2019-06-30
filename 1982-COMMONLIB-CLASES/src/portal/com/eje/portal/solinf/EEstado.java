package portal.com.eje.portal.solinf;

import java.lang.reflect.Field;

public enum EEstado {
	solicitado(10),
	enproceso(15),
	rechazado(20),
	aceptado(30),
	editado(40),
	actualizado(50),
	enviado(60);

	private int idestado;
	
	EEstado(int idestado) {
		this.idestado = idestado;
	}
	
	public int getValor() {
		return this.idestado;
	}
	
	public static EEstado getValor(int intIdEstado) {
		if(intIdEstado != 0) {
			Field[] fields = EEstado.class.getDeclaredFields();
			for(Field f : fields) {
				EEstado e = Enum.valueOf(EEstado.class, f.getName());
				if(intIdEstado== e.getValor()) {
					return e; 
				}
			}	
		}
		throw new RuntimeException("No fue posible determinar el estado ["+EEstado.class.getName()+"]");
	}
}
