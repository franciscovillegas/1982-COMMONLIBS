package portal.com.eje.genericbuttons.beans;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import portal.com.eje.genericconf.ifaces.IButtonBootstrap;
import portal.com.eje.portal.appcontext.MasterBean;
import portal.com.eje.portal.appcontext.ifaces.IBeansList;
import portal.com.eje.portal.factory.Util;

public class GenericAdmButtonsMasterBeans implements IBeansList {

	@Autowired(required=false)
	private List<IButtonBootstrap> buttons = null;
	
	public static GenericAdmButtonsMasterBeans getInstance() {
		return Util.getInstance(GenericAdmButtonsMasterBeans.class);
	}
	
	public List<IButtonBootstrap> getBeans() {
		return MasterBean.getMasterBean(GenericAdmButtonsMasterBeans.class).buttons;
	}
	
}
