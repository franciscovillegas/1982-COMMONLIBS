package highchart.easyifaces.component;

import cl.ejedigital.consultor.def.JsonObject;
import cl.ejedigital.consultor.def.JsonObjects;

public class EasySerieY extends JsonObject {
	private JsonObjects valores;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5734263350870514632L;
	
	public EasySerieY() {
		valores = new JsonObjects();
	}

	public void addProp(String prop, Object valor) {
		put(prop, valor);	
	}
	
	public EasySerieY addValor(JsonObject valor) {
		valores.add(valor);
		return this;
	}
	
	public EasySerieY addValor(Double valor) {
		valores.add(valor);
		return this;
	}
	
	public EasySerieY addValor(Double valor,Double valor2) {
		valores.add(valor, valor2);
		return this;
	}
	
	public EasySerieY addValor(Integer valor) {
		valores.add(valor);
		return this;
	}
	
	public int sizeValores() {
		return valores.size();
	}

}
