package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_tracking", isForeignKey = false, numerica = true) }, tableName = "eje_service_viewer_tracking")
public class Eje_service_viewer_tracking extends Vo {
	private double id_tracking;
	private Date fecha;
	private Boolean isok;
	private double time_ms;
	private int cod_retorno;
	private int id_servicio;

	public double getId_tracking() {
		return id_tracking;
	}

	public void setId_tracking(double id_tracking) {
		this.id_tracking = id_tracking;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Boolean getIsok() {
		return isok;
	}

	public void setIsok(Boolean isok) {
		this.isok = isok;
	}

	public double getTime_ms() {
		return time_ms;
	}

	public void setTime_ms(double time_ms) {
		this.time_ms = time_ms;
	}

	public int getCod_retorno() {
		return cod_retorno;
	}

	public void setCod_retorno(int cod_retorno) {
		this.cod_retorno = cod_retorno;
	}

	public int getId_servicio() {
		return id_servicio;
	}

	public void setId_servicio(int id_servicio) {
		this.id_servicio = id_servicio;
	}

}
