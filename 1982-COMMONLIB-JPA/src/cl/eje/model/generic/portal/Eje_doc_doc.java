package cl.eje.model.generic.portal;

import java.util.Collection;
import java.util.Date;

import portal.com.eje.portal.vo.annotations.ForeignKey;
import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.ForeignKeys;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@ForeignKeys(foreignkeys = { @ForeignKey(fk = "rut_creo"), @ForeignKey(fk = "id_estado") })
@TableReferences(value = { @TableReference(field = "eje_doc_files", fk = @ForeignKeyReference(fk = "id_doc", otherTableField = "id_doc"), voClass = Eje_doc_files.class) })
@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_doc", isForeignKey = false, numerica = true) }, tableName = "eje_doc_doc")
public class Eje_doc_doc extends Vo {
	
	private Collection<Eje_doc_files> eje_doc_files;
	
	private Integer id_doc;
	private String doc_nombre;
	private Integer rut_creo;
	private Date fecha_creo;
	private String modulo_creo;
	private Short id_estado;
	private boolean is_publico;
	
	
	
	
	public Collection<Eje_doc_files> getEje_doc_files() {
		return eje_doc_files;
	}
	public void setEje_doc_files(Collection<Eje_doc_files> eje_doc_files) {
		this.eje_doc_files = eje_doc_files;
	}
	public Integer getId_doc() {
		return id_doc;
	}
	public void setId_doc(Integer id_doc) {
		this.id_doc = id_doc;
	}
	public String getDoc_nombre() {
		return doc_nombre;
	}
	public void setDoc_nombre(String doc_nombre) {
		this.doc_nombre = doc_nombre;
	}
	public Integer getRut_creo() {
		return rut_creo;
	}
	public void setRut_creo(Integer rut_creo) {
		this.rut_creo = rut_creo;
	}
 
	public Date getFecha_creo() {
		return fecha_creo;
	}
	public void setFecha_creo(Date fecha_creo) {
		this.fecha_creo = fecha_creo;
	}
	public String getModulo_creo() {
		return modulo_creo;
	}
	public void setModulo_creo(String modulo_creo) {
		this.modulo_creo = modulo_creo;
	}
	public Short getId_estado() {
		return id_estado;
	}
	public void setId_estado(Short id_estado) {
		this.id_estado = id_estado;
	}
	public boolean isIs_publico() {
		return is_publico;
	}
	public void setIs_publico(boolean is_publico) {
		this.is_publico = is_publico;
	}
	
	
	
}
