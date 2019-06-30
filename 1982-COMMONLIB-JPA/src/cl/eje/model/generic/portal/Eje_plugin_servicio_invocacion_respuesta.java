package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_respuesta", isForeignKey = false, numerica = true) }, tableName = "eje_plugin_servicio_invocacion_respuesta")
public class Eje_plugin_servicio_invocacion_respuesta extends Vo {
	private int id_respuesta;
	private Date fecha;
	private int rut;
	private int tiempo_ms;
	private int id_invocacion;
	public int getId_respuesta() {
		return id_respuesta;
	}
	public void setId_respuesta(int id_respuesta) {
		this.id_respuesta = id_respuesta;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public int getTiempo_ms() {
		return tiempo_ms;
	}
	public void setTiempo_ms(int tiempo_ms) {
		this.tiempo_ms = tiempo_ms;
	}
	public int getId_invocacion() {
		return id_invocacion;
	}
	public void setId_invocacion(int id_invocacion) {
		this.id_invocacion = id_invocacion;
	}
	
	
}
