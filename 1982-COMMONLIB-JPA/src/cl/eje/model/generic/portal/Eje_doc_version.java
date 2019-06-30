package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_ver", isForeignKey = false, numerica = true) }, tableName = "eje_doc_version")
public class Eje_doc_version extends Vo {
	private Integer id_ver;
	private Integer id_doc;
	private Date fecha_update;
	private Integer rut_update;
	private int paginas;
	
	public Integer getId_ver() {
		return id_ver;
	}
	public void setId_ver(Integer id_ver) {
		this.id_ver = id_ver;
	}
	public Integer getId_doc() {
		return id_doc;
	}
	public void setId_doc(Integer id_doc) {
		this.id_doc = id_doc;
	}
	public Date getFecha_update() {
		return fecha_update;
	}
	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}
	public Integer getRut_update() {
		return rut_update;
	}
	public void setRut_update(Integer rut_update) {
		this.rut_update = rut_update;
	}
	public int getPaginas() {
		return paginas;
	}
	public void setPaginas(int paginas) {
		this.paginas = paginas;
	}
	
	
	
}
