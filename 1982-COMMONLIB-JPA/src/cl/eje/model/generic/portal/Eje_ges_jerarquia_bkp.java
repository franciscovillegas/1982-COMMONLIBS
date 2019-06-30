package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_ges_jerarquia_bkp")

public class Eje_ges_jerarquia_bkp extends Vo {
	private int id_corr;
	private String compania;
	private String nodo_id;
	private String nodo_padre;
	private int nodo_nivel;
	private String nodo_imagen;
	private int nodo_corr;
	private int nodo_hijos;
	private int id_tipo;
	private int id_movimiento;
	
	public int getId_corr() {
		return id_corr;
	}
	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}
	public String getCompania() {
		return compania;
	}
	public void setCompania(String compania) {
		this.compania = compania;
	}
	public String getNodo_id() {
		return nodo_id;
	}
	public void setNodo_id(String nodo_id) {
		this.nodo_id = nodo_id;
	}
	public String getNodo_padre() {
		return nodo_padre;
	}
	public void setNodo_padre(String nodo_padre) {
		this.nodo_padre = nodo_padre;
	}
	public int getNodo_nivel() {
		return nodo_nivel;
	}
	public void setNodo_nivel(int nodo_nivel) {
		this.nodo_nivel = nodo_nivel;
	}
	public String getNodo_imagen() {
		return nodo_imagen;
	}
	public void setNodo_imagen(String nodo_imagen) {
		this.nodo_imagen = nodo_imagen;
	}
	public int getNodo_corr() {
		return nodo_corr;
	}
	public void setNodo_corr(int nodo_corr) {
		this.nodo_corr = nodo_corr;
	}
	public int getNodo_hijos() {
		return nodo_hijos;
	}
	public void setNodo_hijos(int nodo_hijos) {
		this.nodo_hijos = nodo_hijos;
	}
	public int getId_tipo() {
		return id_tipo;
	}
	public void setId_tipo(int id_tipo) {
		this.id_tipo = id_tipo;
	}
	public int getId_movimiento() {
		return id_movimiento;
	}
	public void setId_movimiento(int id_movimiento) {
		this.id_movimiento = id_movimiento;
	}
	
	 
}
