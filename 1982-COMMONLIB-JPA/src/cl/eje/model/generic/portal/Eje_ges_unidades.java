package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "unid_id", isForeignKey = false, numerica = false) }, tableName = "eje_ges_unidades")
public class Eje_ges_unidades extends Vo {
	private String unid_empresa;
	private String unid_id;
	private String unid_desc;
	private String area;
	private String vigente;
	private boolean nombre_bloqueado;
	private Integer id_tipo;
	private String texto;
	private Date fecha_no_vigente;

	public String getUnid_empresa() {
		return unid_empresa;
	}

	public void setUnid_empresa(String unid_empresa) {
		this.unid_empresa = unid_empresa;
	}

	public String getUnid_id() {
		return unid_id;
	}

	public void setUnid_id(String unid_id) {
		this.unid_id = unid_id;
	}

	public String getUnid_desc() {
		return unid_desc;
	}

	public void setUnid_desc(String unid_desc) {
		this.unid_desc = unid_desc;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getVigente() {
		return vigente;
	}

	public void setVigente(String vigente) {
		this.vigente = vigente;
	}

	public boolean isNombre_bloqueado() {
		return nombre_bloqueado;
	}

	public void setNombre_bloqueado(boolean nombre_bloqueado) {
		this.nombre_bloqueado = nombre_bloqueado;
	}

	public Integer getId_tipo() {
		return id_tipo;
	}

	public void setId_tipo(Integer id_tipo) {
		this.id_tipo = id_tipo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFecha_no_vigente() {
		return fecha_no_vigente;
	}

	public void setFecha_no_vigente(Date fecha_no_vigente) {
		this.fecha_no_vigente = fecha_no_vigente;
	}

}
