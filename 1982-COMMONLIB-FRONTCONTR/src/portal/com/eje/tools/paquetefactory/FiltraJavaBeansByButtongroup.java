package portal.com.eje.tools.paquetefactory;

import portal.com.eje.genericconf.ifaces.IButton;
import portal.com.eje.portal.administradorportal.IButtonGroup;
import portal.com.eje.tools.paquetefactory.IFiltroJavaBean;

public class FiltraJavaBeansByButtongroup implements IFiltroJavaBean<IButton> {

	private IButtonGroup group;

	public FiltraJavaBeansByButtongroup(IButtonGroup group) {
		super();
		this.group = group;
	}

	@Override
	public boolean isAppend(IButton iface) {
		return iface.getGroup() == this.group;
	}

}