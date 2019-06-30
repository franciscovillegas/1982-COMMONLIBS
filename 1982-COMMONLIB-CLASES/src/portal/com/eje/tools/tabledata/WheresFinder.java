package portal.com.eje.tools.tabledata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.tools.paquetefactory.PaqueteFactory;
import portal.com.eje.tools.tabledata.ifaces.IWhereParam;

public class WheresFinder implements IWhereParam {
	private final String paquete = "portal.com.eje.tools.tabledata.wheres";
	private final List<IWhereParam> wheresFinders;

	public static WheresFinder getIntance() {
		return Util.getInstance(WheresFinder.class);
	}
	
	private WheresFinder() {
		wheresFinders = PaqueteFactory.getInstance().getObjects(paquete, IWhereParam.class);
	}
 
	@Override
	public List<Where> findWhere(Map<String, List<String>> params, IOClaseWeb io) {
		List<Where> wheres = new ArrayList<>();
		
		Map<String,List<String>> map = io.getMapParams();
		for(IWhereParam w : wheresFinders) {
			List<Where> find = w.findWhere(map, io);
			if(find != null) {
				wheres.addAll(find);
			}
		}
		
		return wheres;
	}
	
	
}
