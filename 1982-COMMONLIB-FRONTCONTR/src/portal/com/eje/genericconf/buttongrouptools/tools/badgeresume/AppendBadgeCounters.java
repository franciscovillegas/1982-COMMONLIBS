package portal.com.eje.genericconf.buttongrouptools.tools.badgeresume;

import java.util.List;

import cl.ejedigital.web.freemaker.sethash.SetHashAllFields;
import freemarker.template.SimpleHash;
import portal.com.eje.genericconf.buttongrouptools.IButtonGroupTool;
import portal.com.eje.genericconf.ifaces.IButton;

public class AppendBadgeCounters implements IButtonGroupTool {

	public void escapeToolValues(SimpleHash hash, List<IButton> botones ) {
		BadgeCounter counter = BadgeCounter.resume(botones);
		
		SetHashAllFields.getInstance().setDataFromVo(hash, counter);
	}
	
}
