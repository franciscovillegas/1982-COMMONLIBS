package portal.com.eje.frontcontroller.ioutils;

import cl.eje.bootstrap.ifaces.IPanelResource;
import cl.eje.bootstrap.ifacetemplatesetter.IPanel;
import freemarker.template.SimpleHash;

public interface IIOBootstrapPaneles {

	public void retPanel(IPanelResource container, SimpleHash modelRoot, IPanel panel) throws Exception;
	
}
