package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "idcorr_ruta_persona", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_ruta_persona")
public class Eje_wfgen_ruta_persona extends Vo {
	private int idcorr_ruta_persona;
	private int idcorr_rutas;
	private int rut;
	private Date fecha_update;
	private int rut_asigno;

	public int getIdcorr_ruta_persona() {
		return idcorr_ruta_persona;
	}

	public void setIdcorr_ruta_persona(int idcorr_ruta_persona) {
		this.idcorr_ruta_persona = idcorr_ruta_persona;
	}

	public int getIdcorr_rutas() {
		return idcorr_rutas;
	}

	public void setIdcorr_rutas(int idcorr_rutas) {
		this.idcorr_rutas = idcorr_rutas;
	}

	public int getRut() {
		return rut;
	}

	public void setRut(int rut) {
		this.rut = rut;
	}

	public Date getFecha_update() {
		return fecha_update;
	}

	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}

	public int getRut_asigno() {
		return rut_asigno;
	}

	public void setRut_asigno(int rut_asigno) {
		this.rut_asigno = rut_asigno;
	}

}