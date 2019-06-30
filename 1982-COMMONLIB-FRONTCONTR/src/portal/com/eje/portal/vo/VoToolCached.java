package portal.com.eje.portal.vo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import portal.com.eje.cache2.Cache;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.vo.Vo;

 

class VoToolCached extends VoTool {
	protected final Logger logger = Logger.getLogger(getClass());
	
	public static VoToolCached getInstance() {
		return Util.getInstance(VoToolCached.class);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Method> getAllGetMethodsWithNoParameters_map(Class<?> c) {
		Class<?>[] def = {Class.class};
		Object[] params = {c};
		return Cache.weak(this, "getAllGetMethodsWithNoParameters_map", def, params, Map.class, true);
	}
	
	@Override
	public boolean existMethod(String metodoName, Object o) {
		Class<?>[] def = {String.class, Object.class};
		Object[] params = {metodoName, o};
		return Cache.weak(this, "existMethod", def, params, Boolean.class, true);
	}
	
	@Override
	public String getMetodNameWithoutSetOrGetOrIsLowerCase(Method m) {
		Class<?>[] def = {Method.class};
		Object[] params = {m};
		return Cache.weak(this, "getMetodNameWithoutSetOrGetOrIsLowerCase", def, params, String.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGetsFieldsWithNoParameters(@SuppressWarnings("rawtypes") Class c) {
		Class<?>[] def = {Class.class};
		Object[] params = {c};
		return Cache.weak(this, "getGetsFieldsWithNoParameters", def, params, List.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Method> getGetsMethodsWithNoParameters(@SuppressWarnings("rawtypes") Class c) {
		Class<?>[] def = {Class.class};
		Object[] params = {c};
		return Cache.weak(this, "getGetsMethodsWithNoParameters", def, params, List.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTablePks(Class<? extends Vo> vo) {
		Class<?>[] def = {Class.class};
		Object[] params = {vo};
		List<String> retorno = Cache.weak(this, "getTablePks", def, params, List.class, true);
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<PrimaryKeyDefinition>> getTablePksMap(Class<? extends Vo> vo) {
		Class<?>[] def = {Class.class};
		Object[] params = {vo};
		return Cache.weak(this, "getTablePksMap", def, params, Map.class, true);
	}
}