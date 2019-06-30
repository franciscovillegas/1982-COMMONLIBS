package portal.com.eje.portal.vo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.vo.AbsVoPersiste.EnumReferenceMethod;

/**
 * Pancho 06-03-2019: Una clase que primuebe getConnection en cada método es una clase que promueve el error
 * 
 * @deprecated
 * */
public class CtrMacGeneric {

	public static CtrMacGeneric getInstance() {
		return Ctr.getInstance(CtrMacGeneric.class);
	}
	
	public <T>Collection<T> getAllFromClass(EModulos modulo, Class<T> clazz, List wheres) throws SQLException, NullPointerException {
		
		Connection conn = getConnection(modulo);
		try {
			return CtrTGeneric.getInstance().getAllFromClass(conn, clazz, wheres);
		}
		finally {
			freeConnection(modulo, conn);
		}
	}
	
	public <T>Collection<T> getAllFromClass(EModulos modulo, Class<T> clazz, List wheres, List<Class> classTtoLoad  , EnumReferenceMethod reference ) throws SQLException, NullPointerException {
		Connection conn = getConnection(modulo);
		try {
			return CtrTGeneric.getInstance().getAllFromClass(conn, clazz, wheres, classTtoLoad, reference);
		}
		finally {
			freeConnection(modulo, conn);
		}
	}
	
	
	private Connection getConnection(EModulos modulo) {
		return DBConnectionManager.getInstance().getConnection(modulo, "mac");
	}
	
	private void freeConnection(EModulos modulo, Connection conn) {
		 DBConnectionManager.getInstance().freeConnection(modulo, "mac", conn);
	}
}
