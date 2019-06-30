package portal.com.eje.serhumano.user.utils;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.EnumAccion;
import cl.ejedigital.tool.properties.PropertiesTools;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.enums.EnumDoAccion;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.frontcontroller.util.DoTransactionModulo;
import portal.com.eje.frontcontroller.util.HeaderTool;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.ifaces.AbsLoginPeople;
import portal.com.eje.serhumano.user.startsession.StartSessionPer;

public class S_StartSessionDispatcher {
	

	public static S_StartSessionDispatcher getInstance() {
		return Util.getInstance(S_StartSessionDispatcher.class);
	}

	
	public void iniciaDespachoNormal(HttpServletRequest req, HttpServletResponse resp, EnumDoAccion accion) throws ServletException, IOException {
		HeaderTool.putNoCacheAndAllConnectOrigin(resp);

		PropertiesTools pTools = new PropertiesTools();
		String cWeb = StartSessionPer.class.getName();

		if (pTools.existsBundle("generico")) {
			cWeb = pTools.getString(ResourceBundle.getBundle("generico"), "generico.claseStartSession", StartSessionPer.class.getName());
		}

		StringBuilder lastServlet = new StringBuilder();
		lastServlet.append("EjeCoreI?claseweb=").append(cWeb);
		// appendAllParameters(req, resp, lastServlet);

		RequestDispatcher dispatcher = req.getRequestDispatcher(lastServlet.toString());
		dispatcher.forward(req, resp);
	}

	public void iniciaDespachoComponente(AbsLoginPeople loginComponente, MyHttpServlet myHttp, HttpServletRequest req, HttpServletResponse resp, EnumDoAccion doAccion) {
		
		EnumAccion accion = getAccionFromDoAccion(doAccion);
		IServicioGetter getter = new ServicioGetter(loginComponente);
		DoTransactionModulo.getInstance().doTransactionAZoneUtil(myHttp, req, resp, getter, accion, false, null);
		
		
	}
	
	private EnumAccion getAccionFromDoAccion(EnumDoAccion accion) {
		switch (accion) {
		case doGet:
			return EnumAccion.get;
		case doPost:
			return EnumAccion.add;
		default:
			break;
		}
		
		return null;
	}
	
	class ServicioGetter implements IServicioGetter {
		private AbsLoginPeople loginComponente;
		
	 
		
		public ServicioGetter(AbsLoginPeople loginComponente) {
			super();
			this.loginComponente = loginComponente;
		}



		@Override
		public Object getServicio(IOClaseWeb io) throws Exception {
			return this.loginComponente;
		}
		
	}
}