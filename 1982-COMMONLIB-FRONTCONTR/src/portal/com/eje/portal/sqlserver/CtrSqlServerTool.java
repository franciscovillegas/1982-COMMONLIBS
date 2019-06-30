package portal.com.eje.portal.sqlserver;

import java.util.Collection;

import portal.com.eje.portal.sqlserver.vo.Column;
import portal.com.eje.portal.vo.VoTool;

public class CtrSqlServerTool {
	
	public Collection<Column> getColumns(String jndi, String table) {
		return VoTool.getInstance().buildVo(SqlServerTool.getInstance().getColumns(jndi, table), Column.class);
	}
}
