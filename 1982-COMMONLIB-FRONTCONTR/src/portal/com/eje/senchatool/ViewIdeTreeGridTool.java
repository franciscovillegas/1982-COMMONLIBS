package portal.com.eje.senchatool;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.senchatool.ifaces.IActionColumnTool;
import portal.com.eje.senchatool.ifaces.ITreeGridTool;

class ViewIdeTreeGridTool implements ITreeGridTool{

	public static ITreeGridTool getInstance() {
		return Util.getInstance(ViewIdeTreeGridTool.class);
	}
	
	@Override
	public IActionColumnTool actionColumn() {
		return ViewIdeActionColumnsTool.getInstance();
	}
}
