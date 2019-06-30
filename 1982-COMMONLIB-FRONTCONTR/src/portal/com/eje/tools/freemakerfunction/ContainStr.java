package portal.com.eje.tools.freemakerfunction;

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

public class ContainStr implements TemplateMethodModel {
	public static ContainStr getInstance() {
		return Util.getInstance(ContainStr.class);
	}

	public ContainStr() {

	}

	public boolean isEmpty() {
		return false;
	}

	public TemplateModel exec(List args) {
		SimpleScalar ret = new SimpleScalar("false");

		if(args != null && args.size() == 2) {
			String valor = (String) args.get(0);
			String instr = (String) args.get(1);	
			
			if(valor.contains(instr)) {
				ret = new SimpleScalar("true");
			}
		}
		
		return ret;
	}

}