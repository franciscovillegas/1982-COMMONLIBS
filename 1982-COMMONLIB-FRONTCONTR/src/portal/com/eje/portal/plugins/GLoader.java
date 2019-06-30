package portal.com.eje.portal.plugins;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import cl.eje.helper.AZoneUtil;
import cl.eje.helper.EnumAccion;
import portal.com.eje.portal.plugins.ifaces.IInjectable;
import portal.com.eje.portal.plugins.ifaces.IPgContainer;
import portal.com.eje.portal.plugins.ifaces.ITool;
import portal.com.eje.portal.plugins.vo.InjectablePluginVo;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

/**
 * Iniciador de AZoneUtil y metodos de ITool
 * 
 * @author Pancho
 * @since 22-04-2019
 */

public class GLoader {
	private static Logger logger = Logger.getLogger(GLoader.class);
	/**
	 * Se encarga de llamar el AzoneUtil ubicado en los plugins Activados o en el
	 * plugin en desarrollo (unicado en Src)
	 * 
	 * @author Pancho
	 * @since 22-04-2019
	 */
	public static void executaServicio(MyHttpServlet myHttp, HttpServletRequest req, HttpServletResponse resp, Class<? extends AZoneUtil> claseZoneUtil, EnumAccion accion, boolean checkUsuarioValido, ITool tool) {

		try {
			IPgContainer GLoader = GLocator.getInstance();
			GLoader.executaServicio(myHttp, req, resp, claseZoneUtil, accion, checkUsuarioValido, tool);
		}	
		catch(Exception e) {
			System.out.println("[GLoader no ha podido ejecutar el Servicio]");
			myHttp.retTexto(resp, "[Error al ejecutar el Servicio]");
			
			logger.error(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T>List<T> getImplements(Class<T> clase, ITool tool) {
		if(clase == null) {
			return new ArrayList<T>();
		}
		List<IInjectable> retorno = null;
		
		try {
			IPgContainer GLoader = GLocator.getInstance();
			
			retorno = GLoader.getImplements((Class)clase, tool);
 
		}	
		catch(Exception e) {
			System.out.println("[GLoader no ha podido determinar los IInjectable de \""+clase.getCanonicalName()+"\"]");
			logger.error(e);
		}
		
		return (List<T>) retorno;
	}
}
