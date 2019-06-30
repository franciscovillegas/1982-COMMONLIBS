package portal.com.eje.tools.servlet;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.WordUtils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import portal.com.eje.portal.factory.Util;

public class ToCamelCase implements TemplateMethodModel {
	private String empresa;

	public static ToCamelCase getInstance() {
		return Util.getInstance(ToCamelCase.class);
	}

	public ToCamelCase() {

	}

	public ToCamelCase(String empresa) {

		this.empresa = empresa;
	}

	public boolean isEmpty() {
		return false;
	}

	public TemplateModel exec(List args) {
		SimpleScalar ret = null;

		if (args != null && args.size() >= 1) {
			try {
				ret = new SimpleScalar(WordUtils.capitalizeFully((String) (args.get(0))));
			} catch (NullPointerException e) {
				ret = new SimpleScalar("");
			}
		}

		return ret;
	}

	private ResourceBundle prop;
}