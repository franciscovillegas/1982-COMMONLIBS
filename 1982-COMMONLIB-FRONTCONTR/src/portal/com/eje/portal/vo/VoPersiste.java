package portal.com.eje.portal.vo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.vo.Vo;

public class VoPersiste {

	public static VoPersiste getInstance() {
		return Util.getInstance(VoPersiste.class);
	}
 
	/**
	 * IO viene con dos parámetros del tipo ConsultaData, (@param name)_upd y (@param name)_del, estos serán persistidos
	 * @author Pancho
	 * @throws SQLException 
	 * @throws NullPointerException 
	 * @since 29-08-2018
	 * */
	@SuppressWarnings("unchecked")
	public boolean persiste(IOClaseWeb io, String name, Class<? extends Vo> classDef) throws NullPointerException, SQLException {
		return persiste(io, null, name, classDef);
	}
	/**
	 * IO viene con dos parámetros del tipo ConsultaData, (@param name)_upd y (@param name)_del, estos serán persistidos
	 * @author Pancho
	 * @throws SQLException 
	 * @throws NullPointerException 
	 * @since 29-08-2018
	 * */
	@SuppressWarnings("unchecked")
	public boolean persiste(IOClaseWeb io, Connection conn, String name, Class<? extends Vo> classDef) throws NullPointerException, SQLException {
		if(io != null && name != null) {//io.printParams();
			Collection<Vo> dataDel = (Collection<Vo>) io.getParamCollection(name+"_del", classDef);
			Collection<Vo> dataUpd = (Collection<Vo>) io.getParamCollection(name+"_upd", classDef);
			
			if(dataDel != null && dataUpd != null) {
				List<Class<? extends Vo>> classToDelete = CtrGeneric.getInstance().getAllReferences(classDef);
				
				if(conn != null) {
					CtrTGeneric.getInstance().del(conn, dataDel, classToDelete);
					CtrTGeneric.getInstance().updOrAdd(conn, dataUpd);
				}
				else {
					CtrGeneric.getInstance().del(dataDel, classToDelete);
					CtrGeneric.getInstance().updOrAdd(dataUpd);					
				}
				
				return true;
			}
		}
		
		return false;
	}

}
