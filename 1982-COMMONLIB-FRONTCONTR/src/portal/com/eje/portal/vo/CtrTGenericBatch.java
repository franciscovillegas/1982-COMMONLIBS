package portal.com.eje.portal.vo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.vo.errors.NotSameClassException;
import portal.com.eje.portal.vo.util.BatchTool;
import portal.com.eje.portal.vo.vo.Vo;

public class CtrTGenericBatch extends AbsVoPersisteBatch {

	public static CtrTGenericBatch getInstance() {
		return Ctr.getInstance(CtrTGenericBatch.class);
	}
	
	/**
	 * Determina la existencia de todos los VOs en el arreglo, pueden ser diferentes
	 * 
	 * @author Pancho
	 * @throws NotSameClassException 
	 * @since 30-10-2018
	 * */

	@SuppressWarnings("unused")
	public List<Boolean> exist(Connection conn, List<? extends Vo> cols) throws SQLException {
		if(conn == null || cols == null) {
			throw new NullPointerException();
		}
		
		List<Boolean> existencias = null;;
		if(cols != null && cols.size() > 0) {
			GenericTool.getInstance().isSameObject(cols);
			
			BatchSql batch = buildExist(cols);
			List<ConsultaData> retorno = null;
			
			if(conn != null) {
				retorno = BatchTool.getInstance().execBatch(conn, batch);	
			}
			else {
				String jndi = VoTool.getInstance().getTableDefinition(cols.iterator().next().getClass()).jndi();
				retorno = BatchTool.getInstance().execBatch(jndi, batch);	
			}
			
			return privateConvert(retorno);
		}
		
		return existencias;
	}
	
	/**
	 * Actualiza o inserta una lista de Vo, pueden ser diferentes
	 * 
	 * @author Pancho
	 * @throws NotSameClassException 
	 * @since 30-10-2018
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean updOrAdd(Connection conn, List<? extends Vo> cols) throws SQLException {
		List<Boolean> existencias = exist(conn, cols);
		
		List colsToInsert = new LinkedList();
		List colsToUpd = new LinkedList();
		
		int pos = -1;
		for(Boolean existe : existencias) {
			pos++;
			
			Vo vo = cols.get(pos);
			if(existe) {
				colsToUpd.add(vo);
			}
			else {
				colsToInsert.add(vo);
			}
		}
		
		BatchSqlSingleton batchInserts = buildInsert(colsToInsert);
		BatchSqlSingleton batchsUpds = buildUpdate(colsToUpd);
		
		if(conn != null) {
			BatchTool.getInstance().execBatch(conn, batchInserts);
			BatchTool.getInstance().execBatch(conn, batchsUpds);
		}
		else {
			String jndi = VoTool.getInstance().getTableDefinition(cols.iterator().next().getClass()).jndi();
			
			BatchTool.getInstance().execBatch(jndi, batchInserts);
			BatchTool.getInstance().execBatch(jndi, batchsUpds);
		}
		
		
		return true;
	}
	
	/**
	 * Elimina una lista de VOs, pueden ser diferentes
	 * 
	 * @author Pancho
	 * @throws NotSameClassException 
	 * @since 30-10-2018
	 * */
	public boolean del(Connection conn, List<? extends Vo> cols) throws SQLException  {
		GenericTool.getInstance().isSameObject(cols);
		
		BatchSql batchDeletes = buildGenericDelete(cols);
		
		
		if(conn != null) {
			BatchTool.getInstance().execBatch(conn, batchDeletes);
		}
		else {
			String jndi = VoTool.getInstance().getTableDefinition(cols.iterator().next().getClass()).jndi();
			
			BatchTool.getInstance().execBatch(jndi, batchDeletes);
		}
		
		return true;
	}
	
}
