package portal.com.eje.portal.sqlserver;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.sqlserver.ifaces.ISqlServerMapping;
 

public class SqlServerMapping implements ISqlServerMapping {

	public static ISqlServerMapping getInstance() {
		return Util.getInstance(SqlServerMapping.class);
	}
	
	public Class<? extends Object> getJavaType(String data_type) {
		if(data_type != null) {
			if(data_type.equals("bigint")) {
				return Double.class;
			}
			else if(data_type.equals("binary")) {
				return Byte[].class;
			}
			else if(data_type.equals("bit")) {
				return Boolean.class;
			}
			else if(data_type.equals("char")) {
				return String.class;
			}
			else if(data_type.equals("date")) {
				return Date.class;
			}
			else if(data_type.equals("datetime")) {
				return Date.class;
			}
			else if(data_type.equals("datetime2")) {
				return Date.class;
			}
			else if(data_type.equals("datetimeoffset")) {
				throw new NotImplementedException(); 
			}
			else if(data_type.equals("decimal")) {
				return Double.class;
			}
			else if(data_type.equals("varbinary")) {
				return Byte[].class;
			}
			else if(data_type.equals("float")) {
				return Double.class;
			}
			else if(data_type.equals("image")) {
				return Byte[].class;
			}
			else if(data_type.equals("int")) {
				return Integer.class;
			}
			else if(data_type.equals("money")) {
				return Double.class;
			}
			else if(data_type.equals("nchar")) {
				return char[].class;
			}
			else if(data_type.equals("ntext")) {
				return String.class;
			}
			else if(data_type.equals("numeric")) {
				return Double.class;
			}
			else if(data_type.equals("nvarchar")) {
				return char[].class;
			}
			else if(data_type.equals("real")) {
				return Double.class;
			}
			else if(data_type.equals("rowversion")) {
				return Byte[].class;
			}
			else if(data_type.equals("smalldatetime")) {
				return Date.class;
			}
			else if(data_type.equals("smallint")) {
				return Integer.class;
			}
			else if(data_type.equals("smallmoney")) {
				return Double.class;
			}
			else if(data_type.equals("sql_variant")) {
				return Object.class;
			}
			else if(data_type.equals("text")) {
				return String.class;
			}
			else if(data_type.equals("time")) {
				return Date.class;
			}
			else if(data_type.equals("timestamp")) {
				return Date.class;
			}
			else if(data_type.equals("tinyint")) {
				return Integer.class;
			}
			else if(data_type.equals("uniqueidentifier")) {
				throw new NotImplementedException(); 
			}
			else if(data_type.equals("varbinary")) {
				return Byte[].class;
			}
			else if(data_type.equals("varchar")) {
				return String.class;
			}
			else if(data_type.equals("xml")) {
				throw new NotImplementedException(); 
			}
		}
		
		throw new NotImplementedException(); 
	}

	@Override
	public boolean isNumeric(String data_type) {
		return Number.class.isAssignableFrom(getJavaType(data_type)); 
	}
 
	
}
