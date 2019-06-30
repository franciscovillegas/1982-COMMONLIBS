package portal.com.eje.portal.vo;

import portal.com.eje.portal.vo.vo.Vo;

public class GenericTableDefinition {
	protected Vo vo;
	protected String pk;
	protected String table;
	
	public GenericTableDefinition(Vo vo) {
		this.vo = vo;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.pk = primaryKey;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
}
