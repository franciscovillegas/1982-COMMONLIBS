package portal.com.eje.frontcontroller.util;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import cl.eje.view.sencha.Version;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.EnumTool;

public class ServicioFromAZoneUtil implements IServicioGetter {
	private String classAzoneUtil;

	public ServicioFromAZoneUtil(String classAzoneUtil) {
		this.classAzoneUtil = classAzoneUtil;
	}

	public ServicioFromAZoneUtil(HttpServletRequest req) {
		this.classAzoneUtil = getPathClass(req);
	}

	private String getParameter(HttpServletRequest req, String paramName) {
		String retorno = null;
		if (req != null) {
			retorno = req.getParameter(paramName);
		}

		return retorno;
	}

	private Version getVersion(HttpServletRequest req) {
		String modulo = getParameter(req, "modulo");
		Version v = null;
		if(modulo != null) {
			String version = modulo.split("_")[0];
			v = EnumTool.getEnumByNameIngoreCase(Version.class, version, Version.PORDEFECTO);
		}
		
		return v;
	}

	private String getPathClass(HttpServletRequest req) {

		String pathobjeto = null;
		Version v = getVersion(req);

		if (v != null) {
			pathobjeto = new StringBuilder().append(v.getPaquete()).append(".").append(getParameter(req, "modulo")).append(".").append(getParameter(req, "thing")).toString();
		}

		return pathobjeto;

	}

	@Override
	public Object getServicio(IOClaseWeb io) {
		String clase = classAzoneUtil;

		Class<?>[] consDefClass = {};
		Object[] consParams = {};

		Object objeto = null;

		try {
			if(clase == null) {
				throw new ClassNotFoundException("No existe el servicio null");
			}
			objeto = ClaseGenerica.getInstance().getNew(clase, consDefClass, consParams);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block

		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return objeto;
	}

}
