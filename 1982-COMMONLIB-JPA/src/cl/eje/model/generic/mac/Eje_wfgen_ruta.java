package cl.eje.model.generic.mac;

import java.util.Collection;
import java.util.Date;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "idcorr_ruta", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_ruta")
@TableReferences(value = {
		@TableReference(field = "voEje_wfgen_rutas", fk = @ForeignKeyReference(fk = "idcorr_ruta", otherTableField = "idcorr_ruta"), voClass = Eje_wfgen_rutas.class) })
public class Eje_wfgen_ruta extends Vo {
	private Collection<Eje_wfgen_rutas> voEje_wfgen_rutas;

	private int idcorr_ruta;
	private String nombre;
	private Date fecha_update;
	private int rut_crea;
	private int id_req;
	private boolean forsolicitud;

	public Collection<Eje_wfgen_rutas> getVoEje_wfgen_rutas() {
		return voEje_wfgen_rutas;
	}

	public void setVoEje_wfgen_rutas(Collection<Eje_wfgen_rutas> voEje_wfgen_rutas) {
		this.voEje_wfgen_rutas = voEje_wfgen_rutas;
	}

	public int getId_req() {
		return id_req;
	}

	public void setId_req(int id_req) {
		this.id_req = id_req;
	}

	public boolean isForsolicitud() {
		return forsolicitud;
	}

	public void setForsolicitud(boolean forsolicitud) {
		this.forsolicitud = forsolicitud;
	}

	public int getIdcorr_ruta() {
		return idcorr_ruta;
	}

	public void setIdcorr_ruta(int idcorr_ruta) {
		this.idcorr_ruta = idcorr_ruta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha_update() {
		return fecha_update;
	}

	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}

	public int getRut_crea() {
		return rut_crea;
	}

	public void setRut_crea(int rut_crea) {
		this.rut_crea = rut_crea;
	};

}