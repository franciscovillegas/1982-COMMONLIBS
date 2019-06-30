package highchart.multipleifaces.component;

import cl.ejedigital.consultor.def.JsonObject;
import cl.ejedigital.consultor.def.JsonObjects;

public class EasySerie extends JsonObject {
	private JsonObjects valores;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5734263350870514632L;
	
	public EasySerie(String nombre) {
		
		valores = new JsonObjects();
		put("data",valores);
		
		setNombre(nombre);
	}
	
	
	public void setNombre(String nombre) {
		put("name", nombre);
	}
	
	public void addProp(String prop, Object valor) {
		put(prop, valor);	
	}
	
	public EasySerie addValor(JsonObject valor) {
		valores.add(valor);
		return this;
	}
	
	public EasySerie addValor(Double valor) {
		valores.add(valor);
		return this;
	}
	
	public EasySerie addValor(Double valor,Double valor2) {
		valores.add(valor, valor2);
		return this;
	}
	
	public EasySerie addValor(Integer valor) {
		valores.add(valor);
		return this;
	}
	
	public int sizeValores() {
		return valores.size();
	}

}
