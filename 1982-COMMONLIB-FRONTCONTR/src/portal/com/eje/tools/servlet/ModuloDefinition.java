package portal.com.eje.tools.servlet;

import java.util.List;

import cl.eje.model.generic.portal.Eje_generico_modulo;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;

public class ModuloDefinition implements TemplateMethodModel {
	public static ModuloDefinition getInstance() {
		return Util.getInstance(ModuloDefinition.class);
	}

	public ModuloDefinition() {

	}

	public boolean isEmpty() {
		return false;
	}

	public TemplateModel exec(List args) {
		SimpleScalar ret = null;

		String campo = (String) args.get(0);

		Eje_generico_modulo moduloDef = ParametroLocator.getInstance().getModuloDef(EModulos.getThisModulo());

		if (moduloDef != null) {
			ret = new SimpleScalar(ClaseConversor.getInstance().getObject(VoTool.getInstance().getFieldValue(moduloDef, campo), String.class));
		}

		return ret;
	}

}