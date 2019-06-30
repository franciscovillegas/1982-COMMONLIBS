package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr_pausa", isForeignKey = false, numerica = true) }, tableName = "eje_wf_escalamientos_pausa")
public class Eje_wf_escalamientos_pausa extends Vo {
	private int id_corr_pausa;
	private int id_req;
	private Date fecha_inicio_pausa;
	private Date fecha_termino_pausa;
	private int segundos;

	public int getId_corr_pausa() {
		return id_corr_pausa;
	}

	public void setId_corr_pausa(int id_corr_pausa) {
		this.id_corr_pausa = id_corr_pausa;
	}

	public int getId_req() {
		return id_req;
	}

	public void setId_req(int id_req) {
		this.id_req = id_req;
	}

	public Date getFecha_inicio_pausa() {
		return fecha_inicio_pausa;
	}

	public void setFecha_inicio_pausa(Date fecha_inicio_pausa) {
		this.fecha_inicio_pausa = fecha_inicio_pausa;
	}

	public Date getFecha_termino_pausa() {
		return fecha_termino_pausa;
	}

	public void setFecha_termino_pausa(Date fecha_termino_pausa) {
		this.fecha_termino_pausa = fecha_termino_pausa;
	}

	public int getSegundos() {
		return segundos;
	}

	public void setSegundos(int segundos) {
		this.segundos = segundos;
	}

}
