package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_producto", isForeignKey = true, numerica = true),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_evento", isForeignKey = true, numerica = true),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_suceso", isForeignKey = true, numerica = true) }, tableName = "eje_wf_entidades_base_position")
public class Eje_wf_entidades_base_position extends Vo {
	private int id_producto;
	private int id_evento;
	private int id_suceso;
	private int base_top;
	private int base_left;
	private int max_height;
	private int max_width;
	private double zoom;
	
	public int getId_producto() {
		return id_producto;
	}
	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}
	public int getId_evento() {
		return id_evento;
	}
	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}
	public int getId_suceso() {
		return id_suceso;
	}
	public void setId_suceso(int id_suceso) {
		this.id_suceso = id_suceso;
	}
	public int getBase_top() {
		return base_top;
	}
	public void setBase_top(int base_top) {
		this.base_top = base_top;
	}
	public int getBase_left() {
		return base_left;
	}
	public void setBase_left(int base_left) {
		this.base_left = base_left;
	}
	public int getMax_height() {
		return max_height;
	}
	public void setMax_height(int max_height) {
		this.max_height = max_height;
	}
	public int getMax_width() {
		return max_width;
	}
	public void setMax_width(int max_width) {
		this.max_width = max_width;
	}
	public double getZoom() {
		return zoom;
	}
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	 

}
