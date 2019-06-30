package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_rol", numerica = true, isForeignKey = false) }, tableName = "eje_wf_tupla_rol")
public class Eje_wf_tupla_rol extends Vo {
	private int id_rol;
	private int id_producto;
	private int id_evento;
	private int id_suceso;
	private int rol_type;
	private String rol_name;
	private String rol_detecter;
	private int roldetecter;
	private int flowaccion;
	private int flowdesicion;
	private int flowide_in;
	private int flowide_out;
	private int flowide_visualizacion;
	private int otherside_id_rol;
	
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
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
	public int getRol_type() {
		return rol_type;
	}
	public void setRol_type(int rol_type) {
		this.rol_type = rol_type;
	}
	public String getRol_name() {
		return rol_name;
	}
	public void setRol_name(String rol_name) {
		this.rol_name = rol_name;
	}
	public String getRol_detecter() {
		return rol_detecter;
	}
	public void setRol_detecter(String rol_detecter) {
		this.rol_detecter = rol_detecter;
	}
	public int getRoldetecter() {
		return roldetecter;
	}
	public void setRoldetecter(int roldetecter) {
		this.roldetecter = roldetecter;
	}
	public int getFlowaccion() {
		return flowaccion;
	}
	public void setFlowaccion(int flowaccion) {
		this.flowaccion = flowaccion;
	}
	public int getFlowdesicion() {
		return flowdesicion;
	}
	public void setFlowdesicion(int flowdesicion) {
		this.flowdesicion = flowdesicion;
	}
	public int getFlowide_in() {
		return flowide_in;
	}
	public void setFlowide_in(int flowide_in) {
		this.flowide_in = flowide_in;
	}
	public int getFlowide_out() {
		return flowide_out;
	}
	public void setFlowide_out(int flowide_out) {
		this.flowide_out = flowide_out;
	}
	public int getFlowide_visualizacion() {
		return flowide_visualizacion;
	}
	public void setFlowide_visualizacion(int flowide_visualizacion) {
		this.flowide_visualizacion = flowide_visualizacion;
	}
	public int getOtherside_id_rol() {
		return otherside_id_rol;
	}
	public void setOtherside_id_rol(int otherside_id_rol) {
		this.otherside_id_rol = otherside_id_rol;
	}
	
	
	
}
