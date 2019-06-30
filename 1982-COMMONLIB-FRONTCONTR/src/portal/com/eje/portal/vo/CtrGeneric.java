package portal.com.eje.portal.vo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ISenchaPage;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.util.InsertTool;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.util.InsertTool.EnumInsertMethod;
import portal.com.eje.portal.vo.vo.Vo;
 

public class CtrGeneric extends AbsVoPersisteEnBD {

	public static CtrGeneric getInstance() {
		return Ctr.getInstance(CtrGeneric.class);
	}
	
	public <T>Collection<T> getAllFromClass(Class<T> clazz, List wheres, ISenchaPage sort) throws SQLException, NullPointerException {
		TableDefinition tableDef = VoTool.getInstance().getTableDefinition(clazz);
		
		if(tableDef == null) {
			throw new SQLException("No TableDefinition presente en "+clazz.getCanonicalName());
		}
		return privateGenericGetAll(tableDef.jndi(), clazz, wheres, sort, true);
	}

	@Override
	public boolean add(Object a) throws SQLException, NullPointerException {
		
		Double newId = null;
		if(a != null) {
			TableDefinition tableDef = VoTool.getInstance().getTableDefinition(a.getClass());
			if(tableDef == null) {
				throw new SQLException("No TableDefinition presente en "+a.getClass().getCanonicalName());
			}
			
			EnumInsertMethod method = InsertTool.getInsertMethod((Vo) a);
			switch (method) {
			case DEFAULT:
				newId = genericInsert(tableDef.jndi(), (Vo) a);
				break;
			case NORMAL:
				genericInsertNormal(tableDef.jndi(), (Vo) a);
				newId = 1D;
				break;
			case IDENTITY:
				newId = genericInsertIdentity(tableDef.jndi(), (Vo)a);
				break;
			default:
				break;
			}
		}
		
		return newId != null && newId > 0;
	}

	@Override
	public boolean upd(Object a) throws SQLException, NullPointerException {
		if(a != null) {
			TableDefinition tableDef = VoTool.getInstance().getTableDefinition(a.getClass());
			
			if(tableDef == null) {
				throw new SQLException("No TableDefinition presente en "+a.getClass().getCanonicalName());
			}
			
			return genericUpdate(tableDef.jndi(), (Vo) a) > 0;	
		}
		
		return false;
	}

	@Override
	public boolean del(Object a) throws SQLException, NullPointerException {
		if(a != null) {
			TableDefinition tableDef = VoTool.getInstance().getTableDefinition(a.getClass());
			
			if(tableDef == null) {
				throw new SQLException("No TableDefinition presente en "+a.getClass().getCanonicalName());
			}
			
			return genericDelete(tableDef.jndi(), (Vo) a) > 0;	
		}
		return false;
	}

	@Override
	public boolean exist(Object a) throws SQLException, NullPointerException {
		if(a != null) {
			TableDefinition tableDef = VoTool.getInstance().getTableDefinition(a.getClass());
			
			if(tableDef == null) {
				throw new SQLException("No TableDefinition presente en "+a.getClass().getCanonicalName());
			}
			return genericExist(tableDef.jndi(), (Vo) a);	
		}
		return false;
	}

	@Override
	@Deprecated
	public Collection getAll(List where, ConsultaData sort) throws SQLException, NullPointerException {
		throw new NotImplementedException();
	}

	public ConsultaData getDataFromClass(Class clazz, List wheres, ISenchaPage sort) throws SQLException, NullPointerException {
		TableDefinition tableDef = VoTool.getInstance().getTableDefinition(clazz);
		
		if(tableDef == null) {
			throw new SQLException("No TableDefinition presente en "+clazz.getCanonicalName());
		}
		return (ConsultaData) privateGenericGetAll(tableDef.jndi(), clazz, wheres, sort, false);
	}
	
	public <T>Collection<T> getAllFromClass(Class<T> clazz, List wheres, ConsultaData sort) throws SQLException, NullPointerException {
		return getAllFromClass(clazz, wheres, getBiggerConsultaDataPage(sort));
	}
	
	/**
	 * Debe indicarse las clases a cargar
	 * @deprecated
	 * */
	public <T>Collection<T> getAllFromClass(Class<T> clazz, List wheres , EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		List<Class> classtoLoad = new ArrayList<Class>();
		classtoLoad.add(clazz);
		return getAllFromClass(clazz, wheres, getEmptySort(), classtoLoad, reference);
	}
	
	public <T>Collection<T> getAllFromClass(Class<T> clazz, List wheres , List<Class> classTtoLoad , EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		return getAllFromClass(clazz, wheres, getEmptySort(), classTtoLoad, reference);
	}
	
	public <T>Collection<T> getAllFromClass(Class<T> clazz, List wheres , ConsultaData sort , List<Class> classTtoLoad , EnumReferenceMethod reference ) throws SQLException, NullPointerException {	
		return getAllFromClass(clazz, wheres, getBiggerConsultaDataPage(sort), classTtoLoad, reference);
	}
	
	public <T>Collection<T> getAllFromClass(Class<T> clazz, List wheres , ISenchaPage sort , List<Class> classTtoLoad , EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		Collection cols = getAllFromClass(clazz, wheres, sort);
		
		if(cols != null && cols.size() > 0 && mustLoadReferences((Vo)cols.iterator().next())) {
			loadAllReferences(cols, cols, classTtoLoad, reference);
		}
		
		return cols;
	}
	
	public <T>Collection<T> getAllFromClass(Class<T> clazz, List wheres ) throws SQLException, NullPointerException {
		return getAllFromClass(clazz, wheres, getEmptySort());
	}
	
	public <T>Collection<T> getAllFromClass(Class<T> clazz) throws SQLException, NullPointerException {
		return getAllFromClass(clazz, getEmptyWhere(), getEmptySort());
	}
	
	public <T> T getFromClass(Class<T> clazz, List wheres, ConsultaData sort , List<Class> classTtoLoad , EnumReferenceMethod refMethod) throws SQLException, NullPointerException {
		T t = null;
		
		Collection<T> collections = getAllFromClass(clazz, wheres, sort, classTtoLoad, refMethod);
		
		if(collections != null && collections.size() >=1){
			if(collections.size() > 1) {
				System.out.println("@@ ERROR La collection tiene mas de un objeto.");
			}
			else {
				Iterator ite = collections.iterator();
				t = (T)ite.next();
			}
		}
	
		return t;
	}
	
	public <T> T getFromClass(Class<T> class1, List<Where> wheres) throws NullPointerException, SQLException {
		return getFromClass(class1, wheres, getEmptySort(), null,  null);
	}
	
	public <T> T getFromClass(Class<T> clazz , List wheres, List<Class> classTtoLoad ) throws SQLException, NullPointerException {
		return getFromClass(clazz, wheres, getEmptySort(), classTtoLoad,  null);
	}
	
	public <T> T getFromClass(Class<T> clazz, List wheres , List<Class> classTtoLoad , EnumReferenceMethod refMethod) throws SQLException, NullPointerException {
		return getFromClass(clazz, wheres, getEmptySort(), classTtoLoad, refMethod);
	}
//	
//	public <T> T getFromClass(Class<T> clazz, List<Class> classTtoLoad , EnumReferenceMethod refMethod, Integer... paramPks) throws SQLException, NullPointerException {
//		return getFromClass(clazz, getEmptySort(), classTtoLoad, refMethod, paramPks);
//	}
//	
//	public <T> T getFromClass(Class<T> clazz, Integer... paramPks) throws SQLException, NullPointerException {
//		return getFromClass(clazz, getEmptySort(), null, null, paramPks);
//	}
//	
//	public <T> T getFromClass(Class<T> clazz, ConsultaData sort , List<Class> classTtoLoad , EnumReferenceMethod refMethod, Integer... paramPks) throws SQLException, NullPointerException {
//		T t = null;
//		
//		if(clazz != null && paramPks != null && paramPks.length > 0) {
//			List<Where> wheres = getWhereFromClass(clazz, paramPks);
//			t = getFromClass(clazz, wheres, sort, classTtoLoad, refMethod);
//		}
//	
//		return t;
//	}

	

}
