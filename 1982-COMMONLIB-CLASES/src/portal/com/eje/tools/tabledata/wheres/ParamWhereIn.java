package portal.com.eje.tools.tabledata.wheres;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.util.Wheres;
import portal.com.eje.tools.tabledata.ifaces.IWhereParam;

public class ParamWhereIn implements IWhereParam{
	private final String PREFIJO_PARAMETRO_WHEREIN = "wherein_";

	@Override
	public List<Where> findWhere(Map<String, List<String>> params, IOClaseWeb io) {
		List<Where> wheres = new ArrayList<>();;
		
		for (String field : params.keySet()) {
			field = MyString.getInstance().getUniversalFromUTF8(field);
			
			if (field != null && field.startsWith(PREFIJO_PARAMETRO_WHEREIN)) {
				String columnNme = field.substring(PREFIJO_PARAMETRO_WHEREIN.length(), field.length());
				
				List<String> lista = io.getParamList2(field);
				
				if(lista != null && lista.size() > 0) {
					wheres.addAll(Wheres.where(columnNme, Wheres.IN, lista).build());	
				}
				 
			}
		}
		
		return wheres;
	}


}
