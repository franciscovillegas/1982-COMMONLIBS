package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "nodo_id", isForeignKey = true, numerica = false) }, tableName = "eje_ges_jerarquia")
public class Eje_ges_jerarquia extends Vo {
	private String compania;
	private String nodo_id;
	private String nodo_padre;
	private Integer nodo_nivel;
	private String nodo_imagen;
	private Integer nodo_corr;
	private Integer nodo_hijos;
	private Integer id_tipo;

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

	public Integer getNodo_nivel() {
		return nodo_nivel;
	}

	public void setNodo_nivel(Integer nodo_nivel) {
		this.nodo_nivel = nodo_nivel;
	}

	public String getNodo_imagen() {
		return nodo_imagen;
	}

	public void setNodo_imagen(String nodo_imagen) {
		this.nodo_imagen = nodo_imagen;
	}

	public Integer getNodo_corr() {
		return nodo_corr;
	}

	public void setNodo_corr(Integer nodo_corr) {
		this.nodo_corr = nodo_corr;
	}

	public Integer getNodo_hijos() {
		return nodo_hijos;
	}

	public void setNodo_hijos(Integer nodo_hijos) {
		this.nodo_hijos = nodo_hijos;
	}

	public Integer getId_tipo() {
		return id_tipo;
	}

	public void setId_tipo(Integer id_tipo) {
		this.id_tipo = id_tipo;
	}

}
