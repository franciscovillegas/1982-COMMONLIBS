package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr_comentario", isForeignKey = false, numerica = true) }, tableName = "eje_forms_ingreso_req_comentario")
public class Eje_forms_ingreso_req_comentario extends Vo {
	private int id_corr;
	private int id_corr_comentario;
	private String comentario;
	private Date fecha_update;
	private int rut_update;
	private boolean visible;

	public int getId_corr() {
		return id_corr;
	}

	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}

	public int getId_corr_comentario() {
		return id_corr_comentario;
	}

	public void setId_corr_comentario(int id_corr_comentario) {
		this.id_corr_comentario = id_corr_comentario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getFecha_update() {
		return fecha_update;
	}

	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}

	public int getRut_update() {
		return rut_update;
	}

	public void setRut_update(int rut_update) {
		this.rut_update = rut_update;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
	

}
