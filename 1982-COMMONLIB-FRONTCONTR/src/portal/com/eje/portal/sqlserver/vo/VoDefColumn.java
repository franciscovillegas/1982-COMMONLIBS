package portal.com.eje.portal.sqlserver.vo;

import portal.com.eje.portal.vo.vo.Vo;

public class VoDefColumn extends Vo {
	private int ordinal;
	private String column_name;
	private String data_type;
	private boolean is_nullable;
	private boolean is_identity;
	private boolean pk;
	public int getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public boolean isIs_nullable() {
		return is_nullable;
	}
	public void setIs_nullable(boolean is_nullable) {
		this.is_nullable = is_nullable;
	}
	public boolean isIs_identity() {
		return is_identity;
	}
	public void setIs_identity(boolean is_identity) {
		this.is_identity = is_identity;
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean pk) {
		this.pk = pk;
	}
	
	
	
}