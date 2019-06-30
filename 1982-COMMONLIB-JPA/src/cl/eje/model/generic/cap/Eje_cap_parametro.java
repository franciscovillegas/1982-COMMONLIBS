package cl.eje.model.generic.cap;

import java.util.Date;

import cl.eje.jndi.cap.CapJndi;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = CapJndi.jndiCap, pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_parametro", isForeignKey = false, numerica = true) }, tableName = "eje_cap_parametro")
public class Eje_cap_parametro extends Vo {
	private int	id_parametro;
	private int	id_grupo;
	private int	id_subgrupo;
	private String value;
	private boolean	vigente;
	private Date fecha_creacion;
	private int	orden;
	private String icono;
	private String nemotecnico;
	public int getId_parametro() {
		return id_parametro;
	}
	public void setId_parametro(int id_parametro) {
		this.id_parametro = id_parametro;
	}
	public int getId_grupo() {
		return id_grupo;
	}
	public void setId_grupo(int id_grupo) {
		this.id_grupo = id_grupo;
	}
	public int getId_subgrupo() {
		return id_subgrupo;
	}
	public void setId_subgrupo(int id_subgrupo) {
		this.id_subgrupo = id_subgrupo;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	public Date getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public String getIcono() {
		return icono;
	}
	public void setIcono(String icono) {
		this.icono = icono;
	}
	public String getNemotecnico() {
		return nemotecnico;
	}
	public void setNemotecnico(String nemotecnico) {
		this.nemotecnico = nemotecnico;
	}
	
	
	
	
}