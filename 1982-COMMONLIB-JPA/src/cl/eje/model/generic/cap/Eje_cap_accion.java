package cl.eje.model.generic.cap;

import java.util.Date;

import cl.eje.jndi.cap.CapJndi;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = CapJndi.jndiCap, pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_seccion", isForeignKey = false, numerica = true) }, tableName = "eje_cap_accion")
public class Eje_cap_accion extends Vo {
	private int id_seccion;
	private String nombre;
	private int direccion_region;
	private int direccion_comuna;;
	private int direccion_ciudad;
	private String direccion_calle;
	private String direccion_numero;
	private Date fecha_publicacion;
	private Date fecha_promocion;
	private Date accion_tope;
	private int id_curso;
	private int id_periodo;
	private int tipo_jornada;
	private int tipo_alcance;
	public int getId_seccion() {
		return id_seccion;
	}
	public void setId_seccion(int id_seccion) {
		this.id_seccion = id_seccion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getDireccion_region() {
		return direccion_region;
	}
	public void setDireccion_region(int direccion_region) {
		this.direccion_region = direccion_region;
	}
	public int getDireccion_comuna() {
		return direccion_comuna;
	}
	public void setDireccion_comuna(int direccion_comuna) {
		this.direccion_comuna = direccion_comuna;
	}
	public int getDireccion_ciudad() {
		return direccion_ciudad;
	}
	public void setDireccion_ciudad(int direccion_ciudad) {
		this.direccion_ciudad = direccion_ciudad;
	}
	public String getDireccion_calle() {
		return direccion_calle;
	}
	public void setDireccion_calle(String direccion_calle) {
		this.direccion_calle = direccion_calle;
	}
	public String getDireccion_numero() {
		return direccion_numero;
	}
	public void setDireccion_numero(String direccion_numero) {
		this.direccion_numero = direccion_numero;
	}
	public Date getFecha_publicacion() {
		return fecha_publicacion;
	}
	public void setFecha_publicacion(Date fecha_publicacion) {
		this.fecha_publicacion = fecha_publicacion;
	}
	public Date getFecha_promocion() {
		return fecha_promocion;
	}
	public void setFecha_promocion(Date fecha_promocion) {
		this.fecha_promocion = fecha_promocion;
	}
	public Date getAccion_tope() {
		return accion_tope;
	}
	public void setAccion_tope(Date accion_tope) {
		this.accion_tope = accion_tope;
	}
	public int getId_curso() {
		return id_curso;
	}
	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}
	public int getId_periodo() {
		return id_periodo;
	}
	public void setId_periodo(int id_periodo) {
		this.id_periodo = id_periodo;
	}
	public int getTipo_jornada() {
		return tipo_jornada;
	}
	public void setTipo_jornada(int tipo_jornada) {
		this.tipo_jornada = tipo_jornada;
	}
	public int getTipo_alcance() {
		return tipo_alcance;
	}
	public void setTipo_alcance(int tipo_alcance) {
		this.tipo_alcance = tipo_alcance;
	}
	
	
	
	
	
}
