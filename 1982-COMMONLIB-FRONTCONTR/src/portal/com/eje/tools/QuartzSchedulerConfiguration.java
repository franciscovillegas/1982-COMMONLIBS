package portal.com.eje.tools;

import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;

public class QuartzSchedulerConfiguration {
	private String paramNemo;
	
	public enum QuartzSchedulerConfigurationParam {
		printByConsole
	}
	public QuartzSchedulerConfiguration() {
		if(ParametroLocator.getInstance().getIDModulo()<=0) {
			try {
				paramNemo = QuartzScheduler.class.getCanonicalName();
				ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), paramNemo, "printByConsole", "false");
			} catch (PPMException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean getParamBoolean(QuartzSchedulerConfigurationParam param) {
		ParametroValue pv = ParametroLocator.getInstance().getValor(paramNemo, "printByConsole");
		
		if(pv != null && pv.getValue() != null) {
			return "true".equals(pv.getValue());
		}
		
		return false;
	}

}
