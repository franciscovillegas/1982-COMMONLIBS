package cl.ejedigital.web.frontcontroller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.web.MyHttpServlet;
import cl.ejedigital.web.frontcontroller.io.IWebInput;
import cl.ejedigital.web.frontcontroller.io.IWebOutput;
import cl.ejedigital.web.usuario.IUsuario;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class IOClaseWeb {
	private MyHttpServlet myHttp;
	private HttpServletRequest	req;
	private HttpServletResponse	resp;
	private IWebOutput			webOutput;
	private IWebInput			webInput;
	private IUsuario			user;

	IOClaseWeb(HttpServletRequest req, HttpServletResponse resp, MyHttpServlet myHttp) throws Exception {
		throw new NotImplementedException();
//		this.req = req;
//		this.resp = resp;
//		this.webInput = new WebInputsManager(myHttp, req,myHttp.getServletContext());
//		this.webOutput = new WebOutputsManager(resp,myHttp.getServletContext());
//		this.myHttp = myHttp;
//		
//		ConfigParametroManager pManager = ConfigParametroManager.getInstance();
//		
//		Class[] definicion = {};
//		Object[] parametros = {};
//		ISessionManager session = (ISessionManager)pManager.getClaseConfigParametro(ConfigParametroKey.generico_session_manager,definicion,parametros);
//		user = session.rescatarUsuario(req);
		
	}

	public IWebInput getIn() {
		return webInput;
	}

	public IWebOutput getOut() {
		return webOutput;
	}
	
	public IUsuario getUsuario() {
		return user;
	}
	

	public Connection getConnection(String key) {
		return myHttp.getDBConnectionManager().getConnection(key);
	}
	
	public void freeConnection(String key,Connection conn) {
		myHttp.getDBConnectionManager().freeConnection(key,conn);
	}

}