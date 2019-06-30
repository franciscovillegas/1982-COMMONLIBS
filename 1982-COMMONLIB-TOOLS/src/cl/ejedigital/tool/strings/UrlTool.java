package cl.ejedigital.tool.strings;

import portal.com.eje.portal.factory.Util;

public class UrlTool {
	
	public static UrlTool getInstance() {
		return Util.getInstance(UrlTool.class);
	}
	
	/**
	 * Solo lo quita si inicia con /
	 * */
	public String quitaPrimerSlash(String url) {
		if(url != null && url.startsWith("/")) {
			url = url.substring(1, url.length());
		}
		
		return url;
	}
	
	public String putSlashAtBeginnig(String path) {
		if(path != null && !path.substring(0, 1).equals("/")) {
			path = "/".concat(path);
		}
		return path;
	}
	
	public String siExisteQuitaUltimoSlash(String url) {
		if(url != null) {
			boolean tieneUltimoSlash = "/".equals( url.substring(url.length()-1, url.length()) );
			
			if(tieneUltimoSlash) {
				url = url.substring(0, url.length() - 1);
			}
		}
		
		return url;
	}
	
	public String siExisteQuitaUltimoChar(char c, String url) {
		if(url != null) {
			boolean tieneUltimoSlash = c == ( url.substring(url.length()-1, url.length()) ).toCharArray()[0];
			
			if(tieneUltimoSlash) {
				url = url.substring(0, url.length() - 1);
			}
		}
		
		return url;
	}
}
