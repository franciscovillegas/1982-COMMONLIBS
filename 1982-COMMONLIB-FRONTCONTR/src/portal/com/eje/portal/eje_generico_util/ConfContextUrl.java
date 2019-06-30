package portal.com.eje.portal.eje_generico_util;

import java.util.List;

import org.apache.poi.hslf.record.SlideListWithText.SlideAtomsSet;

import cl.ejedigital.tool.strings.UrlTool;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.tools.EnumTool;
import portal.com.eje.tools.conf.IConfCentral;

public class ConfContextUrl {
	private String paramNemo;

	public ConfContextUrl(IConfCentral c) {
		paramNemo = c.getClass().getCanonicalName();
		checkUrls();
	}

	private void checkUrls() {
		List<EModulos> enums = EnumTool.getArrayList(EModulos.class);
		for (EModulos e : enums) {
			checkUrl(e);
		}
	}

	private boolean checkUrl(EModulos mod) {
		try {
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), paramNemo, getModKey(mod), "");
		} catch (PPMException e) {
			e.printStackTrace();
		}
		return true;
	}

	public String getUrl(EModulos mod) {
		return getUrl(mod, null);
	}
	
	public String getUrl(EModulos mod, IOClaseWeb io) {
		String url = null;
		ParametroValue pv = ParametroLocator.getInstance().getValor(paramNemo, getModKey(mod));
		if (pv != null &&  !"".equals(pv.getValue())) {
			url = pv.getValue();
		}
		else {
			if(io != null) {
				url = io.getUrlContext();
				String toReplace = 	io.getUrlContext().substring(io.getUrlContext().lastIndexOf("/"), io.getUrlContext().length());
				
				url = url.replaceAll( toReplace , "/"+mod.getContexo()) ;

			}
		}

		return UrlTool.getInstance().siExisteQuitaUltimoSlash( url );
	}
	
	/**
	 * 
	 * Retorna la url dentro del contexto del EModulo que se ingrese <br/>
	 * 
	 * Supongamos que llamamos a este método con EModulos.portaldepersonas y en montaje pasamos el parámetro "servlet/StartSession". El retorno será
	 * algo así como "http://www.peoplemanager.cl/beme/portaldepersonas/servlet/StartSession" lo más problable que el dominio y el protocolo varíe
	 * 
	 * @author Pancho
	 * @since 30-01-2019
	 * */
	public String getUrl(IOClaseWeb io, EModulos modulo, String montaje) {
		StringBuilder url = null;
		
		if(io != null && modulo != null && montaje != null ) {
			String urlContext = getUrl(modulo, io);
			
			boolean terminaEnSlash = "/".equals( urlContext.substring(urlContext.length()-1, urlContext.length()) );
			
			if(terminaEnSlash) {
				url = new StringBuilder(urlContext.substring(0, urlContext.length() -1));
			}
			else {
				url = new StringBuilder(urlContext);
			}
			
			url = new StringBuilder( url.toString().substring(0, url.toString().lastIndexOf("/")) );
			
			
			boolean comienzaConSlash = "/".equals( montaje.substring(0, 1) );
			if(comienzaConSlash) {
				montaje = montaje.substring(1, montaje.length());
			}
			
			url.append("/").append(modulo.getContexo()).append("/").append(montaje);
			
		}
		
		return UrlTool.getInstance().siExisteQuitaUltimoSlash( url.toString() );
	}

	private String getModKey(EModulos boton) {
		return "url." + boton.getContexo();
	}

	

}
