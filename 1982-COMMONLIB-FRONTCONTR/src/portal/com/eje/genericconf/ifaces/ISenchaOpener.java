package portal.com.eje.genericconf.ifaces;

import java.util.List;

import portal.com.eje.genericbuttons.enums.EnumSenchaEmbuidoButtons;

public interface ISenchaOpener {

	public List<EnumSenchaEmbuidoButtons> getButtonsIde();

	public void setButtonsIde(List<EnumSenchaEmbuidoButtons> buttonsIde);
	
}
