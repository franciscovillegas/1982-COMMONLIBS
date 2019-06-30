package portal.com.eje.portal.vo;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaToolUtils;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;

public abstract class AbsVoPersisteEnBDTransaction<T>  extends AbsVoPersiste {

	
	public <T> Collection<T> getAll(Connection conn) throws SQLException, NullPointerException {
		return this.getAll(conn, getEmptyWhere(), getEmptySort());
	}
	

	public <T> Collection<T> getAll(Connection conn, List<Where> wheres ) throws SQLException, NullPointerException {
		return this.getAll(conn, wheres, getEmptySort());
	}
	

	public <T> Collection<T> getAll(Connection conn, ConsultaData sort) throws SQLException, NullPointerException {
		return this.getAll(conn, getEmptyWhere(), sort);
	}
	
	public abstract <T> Collection<T> getAll(Connection conn, List<Where> wheres, ConsultaData sort) throws SQLException, NullPointerException;
	
 
	public T get(Connection conn, List<Where> wheres) throws SQLException, NullPointerException {		
		return get(conn, wheres, getEmptySort());
	}

	
	public T get(Connection conn, ConsultaData sort) throws SQLException, NullPointerException {
		return get(conn, getEmptyWhere(), sort);
	}
	
	public T get(Connection conn, List<Where> wheres, ConsultaData sort)  throws SQLException, NullPointerException{
		Collection<T> collections = getAll(conn, wheres, sort);
		if(collections != null && collections.size() >=1){
			if(collections.size() > 1) {
				System.out.println("@@ ERROR La collection tiene mas de un objeto.");
			}
			else {
				Iterator ite = collections.iterator();
				return (T)ite.next();
			}
		}
		return null;
	}
	
	public abstract <T>boolean add(Connection conn, T a) throws SQLException, NullPointerException;
	
	public <T> boolean add(Connection conn, Collection<T> a) throws SQLException, NullPointerException {
		boolean ok = true;
		
		if(a != null) {
			for(T t : a){
				if(!this.exist(conn, t)) {
					this.add(conn, t);	
				}
			}
		}
		
		return ok;
	}
	
	public abstract <T> boolean upd(Connection conn, T a) throws SQLException, NullPointerException;
	
	public <T> boolean upd(Connection conn, Collection<T> a) throws SQLException, NullPointerException {
		boolean ok = true;
		
		if(a != null) {
			for(T t : a){
				ok &= this.upd(conn, t);
			}
		}
		
		return ok;
	}
	
	public abstract <T> boolean del(Connection conn, T a) throws SQLException, NullPointerException;
	
	public <T> boolean del(Connection conn, Collection<T> a) throws SQLException, NullPointerException {
		return del(conn, a, null);
	}
	
	public <T> boolean del(Connection conn, Collection<T> a, List<Class> classToLoad) throws SQLException, NullPointerException {
		boolean ok = true;
		
		if(a != null && a.size() > 0) {
			
			if(mustLoadReferences((Vo)a.iterator().next())) {
				loadAllReferences(conn, a, a,  classToLoad, EnumReferenceMethod.LAZY);
			}
			
			for(T t : a){
				ok &= this.del(conn, t);
			}
		}
		
		return ok;
	}
	
	public abstract <T> boolean exist(Connection conn, T a) throws SQLException, NullPointerException;
	
	public <T> boolean exist(Connection conn, Collection<T> cols) throws SQLException, NullPointerException{
		boolean exist = true;
		if(cols != null) {
			for(T o : cols) {
				exist &= this.exist(conn, o);
			}
		}
		
		return exist;
	}
 
	/**
	 * Se debe usar @see

	 * @see #updOrAdd(Collection) 
	 * @see {@link #updOrAdd(Collection, boolean)}
	 * @author Pancho
	 * @since 06-08-2018
	 * */
	public <T> boolean updOrAdd(Connection conn,T a) throws SQLException, NullPointerException{
		Assert.notNull(conn);
		
		if(exist(conn, a)) {
			return upd(conn,a);
		}
		else {
			return add(conn,a);
		}
	}
	
	public <T> boolean updOrAdd(Connection conn,Collection<T> cols) throws SQLException, NullPointerException{
		Assert.notNull(conn);
		
		boolean ok = true;
		for(Object t : cols) {
			ok&=this.updOrAdd(conn,t);
		}
		return ok;
	}
	
	public <T> boolean updOrAdd(Connection conn,Collection<T> cols, boolean incluDependencies) throws SQLException, NullPointerException{
		boolean ok = updOrAdd(conn, cols);
		
		if(incluDependencies && cols != null && cols.size() > 0) {
			Map<String,Map> tableRefence = getTableReference(cols);
			TableDefinition td = VoTool.getInstance().getTableDefinition(cols.iterator().next().getClass());
			if(td != null && td.pks().length == 1  ) {
				if(tableRefence == null) {
					return true;
				}
				for(String field : tableRefence.keySet()) {
					List<TableReference> lista = (List<TableReference>) tableRefence.get(field).get("TableReference[]");
					
					Method mToGet = null;
					for(Object t : cols) {
						if(lista != null && lista.size() == 1) {
							if(mToGet == null) {
								mToGet = VoTool.getInstance().getGetMethod(t.getClass(), field);
								Object o = VoTool.getInstance().getReturnFromMethod(t, mToGet);
								
								Collection<Vo> colToUpdOrAdd = new ArrayList<Vo>();
								
								if(o instanceof Vo) {
									colToUpdOrAdd.add((Vo)o);
								}
								else if(o instanceof Collection) {
									colToUpdOrAdd.addAll((Collection) o);
								}
								
								ok &= updOrAdd(conn, colToUpdOrAdd, incluDependencies);
							}

						}	
					}
					
				}
			}
		}
		
		return ok;
	}
	/**
	 * Hecho para insertar todos los valores, y para aquella columna PK se incrementa su valor en 1 automáticamente
	 * 
	 * @author Pancho
	 * @since 20-07-2018
	 * */
	public Double genericInsert(Connection conn,Vo vo) throws SQLException {
		return privateGenericInsert(conn, vo);
	}
	
	/**
	 * Hecho para insertar todos los valores menos la PK.
	 * 
	 * @author Pancho
	 * @since 20-07-2018
	 * */
	public Double genericInsertIdentity(Connection conn,Vo vo) throws SQLException {
		return privateGenericInsertIdentity(conn, vo);
	}
	
	public <T>Collection<T> genericGetAll(Connection conn,Class<T> vo,List<Where> wheres, ConsultaData sort) throws SQLException {
		return privateGenericGetAll(conn, vo, wheres, ConsultaToolUtils.getIntance().getBiggerConsultaDataPage(sort));
	}
	
	/**
	 * Esta hecho para insertar todos los valores del vo, no hay ningún incremento adicional de ningún campo ni tampoco se omiten valores al insertar
	 * 
	 * @author Pancho
	 * @since 20-07-2018
	 * 

	 * */
	public boolean genericInsertNormal(Connection conn,Vo vo) throws SQLException {
		return privateGenericInsertNormal(conn, vo);
	}
	
	public int genericUpdate(Connection conn,Vo vo) throws SQLException {
		return privateGenericUpdate(conn, vo);
	}
	
	public int genericDelete(Connection conn,Vo vo) throws SQLException {
		return privateGenericDelete(conn, vo);
	}
	
	public boolean genericExist(Connection conn,Vo vo) throws SQLException {
		return privateGenericExist(conn, vo);
	}
}
