package cl.eje.helper.servletmapping;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.EnumAccion;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.EnumTool;

public class TriadaParamsBySlashsTool {
	private final String SPLITTER = "\\/";
	public final String ACCION = "accion";
	public final String MODULO = "modulo";
	public final String THING = "thing";

	private final String EQUALS = "=";
	private final String AMP = "&";
	private final String QUEST = "?";
	private final String PRIMER_RELLENO = "primeraParte";
	private final String ULTIMO_RELLENO = "ultimaParte";

	public static TriadaParamsBySlashsTool getIntance() {
		return Weak.getInstance(TriadaParamsBySlashsTool.class);
	}

	public TriadaThingVo getDefinition(HttpServletRequest req, String servletName) {
		TriadaThingVo retorno = new TriadaThingVo();

		EnumAccion accion = EnumTool.getEnumByNameIngoreCase(EnumAccion.class, req.getParameter(ACCION), EnumAccion.get);
		retorno.setEnumaccion(accion);

		try {
			if (req != null && req.getRequestURI().indexOf(servletName) >= 0) {
				String query = req.getRequestURI().substring(req.getRequestURI().indexOf(servletName) + servletName.length(), req.getRequestURI().length());
				String[] partes = new StringBuilder().append(PRIMER_RELLENO).append(query).append(ULTIMO_RELLENO).toString().split(SPLITTER);

				if (partes != null && partes.length == 4 && partes[1] != null && partes[1].length() > 0 && partes[2] != null && partes[2].length() > 0) {
					retorno.setModulo(partes[1]);
					retorno.setThing(partes[2]);
				}
			}
		} catch (Exception e) {

		}

		return retorno;
	}

	public String buildParameterGet(HttpServletRequest req, String servletName, TriadaThingVo vo) {
		StringBuilder str = new StringBuilder();
		str.append(servletName).append(QUEST);

		if (req.getParameter(MODULO) == null) {
			str.append(MODULO).append(EQUALS).append(vo.getModulo()).append(AMP);
		}

		if (req.getParameter(THING) == null) {
			str.append(THING).append(EQUALS).append(vo.getThing()).append(AMP);
		}

		if (req.getParameter(ACCION) == null) {
			str.append(ACCION).append(EQUALS).append(vo.getEnumaccion().name().toLowerCase()).append(AMP);
		}

		return str.toString();

	}

	/**
	 * Se llama a si mismo pero con parámetros de modulo, thing y acción
	 * @author Pancho
	 * */
	public void recreaParametrosFromSlashs(MyHttpServlet http, HttpServletRequest req, HttpServletResponse resp, String servletName, boolean checkSession) throws ServletException, IOException {
		recreaParametrosFromSlashs(http, req, resp, servletName, checkSession, TriadaCallZoneUtil.getIntance());
	}

	/**
	 * Se llama a si mismo pero con parámetros de modulo, thing y acción<br/>
	 * La idea de esta autollamada es solamente que la url la transforme en parámetros
	 * @author Pancho
	 * @param caller luego de  llamarse a si mismo hace un redirect o un do transacción o cualquier llamada a clase, servicio o servlet
	 * */
	public void recreaParametrosFromSlashs(MyHttpServlet http, HttpServletRequest req, HttpServletResponse resp, String servletName, boolean checkSession, ITriadaCaller caller) throws ServletException, IOException {
		TriadaThingVo vo = TriadaParamsBySlashsTool.getIntance().getDefinition(req, servletName);

		if (vo.getModulo() != null && vo.getThing() != null && vo.getEnumaccion() != null && req.getParameter(MODULO) == null && req.getParameter(THING) == null) {
			RequestDispatcher d = req.getRequestDispatcher(TriadaParamsBySlashsTool.getIntance().buildParameterGet(req, servletName, vo));
			d.forward(req, resp);
		} else {
			caller.call(vo, http, req, resp, servletName, checkSession);
		}
	}
}
