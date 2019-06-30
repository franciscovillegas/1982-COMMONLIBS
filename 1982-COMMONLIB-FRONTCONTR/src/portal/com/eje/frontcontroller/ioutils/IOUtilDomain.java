package portal.com.eje.frontcontroller.ioutils;

import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.parametro.ParametroLocator;

public class IOUtilDomain extends IOUtil {

	public static IOUtilDomain getIntance() {
		return Weak.getInstance(IOUtilDomain.class);
	}

	/**
	 * Retorna /cliente/modulo
	 * */
	
	public String getContext() {
		try {
			return ParametroLocator.getInstance().getClienteContext() + ParametroLocator.getInstance().getModuloContext();	
		}
		catch(Exception e) {
			return "/nodef/nodef";
		}
		
	}
	
	/**
	 * Retorna /cliente
	 * */
	public String getContextCliente() {
		try {
			return ParametroLocator.getInstance().getClienteContext();	
		}
		catch(Exception e) {
			return "/nodef";
		}
	}
	
	/**
	 * Retorna /cliente/modulo
	 * */
	public String getContextModulo() {
		try {
			return ParametroLocator.getInstance().getModuloContext();	
		}
		catch(Exception e) {
			return "/nodef";
		}
	}
	
	/**
	 * Retorna http://www.peoplemanager.cl/ , bajo el protocolo https si es necesario.
	 * */
	public String getDomain(IIOClaseWebLight io) {
		String[] url = io.getReq().getRequestURL().toString().split("/");
		StringBuilder urlBuilder = new StringBuilder();
		
		for(int i = 0; i < 3 ; i++) {
			if(url != null && url.length >= i ) {
				if(urlBuilder.length() != 0) {
					urlBuilder.append("/");
				}
				urlBuilder.append(url[i]);
			}
		}
		
		return urlBuilder.toString();
	}
	
	/**
	 * Retorna http://www.peoplemanager.cl/cliente/modulo, bajo el protocolo https si es necesario.
	 * */
	public String getUrlContext(IIOClaseWebLight io) {
		String[] url = io.getReq().getRequestURL().toString().split("/");
		StringBuilder urlBuilder = new StringBuilder();
		
		for(int i = 0; i < 5 ; i++) {
			if(url != null && url.length >= i ) {
				if(urlBuilder.length() != 0) {
					urlBuilder.append("/");
				}
				urlBuilder.append(url[i]);
			}
		}
		
		return urlBuilder.toString();
	}
}
