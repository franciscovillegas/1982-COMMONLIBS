package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_modulo", isForeignKey = false, numerica = true) }, tableName = "eje_generico_modulo")
public class Eje_generico_modulo extends Vo {
	private int	id_modulo;
	private String nombre;
	private String contexto;
	private Date fec_update;
	private String descripcion;
	private Boolean vigente;
	
	public int getId_modulo() {
		return id_modulo;
	}
	public void setId_modulo(int id_modulo) {
		this.id_modulo = id_modulo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getContexto() {
		return contexto;
	}
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	public Date getFec_update() {
		return fec_update;
	}
	public void setFec_update(Date fec_update) {
		this.fec_update = fec_update;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getVigente() {
		return vigente;
	}
	public void setVigente(Boolean vigente) {
		this.vigente = vigente;
	}
	
	
	
}
