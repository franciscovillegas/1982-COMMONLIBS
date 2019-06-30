package portal.com.eje.frontcontroller.ioutils;

import java.util.List;

import cl.eje.bootstrap.ifaces.IContainerResource;
import cl.eje.bootstrap.ifaces.IPanelResource;
import cl.eje.bootstrap.ifacetemplatesetter.IPanel;
import freemarker.template.SimpleHash;

public interface IIOBootstrapContainer {

	public void retContainer(IContainerResource nav, SimpleHash modelRoot, List<IPanel> botones, IPanelResource formatoPanel) throws Exception;
 
}