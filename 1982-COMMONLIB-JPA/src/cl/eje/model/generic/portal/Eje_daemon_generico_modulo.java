package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_mod_activacion", numerica = true, isForeignKey = false) }, tableName = ";")
public class Eje_daemon_generico_modulo extends Vo {
	private int id_mod_activacion;
	private Date fecha;
	private int id_modulo;
	private boolean activado;
	public int getId_mod_activacion() {
		return id_mod_activacion;
	}
	public void setId_mod_activacion(int id_mod_activacion) {
		this.id_mod_activacion = id_mod_activacion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getId_modulo() {
		return id_modulo;
	}
	public void setId_modulo(int id_modulo) {
		this.id_modulo = id_modulo;
	}
	public boolean isActivado() {
		return activado;
	}
	public void setActivado(boolean activado) {
		this.activado = activado;
	}
	
	
}
