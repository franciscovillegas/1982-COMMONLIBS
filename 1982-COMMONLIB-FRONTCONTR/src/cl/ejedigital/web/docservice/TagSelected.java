package cl.ejedigital.web.docservice;

import portal.com.eje.portal.vo.vo.Vo;

public class TagSelected extends Vo {
	private int id_tag;
	private String nombre;
	private boolean selected;
	public int getId_tag() {
		return id_tag;
	}
	public void setId_tag(int id_tag) {
		this.id_tag = id_tag;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	
}
