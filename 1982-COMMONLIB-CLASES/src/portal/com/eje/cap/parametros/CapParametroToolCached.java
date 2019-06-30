package portal.com.eje.cap.parametros;

import java.util.Collection;

import cl.eje.model.generic.cap.Eje_cap_parametro;
import portal.com.eje.cache2.Cache;
import portal.com.eje.cap.parametros.CapParametro.CapParametroGrupo;
import portal.com.eje.cap.parametros.ifaces.ICapParametroToolCached;
import portal.com.eje.portal.factory.Util;

public class CapParametroToolCached  extends CapParametroTool implements ICapParametroToolCached{

	public static CapParametroToolCached getInstance() {
		return Util.getInstance(CapParametroToolCached.class);
	}
	
	@Override
	public Integer getIdParametro(CapParametroGrupo grupo, String nemo)  {
		Class<?>[] def = {CapParametroGrupo.class, String.class};
		Object[] params = {grupo, nemo};
		return Cache.weak(this, "getIdParametro", def, params, Integer.class, true);
	}
	
	@Override
	public Integer getIdParametroNoVigente() {
		Class<?>[] def = {};
		Object[] params = {};
		return Cache.weak(this, "getIdParametroNoVigente", def, params, Integer.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Eje_cap_parametro> getParametros(CapParametroGrupo grupo, String value, boolean vigente) {
		Class<?>[] def = {CapParametroGrupo.class, String.class, boolean.class};
		Object[] params = {grupo, value, vigente};
		return Cache.weak(this, "getIdParametro", def, params, Collection.class, true);
	}
	
	@Override
	public Integer getIdParametroVigente() {
		Class<?>[] def = {};
		Object[] params = {};
		return Cache.weak(this, "getIdParametroVigente", def, params, Integer.class, true);
	}
	
	@Override
	public EEstadoAcciones getEstadoFromIdAccion(int idAccion) {
		Class<?>[] def = {int.class};
		Object[] params = {idAccion};
		return Cache.weak(this, "getEstadoFromIdAccion", def, params, EEstadoAcciones.class, true);
	}
}
