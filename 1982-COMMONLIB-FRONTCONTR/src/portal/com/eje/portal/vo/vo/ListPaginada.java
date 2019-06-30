package portal.com.eje.portal.vo.vo;

import java.util.ArrayList;

import cl.ejedigital.consultor.IMetaData;
import cl.ejedigital.consultor.IMetable;

public class ListPaginada<E> extends ArrayList<E> implements IMetable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IMetaData meta;
	
	@Override
	public IMetaData getMetaData() {
		return meta;
	}
	
	@Override
	public void setMetaData(IMetaData meta) {
		this.meta = meta;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
