package portal.com.eje.tools;

import java.util.List;

import portal.com.eje.cache2.Cache;
import portal.com.eje.portal.factory.Util;

class EnumsToolsCached extends EnumsTools{

	public static EnumsToolsCached getInstance() {
		return Util.getInstance(EnumsToolsCached.class);
	}
	
	@SuppressWarnings({ "unchecked"  })
	@Override
	public <T> T getEnumByName(Class<? extends Enum<?>> clazz, String name, Object defaultValue) {
		Class<?>[] def = {Class.class, String.class, Object.class};
		Object[] params = {clazz, name, defaultValue};
		return (T) Cache.weak(getInstance(), "getEnumByName", def, params, Object.class, true);
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> T getEnumByToString(Class<? extends Enum<?>> clazz, String name, Object defaultValue) {
		Class<?>[] def = {Class.class, String.class, Object.class};
		Object[] params = {clazz, name, defaultValue};
		return (T) Cache.weak(getInstance(), "getEnumByToString", def, params, Object.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getArrayList(Class<? extends Enum<?>> clazz) {
		Class<?>[] def = {Class.class };
		Object[] params = {clazz };
		return (List<T>) Cache.weak(getInstance(), "getArrayList", def, params, Object.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getEnum(Class<? extends Enum<?>> clazz, String toString, Object defaultValue) {
		Class<?>[] def = {Class.class , String.class , Object.class};
		Object[] params = {clazz, toString , defaultValue};
		return (T) Cache.weak(getInstance(), "getEnum", def, params, Object.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getEnumByNameIngoreCase(Class<? extends Enum<?>> clazz, String name, Object defaultValue) {
		Class<?>[] def = {Class.class , String.class , Object.class};
		Object[] params = {clazz, name , defaultValue};
		return (T) Cache.weak(getInstance(), "getEnumByNameIngoreCase", def, params, Object.class, true);
	}

	public <T>Enum<?> getEnumByToStringIngoreCase(Class<? extends Enum<?>> clase, String valor)  {
		Class<?>[] def = {Class.class , String.class};
		Object[] params = {clase, valor };
		return Cache.weak(getInstance(), "getEnumByToStringIngoreCase", def, params, Enum.class, true);
	}
}
