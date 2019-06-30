package portal.com.eje.genericconf.buttongrouptools;

import java.util.List;

import freemarker.template.SimpleHash;
import portal.com.eje.genericconf.ifaces.IButton;

public interface IButtonGroupTool {

	public void escapeToolValues(SimpleHash hash, List<IButton> botones );
	
}
