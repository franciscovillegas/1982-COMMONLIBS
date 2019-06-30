package portal.com.eje.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import portal.com.eje.portal.factory.Util;

/**
 
 * @author Pancho
 * @since 22-04-2019
 * */
public class ClaseDetector {

	public static ClaseDetector getInstance() {
		return Util.getInstance(ClaseDetector.class);
	}
	
	/**
	 * */
	public List<Class<?>> getClassInPackage(String vpaquete, Class<?> interfaze) {

		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		interfaces.add(interfaze);

		return getClassInPackage(vpaquete, interfaces);
	}
	
	
	
	/**
	 * @author Pancho
	 * @since 22-04-2019
	 * */
	public List<Class<?>> getClassInPackages(String[] vpaquetes, Class<?> interfaze) {
		List<Class<?>> retorno = new ArrayList<Class<?>>();
		for(String paquete : vpaquetes) {
			retorno.addAll(getClassInPackage(paquete, interfaze));
		}
		
		return retorno;
	}

	public List<Class<?>> getClassInPackage(String vpaquete) {
		List<Class<?>> interfaces = new ArrayList<Class<?>>();

		return getClassInPackage(vpaquete, interfaces);
	}

	public List<Class<?>> getClassInPackage(List<String> paquetes, List<Class<?>> interfaces) {
		List<Class<?>> retorno = new ArrayList<>();
		for(String p : paquetes) {
			List<Class<?>> clases = getClassInPackage(p, interfaces);
			if(clases != null && clases.size() > 0) {
				retorno.addAll(clases);
			}
		}
		
		return retorno;
	}
	public List<Class<?>> getClassInPackage(String vpaquete, List<Class<?>> interfaces) {
		List<Class<?>> encontradas = new ArrayList<Class<?>>();

		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

		provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

		final Set<BeanDefinition> classes = provider.findCandidateComponents(vpaquete);

		for (BeanDefinition bean : classes) {

			Class<?> clazz = null;
			try {
				clazz = Class.forName(bean.getBeanClassName());
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if(clazz != null) {
				boolean esInterfaz = true;

				for (Class<?> interfaze : interfaces) {
					esInterfaz &= interfaze.isAssignableFrom(clazz);
				}

				if (esInterfaz) {
					encontradas.add(clazz);
				}
			}

		}

		return encontradas;
	}
	
	public <T>List<Class<T>> filtraLista(List<Class<?>> lista, Class<T> inter) {
		List<Class<T>> filtro = new ArrayList<Class<T>>();
		filtro.add(inter);
		
		return filtraLista(lista, filtro);
	}
	
	@SuppressWarnings("unchecked")
	public <T>List<Class<T>> filtraLista(List<Class<?>> lista, List<Class<T>> filtro) {
		List<Class<T>> encontradas = new ArrayList<Class<T>>();

		if(lista != null ) {
			for (Class<?> clase : lista) {
				boolean esInterfaz = true;

				for (Class<?> interfaze : filtro) {
					esInterfaz &= interfaze.isAssignableFrom(clase);
				}

				if (esInterfaz) {
					encontradas.add((Class<T>) clase);
				}

			}
		}
		

		return encontradas;
	}
	
	/**
	 * Retorna una Lista de Objectos Ya 
	 * */
	public List<Object> getClassInPackageAndBuild(String vpaquete, Class<?> interfaze) {

		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		interfaces.add(interfaze);

		List<Class<?>> clases = getClassInPackage(vpaquete, interfaces);
		
		return buildAll(clases);
	}
	
	private List<Object> buildAll(List<Class<?>> clases) {
		List<Object> retorno = new ArrayList<>();
		if(clases != null) {
			for(Class<?> c : clases) {
				retorno.add(ClaseGenerica.getInstance().getNewFromClass(c));
			}	
		}
		
		return retorno;
		
	}
}
