package portal.com.eje.portal.sqlserver.vo;

import portal.com.eje.portal.vo.vo.Vo;

public class Column extends Vo {
	private int ordinal_position;
	private String column_name;
	private String data_type;
	private boolean is_nullable;
	private boolean is_identity;
	private boolean pk;
	public int getOrdinal_position() {
		return ordinal_position;
	}
	public void setOrdinal_position(int ordinal_position) {
		this.ordinal_position = ordinal_position;
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
	
	public String toString()  {
		return column_name + "["+data_type+"]";
	}
}
