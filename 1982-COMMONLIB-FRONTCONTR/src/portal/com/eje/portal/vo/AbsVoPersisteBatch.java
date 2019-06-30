package portal.com.eje.portal.vo;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.errors.MethodNotFoundException;
import portal.com.eje.portal.vo.util.InsertTool;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.ListUtils;

public class AbsVoPersisteBatch {

	protected List<Boolean> privateConvert(List<ConsultaData> retorno) {
		List<Boolean> existencias = new LinkedList<Boolean>();
		if (retorno != null) {
			for (ConsultaData data : retorno) {
				existencias.add(data != null && data.next());
			}
		}

		Assert.isTrue(existencias.size() == retorno.size());

		return existencias;
	}

	protected BatchSql buildExist(List<? extends Vo> vos) throws SQLException {
		Assert.notNull(vos);
		Assert.notEmpty(vos);

		BatchSql batchSql = null;

		Object firstObject = vos.iterator().next();
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(firstObject.getClass());

		List<ConsultaData> retorno = null;
		String table = tableDefinition.tableName();

		if (table == null) {
			throw new SQLException("No existe una tabla definida ");
		}
		{
			if (tableDefinition.pks().length == 0 || tableDefinition.pks().length > 1) {
				throw new NotImplementedException();
			}

			if (tableDefinition != null) {
				StringBuilder sql = new StringBuilder(" select top 1 existe=1 from ").append(table).append(" WHERE ");
				for (PrimaryKeyDefinition pk : tableDefinition.pks()) {
					sql.append(pk.field()).append("=").append("?");
				}

				List<Object[]> params = null;
				try {
					params = VoTool.getInstance().getObjectArray(vos, new String[] { tableDefinition.pks()[0].field() });
				} catch (MethodNotFoundException e) {
					throw new SQLException("No existe el field '" + tableDefinition.pks()[0].field() + "' para obtener los datos");
				}

				Assert.isTrue(params.size() == vos.size());

				batchSql = new BatchSql(sql, params);

			}
		}

		return batchSql;
	}

	protected BatchSqlSingleton buildInsert(List<? extends Vo> vos) throws SQLException {
		BatchSqlSingleton singleton = new BatchSqlSingleton();

		for (Vo vo : vos) {
			switch (InsertTool.getInsertMethod(vo)) {
			case IDENTITY:
				singleton.addAll(buildGenericInsertIdentity(vo));
				break;
			case NORMAL:
				singleton.addAll(buildGenericInsertNormal(vo));
				break;
			case DEFAULT:
				singleton.addAll(buildGenericInsert(vo));
				break;
			default:
				break;
			}
		}

		return singleton;
	}

	protected BatchSqlSingleton buildGenericInsertIdentity(Vo vo) throws SQLException {
		BatchSqlSingleton batch = new BatchSqlSingleton();

		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());

		String table = null;

		if (table == null && tableDefinition != null && tableDefinition.tableName() != null) {
			table = tableDefinition.tableName();
		}

		if (table == null) {
			throw new SQLException("No existe una tabla definida ");
		}

		if (tableDefinition != null && tableDefinition.pks().length == 1 && tableDefinition.pks()[0].autoIncremental() && tableDefinition.pks()[0].numerica()) {
			// tiene pk
		} else {
			throw new SQLException("La tabla debe tener SIEMPRE una PK autonumérica definida ");
		}

		Double newId = -1D;
		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());

		if (fieldsList != null && fieldsList.size() > 0) {
			if (tableDefinition != null) {
				fieldsList.remove(tableDefinition.pks()[0].field());
			}

			String key = "insertIdentity_" + vo.getClass().getCanonicalName();
			String select = (String) WeakCacheLocator.getInstance(this.getClass()).get(key);

			if (select == null) {
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO ").append(table);
				sql.append(" (").append(ListUtils.getInstance().concatenateValues(fieldsList)).append(")");
				sql.append(" VALUES");
				sql.append(" (").append(ListUtils.getInstance().concatenateReplace(fieldsList, "?")).append(")");

				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}

			List<Object> params = new LinkedList<Object>();
			for (String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field);

				if (metodo == null) {
					throw new SQLException("No existe el campo " + field + " definido en el VO");
				}

				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));
			}

			batch.addBatch(select, params.toArray());
		}

		return batch;
	}

	protected BatchSqlSingleton buildGenericInsertNormal(Vo vo) throws SQLException {
		BatchSqlSingleton batch = new BatchSqlSingleton();

		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());
		boolean ok = false;
		String table = null;

		if (table == null && tableDefinition != null && tableDefinition.tableName() != null) {
			table = tableDefinition.tableName();
		}

		if (table == null) {
			throw new SQLException("No existe una tabla definida ");
		}

		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());

		if (fieldsList != null && fieldsList.size() > 0) {

			String key = "insertNormal_" + vo.getClass().getCanonicalName();
			String select = (String) SoftCacheLocator.getInstance(this.getClass()).get(key);

			if (select == null) {
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO ").append(table);
				sql.append(" (").append(ListUtils.getInstance().concatenateValues(fieldsList)).append(")");
				sql.append(" VALUES");
				sql.append(" (").append(ListUtils.getInstance().concatenateReplace(fieldsList, "?")).append(")");

				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}

			List<Object> params = new LinkedList<Object>();
			for (String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field);

				if (metodo == null) {
					throw new SQLException("No existe el campo " + field + " definido en el VO");
				}

				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));
			}

			batch.addBatch(select, params.toArray());

		}

		return batch;
	}

	protected BatchSqlSingleton buildGenericInsert(Vo vo) throws SQLException {
		BatchSqlSingleton batch = new BatchSqlSingleton();
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());

		String table = null;

		if (table == null && tableDefinition != null && tableDefinition.tableName() != null) {
			table = tableDefinition.tableName();
		}

		if (table == null) {
			throw new SQLException("No existe una tabla definida ");
		}

		if (tableDefinition != null && tableDefinition.pks().length == 1 && !tableDefinition.pks()[0].autoIncremental() && tableDefinition.pks()[0].numerica()) {
			// tiene pk
		} else {
			throw new SQLException("La tabla debe tener SIEMPRE una PK numérica, esta PK se le incrementará en 1 su valor ");
		}

		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());

		if (fieldsList != null && fieldsList.size() > 0) {
			String pk = null;

			if (tableDefinition != null) {
				pk = (tableDefinition.pks()[0].field());
			}

			fieldsList.remove(pk);

			String key = "insert_" + vo.getClass().getCanonicalName();
			String select = (String) SoftCacheLocator.getInstance(this.getClass()).get(key);

			if (select == null) {
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO ").append(table);
				sql.append(" ( (select isnull(MAX(").append(pk).append("),0)+1 from ").append(table).append("),").append(ListUtils.getInstance().concatenateValues(fieldsList)).append(")");
				sql.append(" VALUES");
				sql.append(" (?,").append(ListUtils.getInstance().concatenateReplace(fieldsList, "?")).append(")");

				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}

			List<Object> params = new LinkedList<Object>();

			for (String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field);

				if (metodo == null) {
					throw new SQLException("No existe el campo " + field + " definido en el VO");
				}

				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));
			}

			batch.addBatch(select, params.toArray());
		}

		return batch;
	}

	protected BatchSqlSingleton buildUpdate(List<Vo> vos) throws SQLException {
		BatchSqlSingleton batch = new BatchSqlSingleton();

		for (Vo vo : vos) {
			batch.addAll(buildGenericUpdate(vo));
		}

		return batch;
	}

	protected BatchSqlSingleton buildGenericUpdate(Vo vo) throws SQLException {
		BatchSqlSingleton batch = new BatchSqlSingleton();

		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());

		String table = null;

		if (table == null && tableDefinition != null && tableDefinition.tableName() != null) {
			table = tableDefinition.tableName();
		}

		if (table == null) {
			throw new SQLException("No existe una tabla definida ");
		}

		if (tableDefinition != null && tableDefinition.pks().length >= 0) {
			// tiene pk
		} else {
			throw new SQLException("La tabla debe tener al menos una PK definida. ");
		}

		int update = -1;
		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());

		if (fieldsList != null && fieldsList.size() > 0) {

			if (tableDefinition != null) {
				for (PrimaryKeyDefinition pkd : tableDefinition.pks()) {
					fieldsList.remove(pkd.field());
				}

			}

			String key = "update_" + vo.getClass().getCanonicalName();
			String select = (String) SoftCacheLocator.getInstance(this.getClass()).get(key);

			if (select == null) {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE ").append(table);
				sql.append(" SET ").append(ListUtils.getInstance().concatenateSufijo(fieldsList, "=?"));

				StringBuilder wherepk = new StringBuilder();

				if (tableDefinition != null && "".equals(wherepk.toString())) {
					for (PrimaryKeyDefinition pk : tableDefinition.pks()) {
						if (!"".equals(wherepk.toString())) {
							wherepk.append(" and ");
						}
						wherepk.append("  ").append(pk.field()).append("=?");

					}
				}

				if (wherepk.toString().length() > 0) {
					sql.append(" WHERE ").append(wherepk.toString());
				}

				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}

			List<Object> params = new LinkedList<Object>();
			for (String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field);

				if (metodo == null) {
					throw new SQLException("No existe el campo " + field + " definido en el VO");
				}

				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));
			}

			{

				if (tableDefinition != null) {
					for (PrimaryKeyDefinition pk : tableDefinition.pks()) {
						Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), pk.field());

						if (metodo == null) {
							throw new SQLException("No existe el pk " + pk.field() + " definido en el VO");
						}

						params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));
					}
				}
			}

			batch.addBatch(select, params.toArray());

		}

		return batch;
	}
	
 

	protected BatchSql buildGenericDelete(List<? extends Vo> vos) throws SQLException {
		BatchSql batch = null;

		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vos.iterator().next().getClass());

		String table = null;

		if (table == null && tableDefinition != null && tableDefinition.tableName() != null) {
			table = tableDefinition.tableName();
		}

		if (table == null) {
			throw new SQLException("No existe una tabla definida ");
		}

		if (tableDefinition != null && tableDefinition.pks().length >= 0) {
			// tiene pk
		} else {
			throw new SQLException("La tabla debe tener al menos una PK definida. ");
		}

		int update = 0;

		String key = "delete_" + vos.iterator().next().getClass().getCanonicalName();
		String select = (String) SoftCacheLocator.getInstance(this.getClass()).get(key);

		if (select == null) {
			StringBuilder sql = new StringBuilder();
			sql.append(" DELETE FROM ").append(table);

			StringBuilder wherepk = new StringBuilder();

			if (tableDefinition != null) {
				for (PrimaryKeyDefinition pk : tableDefinition.pks()) {
					if (!"".equals(wherepk.toString())) {
						wherepk.append(" and ");
					}
					wherepk.append("  ").append(pk.field()).append("=?");

				}
			}

			if (wherepk.toString().length() > 0) {
				sql.append(" WHERE ").append(wherepk.toString());
			}

			select = sql.toString();
			SoftCacheLocator.getInstance(this.getClass()).put(key, select);
		}

	
		List<Object[]> params = null;
		try {
			params = VoTool.getInstance().getObjectArray(vos, new String[] { tableDefinition.pks()[0].field() });
		} catch (MethodNotFoundException e) {
			throw new SQLException("No existe el field '" + tableDefinition.pks()[0].field() + "' para obtener los datos");
		}

		Assert.isTrue(params.size() == vos.size());
		
		batch = new BatchSql(select, params);

		return batch;
	}

}
