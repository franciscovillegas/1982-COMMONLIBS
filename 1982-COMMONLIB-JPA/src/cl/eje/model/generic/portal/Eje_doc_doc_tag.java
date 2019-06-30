package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_doc_doc_tag")
public class Eje_doc_doc_tag extends Vo {
	private int id_corr;
	private int id_doc;
	private int id_tag;
	
	public int getId_corr() {
		return id_corr;
	}
	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}
	public int getId_doc() {
		return id_doc;
	}
	public void setId_doc(int id_doc) {
		this.id_doc = id_doc;
	}
	public int getId_tag() {
		return id_tag;
	}
	public void setId_tag(int id_tag) {
		this.id_tag = id_tag;
	}
	
	

}
