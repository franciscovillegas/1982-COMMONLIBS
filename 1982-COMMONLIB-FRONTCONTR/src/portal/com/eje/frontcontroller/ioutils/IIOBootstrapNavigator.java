package portal.com.eje.frontcontroller.ioutils;

import java.util.List;

import cl.eje.bootstrap.ifaces.INavigatorResource;
import freemarker.template.SimpleHash;
import portal.com.eje.genericconf.ifaces.IButton;

public interface IIOBootstrapNavigator {

	public void retNavigator(INavigatorResource nav, SimpleHash modelRoot, List<IButton> botones) throws Exception;
}