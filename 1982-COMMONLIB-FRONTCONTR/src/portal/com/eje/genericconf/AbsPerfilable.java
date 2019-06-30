package portal.com.eje.genericconf;

import portal.com.eje.genericconf.ifaces.IObjetoPerfilable;
import portal.com.eje.portal.generico_appid.EnumAppId;

public class AbsPerfilable extends AbsObjetable implements IObjetoPerfilable {
	private EnumAppId appIdRequerido;
	private boolean visible;
	
	public AbsPerfilable() {
		setAppIdRequerido(EnumAppId.ADM);
		setVisible(true);
	}
	
		 
	public EnumAppId getAppIdRequerido() {
		return appIdRequerido;
	}
	public void setAppIdRequerido(EnumAppId appIdRequerido) {
		this.appIdRequerido = appIdRequerido;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
