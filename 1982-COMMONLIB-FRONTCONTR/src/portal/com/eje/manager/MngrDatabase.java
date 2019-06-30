package portal.com.eje.manager;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;


public class MngrDatabase {

	
	
	public ConsultaData getDefinition(String jndi ) {
		try {
			return ConsultaTool.getInstance().getData(jndi, QueryManagerDatabase.definition.getSql() );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ConsultaData getDefinition(String jndi, String tableName ) {
		try {
			Object[] params = { tableName };
			return ConsultaTool.getInstance().getData(jndi, QueryManagerDatabase.definition_sTable_sField.getSql(), params );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ConsultaData getDefinition(String jndi, String tableName, String fieldName ) {
		try {
			Object[] params = { tableName , fieldName };
			return ConsultaTool.getInstance().getData(jndi, QueryManagerDatabase.definition_sTable_sField.getSql(), params );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean alterTableAdd(String jndi, String tableName, String fieldName, DataTypeSqlServer tipo, int largo) {
		String sql = " ALTER TABLE ".concat(tableName).concat(" ADD ").concat(fieldName).concat(" ").concat(tipo.toString()).concat(" (").concat(String.valueOf(largo)).concat(")");
		
		try {
			ConsultaTool.getInstance().update(jndi,sql);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}

enum QueryManagerDatabase {
	definition              (" SELECT a.[name] as 'Table', b.[name] as 'Column', c.[name] as 'Datatype', b.[length] as 'Length', CASE WHEN b.[cdefault] > 0 THEN d.[text] ELSE NULL END as 'Default', CASE WHEN b.[isnullable] = 0 THEN 'No' ELSE 'Yes' END as 'Nullable' FROM sysobjects a INNER JOIN syscolumns b ON a.[id] = b.[id] INNER JOIN systypes c ON b.[xtype] = c.[xtype] LEFT JOIN syscomments d ON b.[cdefault] = d.[id] WHERE a.[xtype] = 'u' AND a.[name] <> 'dtproperties' ORDER BY a.[name],b.[colorder]"),
	definition_sTable       (" SELECT a.[name] as 'Table', b.[name] as 'Column', c.[name] as 'Datatype', b.[length] as 'Length', CASE WHEN b.[cdefault] > 0 THEN d.[text] ELSE NULL END as 'Default', CASE WHEN b.[isnullable] = 0 THEN 'No' ELSE 'Yes' END as 'Nullable' FROM sysobjects a INNER JOIN syscolumns b ON a.[id] = b.[id] INNER JOIN systypes c ON b.[xtype] = c.[xtype] LEFT JOIN syscomments d ON b.[cdefault] = d.[id] WHERE a.[xtype] = 'u' AND a.[name] <> 'dtproperties' and a.[name]= ?  ORDER BY a.[name],b.[colorder]"),
	definition_sTable_sField(" SELECT a.[name] as 'Table', b.[name] as 'Column', c.[name] as 'Datatype', b.[length] as 'Length', CASE WHEN b.[cdefault] > 0 THEN d.[text] ELSE NULL END as 'Default', CASE WHEN b.[isnullable] = 0 THEN 'No' ELSE 'Yes' END as 'Nullable' FROM sysobjects a INNER JOIN syscolumns b ON a.[id] = b.[id] INNER JOIN systypes c ON b.[xtype] = c.[xtype] LEFT JOIN syscomments d ON b.[cdefault] = d.[id] WHERE a.[xtype] = 'u' AND a.[name] <> 'dtproperties' and a.[name]= ?  and b.[name]= ? ORDER BY a.[name],b.[colorder]");

	private String sql;
	
	QueryManagerDatabase(String sql) {
		this.sql = sql;
	}
	
	public String getSql() {
		return sql;
	}
}