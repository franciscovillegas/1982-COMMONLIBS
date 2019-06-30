package portal.com.eje.tools.paquetefactory;

import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.web.datos.Order;
import portal.com.eje.genericconf.ifaces.IObjetoPerfilable;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.generico_appid.EnumAppId;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.ClaseDetector;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.sortcollection.VoSort;

/**
 * Retorna objetos ubicados en paquetes
 * 
 * @since 27-05-2019
 * */
public class PaqueteFactory {
	
	public static PaqueteFactory getInstance() {
		return PaqueteFactoryCached.getInstance();
	}

	/**
	 * Retorna una Lista de Objectos<br/>
	 * Nunca retorno null
	 * */
	public <T>List<T> getObjects(String vpaquete, Class<T> interfaze) {
		return getObjects(vpaquete, interfaze, null);
	}
	
	/**
	 * Obtiene todos los objetos que están en un paquete y también los ordena ascendentemente
	 * @author Pancho
	 * @since 28-05-2019
	 * */
 
	public <T>List<T> getObjects(String vpaquete, Class<T> interfaze, IFiltroJavaBean<T> filtro) {
		return getObjects(null, vpaquete, interfaze, filtro); 
	}
	
	/**
	 * Obtiene todos los objetos que están en un paquete<br/>
	 * <ul>
	 * 	<li>Los ordena ascendentemente si es que tiene el método position</li>
	 *  <li>Solo retornará aquellos objetos que se tenga el privilegio, para determina esto el objeto o algunos de sus padres deben haber implementado la interfaz IObjetoPerfilable</li>
	 * </ul>
	 * @author Pancho
	 * @since 28-05-2019
	 * */
	@SuppressWarnings("unchecked")
	public <T>List<T> getObjects(Usuario usuario, String vpaquete, Class<T> interfaze) {
		return getObjects(usuario, vpaquete, interfaze, null);
	}
 
	public <T>List<T> getObjects(Usuario usuario, String vpaquete, Class<T> interfaze, IFiltroJavaBean filtro) {
		List<String> vpaquetes = new ArrayList<>();
		vpaquetes.add(vpaquete);
		
		return getObjects(usuario, vpaquetes, interfaze, filtro);
	}
	/**
	 * Obtiene todos los objetos que están en un paquete<br/>
	 * <ul>
	 * 	<li>Los ordena ascendentemente si es que tiene el método position</li>
	 *  <li>Solo retornará aquellos objetos que se tenga el privilegio, para determina esto el objeto o algunos de sus padres deben haber implementado la interfaz IObjetoPerfilable</li>
	 * </ul>
	 * @author Pancho
	 * @since 28-05-2019
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T>List<T> getObjects(Usuario usuario, List<String> vpaquetes, Class<T> interfaze, IFiltroJavaBean filtro) {

		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		interfaces.add(interfaze);

		List<Class<?>> clases = ClaseDetector.getInstance().getClassInPackage(vpaquetes, interfaces);
		
		List<T> encontrados = (List<T>)buildAll(clases, interfaze);
		encontrados = retainsAllByPerfil(usuario, encontrados);
		
		List<T> retorno = new ArrayList<>(); 
		
		if(filtro != null) {
			for(T t : encontrados) {
				if( t != null && filtro.isAppend(t)) {
					retorno.add(t);
				}
			}
		}
		else {
			retorno.addAll(encontrados);
		}
		
		if(tienenMetodoName(retorno, "getPosition")) {
			VoSort.getInstance().sortByMethodValue(retorno, "position", Integer.class, Order.Ascendente);
		}
		
		return retorno;
	}
	
	private <T>List<T> retainsAllByPerfil(Usuario usuario, List<T> encontrados) {
		List<T> retorno = new ArrayList<>();
		
		if(usuario != null && encontrados != null && !encontrados.isEmpty()) {
			List<EnumAppId> privilegios = usuario.getAppIds();
			
			if(usuario != null && encontrados != null && encontrados.size()>0) {
				for(T t : encontrados) {
					boolean tieneAcceso = false;
					if(IObjetoPerfilable.class.isAssignableFrom(t.getClass())) {
						EnumAppId req = ((IObjetoPerfilable) t).getAppIdRequerido() ;
						tieneAcceso = (req == null || privilegios.contains(req ));
					}
				 
					if(tieneAcceso) {
						retorno.add(t);
					}
				}
			}
		}
		else if(encontrados != null && !encontrados.isEmpty()) {
			retorno.addAll(encontrados);
		}

		
		return retorno;
	}
	
	private boolean tienenMetodoName(List<?> objetos, String metodoName) {
		Class<?>[] def = {};
		
		if(objetos != null && metodoName != null && def != null) {
			for(Object o : objetos ) {
				
				boolean tiene = VoTool.getInstance().existMethod(metodoName, o);
				
				if(!tiene) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	
	public <T>List<Object> buildAll(List<Class<?>> clases , Class<T> tipo) {
		List<Object> retorno = new ArrayList<>();
		if(clases != null) {
			for(Class<?> c : clases) {
				retorno.add(Weak.getInstance(c));
			}	
		}
		
		return retorno;
		
	}
}
