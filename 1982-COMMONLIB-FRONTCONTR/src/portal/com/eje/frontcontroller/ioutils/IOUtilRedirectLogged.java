package portal.com.eje.frontcontroller.ioutils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.strings.UrlTool;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.eje_generico_util.ConfContext;
import portal.com.eje.portal.factory.Util;
 
/**
 * Util creada para redirigir a otros modulos de forma logeada
 * 
 * @author Pancho
 * @since 30-01-2019
 * */
public class IOUtilRedirectLogged extends IOUtil {
	private String urlIDEredirect = "view/ide/ejeb_generico_redirectlogged";
	
	public static IOUtilRedirectLogged getInstance() {
		return Util.getInstance(IOUtilRedirectLogged.class);
	}
	
	public boolean redirecLogged(IOClaseWeb io, EModulos modulo, String montajeToRedirect) {
		boolean ok = false;
		try {
			String servidorSubmit = ConfContext.getInstance().getConfContextUrl().getUrl(modulo, io);
			
			if(servidorSubmit != null) {
				servidorSubmit = quitaNumero(servidorSubmit);
				
				if(montajeToRedirect != null) {
					montajeToRedirect += "&r=" +  Math.random();
				}
				
				io.getResp().sendRedirect(
						new StringBuilder()
						.append(urlIDEredirect).append("?")
						.append("servidorSubmit=").append( URLEncoder.encode(servidorSubmit, "UTF-8") )
						.append("&startPage=").append(URLEncoder.encode( UrlTool.getInstance().quitaPrimerSlash (montajeToRedirect) , "UTF-8")).toString());
	 
	 
				ok = true;
			}
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	private String quitaNumero(String servidorSubmit) {
		if(servidorSubmit != null) {
			String[] partes = servidorSubmit.split("/");
			StringBuilder retorno = new StringBuilder();
			
			for(int i = 0 ; i < partes.length ; i++) {
				if(i >= partes.length-2) {
					partes[i] = (MyString.getInstance().quitaNumeros(partes[i]));
				}
				 
				if(i != 0) {
					retorno.append("/");	
				}
				
				retorno.append(partes[i]);
			}
			
			servidorSubmit = retorno.toString();
		}
		
		return servidorSubmit;
	}
	
	
	
}
