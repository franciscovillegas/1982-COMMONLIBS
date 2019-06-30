package portal.com.eje.portal.vo;

import java.sql.Connection;
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
import portal.com.eje.portal.vo.util.InsertTool.EnumInsertMethod;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;
 

public class CtrTGeneric extends AbsVoPersisteEnBDTransaction {

	public static CtrTGeneric getInstance() {
		return Ctr.getInstance(CtrTGeneric.class);
	}
	
	
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz, List wheres, ISenchaPage sort) throws SQLException, NullPointerException {
		TableDefinition tableDef = VoTool.getInstance().getTableDefinition(clazz);
		
		if(tableDef == null) {
			throw new SQLException("No TableDefinition presente en "+clazz.getCanonicalName());
		}
		
		return privateGenericGetAll2(conn, clazz, clazz,wheres, sort, true);
	}

	@Override
	public boolean add(Connection conn, Object a) throws SQLException, NullPointerException {
		Double newId = null;
		if(a != null) {
			
			EnumInsertMethod method = InsertTool.getInsertMethod((Vo) a);
			switch (method) {
			case DEFAULT:
				newId = genericInsert(conn, (Vo) a);
				break;
			case NORMAL:
				genericInsertNormal(conn, (Vo) a);
				newId = 1D;
				break;
			case IDENTITY:
				newId = genericInsertIdentity(conn, (Vo)a);
				break;
			default:
				break;
			}
		 
		}
		
		return newId != null && newId > 0;
	}


	
	
	@Override
	public boolean upd(Connection conn, Object a) throws SQLException, NullPointerException {
		if(a != null) {
			TableDefinition tableDef = VoTool.getInstance().getTableDefinition(a.getClass());
			return genericUpdate(conn, (Vo) a) > 0;	
		}
		
		return false;
	}

	@Override
	public boolean del(Connection conn,Object a) throws SQLException, NullPointerException {
		if(a != null) {
			TableDefinition tableDef = VoTool.getInstance().getTableDefinition(a.getClass());
			return genericDelete(conn, (Vo) a) > 0;	
		}
		return false;
	}

	@Override
	public boolean exist(Connection conn, Object a) throws SQLException, NullPointerException {
		if(a != null) {
			TableDefinition tableDef = VoTool.getInstance().getTableDefinition(a.getClass());
			return genericExist(conn, (Vo) a);	
		}
		return false;
	}

	@Override
	@Deprecated
	public Collection getAll(Connection conn, List where, ConsultaData sort) throws SQLException, NullPointerException {
		throw new NotImplementedException();
	}
	
	public ConsultaData getDataFromClass(Connection conn, Class clazz, List wheres, ISenchaPage sort) throws SQLException, NullPointerException {
		TableDefinition tableDef = VoTool.getInstance().getTableDefinition(clazz);
		
		if(tableDef == null) {
			throw new SQLException("No TableDefinition presente en "+clazz.getCanonicalName());
		}
		
		return (ConsultaData) privateGenericGetAll2(conn, clazz, clazz, wheres, sort, false);
	}
	
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz, List wheres, ConsultaData sort) throws SQLException, NullPointerException {
		return getAllFromClass(conn, clazz, wheres, getBiggerConsultaDataPage(sort));
	}

	/**
	 * Debe indicarse las clases a cargar
	 * @deprecated
	 * */
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz, List wheres , EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		List<Class> classtoLoad = new ArrayList<Class>();
		classtoLoad.add(clazz);
		return getAllFromClass(conn, clazz, wheres, getEmptySort(), classtoLoad, reference);
	}
	
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz, List wheres, List<Class> classTtoLoad  , EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		return getAllFromClass(conn, clazz, wheres, getEmptySort(), classTtoLoad, reference);
	}
 
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz, List wheres , ConsultaData sort , List<Class> classtoLoad,EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		return getAllFromClass(conn, clazz, wheres, getBiggerConsultaDataPage(sort), classtoLoad, reference);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz, List wheres , ISenchaPage sort , List<Class> classtoLoad,EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		Collection cols = getAllFromClass(conn, clazz, wheres, sort);
		
		if(cols != null && cols.size() > 0 && mustLoadReferences((Vo)cols.iterator().next())) {
			loadAllReferences(conn, cols, cols, classtoLoad, reference);
		}
		
		return cols;
	}
	
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz, List wheres ) throws SQLException, NullPointerException {
		return getAllFromClass(conn, clazz, wheres, getEmptySort());
	}
	
	public <T>Collection<T> getAllFromClass(Connection conn, Class<T> clazz) throws SQLException, NullPointerException {
		return getAllFromClass(conn, clazz, getEmptyWhere(), getEmptySort());
	}
	
	public <T> T getFromClass(Connection conn, Class<T> clazz, List wheres, ConsultaData sort , List<Class> classTtoLoad , EnumReferenceMethod refMethod) throws SQLException, NullPointerException {
		T t = null;
		
		Collection<T> collections = getAllFromClass(conn, clazz, wheres, sort, classTtoLoad, refMethod);
		
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
	
	public <T> T getFromClass(Connection conn, Class<T> class1, List<Where> wheres) throws NullPointerException, SQLException {
		return getFromClass(conn, class1, wheres, getEmptySort(), null,  null);
	}
	
	public <T> T getFromClass(Connection conn, Class<T> clazz, List<Class> classTtoLoad , List wheres) throws SQLException, NullPointerException {
		return getFromClass(conn, clazz, wheres, getEmptySort(), classTtoLoad ,EnumReferenceMethod.EAGER);
	}
	
	public <T> T getFromClass(Connection conn, Class<T> clazz, List wheres, List<Class> classTtoLoad , EnumReferenceMethod refMethod) throws SQLException, NullPointerException {
		return getFromClass(conn, clazz, wheres, getEmptySort(), classTtoLoad ,refMethod);
	}
	
//	public <T> T getFromClass(Connection conn, Class<T> clazz, List<Class> classTtoLoad , EnumReferenceMethod refMethod, Object... paramPks) throws SQLException, NullPointerException {
//		return getFromClass(conn, clazz, getEmptySort(), classTtoLoad, refMethod, paramPks);
//	}
//	
//	public <T> T getFromClass(Connection conn, Class<T> clazz, Object... paramPks) throws SQLException, NullPointerException {
//		return getFromClass(conn, clazz, getEmptySort(), null, null, paramPks);
//	}
//	
//	public <T> T getFromClass(Connection conn, Class<T> clazz,  ConsultaData sort , List<Class> classTtoLoad , EnumReferenceMethod refMethod, Object... paramPks) throws SQLException, NullPointerException {
//		T t = null;
//		
//		if(clazz != null && paramPks != null && paramPks.length > 0) {
//			List<Where> wheres = getWhereFromClass(clazz, paramPks);
//			t = getFromClass(conn, clazz, wheres, sort, classTtoLoad, refMethod);
//		}
//	
//		return t;
//	}
}
