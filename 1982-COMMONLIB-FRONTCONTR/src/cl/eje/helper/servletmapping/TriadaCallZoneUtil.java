package cl.eje.helper.servletmapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.util.DoTransactionModulo;
import portal.com.eje.frontcontroller.util.GetterAZoneUtilFromClass;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class TriadaCallZoneUtil implements ITriadaCaller {

	public static TriadaCallZoneUtil getIntance() {
		return Weak.getInstance(TriadaCallZoneUtil.class);
	}
	

	@Override
	public void call(TriadaThingVo vo, MyHttpServlet http, HttpServletRequest req, HttpServletResponse resp, String servletName, boolean checkSession) {
		DoTransactionModulo.getInstance().doTransactionAZoneUtil(http, req, resp, new GetterAZoneUtilFromClass(vo), vo.getEnumaccion(), checkSession, null);

	}

}
