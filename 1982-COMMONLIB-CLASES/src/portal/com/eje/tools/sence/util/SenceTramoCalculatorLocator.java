package portal.com.eje.tools.sence.util;

import portal.com.eje.tools.sence.ifaces.ISenceCalculator;

public class SenceTramoCalculatorLocator {

	public static ISenceCalculator getImplementacion() {
		return SenceTramoCalculatorImplDefault.getInstance();
	}
}
