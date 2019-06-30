package portal.com.eje.tools.tabledata.ifaces;

import java.util.List;
import java.util.Map;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.vo.util.Where;

public interface IWhereParam {

	public List<Where> findWhere(Map<String, List<String>> params, IOClaseWeb io);
}
