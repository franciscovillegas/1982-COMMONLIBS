package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_unidad_excepcion", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_unidad_excepcion")
public class Eje_wf_escalamientos_unidad_excepcion extends Vo {
	private int id_unidad_excepcion;
	private int id_escalamiento_impl;
	private String unid_id;
	private String text;
	
	public int getId_unidad_excepcion() {
		return id_unidad_excepcion;
	}
	public void setId_unidad_excepcion(int id_unidad_excepcion) {
		this.id_unidad_excepcion = id_unidad_excepcion;
	}
	public int getId_escalamiento_impl() {
		return id_escalamiento_impl;
	}
	public void setId_escalamiento_impl(int id_escalamiento_impl) {
		this.id_escalamiento_impl = id_escalamiento_impl;
	}
	public String getUnid_id() {
		return unid_id;
	}
	public void setUnid_id(String unid_id) {
		this.unid_id = unid_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
