package portal.com.eje.portal.vo;

import java.sql.SQLException;
import java.util.List;

import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.vo.vo.Vo;

public class CtrGenericBatch extends AbsVoPersisteBatch {

	public static CtrGenericBatch getInstance() {
		return Ctr.getInstance(CtrGenericBatch.class);
	}
	
	public List<Boolean> exist(List<? extends Vo> cols) throws SQLException, NullPointerException{
		
		return CtrTGenericBatch.getInstance().exist(null, cols);
	}
	
	public boolean updOrAdd(List<? extends Vo> cols) throws NullPointerException, SQLException {
		return CtrTGenericBatch.getInstance().updOrAdd(null, cols);
	}
	
	public boolean del(List<? extends Vo> cols) throws SQLException {
		return CtrTGenericBatch.getInstance().del(null, cols);
	}
}
