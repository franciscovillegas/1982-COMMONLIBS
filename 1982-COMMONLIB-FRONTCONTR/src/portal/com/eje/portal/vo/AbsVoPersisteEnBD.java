package portal.com.eje.portal.vo;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaToolUtils;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;

public abstract class AbsVoPersisteEnBD<T> extends AbsVoPersiste {

	public <T> Collection<T> getAll() throws SQLException, NullPointerException {
		return this.getAll(getEmptyWhere(), getEmptySort());
	}

	public <T> Collection<T> getAll(List<Where> where) throws SQLException, NullPointerException {
		return this.getAll(where, getEmptySort());
	}

	
	public <T> Collection<T> getAll(ConsultaData sort) throws SQLException, NullPointerException {
		return this.getAll(getEmptyWhere(), sort);
	}
	
	public abstract <T> Collection<T> getAll(List<Where> where, ConsultaData sort) throws SQLException, NullPointerException;
 
	public T get(List<Where> where) throws SQLException, NullPointerException {
		return get(where, getEmptySort());
	}
	
	public T get(ConsultaData sort) throws SQLException, NullPointerException {
		return get(getEmptyWhere(), sort);
	}
	
	public T get(List<Where> where, ConsultaData sort) throws SQLException, NullPointerException {
		Collection<T> collections = getAll(where, sort);
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
	 	
	public abstract <T> boolean add(T a) throws SQLException, NullPointerException;
	
	public <T> boolean add(Collection<T> a) throws SQLException, NullPointerException {
		boolean ok = true;
		
		if(a != null) {
			for(T t : a){
				this.add(t);	
				 
			}
		}
		
		return ok;
	}
	
	public abstract <T> boolean upd(T a) throws SQLException, NullPointerException;
	
	public <T> boolean upd(Collection<T> a) throws SQLException, NullPointerException {
		boolean ok = true;
		
		if(a != null) {
			for(T t : a){
				ok &= this.upd(t);
			}
		}
		
		return ok;
	}
	
	public abstract <T> boolean del(T a) throws SQLException, NullPointerException;
	
	public <T> boolean del(Collection<T> a) throws SQLException, NullPointerException {
		return del(a, null);
	}
	
	public <T> boolean del(Collection<T> a, List<Class> classToLoad) throws SQLException, NullPointerException {
		boolean ok = true;
		
		if(a != null) {
			loadAllReferences(a, classToLoad, EnumReferenceMethod.EAGER);
			
			for(T t : a){
				ok &= this.del(t);
			}
		}
		
		return ok;
	}
	
	/**
	 * Se debe usar 
	 * @deprecated
	 * @see #updOrAdd(Collection) 
	 * @see {@link #updOrAdd(Collection, boolean)}
	 * @author Pancho
	 * @since 06-08-2018
	 * */
	public abstract <T> boolean exist(T a)  throws SQLException, NullPointerException;
	
	public <T> boolean exist(Collection<T> cols) throws SQLException, NullPointerException {
		boolean exist = true;
		if(cols != null) {
			for(T o : cols) {
				exist &= this.exist(o);
			}
		}
		
		return exist;
	}
 
	public <T> boolean updOrAdd(T a) throws SQLException, NullPointerException {
		if(exist(a)) {
			return upd(a);
		}
		else {
			return add(a);
		}
	}
	
	public <T> boolean updOrAdd(Collection<T> cols) throws SQLException, NullPointerException {
		boolean ok = true;
		for(Object t : cols) {
			boolean retorno = this.updOrAdd(t);
			ok&= retorno;
		}
		return ok;
	}
	
	public <T> boolean updOrAdd(Collection<T> cols, boolean incluDependencies) throws SQLException, NullPointerException{
		boolean ok = updOrAdd(cols);
		
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
								
								ok &= updOrAdd(colToUpdOrAdd, incluDependencies);
							}

						}	
					}
					
				}
			}
		}
		
		return ok;
	}
	
	public Double genericInsertIdentity(String jndi,Vo vo) throws SQLException {
		return privateGenericInsertIdentity(jndi, vo);
	}
	
	public Double genericInsert(String jndi,Vo vo) throws SQLException {
		return privateGenericInsert(jndi, vo);
	}
	
	public <T>Collection<T> genericGetAll(String jndi,Class<T> vo, List<Where> wheres, ConsultaData sort) throws SQLException {
		return privateGenericGetAll(jndi, vo, wheres, ConsultaToolUtils.getIntance().getBiggerConsultaDataPage(sort));
	}
	
	public boolean genericInsertNormal(String jndi,Vo vo) throws SQLException {
		return privateGenericInsertNormal(jndi, vo);
	}
	
	public int genericUpdate(String jndi,Vo vo) throws SQLException {
		return privateGenericUpdate(jndi, vo);
	}
	
	public int genericDelete(String jndi,Vo vo) throws SQLException {
		return privateGenericDelete(jndi, vo);
	}
	
	public boolean genericExist(String jndi,Vo vo) throws SQLException {
		return privateGenericExist(jndi, vo);
	}
}
