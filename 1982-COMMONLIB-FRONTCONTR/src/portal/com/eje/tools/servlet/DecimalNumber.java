package portal.com.eje.tools.servlet;

import java.util.List;

import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import portal.com.eje.portal.factory.Util;

public class DecimalNumber implements TemplateMethodModel {

	public static DecimalNumber getInstance() {
		return Util.getInstance(DecimalNumber.class);
	}

	public DecimalNumber() {

	}

	public boolean isEmpty() {
		return false;
	}

	public TemplateModel exec(List args) {
		SimpleScalar ret = null;
		/**
		 * Primer param el numero Segundo param la cantidad de decimales
		 */
		if (args != null && args.size() == 2) {
			try {
				double numero = Validar.getInstance().validarDouble((String) args.get(0), 0);
				int decimales = Validar.getInstance().validarInt((String) args.get(1), 0);
				ret = new SimpleScalar(Formatear.getInstance().toDecimalNumber(numero, decimales));

			} catch (NullPointerException e) {
				ret = new SimpleScalar("0");
			}

			return ret;
		}

		ret = new SimpleScalar("");
		return ret;
	}

}