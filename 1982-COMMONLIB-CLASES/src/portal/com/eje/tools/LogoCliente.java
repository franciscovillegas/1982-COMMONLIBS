package portal.com.eje.tools;

import freemarker.template.SimpleHash;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;

/**
 * Obtiene el logo desde el parámetro "cliente"
 * 
 * @author Pancho
 * @since 29-04-2019
 * */
public class LogoCliente {
	private String logoVarFreemaker = "logo";
	private String paramName = "cliente";
	private String paramKey = "logo";
	
	public static LogoCliente getInstance() {
		return Util.getInstance(LogoCliente.class);
	}
	
	public SimpleHash setLogoCliente(SimpleHash modelRoot, int empresa) {
		
		ParametroValue value = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), paramName , paramKey.concat(String.valueOf(empresa)));
		
		if(value != null) {
			String pathImagen = value.getBase64file();
			modelRoot.put(logoVarFreemaker, pathImagen);	
		}
		
		return modelRoot;
	}

}
