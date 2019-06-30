package cl.eje.model.generic.mac;

import java.util.Collection;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_tipo_mensaje", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_tipo_mensaje")
@TableReferences({ 
		@TableReference(field = "eje_wf_tupla_tipo_mensaje_vo"	, fk = @ForeignKeyReference(fk = "id_tipo_mensaje", otherTableField = "id_tipo_mensaje"), voClass = Eje_wf_tupla_tipo_mensaje.class),
		@TableReference(field = "eje_wf_tupla_mensaje_vo"		, fk = @ForeignKeyReference(fk = "id_tipo_mensaje", otherTableField = "id_tipo_mensaje"), voClass = Eje_wf_tupla_mensaje.class)
	})
public class Eje_wfgen_tipo_mensaje extends Vo {
	private Collection<Eje_wf_tupla_tipo_mensaje> eje_wf_tupla_tipo_mensaje_vo;
	private Collection<Eje_wf_tupla_mensaje> eje_wf_tupla_mensaje_vo;
	
	private int	id_tipo_mensaje;
	private String nombre;
	
	
	public Collection<Eje_wf_tupla_tipo_mensaje> getEje_wf_tupla_tipo_mensaje_vo() {
		return eje_wf_tupla_tipo_mensaje_vo;
	}
	public void setEje_wf_tupla_tipo_mensaje_vo(Collection<Eje_wf_tupla_tipo_mensaje> eje_wf_tupla_tipo_mensaje_vo) {
		this.eje_wf_tupla_tipo_mensaje_vo = eje_wf_tupla_tipo_mensaje_vo;
	}
	public Collection<Eje_wf_tupla_mensaje> getEje_wf_tupla_mensaje_vo() {
		return eje_wf_tupla_mensaje_vo;
	}
	public void setEje_wf_tupla_mensaje_vo(Collection<Eje_wf_tupla_mensaje> eje_wf_tupla_mensaje_vo) {
		this.eje_wf_tupla_mensaje_vo = eje_wf_tupla_mensaje_vo;
	}
	public int getId_tipo_mensaje() {
		return id_tipo_mensaje;
	}
	public void setId_tipo_mensaje(int id_tipo_mensaje) {
		this.id_tipo_mensaje = id_tipo_mensaje;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
