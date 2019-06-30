package portal.com.eje.portal.sqlserver.ifaces;

public interface ISqlServerMapping {

	public Class<? extends Object> getJavaType(String data_type);

	public boolean isNumeric(String data_type);
}
