package portal.com.eje.portal.parametro;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import cl.eje.model.generic.portal.Eje_generico_modulo;
import cl.ejedigital.tool.misc.MappingFactory;
import cl.ejedigital.tool.misc.MappingLocator;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import cl.ejedigital.web.datos.error.DuplicateKeyException;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;

public class ParametroWithCache extends Parametro {
 
	
	ParametroWithCache() {
		super();
	}
	
	 
	@Override
	public List<ParametroValue> getValores(EModulos mods, String nemoParam) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getValores");
		
		String key = toString(mods) + "_nemoparam:" + nemoParam;
	 
		if(cache.get(key) == null || ((List<ParametroValue>)cache.get(key)).size() == 0 ) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null || ((List<ParametroValue>)cache.get(key)).size() == 0) {
					List<ParametroValue> lista = super.getValores(mods, nemoParam);
					cache.put(key, lista);
				}
			}
		}
		
		return (List<ParametroValue>)cache.get(key);
	}
	
	@Override
	public ParametroKey getParamKey(EModulos modulo, String keyValor) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getParamKey");
		
		String key = toString(modulo) + "_keyvalue:" + keyValor;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					ParametroKey lista = super.getParamKey(modulo, keyValor);
					cache.put(key, lista);
				}
			}
		}
		
		return (ParametroKey) cache.get(key);
	}
	
	@Override
	public String getCoreValue(EParametroCore p) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getCoreValue");
		
		String key = toString(p) ;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					String val = super.getCoreValue(p);
					cache.put(key, val);
				}
			}
		}
		
		return (String) cache.get(key);
	}
	
	@Override
	public Integer getIDModuloFromBD(EModulos modulo) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getIDModuloFromBD");
		
		String key = toString(modulo);
		
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					Integer val = super.getIDModuloFromBD(modulo);
					cache.put(key, val);
				}
			}
		}
		
		return (Integer) cache.get(key);
	}
	
	@Override
	public Integer getIDParamFromBD(Integer idMoldulo, String nemo) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getIDParamFromBD");
		
		String key = idMoldulo + "_" + nemo;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					Integer val = super.getIDParamFromBD(idMoldulo, nemo);
					cache.put(key, val);
				}
			}
		}
		
		return (Integer) cache.get(key);
	}
	
	@Override
	protected Map<String, Object> getModulo(String context) throws ModuloNotRecognizedException {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getModulo");
		
		String key = context;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					Map<String, Object> val = super.getModulo(context);
					cache.put(key, val);
				}
			}
		}
		
		return (Map<String, Object>) cache.get(key);
	}
	
	@Override
	public ParametroValue getValor(String nemoParam, String keyParam) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getValor");
		
		String key = nemoParam + "_"+ keyParam;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					ParametroValue val = super.getValor(nemoParam, keyParam);
					cache.put(key, val);
				}
			}
		}
		
		return (ParametroValue) cache.get(key);

	}
	
	@Override
	public ParametroValue getValor(EModulos mods, String nemoParam, String keyParam) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getValor");
		
		String key = toString(mods) + "_" + nemoParam + "_"+ keyParam;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					ParametroValue val = super.getValor(mods, nemoParam, keyParam);
					cache.put(key, val);
				}
			}
		}
		
		return (ParametroValue) cache.get(key);

	}
 
	@Override
	public List<ParametroValue> getValores(EModulos mods, int idCliente, String nemoParam) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getValor");
		
		String key =  toString(mods)  + "_" + idCliente + "_"+ nemoParam;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					List<ParametroValue> val = super.getValores(mods, idCliente, nemoParam);
					cache.put(key, val);
				}
			}
		}
		
		return (List<ParametroValue>) cache.get(key);
		
	}
	
	@Override
	public List<ParametroValue> getValores(String nemoParam) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getValores");
		
		String key = nemoParam;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					List<ParametroValue> val = super.getValores(nemoParam);
					cache.put(key, val);
				}
			}
		}
		
		return (List<ParametroValue>) cache.get(key);
		
	}
	
	public ParametroValue getValue(EModulos modulo, String param) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getValue");
		
		String key =  toString(modulo) +"_"+param;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					ParametroValue val = super.getValue(modulo, param);
					cache.put(key, val);
				}
			}
		}
		
		return (ParametroValue) cache.get(key);
	}
	
	public List<ParametroValue> getValues(EModulos modulo, String param) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.getValues(EModulos,String)");
		
		String key = toString(modulo) +"_"+param;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					List<ParametroValue> val = super.getValues(modulo, param);
					cache.put(key, val);
				}
			}
		}
		
		return (List<ParametroValue>) cache.get(key);
	}
	
	@Override
	public void ifNotExistThenBuildParametro(EModulos modulo, String paramNemo) throws PPMException {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.ifNotExistThenBuildParametro(EModulos,String)");
		
		String key = toString(modulo)+"_"+paramNemo;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					super.ifNotExistThenBuildParametro(modulo, paramNemo);
					cache.put(key, "void");
				}
			}
		}
		
		cache.get(key);
	}
	
	@Override
	public void ifNotExistThenBuildParametro(EModulos modulo, String paramNemo, String valueKey, String value) throws PPMException {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.ifNotExistThenBuildParametro(EModulos,String,String,String)");
		
		String key = toString(modulo)+"_"+paramNemo+"_"+valueKey+"_"+value;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					super.ifNotExistThenBuildParametro(modulo, paramNemo, valueKey, value);
					cache.put(key, "void");
				}
			}
		}
		
		cache.get(key);
	}
	
	@Override
	public void ifNotExistThenBuildParametro(List<EModulos> modulos, String paramNemo, String valueKey, String valueDefault) throws PPMException {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.ifNotExistThenBuildParametro(List<EModulos>,String,String,String)");
		
		
		String key = toString(modulos)+"_"+paramNemo+"_"+valueKey+"_"+valueDefault;
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					super.ifNotExistThenBuildParametro(modulos, paramNemo, valueKey, valueDefault);
					cache.put(key, "void");
				}
			}
		}
		
		cache.get(key);
	}
	
	private String toString(List<EModulos> mods) {
		StringBuilder str = new StringBuilder();
		
		if(mods != null) {
			for(EModulos e : mods) {
				str.append(e.getContexo()).append("_");
			}
		}

		return str.toString();
	}
	
	private String toString(EModulos mods) {
		return  mods != null ? mods.getContexo() : "" ;
	}
	
	private String toString(EParametroCore c) {
		return String.valueOf(c);
 
	}

	@Override
	public void recognizeIDCliente(ServletContext sc) {
		Map cache = MappingFactory.getFactory(ParametroWithCache.class).getMapping("ParametroWithCache.recognizeIDCliente(ServletContext sc)");
		
		if(sc == null) {
			return;
		}
		String key = sc.getServletContextName();
		
		if(cache.get(key) == null) {
			synchronized (ParametroWithCache.class) {
				if(cache.get(key) == null) {
					super.recognizeIDCliente(sc);
					cache.put(key, "void");
				}
			}
		}

	}
	
	public List<EModulos> getModulos(boolean vigente) {
		List<EModulos> modulos = null;
		try {
			modulos = Cache.weak(this , "getModulos", new Class[] {boolean.class}, new Object[] {vigente}, List.class, true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modulos;
	}
	
	public List<String> getJndis(EModulos modulo) {
		List<String> lista = null;
		try {
			lista = Cache.weak(this , "getJndis", new Class[] {EModulos.class}, new Object[] {modulo}, List.class, true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
	}
	
	public List<String> getNemoParams(EModulos modulo) {
		List<String> lista = null;
		try {
			lista = Cache.weak(this, "getNemoParams", new Class[] {EModulos.class}, new Object[] {modulo}, List.class, true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
	}
	
	@Override
	public Eje_generico_modulo getModuloDef(EModulos modulo) {
		Eje_generico_modulo retorno = null;
		try {
			retorno = Cache.weak(this, "getModuloDef", new Class[] {EModulos.class}, new Object[] {modulo}, Eje_generico_modulo.class, true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	@Override
	public Map<String, ParametroValue> getMapParamValues(String paramNemo) throws DuplicateKeyException{
		
		Map<String, ParametroValue> retorno = null;
		
		try {
			Class[] def = {String.class};
			Object[] params = {paramNemo};
			
			retorno = Cache.weak(this, "getMapParamValues", def, params, Map.class, true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}
	
}
