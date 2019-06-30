package cl.eje.bootstrap.abs;

import cl.eje.bootstrap.ifacetemplatesetter.EnumPanelType;
import cl.eje.bootstrap.ifacetemplatesetter.IPanel;

public class AbsPanel extends AbsTemplateSetter implements IPanel {


	private EnumPanelType panelType;
	
	public AbsPanel() {
		setHeader(true);
		setPanelType(EnumPanelType.DEFAULT);
	}

	public EnumPanelType getPanelType() {
		return panelType;
	}

	public void setPanelType(EnumPanelType panelType) {
		this.panelType = panelType;
	}

 
 
}
