package portal.com.eje.portal.sqlserver;

import java.util.Collection;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.sqlserver.vo.VoDefColumn;

class SqlServerToolCached extends SqlServerTool implements ISqlServerTool {

	public static ISqlServerTool getInstance() {
		return Util.getInstance(SqlServerToolCached.class);
	}
	
	@Override
	public ConsultaData getColumns(String jndi, String table) {
		ConsultaData retorno = null;
		
		try {
			Class<?>[] def = {String.class, String.class};
			Object[] params = {jndi, table};
			
			retorno = Cache.weak(this, "getColumns", def, params, ConsultaData.class, true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<VoDefColumn> getColumnsVo(String jndi, String table) {
		Collection<VoDefColumn> retorno = null;
		try {
			Class<?>[] def = {String.class, String.class};
			Object[] params = {jndi, table};
			
			retorno = Cache.weak(this, "getColumnsVo", def, params, Collection.class, true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	@Override
	public boolean existColumn(String jndi, String table, String column) {
		boolean retorno = false;
		try {
			Class<?>[] def = {String.class, String.class, String.class};
			Object[] params = {jndi, table, column};
			
			retorno = Cache.weak(this, "existColumn", def, params, boolean.class, true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	@Override
	public boolean existTable(EModulos modulo, String jndi, String table) {
		boolean retorno = false;
		try {
			Class<?>[] def = {EModulos.class, String.class, String.class};
			Object[] params = {modulo, table, table};
			
			retorno = Cache.weak(this, "existTable", def, params, boolean.class, true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
}
