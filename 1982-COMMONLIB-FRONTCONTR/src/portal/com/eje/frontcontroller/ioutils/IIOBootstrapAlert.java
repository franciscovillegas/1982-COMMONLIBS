package portal.com.eje.frontcontroller.ioutils;

import cl.eje.bootstrap.alert.IAlert;
import cl.eje.bootstrap.alert.IAlertResource;
import freemarker.template.SimpleHash;

public interface IIOBootstrapAlert {

	public void retAlert(IAlertResource alert, SimpleHash modelRoot, IAlert alerta) throws Exception;
}