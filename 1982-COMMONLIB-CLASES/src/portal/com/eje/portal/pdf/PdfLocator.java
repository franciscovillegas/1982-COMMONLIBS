package portal.com.eje.portal.pdf;

import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;

public class PdfLocator {

	public static IPdf getInstance() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(PdfTool.class);
	}
}
