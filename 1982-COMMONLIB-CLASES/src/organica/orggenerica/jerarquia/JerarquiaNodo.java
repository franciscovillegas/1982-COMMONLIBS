package organica.orggenerica.jerarquia;

import java.util.ArrayList;
import java.util.List;


public class JerarquiaNodo implements IJerarquiaNodo {
	private String nombre;
	private String key;
	private String classHtml;
	private List<IJerarquiaNodo> hijos;
	private boolean isRootNode;
	
	public JerarquiaNodo(String nombre, String key, String classHtml, boolean isRootNode) {
		this.nombre = nombre;
		this.key = key;
		this.hijos = new ArrayList<IJerarquiaNodo>();
		this.classHtml = classHtml;
		this.isRootNode = isRootNode;
	}
	
	public List<IJerarquiaNodo> getHijos() {
		return hijos;
	}

	public void addHijo(IJerarquiaNodo hijo) {
		hijos.add(hijo);		
	}

	public String getNombre() {
		return nombre;
	}

	public String getId() {
		return key;
	}

	public String getClassHtml() {
		return classHtml;
	}
	
	public void setClassHtml(String classHtml) {
		this.classHtml = classHtml;
	}

	public boolean isRootNode() {
		return isRootNode;
	}

	
	
}
