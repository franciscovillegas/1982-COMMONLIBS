package portal.com.eje.tools.tabledata.wheres;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.ejedigital.tool.strings.MyString;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.util.Wheres;
import portal.com.eje.tools.tabledata.ifaces.IWhereParam;

public class ParamWhere implements IWhereParam {
	private final String PREFIJO_PARAMETRO_WHERE = "where_";
	
	public List<Where> findWhere(Map<String, List<String>> params, IOClaseWeb io) {
		
		
		List<Where> wheres = new ArrayList<>();;
		
		for (String field : params.keySet()) {
			field = MyString.getInstance().getUniversalFromUTF8(field);
			
			if (field != null && field.startsWith(PREFIJO_PARAMETRO_WHERE)) {
				String columnNme = field.substring(PREFIJO_PARAMETRO_WHERE.length(), field.length());
				List<String> value = params.get(field);
				
				if(value.size() == 1) {
					wheres.addAll(Wheres.where(columnNme, Wheres.EQUALS, value).build());	
				}
				else {
					wheres.addAll(Wheres.where(columnNme, Wheres.IN, value).build());
				}
				 
			}
		}
		
		return wheres;
	}
}
