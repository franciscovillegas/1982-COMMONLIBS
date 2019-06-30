package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_req_global", isForeignKey = false, numerica = true) }, tableName = "eje_wfs_bst_ticket")
public class Eje_wfs_bst_ticket extends Vo{
	private int	id_req_global;
	private int	id_req;
	private String	contexto;
	private Date fecha_creacion;
	public int getId_req_global() {
		return id_req_global;
	}
	public void setId_req_global(int id_req_global) {
		this.id_req_global = id_req_global;
	}
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public String getContexto() {
		return contexto;
	}
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	public Date getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	
	
	
}
