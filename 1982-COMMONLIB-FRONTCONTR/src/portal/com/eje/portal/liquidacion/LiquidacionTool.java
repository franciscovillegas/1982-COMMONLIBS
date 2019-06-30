package portal.com.eje.portal.liquidacion;

import portal.com.eje.portal.appcontext.MasterBean;


public class LiquidacionTool {

	private ILiquidacionTool beanLiqTool;

	public ILiquidacionTool getBeanLiqTool() {
		return beanLiqTool;
	}

	public void setBeanLiqTool(ILiquidacionTool beanLiqTool) {
		this.beanLiqTool = beanLiqTool;
	}

	public static ILiquidacionTool getInstance() {
		LiquidacionTool lt = MasterBean.getMasterBean(LiquidacionTool.class);
		
		return lt.beanLiqTool;
	}

}
