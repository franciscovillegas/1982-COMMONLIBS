package portal.com.eje.portal.intranetmain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cl.ejedigital.web.datos.Order;
import portal.com.eje.genericconf.ifaces.IButtonBootstrap;
import portal.com.eje.portal.appcontext.MasterBean;
import portal.com.eje.tools.sortcollection.VoSort;


public class IntranetLinksApps {

	@Autowired(required=false)
	private final List<IButtonBootstrap> apps = null;
	
	public static List<IButtonBootstrap> getBeans() {
		List<IButtonBootstrap> beans = MasterBean.getMasterBean(IntranetLinksApps.class).apps;
		if(beans != null) {
			VoSort.getInstance().sortByMethodValue(beans, "position", Integer.class, Order.Ascendente);
		}
		
		return beans;
	}
}
