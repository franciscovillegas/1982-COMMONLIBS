package portal.com.eje.portal.plugins.ifaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.AZoneUtil;
import cl.eje.helper.EnumAccion;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

/**
 * Encargado de obtener las clases de los plugins
 * 
 * @author Pancho
 * @since 22-04-2019
 * */
public interface IPgContainer {

	
	public void executaServicio(MyHttpServlet myHttp, HttpServletRequest req, HttpServletResponse resp, Class<? extends AZoneUtil> claseZoneUtil, EnumAccion accion, boolean checkUsuarioValido, ITool servicios);

	public <T>List<T> getImplements(Class<T> clase, ITool tool);

	


}
