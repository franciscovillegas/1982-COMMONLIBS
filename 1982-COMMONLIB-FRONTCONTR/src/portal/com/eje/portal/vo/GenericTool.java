package portal.com.eje.portal.vo;

import java.util.Collection;
import java.util.List;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.errors.NotSameClassException;
import portal.com.eje.portal.vo.vo.Vo;

public class GenericTool {

	public static GenericTool getInstance() {
		return Util.getInstance(GenericTool.class);
	}
	
	/**
	 * retorna false si hay más de un tipo de objeto en el Collection
	 * 
	 * @since 24-10-2018
	 * @author Pancho
	 * */
	protected boolean isSameObject(List<? extends Vo> vos) throws NotSameClassException {
		boolean isSame = true;
		
		if(vos != null) {
			Class<?> clase = null;
			for(Vo vo : vos) {
				if(clase == null) {
					clase = vo.getClass();
				}
				
				if(vo.getClass() != clase) {
					throw new NotSameClassException();
				}
			}
		}
		
		return isSame;
	}
	
}
