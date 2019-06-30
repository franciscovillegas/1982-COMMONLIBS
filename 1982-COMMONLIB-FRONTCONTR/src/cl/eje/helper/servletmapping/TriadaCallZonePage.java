package cl.eje.helper.servletmapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.util.DoTransactionModulo;
import portal.com.eje.frontcontroller.util.GetterAZoneUtilFromClass;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class TriadaCallZonePage implements ITriadaCaller {

	public static TriadaCallZonePage getIntance() {
		return Weak.getInstance(TriadaCallZonePage.class);
	}
	

	@Override
	public void call(TriadaThingVo vo, MyHttpServlet http, HttpServletRequest req, HttpServletResponse resp, String servletName, boolean checkSession) {
		DoTransactionModulo.getInstance().doTransactionPage(http, req, resp, new GetterAZoneUtilFromClass(vo), vo.getEnumaccion(), checkSession);

	}

}
