package cl.eje.model.generic.portal;

import java.util.Collection;

import portal.com.eje.portal.vo.annotations.ForeignKeyReference;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.annotations.TableReferences;
import portal.com.eje.portal.vo.vo.Vo;

@TableReferences(value = { @TableReference(field = "eje_files_unico", fk = @ForeignKeyReference(fk = "id_file", otherTableField = "id_file"), voClass = Eje_files_unico.class) })
@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_doc_file", isForeignKey = false, numerica = true) }, tableName = "eje_doc_files")
public class Eje_doc_files extends Vo {
	
	private Collection<Eje_files_unico> eje_files_unico;
	private int	id_doc_file;
	private int	id_file;
	private int	id_doc;
	
	public Collection<Eje_files_unico> getEje_files_unico() {
		return eje_files_unico;
	}
	public void setEje_files_unico(Collection<Eje_files_unico> eje_files_unico) {
		this.eje_files_unico = eje_files_unico;
	}
	public int getId_doc_file() {
		return id_doc_file;
	}
	public void setId_doc_file(int id_doc_file) {
		this.id_doc_file = id_doc_file;
	}
	public int getId_file() {
		return id_file;
	}
	public void setId_file(int id_file) {
		this.id_file = id_file;
	}
	public int getId_doc() {
		return id_doc;
	}
	public void setId_doc(int id_doc) {
		this.id_doc = id_doc;
	}
	
	
	
	
}
