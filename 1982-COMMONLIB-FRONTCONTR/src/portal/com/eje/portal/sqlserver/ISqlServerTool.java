package portal.com.eje.portal.sqlserver;

import java.util.Collection;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.sqlserver.vo.VoDefColumn;

public interface ISqlServerTool {

	ConsultaData getColumns(String jndi, String table);

	Collection<VoDefColumn> getColumnsVo(String jndi, String table);

	boolean existColumn(String jndi, String table, String column);

	CtrSqlServerTool getCtr();

	boolean existTable(EModulos modulo, String jndi, String table);

}