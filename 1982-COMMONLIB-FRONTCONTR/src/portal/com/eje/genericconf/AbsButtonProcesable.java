package portal.com.eje.genericconf;

import portal.com.eje.genericconf.ifaces.IButtonProcesable;

public class AbsButtonProcesable extends AbsButtonBootstrapBadgeteable implements IButtonProcesable {
	private String spinnerIcon;

	public AbsButtonProcesable() {
		 
	}
	
	@Override
	public String getSpinnerIcon() {
		return spinnerIcon;
	}

	@Override
	public void setSpinnerIcon(String spinnerIcon) {
		this.spinnerIcon = spinnerIcon;
	}
	
	
	
}
