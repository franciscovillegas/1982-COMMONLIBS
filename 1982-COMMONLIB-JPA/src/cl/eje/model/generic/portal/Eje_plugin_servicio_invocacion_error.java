package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_error", isForeignKey = false, numerica = true) }, tableName = "eje_plugin_servicio_invocacion_error")
public class Eje_plugin_servicio_invocacion_error extends Vo {
	public int	id_error;
	public Date fecha;
	public String stack;
	public int	id_invocacion;
	public int getId_error() {
		return id_error;
	}
	public void setId_error(int id_error) {
		this.id_error = id_error;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getStack() {
		return stack;
	}
	public void setStack(String stack) {
		this.stack = stack;
	}
	public int getId_invocacion() {
		return id_invocacion;
	}
	public void setId_invocacion(int id_invocacion) {
		this.id_invocacion = id_invocacion;
	}
	
	
}
