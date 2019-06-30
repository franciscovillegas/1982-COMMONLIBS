package portal.com.eje.portal.vo;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.ConsultaToolUtils;
import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.ListUtils;

public abstract class AbsVoPersiste {
	private ClaseGenerica cg; 
			
	public enum EnumReferenceMethod {
		LAZY,
		EAGER
	}
	
	protected List<String> pk;
	protected String table;
	
	
	public AbsVoPersiste() {
		pk = new ArrayList<String>();
		cg = Ctr.getInstance(ClaseGenerica.class);
	}
	
	/**
	 * Agrega la única PK existente
	 * */
	protected void setPrimaryKey(String primaryKey) {
		this.pk.clear();
		this.pk.add(primaryKey);
	}
	
	/**
	 * Agrega la única PK existente
	 * */
	protected void addPrimaryKey(String primaryKey) {
		this.pk.add(primaryKey);
	}
	
	protected void setTable(String table) {
		this.table = table;
	}
	
	protected  Collection privateGenericGetAll(Object jndiOrConn,Class  vo,List<Where> wheres, ISenchaPage page) throws SQLException {
		return privateGenericGetAll(jndiOrConn, vo, wheres, page, true);
	}
		
	protected  Collection  privateGenericGetAll(Object jndiOrConn, Class vo,List<Where> wheres, ISenchaPage page, boolean transformToVos) throws SQLException {
		return privateGenericGetAll2(jndiOrConn, vo, vo, wheres, page, transformToVos);
	}
	
	protected  Collection  privateGenericGetAll2(Object jndiOrConn, Class<?> vo, Class<?> voMaster,List<Where> wheres, ISenchaPage page, boolean transformToVos) throws SQLException {
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo);
		
		String table = this.table;
		
		if(table == null) {
			table = tableDefinition != null && tableDefinition.tableName() != null ? tableDefinition.tableName() : null;
		}
		if(table == null ) {
			throw new SQLException("No existe una tabla definida en "+vo.getCanonicalName());
		}
		
		ConsultaData data = null;
		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo);
		
		if(fieldsList != null && fieldsList.size() > 0) {

			
			String key = "selectall_"+vo.getCanonicalName();
			String select = (String)SoftCacheLocator.getInstance(this.getClass()).get(key);
			
			if(select == null) {
				StringBuilder sql = new StringBuilder();
				sql.append(" SELECT ");
				sql.append(" ").append(ListUtils.getInstance().concatenateValues(fieldsList));
				sql.append(" FROM ").append(table).append(" ");
				
				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}

			select+=(buildWhere(wheres));
			//select+=(buildSort(page.getSorts()));
			
			List<String> pks = VoTool.getInstance().getTablePks((Class<? extends Vo>) vo);
			
			if(jndiOrConn instanceof String) {
				data = ConsultaTool.getInstance().getDataPagged((String)jndiOrConn, select , buildParam(wheres), page, pks);	
			}
			else if(jndiOrConn instanceof Connection) {
				Connection conn = (Connection)jndiOrConn;
				 
				
				/*
				 * Pancho 20190215 
				 * Error: el método getJndiFromConnection guarda los jndis de la conexión, si  se llama la conexión a wf_dotacion con el jndi mac
				 * y luego se llama la misma conexión pero con otro JNDI lo que pasa es que el MÉTODO (getJndiFromConnection)
				 *  almacena como JNDI de la conexión el primer JNDI con el que se llamó la conexión, el error ocurre cuando se llama la segunda vez
				 *  a la misma conexíon con un diferente JNDI,ocurre que el siguiente IF obliga a que se llame el objeto modelo 
				 *  con el jndi base y no con la cinexión que le corresponde.
				 *  
				 *  Al parecer el siguiente if se creó para proteger eventuales errores en la conexión almacenada. 
				 *  ( se modifica el catalog y luego se almacena)
				 * */
//				if(tableDefinition.jndi().equals(db.getJndiFromConnection(conn))) {
				
				TableDefinition tDefMaster = VoTool.getInstance().getTableDefinition(voMaster);
				boolean esElMismoJndi = ( tDefMaster != null && tDefMaster.jndi().equals(tableDefinition.jndi()) );
				
				if(esElMismoJndi) {
					data = ConsultaTool.getInstance().getDataPagged(conn, select , buildParam(wheres), page, pks);	
				}
				else {
					data = ConsultaTool.getInstance().getDataPagged(tableDefinition.jndi(), select , buildParam(wheres), page, pks);
				}
								
					
//				}
//				else {
//					data = ConsultaTool.getInstance().getDataPagged(tableDefinition.jndi(), select , buildParam(wheres), page, pks);	
//				}
			}
		 
		}

		if(transformToVos) {
			Collection colls = VoTool.getInstance().buildVo(data, vo);
			
			return colls;	
		}
		else {
			return data;
		}
		
	}
 

	
	/**
	 * Inserta un registra en una tabla con PK autonumérica definida<br/>
	 * this.table debe ser distinta de null<br/>
	 * debe siempre tener una PK<br/>
	 * 
	 * @author Pancho
	 * @since 17-07-2018
	 *
	 * */
	
	protected Double privateGenericInsertIdentity(Object jndiOrConn,Vo vo) throws SQLException {
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());
		
		String table = this.table;
		
		if(table == null && tableDefinition != null && tableDefinition.tableName() != null ) {
			table =   tableDefinition.tableName() ;
		}
		
		
		if(table == null) {
			throw new SQLException("No existe una tabla definida ");
		}
		
		if(this.pk.size() != 1 ) {
			if( tableDefinition != null && tableDefinition.pks().length == 1 && tableDefinition.pks()[0].autoIncremental() && tableDefinition.pks()[0].numerica()) {
				//tiene pk
			}
			else {
				throw new SQLException("La tabla debe tener SIEMPRE una PK autonumérica definida ");
			}
		}
		
  
		Double newId = -1D;
		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());
		
		if(fieldsList != null && fieldsList.size() > 0) {
			if(this.pk.size() > 0) {
				fieldsList.remove(this.pk.get(0));	
			}
			
			if(tableDefinition != null && this.pk.size() == 0) {
				fieldsList.remove(tableDefinition.pks()[0].field());	
			}
			
			String key = "insertIdentity_"+vo.getClass().getCanonicalName();
			String select = (String)SoftCacheLocator.getInstance(this.getClass()).get(key);
			
			if(select == null) {
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO ").append(table);
				sql.append(" (").append(ListUtils.getInstance().concatenateValues(fieldsList)).append(")");
				sql.append(" VALUES");
				sql.append(" (").append(ListUtils.getInstance().concatenateReplace(fieldsList,"?")).append(")");
				
				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}

			List<Object> params = new LinkedList<Object>();
			for(String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field );
				
				if(metodo == null) {
					throw new SQLException("No existe el campo "+field+" definido en el VO");
				}
				
				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));	
			}
			
			if(jndiOrConn instanceof String) {
				newId = ConsultaTool.getInstance().insertIdentity((String)jndiOrConn, select, params.toArray());	
			}
			else if(jndiOrConn instanceof Connection) {
				newId = ConsultaTool.getInstance().insertIdentity((Connection)jndiOrConn, select, params.toArray());
			}
		 
			if(newId != null) {
				putPkValue(vo, tableDefinition, newId);
				sendNewIdToDependencies(vo);
			}
		}

		return newId;
	}
		
	/**
	 * Inserta siempre todos lo valores<br/>
	 * Automáticamente se incremente +1 al campo PK
	 * 
	 * @author Pancho
	 * @since 17-07-2018
	 * 
	 * */
	
	protected Double privateGenericInsert(Object jndiOrConn,Vo vo) throws SQLException {
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());
		
		String table = this.table;
		
		if(table == null && tableDefinition != null && tableDefinition.tableName() != null ) {
			table =   tableDefinition.tableName() ;
		}
		
		if(table == null) {
			throw new SQLException("No existe una tabla definida ");
		}
		
		if(this.pk.size() != 1 ) {
			if( tableDefinition != null  && tableDefinition.pks().length == 1 && !tableDefinition.pks()[0].autoIncremental() && tableDefinition.pks()[0].numerica() ) {
				//tiene pk
			}
			else {
				throw new SQLException("La tabla debe tener SIEMPRE una PK numérica, esta PK se le incrementará en 1 su valor ");
			}
		}
		
		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());
		
		if(fieldsList != null && fieldsList.size() > 0) {
			String pk = null;
			
			if(this.pk.size() == 1 ) {
				pk = this.pk.get(0);
			}
			
			if(tableDefinition != null && this.pk.size() == 0 ) {
				pk = (tableDefinition.pks()[0].field());
			}
			
			fieldsList.remove(pk);
			
			String key = "insert_"+vo.getClass().getCanonicalName();
			String select = (String)SoftCacheLocator.getInstance(this.getClass()).get(key);
			
			if(select == null) {
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO ").append(table);
				sql.append(" (").append(pk).append(",").append(ListUtils.getInstance().concatenateValues(fieldsList)).append(")");
				sql.append(" VALUES");
				sql.append(" (?,").append(ListUtils.getInstance().concatenateReplace(fieldsList,"?")).append(")");
				
				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}
			 
			List<Object> params = new LinkedList<Object>();
			Double newId = getNewIdForNormal(jndiOrConn, table, pk);
			
			params.add(newId);
			
			for(String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field );
				
				if(metodo == null) {
					throw new SQLException("No existe el campo "+field+" definido en el VO");
				}
				
				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));	
			}
			
			if(jndiOrConn instanceof String) {
				ConsultaTool.getInstance().insert((String)jndiOrConn, select, params.toArray());	
			}
			else if(jndiOrConn instanceof Connection) {
				ConsultaTool.getInstance().insert((Connection)jndiOrConn, select, params.toArray());
			}
			
			if(newId != null) {
				putPkValue(vo, tableDefinition, newId);
				sendNewIdToDependencies(vo);
			}
		 
			return newId;
		}

		return -1D;
	}
	
	/**
	 * Inserta siempre todos lo valores, no importa si no tiene PK o si Tiene
	 * @author Pancho
	 * @since 17-07-2018
	 * 
	 * */
	
	protected boolean privateGenericInsertNormal(Object jndiOrConn,Vo vo) throws SQLException {
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());
		boolean ok = false;
		String table = this.table;
		
		if(table == null && tableDefinition != null && tableDefinition.tableName() != null ) {
			table =   tableDefinition.tableName() ;
		}
		
		if(table == null) {
			throw new SQLException("No existe una tabla definida ");
		}
		 
		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());
		
		if(fieldsList != null && fieldsList.size() > 0) {
			
			String key = "insertNormal_"+vo.getClass().getCanonicalName();
			String select = (String)SoftCacheLocator.getInstance(this.getClass()).get(key);
			
			if(select == null) {
				StringBuilder sql = new StringBuilder();
				sql.append("INSERT INTO ").append(table);
				sql.append(" (").append(ListUtils.getInstance().concatenateValues(fieldsList)).append(")");
				sql.append(" VALUES");
				sql.append(" (").append(ListUtils.getInstance().concatenateReplace(fieldsList,"?")).append(")");
				
				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}
			
			List<Object> params = new LinkedList<Object>();
			for(String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field );
				
				if(metodo == null) {
					throw new SQLException("No existe el campo "+field+" definido en el VO");
				}
				
				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));	
			}
			
			if(jndiOrConn instanceof String) {
				ok= ConsultaTool.getInstance().insert((String)jndiOrConn, select, params.toArray());	
			}
			else if(jndiOrConn instanceof Connection) {
				ok= ConsultaTool.getInstance().insert((Connection)jndiOrConn, select, params.toArray());
			}
		 
		}

		return ok;
	}
	
	protected Double getNewIdForNormal(Object jndiOrConn, String table, String pk) throws SQLException {
		String sql = "SELECT newid=isnull(MAX("+pk+"),0) FROM "+table;
		
		ConsultaData data = null;
		
		if(jndiOrConn instanceof String) {
			data = ConsultaTool.getInstance().getData((String)jndiOrConn, sql);	
		}
		else if(jndiOrConn instanceof Connection) {
			data = ConsultaTool.getInstance().getData((Connection)jndiOrConn, sql);	
		}
		
		if(data != null) {
			data.setMode(ConsultaDataMode.CONVERSION);
			if(data.next()) {
				return data.getDouble("newid")+1;
			}	
		}
		
		
		return -1D;
	}
	
	/**
	 * reporna la cantidad de registros actualizados <br/>
	 * la definición de la tabla debe tener siempre tabla y al menos una PK definida.
	 * 
	 * @author Pancho
	 * @since 17-07-2018
	 * 
	 * */
	
	protected int privateGenericUpdate(Object jndiOrConn,Vo vo) throws SQLException {
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());
	
		String table = this.table;
		
		if(table == null && tableDefinition != null && tableDefinition.tableName() != null ) {
			table =   tableDefinition.tableName() ;
		}
		
		if(table == null) {
			throw new SQLException("No existe una tabla definida ");
		}
		
		if(this.pk.size() == 0 ) {
			if( tableDefinition != null && tableDefinition.pks().length >= 0 ) {
				//tiene pk
			}
			else {
				throw new SQLException("La tabla debe tener al menos una PK definida. ");	
			}
		}
		
		int update = -1;
		List<String> fieldsList = VoTool.getInstance().getGetsFieldsWithNoParameters(vo.getClass());
		
		if(fieldsList != null && fieldsList.size() > 0) {
			if(this.pk.size() > 0) {
				fieldsList.remove(this.pk.get(0));	
			}
			
			if(tableDefinition != null && this.pk.size() == 0) {
				for(PrimaryKeyDefinition pkd : tableDefinition.pks()) {
					fieldsList.remove(pkd.field());	
				}
				
			}
			
			String key = "update_"+vo.getClass().getCanonicalName();
			String select = (String)SoftCacheLocator.getInstance(this.getClass()).get(key);
			
			if(select == null) {

				
				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE ").append(table);
				sql.append(" SET ").append(ListUtils.getInstance().concatenateSufijo(fieldsList,"=?"));
				
				
				StringBuilder wherepk = new StringBuilder();
				for(String pk : this.pk) {
					if(!"".equals(wherepk.toString())) {
						wherepk.append(" and ");
					}
					wherepk.append("  ").append(pk).append("=?");
					
				}
				
				if(tableDefinition != null && "".equals(wherepk.toString()) ) {
					for(PrimaryKeyDefinition pk : tableDefinition.pks()) {
						if(!"".equals(wherepk.toString())) {
							wherepk.append(" and ");
						}
						wherepk.append("  ").append(pk.field()).append("=?");
						
					}
				}
				
				if(wherepk.toString().length() > 0) {
					sql.append(" WHERE ").append(wherepk.toString());
				}
				
				select = sql.toString();
				SoftCacheLocator.getInstance(this.getClass()).put(key, select);
			}
		 
			List<Object> params = new LinkedList<Object>();
			for(String field : fieldsList) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), field );
				
				if(metodo == null) {
					throw new SQLException("No existe el campo "+field+" definido en el VO");
				}
				
				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));	 
			}
			
			{
				for(String pk : this.pk) {
					Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), pk );
					
					if(metodo == null) {
						throw new SQLException("No existe el pk "+pk+" definido en el VO");
					}
					
					params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));	

				}
				
				if(tableDefinition != null && this.pk.size() == 0) {
					for(PrimaryKeyDefinition pk : tableDefinition.pks()) {
						Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), pk.field() );
						
						if(metodo == null) {
							throw new SQLException("No existe el pk "+pk.field()+" definido en el VO");
						}
						
						params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo)); 
					}
				}
			}
			
			if(jndiOrConn instanceof String) {
				update = ConsultaTool.getInstance().update((String)jndiOrConn, select, params.toArray());	
			}
			else if(jndiOrConn instanceof Connection) {
				update = ConsultaTool.getInstance().update((Connection)jndiOrConn, select, params.toArray());
			}
		 
		}

		return update;
	}
	

	/**
	 * reporna true si se eliminó de la tabla <br/>
	 * la definición de la tabla debe tener siempre tabla y al menos una PK definida.
	 * 
	 * @author Pancho
	 * @since 17-07-2018
	 * 
	 * */
	
	protected int privateGenericDelete(Object jndiOrConn,Vo vo) throws SQLException {
			
		return deleteOnCascadeRecursivo(jndiOrConn, vo);
	}
	
	private  int privateGenericDelete2(Object jndiOrConn,Vo vo) throws SQLException { 
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());
		
		String table = this.table;
		
		if(table == null && tableDefinition != null && tableDefinition.tableName() != null ) {
			table =   tableDefinition.tableName() ;
		}
		
		
		if(table == null) {
			throw new SQLException("No existe una tabla definida ");
		}
		if(this.pk.size() == 0 ) {
			if( tableDefinition != null && tableDefinition.pks().length >= 0 ) {
				//tiene pk
			}
			else {
				throw new SQLException("La tabla debe tener al menos una PK definida. ");	
			}
		}
		
		int update = 0;
		
		String key = "delete_"+vo.getClass().getCanonicalName();
		String select = (String)SoftCacheLocator.getInstance(this.getClass()).get(key);
		
		if(select == null) {
			StringBuilder sql = new StringBuilder();
			sql.append(" DELETE FROM ").append(table);
					
			StringBuilder wherepk = new StringBuilder();
			for(String pk : this.pk) {
				if(!"".equals(wherepk.toString())) {
					wherepk.append(" and ");
				}
				wherepk.append("  ").append(pk).append("=?");
				
			}
			
			if(tableDefinition != null && this.pk.size() == 0) {
				for(PrimaryKeyDefinition pk : tableDefinition.pks()) {
					if(!"".equals(wherepk.toString())) {
						wherepk.append(" and ");
					}
					wherepk.append("  ").append(pk.field()).append("=?");
					
				}
			}
			
			if(wherepk.toString().length() > 0) {
				sql.append(" WHERE ").append(wherepk.toString());
			}
			
			select = sql.toString();
			SoftCacheLocator.getInstance(this.getClass()).put(key, select);
		}
 
		List<Object> params = new LinkedList<Object>();
		{
			for(String pk : this.pk) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), pk );
				
				if(metodo == null) {
					throw new SQLException("No existe el pk "+pk+" definido en el VO");
				}
				
				params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo));	
			}
			
			if(tableDefinition != null && this.pk.size() == 0 ) {
				for(PrimaryKeyDefinition pk : tableDefinition.pks()) {
					Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), pk.field() );
					
					if(metodo == null) {
						throw new SQLException("No existe el pk "+pk.field()+" definido en el VO");
					}
					
					params.add(VoTool.getInstance().getReturnFromMethod(vo, metodo)); 
				}
			}
		}
		
		
		
		if(jndiOrConn instanceof String) {
			update = ConsultaTool.getInstance().update((String)jndiOrConn, select, params.toArray());	
		}
		else if(jndiOrConn instanceof Connection) {
			update = ConsultaTool.getInstance().update((Connection)jndiOrConn, select, params.toArray());
		}
		 
		return update;
	}
	
	/**
	 * reporna true si existe el vo<br/>
	 * la definición de la tabla debe tener siempre tabla y al menos una PK definida.
	 * 
	 * @author Pancho
	 * @since 17-07-2018
	 * 
	 * */
	
	
	protected boolean privateGenericExist(Object jndiOrConn,Vo vo) throws SQLException {
		TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition(vo.getClass());
		
		String table = this.table;
		
		if(table == null && tableDefinition != null && tableDefinition.tableName() != null ) {
			table =   tableDefinition.tableName() ;
		}
		
		
		if(table == null) {
			throw new SQLException("No existe una tabla definida ");
		}
		
		if(this.pk.size() == 0 ) {
			if( tableDefinition != null && tableDefinition.pks().length >= 0 ) {
				//tiene pk
			}
			else {
				throw new SQLException("La tabla debe tener al menos una PK definida. ");	
			}
		}
		
		List<Where> wheres = new ArrayList<Where>();
		//List<Object> params = new LinkedList<Object>();
		{
			for(String pk : this.pk) {
				Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), pk );
				
				if(metodo == null) {
					throw new SQLException("No existe el pk "+pk+" definido en el VO");
				}
				
				Object valorGet = VoTool.getInstance().getReturnFromMethod(vo, metodo);
				//params.add(valorGet);	
				wheres.add(new Where(pk, "=", valorGet));
			}			
			
			if(tableDefinition != null && this.pk.size() == 0) {
				for(PrimaryKeyDefinition pk : tableDefinition.pks()) {
					Method metodo = VoTool.getInstance().getGetMethod(vo.getClass(), pk.field() );
					Object valorGet = VoTool.getInstance().getReturnFromMethod(vo, metodo);
					//params.add(valorGet);	
					wheres.add(new Where(pk.field(), "=", valorGet));
					
				}
			}
		}
		
		Collection cols = privateGenericGetAll(jndiOrConn, vo.getClass(), wheres, getBiggerConsultaDataPage(getEmptySort()));
		return  cols != null && cols.size() > 0;

	}

	public String buildSort(ConsultaData sort) {
		return buildSort(null, sort);
	}
	
	public String buildSort(String prefijo, ConsultaData sort) {
		return ConsultaToolUtils.getIntance().buildSort(prefijo, sort);
	}
	
	

	
	public String buildWhere(List<Where> where) {
		if(where != null && where.size() > 0) {
			StringBuilder sql = new StringBuilder();
			
			for(Where w : where) {
				if(!sql.toString().equals("")) {
					sql.append(" and ");
				}
				
				sql.append(buildWheres(w));
			}
			
			return (" WHERE ").concat(sql.toString()).concat("\n");
		}
		return "";	
		
		 
	}
	
	private String buildWheres(Where w) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(buildWhere(w));
		
		if(w.getOr() != null) {
			for(Where or : w.getOr()) {
				sql.append(" or ").append(buildWheres(or));
			}
		}
		
		if(w.getAnd() != null) {
			for(Where and : w.getAnd()) {
				sql.append(" and ").append(buildWheres(and));
			}
		}
		
		if(w.getOr() == null && w.getAnd()  == null) {
			return sql.toString().concat(" ");
		}
		else {
			return "(".concat(sql.toString()).concat(") ");
		}
	}
	
	private String buildWhere(Where w) {
		StringBuilder sql = new StringBuilder();
		sql.append(w.getCampo()).append(" ").append(w.getSignus());
		
		StringBuilder sql2 = new StringBuilder();
		 
		List lista =  w.getValues();
		for(Object objeto : lista ) {
			if(!sql2.toString().equals("")) {
				sql2.append(",");
			}
			sql2.append(" ?");
		}
		 
		
		if(w.getSignus().trim().toLowerCase().equals("in")) {
			sql.append("(").append(sql2.toString()).append(")").append("\n");	
		}
		else if(w.getValues().size() == 1) {
			sql.append(sql2.toString()).append(" \n");	
		}
		else if(w.getValues().size() > 1) {
			sql.append("(").append(sql2.toString()).append(")").append("\n");	
		}
		
		return sql.toString();
	}
	
	public String buildParamString(Object[] params) {
		StringBuilder str = new StringBuilder("[");
		
		if(params != null) {
			for(int i = 0 ; i < params.length ; i++) {
				if(!"".equals(str.toString())) {
					str.append(",");
				}
				str.append(ClaseConversor.getInstance().getObject(params[i], String.class));
			}
		}

		str.append("]");
		return str.toString();
	}
	
	public Object[] buildParam(List<Where> where) {
		
		if(where != null) {
			List<Object> params = new ArrayList<Object>();
			
			for(Where w : where) {
				params.addAll(buildParam(w));

				if(w.getOr() != null) {
					for(Where or : w.getOr()) {
						params.addAll(buildParam(or));
					}
				}
				
				if(w.getAnd() != null) {
					for(Where and : w.getAnd()) {
						params.addAll(buildParam(and));
					}
				}
			}
			
			
			return params.toArray();
		}

		return null;
	}
	
	private List buildParam(Where w) {
		List<Object> params = new ArrayList<Object>();
		Object key = w.getCampo();
		if(w.size() == 1) {
			Object o =  w.getValue();
			params.add(o);
		}
		else {
			
			List lista =  w.getValues();
			for(Object objeto : lista ) {
				params.add(objeto);
			}
			
		}
		
		return params;
	}
	
	/**
	 * Retorna un empty sort
	 * @author Pancho
	 * @since 09-08-2018
	 * */
	public ConsultaData getEmptySort() {
		
		List<String> sort = new ArrayList<String>();
		sort.add("direction");
		sort.add("property");
		ConsultaData data = new ConsultaData(sort);
		
		return data;
	}
	
	/**
	 * Si sort es igual a null, retorna la máxima page con sort = a getEmptySort(); <br/>
	 * La página más grande es de 1.000.000 de registros, paginados a 25 registros por página
	 * 
	 * @author Pancho
	 * @since 09-08-2018
	 * 
	 * @see {@link #getEmptySort()}
	 * */
	public ISenchaPage getBiggerConsultaDataPage(ConsultaData sort) {
		return ConsultaToolUtils.getIntance().getBiggerConsultaDataPage(sort);
	}
	
	public List<Where> getEmptyWhere() {
		List<Where> wheres = new ArrayList<Where>();
		return wheres;
	}
	
	public <T>void loadAllReferences(Collection<T> cols , List<Class> classToLoad, EnumReferenceMethod methodRef) {
		loadAllReferences(null, cols, cols, classToLoad,  methodRef);
	}
	
	public <T>void loadAllReferences(Collection<T> cols, Collection<T> colsMaster, List<Class> classToLoad, EnumReferenceMethod methodRef) {
		loadAllReferences(null, cols, colsMaster, classToLoad,  methodRef);
	}
	
	
	public <T>void loadAllReferences(Connection conn, Collection<T> cols, Collection<T> colsMaster, List<Class> classToLoad, EnumReferenceMethod methodRef) {
		privateloadAllReferences(conn, cols, colsMaster, classToLoad, methodRef, 0);
	}
	/**
	 * Carga todas las referencias de un objeto, metodo Lazy Ocupa selectes para obtener todos los valores.
	 * Método eager hace una llamada por Field con todos los registros y depsues hacer un equal para obtener la sublista <br/>
	 * 
	 * ColsMaster: solicitud Original de Vos
	 * 
	 * @author Pancho
	 * @since 04-08-2018
	 * */
	private <T>void privateloadAllReferences(Connection conn, Collection<T> cols, Collection<T> colsMaster, List<Class> classToLoad, EnumReferenceMethod methodRef, int stackCount) {
		if(stackCount == 100) {
			throw new StackOverflowError("Ha llegado al limite de stacks ");
		}
		if(classToLoad == null && cols != null && cols.size()> 0) {
			classToLoad = new ArrayList<Class>();
			classToLoad.add(cols.iterator().next().getClass());
		}
		
		if(cols != null) {
			Map<String, List<Collection>> vosReferenced = new HashMap<String, List<Collection>>();
			Map<Class,Collection> allColsReferenced = new HashMap<Class,Collection>();
			Map<String,Map> tableReference = getTableReference(cols);
			
			if(methodRef == EnumReferenceMethod.LAZY && tableReference != null) {
				
				for(T t : cols) {
					TableReference[] references = VoTool.getInstance().getTableReference(t.getClass());
					
					if(references == null) {
						continue;
					}
					
					for(String keyField : tableReference.keySet()) {
						Collection colsToSet = getValueForaneo( conn, t, keyField, tableReference.get(keyField) , classToLoad);
						
						putVosReferenciados(t, keyField, tableReference, colsToSet);
						
						guardaVoReferenciados(vosReferenced, keyField, colsToSet);
					}	
				}
			}
			else if(methodRef == EnumReferenceMethod.EAGER && tableReference != null) {
				Map<String,Collection> mapWithAllCol = getValuesForaneo(conn ,cols,colsMaster, tableReference , classToLoad);
				
				for(T t : cols) {
					TableReference[] references = VoTool.getInstance().getTableReference(t.getClass());
					
					if(references == null) {
						continue;
					}
					
					for(String keyField : tableReference.keySet()) {
						List<TableReference> listRef = (List<TableReference>)tableReference.get(keyField).get("TableReference[]");
						Collection colsToSet = getSubValuesForaneo(t, mapWithAllCol, keyField, listRef);
						
						putVosReferenciados(t, keyField, tableReference, colsToSet);
						
						guardaVoReferenciados(vosReferenced, keyField, colsToSet);
					}	
				}
			}
		 
			
			Set<String> sets = vosReferenced.keySet();
			for(String set : sets) {
				privateloadAllReferences(conn, (Collection<T>) vosReferenced.get(set), colsMaster, classToLoad, methodRef, ++stackCount);
			}
		}
	}
	
	public <T>void loadAllEmptyReferences(Connection conn, Collection<T> cols, List<Class> classToLoad) {
		Map<String, List<Collection>> vosReferenced = new HashMap<String, List<Collection>>();
		Map<Class,Collection> allColsReferenced = new HashMap<Class,Collection>();
		Map<String,Map> tableReference = getTableReference(cols);
		
		for(T t : cols) {
			TableReference[] references = VoTool.getInstance().getTableReference(t.getClass());
			
			if(references == null) {
				continue;
			}
			
			for(String keyField : tableReference.keySet()) {
				List<TableReference> listRef = (List<TableReference>)tableReference.get(keyField).get("TableReference[]");
				
				if(listRef.size() > 0 && classToLoad.contains(listRef.get(0).voClass())) {
					Collection colsToSet = new ArrayList<Object>();
					Object vo = cg.getNewFromClass(listRef.get(0).voClass());
					colsToSet.add(vo);
					
					putVosReferenciados(t, keyField, tableReference, colsToSet);
					
					guardaVoReferenciados(vosReferenced, keyField, colsToSet);
				}

			}	
		}
		
		Set<String> sets = vosReferenced.keySet();
		for(String set : sets) {
			loadAllEmptyReferences(conn, vosReferenced.get(set), classToLoad);
		}
	}
	
	/**
	 * Almacena los vos referencias para cargar sus dependencias en una segunda instancia<br/>
	 * @param t es el Vo que existende de Vo
	 * @param vosReferenced guarda los vos referenciados, como esto viene una collection de vos cada Key de este collection podrá contener muchos objetos referidos, por eso el List<br/>
	 * @param KeyField hace mención al nombre del campo al cual hace referencia dentro del Vo<br/>
	 * @param ColsToSet contiene los objetos referenciados contruidos a través del TableDefinition
	 * 
	 * @author Pancho
	 *  @since 04-08-2018
	 * */
	private void guardaVoReferenciados( Map<String, List<Collection>> vosReferenced, String keyField, Collection colsToSet) {
		if( colsToSet != null && colsToSet.size() > 0) {
			if(vosReferenced.get(keyField) == null) {
				vosReferenced.put(keyField, new ArrayList<Collection>());
			}
			vosReferenced.get(keyField).addAll(colsToSet);
		}
	}
	/**
	 * Este método se llama por cada Vo existente, está hecho para insertar las referencias dentro del Vo, las referencias pueden
	 * ser Colletions o JavaBeans simples <br/>
	 * @param t es el Vo que existende de Vo
	 * @param keyField es el nombre del campo que contiene el o los Vos</br>
	 * @param tableReference contiene la referencia de TableReference y TableDefinition de la clase referenciada</br>
	 * @param colsToSet es el conjunto de collection producto de la relación 1 a n o 1 a 1.</br>
	 * 
	 * @author Pancho
	 * @since 04-08-2018
	 * */
	private void putVosReferenciados(Object t, String keyField, Map<String,Map> tableReference, Collection colsToSet) {
		List<TableReference> listRef = (List<TableReference>)tableReference.get(keyField).get("TableReference[]");
		TableDefinition td =  (TableDefinition)tableReference.get(keyField).get("TableDefinition");
		
		Class[] defToSetCollection = {Collection.class};
		Method toSet = VoTool.getInstance().getSetMethod(t.getClass(), keyField, defToSetCollection);
		
		if(toSet != null) {
			Object[] paramToSet = {getCollection(colsToSet)};
			VoTool.getInstance().getReturnFromMethod(t, toSet , paramToSet);	
		}
		else {
			Class[] defToSetObj = {listRef.get(0).voClass()};
			toSet = VoTool.getInstance().getSetMethod(t.getClass(), keyField, defToSetObj);
			
			if(toSet != null) {
				Object[] paramToSet = {getObject(colsToSet, t.getClass())};
				VoTool.getInstance().getReturnFromMethod(t, toSet , paramToSet);
			}	
		}
	}
	
	/**
	 * Valida la collection como param, siempre retorna una collection
	 * */
	private Collection getCollection(Collection colsToSet) {
		if(colsToSet ==null) {
			colsToSet = new ArrayList();
		}
		
		return colsToSet;
	}
	
	/**
	 * Valida el objeto a retornar, solo si hay un objeto disponible se retorna dicho objeto
	 * */
	private Vo getObject(Collection colsToSet, Class clase) {
		Vo vo = null;
		
		if(colsToSet != null && colsToSet.size() > 0) {
			vo = (Vo)colsToSet.iterator().next();
		}
		
//		if(vo == null) {
//			ClaseGenerica cg = Util.getInstance(ClaseGenerica.class);
//			vo = cg.getNewFromClass(clase);
//		}
		
		return vo;
	}
	
	/**
	 * retorna una sublista de valores a partir de la lista general de un field<br/>
	 * La lista general de un fueld es aquella que tiene los valores para todas las FK
	 * 
	 * @author Pancho
	 * @since 03-08-2018
	 * */
	private Collection getSubValuesForaneo(Object vo ,Map<String,Collection> mapWithAllCol, String keyField , List<TableReference> listRef ) {
		Collection colsToSet = mapWithAllCol.get(keyField);
		Map<String,Map> toCompare = new HashMap<String, Map>();
		
		for(TableReference tr : listRef) {
			 
			Method getMethod = VoTool.getInstance().getGetMethod(vo.getClass(), tr.fk().fk());
			Object objetoToComp = VoTool.getInstance().getReturnFromMethod(vo, getMethod);
			
			Map map = new HashMap<String, Object>();
			map.put( "objetoToComp" , objetoToComp );
			map.put( "voClass" , tr.voClass() );
			map.put( "otherTableField" , tr.fk().otherTableField() );
			toCompare.put(tr.fk().fk(), map);
		}
		
		Collection cols = mapWithAllCol.get(keyField);
		
		Collection colsFiltered = new ArrayList();
		
		for(Object voToFilter : cols) {
			boolean is = true;
			
			for(String fk : toCompare.keySet()) {
				Map map = toCompare.get(fk);
				
				Object objetoToComp = map.get("objetoToComp");
				
				if( objetoToComp != null) {
					Class voClass = (Class) map.get("voClass");
					String  otherTableField  = (String) map.get("otherTableField");
					
					Method m = VoTool.getInstance().getGetMethod(voClass, otherTableField);
					Object retorno = VoTool.getInstance().getReturnFromMethod(voToFilter, m);
					
					is &= objetoToComp.equals(retorno);
				}
			}
			
			if(is) {
				colsFiltered.add(voToFilter);
			}
		}
		
		return colsFiltered;
	}
	
	/**
	 * Retorna las referencias de forma ordenada de la clase de los objetos que contiene Collection.<br/>
	 * En un Map con las el field como key y en el valor la otro map que tiene dos objetos<br/>
	 * en TableReference[] está la lista de TableReference<br/>
	 * en TableDefinition está la definición de la tabla foranea<br/>
	 * La collection debe ser de solo 1 objeto<br/>
	 * 
	 * @since 03-08-2018
	 * @author Pancho
	 * */
	public Map<String,Map> getTableReference(Vo vo) {
		List<Vo> cols = new ArrayList<Vo>();
		cols.add(vo);
		
		return getTableReference(cols);
	}
  
	/**
	 * Retorna las referencias de forma ordenada de la clase de los objetos que contiene Collection.<br/>
	 * En un Map con las el field como key y en el valor la otro map que tiene dos objetos<br/>
	 * en TableReference[] está la lista de TableReference<br/>
	 * en TableDefinition está la definición de la tabla foranea<br/>
	 * La collection debe ser de solo 1 objeto<br/>
	 * 
	 * @since 03-08-2018
	 * @author Pancho
	 * */
	public Map<String,Map> getTableReference(Collection cols) {
 		if(cols != null && cols.size()> 0 ) {
			for(Object vo : cols) {
				return getTableReference(vo.getClass());
			}
		}
		
		return null;
	}
	
	/**
	 * Retorna las referencias de forma ordenada de la clase de los objetos que contiene Collection.<br/>
	 * En un Map con las el field como key y en el valor la otro map que tiene dos objetos<br/>
	 * en TableReference[] está la lista de TableReference<br/>
	 * en TableDefinition está la definición de la tabla foranea<br/>
	 * La collection debe ser de solo 1 objeto<br/>
	 * 
	 * @since 03-08-2018
	 * @author Pancho
	 * */
	public Map getTableReference(Class voClass) {
		TableReference[] references = VoTool.getInstance().getTableReference(voClass);
		
		if(references == null) {
			return null;
		}
		
		Map<String,Map> tableReference = new HashMap<String,Map>();
		for(TableReference reference : references) {

			if(tableReference.get(reference.field()) == null) {
				TableDefinition tableDefinition = VoTool.getInstance().getTableDefinition( reference.voClass() );
				if(tableDefinition == null) {
					continue;
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("TableReference[]", new ArrayList<TableReference>());
				map.put("TableDefinition", tableDefinition);
				tableReference.put(reference.field(), map );
			}
			
			
			((List)tableReference.get(reference.field()).get("TableReference[]")).add(reference);
		}
		
		return tableReference;
	}
	
	/**
	 * Retorna todas clases de todos los objetos referenciados por @TableReference
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	@SuppressWarnings("unchecked")
	public List<Class<? extends Vo>> getAllReferences(Class<? extends Vo> classDef) {
		List<Class<? extends Vo>> lista = new ArrayList<Class<? extends Vo>>();
		
		if(classDef != null) {
			if(!lista.contains(classDef)) {
				lista.add(classDef);
			}
			
			Map<String,Object> map = getTableReference(classDef);
			if(map != null) {
				for(String field : map.keySet()) {
					List<TableReference> listaReferencias = (List<TableReference>) ((Map)map.get(field)).get("TableReference[]");
					if(listaReferencias != null && listaReferencias.size() > 0) {
						
						
						List<Class<? extends Vo>> listaRecursiva = getAllReferences(listaReferencias.get(0).voClass());
						ListUtils.getInstance().addIfNotExists((List)lista, (List)listaRecursiva);
					}
				}
			}

		}
		
		return lista;
	}

	
	/**
	 * Son todos los valores para todos los Vo agrupados en un misma collection, por eso está en un Map, la collection se debe filtrar una vez obtenida para que coincida para cada vo
	 * */
	private Map<String,Collection> getValuesForaneo(Connection conn, Collection cols, Collection colsMaster, Map<String,Map> tableReference , List<Class> classToLoad) {
		Map<String, Collection> mapWithAllCols = new HashMap<String, Collection>();
		
		for(String keyField : tableReference.keySet()) {
			Map map = tableReference.get(keyField);
			
			TableDefinition tf = (TableDefinition)map.get("TableDefinition");
			List<TableReference> trList = (List<TableReference>)map.get("TableReference[]");
			TableReference trFirst = (TableReference)trList.get(0);
			
			if(classToLoad.contains(trFirst.voClass())) {
				List<Where> wheres = getEmptyWhere();
				List<Object> toIn = new ArrayList<Object>();
				for(Object vo : cols) {
					for(TableReference tr : trList) {
						toIn.add( getValueFromVo(vo, tr.fk().fk()) );
					}
				}
				for(TableReference tr : trList) {
					wheres.add(new Where(tr.fk().otherTableField(), "in", toIn));
				}
				
				if(map != null && map.get("TableReference[]") != null && map.get("TableDefinition") != null) {
					
					
					try {
						Collection colToMap = null;
						
						if(conn != null) {
							colToMap = privateGenericGetAll2(conn, 
															trFirst.voClass(), 
															colsMaster.iterator().next().getClass(), 
															wheres, 
															CtrGeneric.getInstance().getBiggerConsultaDataPage(CtrGeneric.getInstance().getEmptySort()), 
															true);
						}
						else {
							colToMap = CtrGeneric.getInstance().getAllFromClass( trFirst.voClass(), wheres);
						}
						
						mapWithAllCols.put(keyField, colToMap);
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else {
				mapWithAllCols.put(keyField, new ArrayList());
			}
		}
		
		return mapWithAllCols;
	}
	
	
	/**
	 * Se ocupa para el modo Lazy, retorna la collection de valores que coinciden con todas las FK de un determinado Vo
	 * */
	private Collection getValueForaneo(Connection conn, Object vo, String keyField , Map map , List<Class> classToLoad) {
		if(map != null && map.get("TableReference[]") != null && map.get("TableDefinition") != null ) {
			CtrTGeneric ctrTGeneric = Ctr.getInstance(CtrTGeneric.class);
			CtrGeneric ctrGeneric = Ctr.getInstance(CtrGeneric.class);
			
			TableDefinition tf = (TableDefinition)map.get("TableDefinition");
			List<TableReference> trList = (List<TableReference>)map.get("TableReference[]");
			TableReference trFirst = (TableReference)trList.get(0);
			
			if(classToLoad == null || classToLoad.contains(trFirst.voClass())) {
				//si no se debe cargar retorna null
				List<Where> wheres = getEmptyWhere();
				for(TableReference tr : trList) {
					wheres.add(new Where(tr.fk().otherTableField(), "=", getValueFromVo(vo, tr.fk().fk())));
				}
				
				try {
					if(conn != null) {		
						return ctrTGeneric.getAllFromClass( conn, trFirst.voClass(), wheres);	
					}
					else {
						return ctrGeneric.getAllFromClass( trFirst.voClass(), wheres);
					}
					
					
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return new ArrayList();
	}
	
	/**
	 * Retorna un valor de un determinado field de un vo
	 * @author Pancho
	 * @since 03-08-2018
	 * 
	 * */
	private Object getValueFromVo(Object vo, String field) {
		Method m = VoTool.getInstance().getGetMethod(vo.getClass(), field);
		return VoTool.getInstance().getReturnFromMethod(vo, m);
	}
	
	
	/**
	 * Borra en cascada para eliminar la integredidad referencial
	 * 
	 * @author Pancho
	 * @since 03-08-2018
	 * @throws SQLException 
	 * @deprecate
	 * */
	private void deleteOnCascade(Object jndiOrConn, Vo vo) throws SQLException {
		if(vo != null) {
			TableReference[] trArray =VoTool.getInstance().getTableReference(vo.getClass());
			
			if(trArray == null) {
				return;
			}
			
			for(TableReference tr : trArray ) {
				
				Method m = VoTool.getInstance().getGetMethod(vo.getClass(), tr.field());
				Object retorno = VoTool.getInstance().getReturnFromMethod(vo, m);

				if(retorno != null) {
					deleteOnCascadeRecursivo(jndiOrConn, (Vo) retorno);
				}
				
			}
			
			
		}
	}
	
	/**
	 * Borra en las tablas referenciadas de forma recursiva (hojas primero)
	 * @author Pancho
	 * @since 03-08-2018
	 * */
	private int deleteOnCascadeRecursivo(Object jndiOrConn, Vo vo) throws SQLException {
		int count = 0;
		
		if(vo != null) {
			TableReference[] trArray =VoTool.getInstance().getTableReference(vo.getClass());
			
			if(trArray != null) {
				for(TableReference tr : trArray ) {
					String field = tr.field();
					Method m = VoTool.getInstance().getGetMethod(vo.getClass(),field );
					Object retorno = VoTool.getInstance().getReturnFromMethod(vo, m);
					
					if(retorno instanceof Collection) {
						for(Object voin : (Collection)retorno) {
							count += deleteOnCascadeRecursivo(jndiOrConn, (Vo)voin);	
						}
					}
					else if(retorno instanceof Vo) {
						count += deleteOnCascadeRecursivo(jndiOrConn, (Vo)retorno);	
					}
				}
				
				
			}

			
			count += privateGenericDelete2(jndiOrConn, vo);
		}
		
		return count;
	}
	
	/**
	 * Retorna true cuando un objeto tiene al menos una refencia en null,
	 * Debería preguntar por sus dependencias pero me dio flojera hacer resto
	 * */
	public boolean mustLoadReferences(Vo vo) {
		TableReference[] tbs = VoTool.getInstance().getTableReference(vo.getClass());
		if(tbs != null) {
			for(TableReference tb : tbs) {
				Method m = VoTool.getInstance().getGetMethod(vo.getClass(), tb.field());
				Object retorno = VoTool.getInstance().getReturnFromMethod(vo, m);
				
				if(retorno == null) {
					return true;	
				}
				
			}
		}
		
		return false;
	}
	
	/**
	 * Lo que hace es comunicar el nuevo ID de este objeto a los objetos dependientes<br/>
	 * Solo actua en el nivel de este objeto, o sea los objetos dependientes no comunican su nuevo id a menos que ellos se inserten nuevamente <br/>
	 * El objeto solo debe tener una referencia hacia otro obj y además la referencia debe ser a nivel e su PK
	 * 
	 * @author Pancho
	 * @since 04-08-2018
	 * */
	private void sendNewIdToDependencies(Vo vo) {
		if(vo  != null ) {
			Collection<Vo> cols = new ArrayList<Vo>();
			cols.add(vo);
			Map<String, Map> tableReference = getTableReference(cols);
			TableDefinition td = VoTool.getInstance().getTableDefinition(vo.getClass());
				
			if(td  == null || td.pks() == null || (td.pks().length != 1 || tableReference == null)) {
				return;
			}
			
			for(String field : tableReference.keySet()) {
				String fieldPK = td.pks()[0].field();
				Method mGetPkValue = VoTool.getInstance().getGetMethod(vo.getClass(), fieldPK);
				Object pkValue = VoTool.getInstance().getReturnFromMethod(vo, mGetPkValue);
				
				if(pkValue == null) {
					continue;
				}
						
				Map<String,Object> mapValuesFromPK = (Map<String,Object>)tableReference.get(field);
				
				if(mapValuesFromPK == null) {
					return;
				}
				
				List<TableReference> trs = (List<TableReference>) mapValuesFromPK.get("TableReference[]");
			 
				if(trs == null || trs.size() != 1) {
					return;
				}
				
				for(TableReference tr : trs ) {
					Method m = VoTool.getInstance().getGetMethod(vo.getClass(), tr.field());
					
					if(m != null) {
						Object retorno = VoTool.getInstance().getReturnFromMethod(vo, m);
						
						if(retorno != null) {
							if(retorno instanceof Collection) {
								
								Method mToSet = null;
								for(Vo voInCol : (Collection<Vo>)retorno) {
									if(mToSet == null) {
										mToSet = VoTool.getInstance().getSetMethod(voInCol.getClass(), tr.fk().otherTableField(), 1);
									}
									
									if(mToSet != null) {
										Class<?>[] paramTypes = mToSet.getParameterTypes();
										Object toSetObject = ClaseConversor.getInstance().getObject(pkValue, paramTypes[0]);
										Object[] toSetParams = {toSetObject};
										VoTool.getInstance().getReturnFromMethod(voInCol, mToSet, toSetParams);
									}
		 
								}
							}
							else if(retorno instanceof Vo) {
								Method mToSet = VoTool.getInstance().getSetMethod(retorno.getClass(), tr.fk().otherTableField(), 1);
								
								if(mToSet != null) {
									Class<?>[] paramTypes = mToSet.getParameterTypes();
									Object toSetObject = ClaseConversor.getInstance().getObject(pkValue, paramTypes[0]);
									Object[] toSetParams = {toSetObject};
									VoTool.getInstance().getReturnFromMethod(retorno, mToSet, toSetParams);
								}
							}
						}
					}
					
				}
			}
			
		}
	}
	
	/**
	 * Set de PK del Vo
	 * 
	 * @author Pancho
	 * @since 04-08-2018
	 * */
	private void putPkValue (Object a, TableDefinition tableDef, Double newId) {
		if(tableDef != null && tableDef.pks() != null) {
			for(PrimaryKeyDefinition pk : tableDef.pks()) {
				String methodName = "set" + pk.field().substring(0, 1).toUpperCase() + pk.field().substring(1, pk.field().length());
				List<Method> methods = VoTool.getInstance().getSetsMethodsWithOneParameter(a.getClass());
				for(Method m : methods) {
					if(m.getName().equals(methodName) && m.getParameterTypes().length == 1) {
						Class type = m.getParameterTypes()[0];
						
						Object[] params = {ClaseConversor.getInstance().getObject(newId, type)};
						VoTool.getInstance().getReturnFromMethod(a, m, params);
						
						break;
					}
				}
			}
		}

	}
	
	
	/**
	 * Reinicia las PK recursivamente de todos los objetos referidos e incluidos los que se ingresan
	 * @author Pancho
	 * @since 04-08-2018
	 * */
	public void clearPKs(Collection cols) {
		if(cols != null && cols.size() >= 1) {
			
			Map<String, Map> tableReference = getTableReference(cols);
			TableDefinition td = VoTool.getInstance().getTableDefinition(cols.iterator().next().getClass());
					
			if( td == null) {
				return;
			}
			
			for(Object vo : cols) {
				 
				for(PrimaryKeyDefinition pkdef : td.pks()) {
					Method mToSet = VoTool.getInstance().getSetMethod(vo.getClass(), pkdef.field(),1);
					
					if(mToSet != null) {
						Class<?>[] classDef = mToSet.getParameterTypes();
						Object toSet = ClaseConversor.getInstance().getObject(0, classDef[0]);
						Object[] params = {toSet};
						VoTool.getInstance().getReturnFromMethod(vo, mToSet, params);
						sendNewIdToDependencies((Vo)vo);
					}
					
				}
				
				if(tableReference != null) {
					for(String field : tableReference.keySet()) {
						Method mGetToClearPks = VoTool.getInstance().getGetMethod(vo.getClass(), field);
						
						if(mGetToClearPks != null) {
							Object toClearPks = VoTool.getInstance().getReturnFromMethod(vo, mGetToClearPks);
							
							if(toClearPks != null) {
								if(toClearPks instanceof Collection) {
									clearPKs((Collection)toClearPks);
								}
								else if(toClearPks instanceof Vo) {
									Collection<Vo> col = new ArrayList<Vo>();
									col.add((Vo)toClearPks);
									clearPKs(col);
								}
							}

						}
					}
				}
			}
		}
	}
	
	/**
	 * Retorna un where con los mismos valores de las pks de la clase
	 * @author Pancho
	 * @since 08-08-2018
	 * */
	public List<Where> getWhereFromClass(Class clase, Object... pksValues) {
		List<Where> where = new ArrayList<Where>();
		
		if(clase != null) {
			TableDefinition td = VoTool.getInstance().getTableDefinition(clase);
		 
			if(td != null && pksValues != null) {
				PrimaryKeyDefinition[] pks = td.pks();
				
				if(pks.length == pksValues.length) {
					int pos = 0;
					for(PrimaryKeyDefinition pk : pks) {
						if(pksValues.length > pos) {
							where.add(new Where(pk.field(),"=", pksValues[pos]));	
						}
						
					}
				}
			}
		}
		
		return where;
	}
}
