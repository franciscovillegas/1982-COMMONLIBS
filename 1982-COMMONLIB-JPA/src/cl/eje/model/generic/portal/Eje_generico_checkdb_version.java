package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "bd_version", isForeignKey = false, numerica = false) }, tableName = "eje_generico_checkdb_version")
public class Eje_generico_checkdb_version extends Vo {
	private String bd_version;
	private Date bd_fec_upd;
	private int veces_aplicada;
	private int estado_aplicacion;
	
	public String getBd_version() {
		return bd_version;
	}
	public void setBd_version(String bd_version) {
		this.bd_version = bd_version;
	}
	public Date getBd_fec_upd() {
		return bd_fec_upd;
	}
	public void setBd_fec_upd(Date bd_fec_upd) {
		this.bd_fec_upd = bd_fec_upd;
	}
	public int getVeces_aplicada() {
		return veces_aplicada;
	}
	public void setVeces_aplicada(int veces_aplicada) {
		this.veces_aplicada = veces_aplicada;
	}
	public int getEstado_aplicacion() {
		return estado_aplicacion;
	}
	public void setEstado_aplicacion(int estado_aplicacion) {
		this.estado_aplicacion = estado_aplicacion;
	}

	
	
}
