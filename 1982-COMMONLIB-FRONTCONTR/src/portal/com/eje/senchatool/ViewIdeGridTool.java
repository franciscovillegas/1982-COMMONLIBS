package portal.com.eje.senchatool;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.senchatool.ifaces.IActionColumnTool;
import portal.com.eje.senchatool.ifaces.IGridTool;

class ViewIdeGridTool implements IGridTool{

	public static IGridTool getInstance() {
		return Util.getInstance(ViewIdeGridTool.class);
	}
	
	@Override
	public IActionColumnTool actionColumn() {
		return ViewIdeActionColumnsTool.getInstance();
	}
}
