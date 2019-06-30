package portal.com.eje.portal.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import portal.com.eje.portal.appcontext.MasterBean;
import portal.com.eje.portal.plugins.ifaces.IPgContainer;

@Controller
public class GLocator {

	private IPgContainer pgContainer;

	public IPgContainer getPgContainer() {
		return pgContainer;
	}

	@Autowired
	public void setPgContainer(IPgContainer pgContainer) {
		this.pgContainer = pgContainer;
	}

	public static IPgContainer getInstance() {
		// IPgContainer container =
		// AppContext.getBean(GabrielLocator.class).pgContainer;

		return MasterBean.getMasterBean(GLocator.class).pgContainer;
	}

}
