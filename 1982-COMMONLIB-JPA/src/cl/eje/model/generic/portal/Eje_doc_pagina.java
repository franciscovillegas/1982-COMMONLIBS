package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_pag", isForeignKey = false, numerica = true) }, tableName = "eje_doc_pagina")
public class Eje_doc_pagina extends Vo {
	private Integer id_pag;
	private Integer id_ver;
	private String contenido;
	private Integer pag;
	public Integer getId_pag() {
		return id_pag;
	}
	public void setId_pag(Integer id_pag) {
		this.id_pag = id_pag;
	}
	public Integer getId_ver() {
		return id_ver;
	}
	public void setId_ver(Integer id_ver) {
		this.id_ver = id_ver;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public Integer getPag() {
		return pag;
	}
	public void setPag(Integer pag) {
		this.pag = pag;
	}
	
	
	
}
