package portal.com.eje.frontcontroller.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.iface.AbsServicioCaller;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.frontcontroller.iface.IServicioListener;
import portal.com.eje.frontcontroller.ioutils.IOUtilSencha;
import portal.com.eje.portal.transactions.ITransaction;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.transactions.TransactionTool;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.errors.UsuarioAlreadyExistException;
import portal.com.eje.serhumano.user.errors.UsuarioSessionNotValidException;

/**
 * Para 
 * */
public class  DoCheckSession implements ITransaction {

	private String tipo;
	private IServicioGetter servicioGetter;
	private MyHttpServlet myHttp;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private boolean chechUsuarioValido;
	private AbsServicioCaller servletCaller;
	private IServicioListener servicioListener;
	private Cronometro cro;
	
	DoCheckSession(MyHttpServlet myHttp, HttpServletRequest req, HttpServletResponse resp, 
				IServicioGetter servicioGetter, String tipo, boolean chechUsuarioValido, AbsServicioCaller servletCaller,
				IServicioListener servicioListener,
				Cronometro cro) {
		
		this.servletCaller = servletCaller;
		this.tipo = tipo;
		this.servicioGetter = servicioGetter;
		this.myHttp = myHttp;
		this.req = req;
		this.resp = resp;
		this.chechUsuarioValido = chechUsuarioValido;
		this.servicioListener = servicioListener;
		this.cro = cro;
	}

	@Override
	public boolean transaction(TransactionConnection cons, Map<String, Object> paramsVars, TransactionTool tool) throws Exception {
		boolean ok = false;
		IOClaseWeb io = new IOClaseWeb(cons, this.myHttp, this.req, this.resp);
		
		HeaderTool.putNoCacheAndAllConnectOrigin(resp);
    	
    	try {
    		
			
			/* CARRETERA CONOCIDA */
		 	//CorrespondenciaLocator.getInstance().sendCorreosPendientes(io);
		 	//ParametroLocator.getInstance().recognizeIDCliente(io.getServletContext());
		 	//IOClaseWebSchedulerManager.getInstance().tryToExec(io);
		 	/* END CARRETERA CONOCIDA*/
		 	
    		if(this.chechUsuarioValido && io.getUsuario().esValido()) {
    			ok= servletCaller.doCall(io, servicioGetter, tipo, this.servicioListener, this.cro);
    		}
    		else if(!this.chechUsuarioValido) {
    			ok= servletCaller.doCall(io, servicioGetter, tipo, this.servicioListener, this.cro);
    		}
    		else {
    			//req.getHeaderNames()
    			
    		 
    			throw new UsuarioSessionNotValidException("Sesión inválida");
    		}

    	}
    	catch (ClassNotFoundException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    	catch (UsuarioSessionNotValidException e) {
    		String accept = io.getReq().getHeader("accept");
    		
    		if(accept != null && accept.contains("application/json") || "application/json".equals(io.getParamString("return_type", ""))) {
    			io.retSenchaJson("Sesión inválida", false);
			}
			else {
				io.getMy().getMensaje().devolverPaginaSinSesion(io.getReq(),io.getResp(),"Sin Sesion","");	
			}
		}
		catch (SecurityException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (NoSuchMethodException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (IllegalArgumentException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (InstantiationException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (IllegalAccessException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (InvocationTargetException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (IOException e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			try {
				io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
    	return ok;
     }

	
        
}