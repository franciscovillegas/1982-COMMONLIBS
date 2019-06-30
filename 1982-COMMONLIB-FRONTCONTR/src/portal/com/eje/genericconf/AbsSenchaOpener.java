package portal.com.eje.genericconf;

import java.util.ArrayList;
import java.util.List;

import portal.com.eje.genericbuttons.enums.EnumSenchaEmbuidoButtons;
import portal.com.eje.genericconf.ifaces.ISenchaOpener;

public class AbsSenchaOpener extends AbsButtonBootstrap implements ISenchaOpener{
	protected List<EnumSenchaEmbuidoButtons> buttonsIde;

	public AbsSenchaOpener() {
		setButtonsIde(new ArrayList<>());
		getButtonsIde().add(EnumSenchaEmbuidoButtons.CLOSE);
	}
	
	public List<EnumSenchaEmbuidoButtons> getButtonsIde() {
		return buttonsIde;
	}

	public void setButtonsIde(List<EnumSenchaEmbuidoButtons> buttonsIde) {
		this.buttonsIde = buttonsIde;
	}
	
	
	
}
