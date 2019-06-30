package portal.com.eje.portal.sqlserver;

import java.sql.SQLException;
import java.util.Collection;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.sqlserver.vo.VoDefColumn;
import portal.com.eje.portal.vo.VoTool;

public class SqlServerTool implements ISqlServerTool {

	public static ISqlServerTool getInstance() {
		return Util.getInstance(SqlServerToolCached.class);
	}
	
	public ConsultaData getColumns(String jndi, String table) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT c.ordinal_position,c.COLUMN_NAME, c.DATA_TYPE , IS_NULLABLE=case when c.IS_NULLABLE = 'YES' then 1 else 0 end, s.is_identity , pk= case when not u.COLUMN_NAME is null then 1 end \n");
		sql.append(" FROM INFORMATION_SCHEMA.COLUMNS C INNER JOIN  sys.columns S  ON c.COLUMN_NAME = s.name and s.object_id = object_id('['+c.table_name+']') \n");  
		sql.append(" 	left outer join INFORMATION_SCHEMA.KEY_COLUMN_USAGE u on OBJECTPROPERTY(OBJECT_ID(u.CONSTRAINT_SCHEMA + '.' + QUOTENAME(u.CONSTRAINT_NAME)), 'IsPrimaryKey') = 1 \n");
		sql.append(" 															AND u.TABLE_NAME = c.table_name and u.column_name = c.COLUMN_NAME \n");
		sql.append(" WHERE c.TABLE_NAME = ? \n");
		sql.append(" ORDER BY c.ordinal_position  \n");
		
		Object[] params = {table};
		try {
			return ConsultaTool.getInstance().getData(jndi, sql.toString(),params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Collection<VoDefColumn> getColumnsVo(String jndi, String table) {
		ConsultaData data = getColumns(jndi, table);
		return VoTool.getInstance().buildVo(data, VoDefColumn.class);
	}
	
	public boolean existColumn(String jndi, String table, String column) {
		Collection<VoDefColumn> columns = getColumnsVo(jndi, table);
		
		boolean ok = false;
		for(VoDefColumn vo : columns) {
			if(vo != null && column != null && vo.getColumn_name().toLowerCase().equals(column.toLowerCase())) {
				ok = true;
				break;
			}
		}
		
		return ok;
	}
	
	public CtrSqlServerTool getCtr() {
		return Util.getInstance(CtrSqlServerTool.class);
	}

	public boolean existTable(EModulos modulo, String jndi, String table) {
		
		ConsultaData data = null;
		try {
			if(modulo != null && jndi != null && table != null) {
				String sql = " select * from sysobjects where name= ?  and xtype='U'  ";
				Object[] params = {table};
				
				data = ConsultaTool.getInstance().getData(modulo, jndi, sql, params);
			}
			
		} catch (Exception e) {
			
		}
		
		return data != null && data.next();
	}
}
