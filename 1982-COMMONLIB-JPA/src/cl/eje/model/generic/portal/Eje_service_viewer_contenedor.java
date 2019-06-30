package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { 
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_contenedor", isForeignKey = false, numerica = true) }, 
		tableName = "eje_service_viewer_contenedor")
public class Eje_service_viewer_contenedor extends Vo {
	private int id_contenedor;
	private String nombre;
	private Boolean activa;
	private int id_cliente;
	
	public int getId_contenedor() {
		return id_contenedor;
	}
	public void setId_contenedor(int id_contenedor) {
		this.id_contenedor = id_contenedor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean getActiva() {
		return activa;
	}
	public void setActiva(Boolean activa) {
		this.activa = activa;
	}
	public int getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}
	
	
}
