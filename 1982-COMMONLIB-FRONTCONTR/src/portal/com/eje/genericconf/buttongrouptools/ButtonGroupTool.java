package portal.com.eje.genericconf.buttongrouptools;

import java.util.List;

import freemarker.template.SimpleHash;
import portal.com.eje.genericconf.ifaces.IButton;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.tools.paquetefactory.PaqueteFactory;

public class ButtonGroupTool implements IButtonGroupTool {

	public static ButtonGroupTool getIntance() {
		return Weak.getInstance(ButtonGroupTool.class);
	}
	
	private final String paquete = "portal.com.eje.genericconf.buttongrouptools.tools";
	private List<IButtonGroupTool> tools;
	
	private ButtonGroupTool() {
		tools = PaqueteFactory.getInstance().getObjects(paquete, IButtonGroupTool.class);
	}

	@Override
	public void escapeToolValues(SimpleHash hash, List<IButton> botones) {
		if(tools != null && hash != null && botones != null) {
			for(IButtonGroupTool tool : tools) {
				tool.escapeToolValues(hash, botones);
			}
		}
	}
	
	
	
}
