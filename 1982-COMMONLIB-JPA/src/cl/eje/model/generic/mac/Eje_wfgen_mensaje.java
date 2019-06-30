package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_mensaje", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_tipo_mensaje")
@TableReferences({ @TableReference(field = "id_tipo_mensaje", fk = @ForeignKeyReference(fk = "id_tipo_mensaje", otherTableField = "id_tipo_mensaje"), voClass = Eje_wfgen_mensaje.class) })
public class Eje_wfgen_mensaje extends Vo {
	private Eje_wfgen_mensaje id_tipo_mensaje_vo;
	
	private int	id_mensaje;
	private int	id_tipo_mensaje;
	private String	mensaje;
	private boolean	vigente;
	private boolean por_defecto;
	private int orden;
	
	public Eje_wfgen_mensaje getId_tipo_mensaje_vo() {
		return id_tipo_mensaje_vo;
	}
	public void setId_tipo_mensaje_vo(Eje_wfgen_mensaje id_tipo_mensaje_vo) {
		this.id_tipo_mensaje_vo = id_tipo_mensaje_vo;
	}
	public int getId_mensaje() {
		return id_mensaje;
	}
	public void setId_mensaje(int id_mensaje) {
		this.id_mensaje = id_mensaje;
	}
	public int getId_tipo_mensaje() {
		return id_tipo_mensaje;
	}
	public void setId_tipo_mensaje(int id_tipo_mensaje) {
		this.id_tipo_mensaje = id_tipo_mensaje;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	public boolean isPor_defecto() {
		return por_defecto;
	}
	public void setPor_defecto(boolean por_defecto) {
		this.por_defecto = por_defecto;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	
	
}
