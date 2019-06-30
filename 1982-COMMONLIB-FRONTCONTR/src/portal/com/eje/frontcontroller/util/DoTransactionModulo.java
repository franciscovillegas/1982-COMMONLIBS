package portal.com.eje.frontcontroller.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.AZoneUtil;
import cl.eje.helper.EnumAccion;
import cl.eje.helper.SenchaController2018;
import cl.eje.helper.SenchaTipoPeticion;
import cl.eje.view.sencha.Version;
import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.frontcontroller.iface.IServicioListener;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.transactions.TransactionTool;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.EnumTool;

/**
 * Es un iniciador de transacciones, existen unicamente dos formas de iniciar una transacción<br/>
 * Iniciar llamada de un servlet EjeCore, EjeS, EjeI,EjeCoreI, <br/>
 * Iniciar la llamada de un AZoneUtil
 * */
public class DoTransactionModulo {
	
	public static DoTransactionModulo getInstance() {
		return Util.getInstance(DoTransactionModulo.class);
	}
	
	/**
	 * retorna true si es que se ejecuto correctamente <br/> 
	 * 
	 * El método retSencha(boolean) , boolean es igual al retorno de este método
	 * 
	 * @author Pancho
	 * @since 23-04-2019
	 * */
	public boolean doTransactionServlet (MyHttpServlet myHttp, HttpServletRequest req, HttpServletResponse resp, String tipo, boolean checkUsuarioValido) {
		TransactionTool tool = new TransactionTool(false);
		Cronometro cro = new Cronometro();
		cro.start();
		
		return tool.doTransaction(new DoCheckSession(myHttp, req, resp, ServicioFromClaseWeb.getInstance(), tipo, checkUsuarioValido, CallerServletGenerico.getInstance(), null, cro));
	}
	
	/**
	 * retorna true si es que se ejecuto correctamente <br/> 
	 * 
	 * El método retSencha(boolean) , boolean es igual al retorno de este método
	 * 
	 * @author Pancho
	 * @since 23-04-2019
	 * */
	public boolean doTransactionAZoneUtil (MyHttpServlet myHttp, HttpServletRequest req, HttpServletResponse resp,
			IServicioGetter servicioGetter, EnumAccion accion, boolean checkUsuarioValido, IServicioListener servicioListener) {
		Cronometro cro = new Cronometro();
		cro.start();
		
		TransactionTool tool = new TransactionTool(false);
		
		
		return tool.doTransaction(new DoCheckSession(myHttp, req, resp, servicioGetter, accion.name(), checkUsuarioValido, CallerAZoneUtil.getInstance(), servicioListener, cro));
	}

	public boolean doTransactionPage (MyHttpServlet myHttp, HttpServletRequest req, HttpServletResponse resp,
			IServicioGetter getter,
			EnumAccion accion, boolean checkUsuarioValido) {
		Cronometro cro = new Cronometro();
		cro.start();
		
		TransactionTool tool = new TransactionTool(false);
		
		if(getter == null) {
			getter = new ServicioFromAZoneUtil(req);
		}
		
	 	return tool.doTransaction(new DoCheckSession(myHttp, req, resp, getter , accion.name(), checkUsuarioValido, CallerPage.getInstance(), null, cro));
	}
	
	
}
