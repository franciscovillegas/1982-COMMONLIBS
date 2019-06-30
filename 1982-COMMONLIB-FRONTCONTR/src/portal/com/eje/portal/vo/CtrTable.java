package portal.com.eje.portal.vo;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;
 

public class CtrTable extends AbsVoPersisteEnBD<Vo> {

	@Override
	public <T> Collection<T> getAll(List<Where> where, ConsultaData sort) throws SQLException, NullPointerException {
		throw new NotImplementedException();
	}

	@Override
	public <T> boolean add(T a) throws SQLException, NullPointerException {
		throw new NotImplementedException();
	}

	@Override
	public <T> boolean upd(T a) throws SQLException, NullPointerException {
		throw new NotImplementedException();
	}

	@Override
	public <T> boolean del(T a) throws SQLException, NullPointerException {
		throw new NotImplementedException();
	}

	@Override
	public <T> boolean exist(T a) throws SQLException, NullPointerException {
		throw new NotImplementedException();
	}

}
