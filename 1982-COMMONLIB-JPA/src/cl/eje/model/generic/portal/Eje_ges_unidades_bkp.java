package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_ges_unidades_bkp")
public class Eje_ges_unidades_bkp extends Vo {
	private int id_corr;
	private String unid_id;
	private String unid_empresa;
	private String unid_desc;
	private String area;
	private Boolean vigente;
	private Boolean nombre_bloqueado;
	private int id_tipo;
	private String texto;
	private int id_movimiento;
	public int getId_corr() {
		return id_corr;
	}
	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}
	public String getUnid_id() {
		return unid_id;
	}
	public void setUnid_id(String unid_id) {
		this.unid_id = unid_id;
	}
	public String getUnid_empresa() {
		return unid_empresa;
	}
	public void setUnid_empresa(String unid_empresa) {
		this.unid_empresa = unid_empresa;
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
	public Boolean getVigente() {
		return vigente;
	}
	public void setVigente(Boolean vigente) {
		this.vigente = vigente;
	}
	public Boolean getNombre_bloqueado() {
		return nombre_bloqueado;
	}
	public void setNombre_bloqueado(Boolean nombre_bloqueado) {
		this.nombre_bloqueado = nombre_bloqueado;
	}
	public int getId_tipo() {
		return id_tipo;
	}
	public void setId_tipo(int id_tipo) {
		this.id_tipo = id_tipo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getId_movimiento() {
		return id_movimiento;
	}
	public void setId_movimiento(int id_movimiento) {
		this.id_movimiento = id_movimiento;
	}

	 

}
