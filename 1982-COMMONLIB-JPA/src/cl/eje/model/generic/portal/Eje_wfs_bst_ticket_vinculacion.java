package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "Eje_wfs_bst_ticket_vinculacion")
public class Eje_wfs_bst_ticket_vinculacion extends Vo{
	private int	id_corr;
	private int	id_req_global;
	private String contexto;
	private int	id_req;
	private Date fecha_vinculacion;
	
	public int getId_corr() {
		return id_corr;
	}
	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}
	public int getId_req_global() {
		return id_req_global;
	}
	public void setId_req_global(int id_req_global) {
		this.id_req_global = id_req_global;
	}
	public String getContexto() {
		return contexto;
	}
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public Date getFecha_vinculacion() {
		return fecha_vinculacion;
	}
	public void setFecha_vinculacion(Date fecha_vinculacion) {
		this.fecha_vinculacion = fecha_vinculacion;
	}
	
	
	
	
}
