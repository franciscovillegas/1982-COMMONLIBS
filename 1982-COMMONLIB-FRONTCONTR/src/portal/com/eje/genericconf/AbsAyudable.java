package portal.com.eje.genericconf;

import portal.com.eje.genericconf.ifaces.IAyudable;

public class AbsAyudable extends AbsPerfilable implements IAyudable {

	private String tooltip;

	public AbsAyudable() {
		tooltip = "";
	}
	
	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	
	
}
