package portal.com.eje.tools.servlet;

import java.util.List;
import java.util.ResourceBundle;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import portal.com.eje.portal.factory.Util;

public class ToLowerCase implements TemplateMethodModel {
	private String empresa;

	public static ToLowerCase getInstance() {
		return Util.getInstance(ToLowerCase.class);
	}

	public ToLowerCase() {

	}

	public ToLowerCase(String empresa) {

		this.empresa = empresa;
	}

	public boolean isEmpty() {
		return false;
	}

	public TemplateModel exec(List args) {
		SimpleScalar ret = null;

		if (args != null && args.size() >= 1) {
			try {
				ret = new SimpleScalar(((String) (args.get(0))).toLowerCase());
			} catch (NullPointerException e) {
				ret = new SimpleScalar("");
			}
		}

		ret = new SimpleScalar("");
		return ret;
	}

	private ResourceBundle prop;
}