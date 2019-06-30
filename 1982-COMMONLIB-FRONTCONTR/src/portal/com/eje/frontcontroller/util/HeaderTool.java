package portal.com.eje.frontcontroller.util;

import javax.servlet.http.HttpServletResponse;

import portal.com.eje.portal.factory.Util;

public class HeaderTool {
	private final String EXPIRES = "Expires";
	private final String PRAGMA = "Pragma";
	private final String ACCESS_ORIGIN = "Access-Control-Allow-Origin";
	private final String ACCESS_METHODS = "Access-Control-Allow-Methods";
	private final String CERO = "0";
	private final String NOCACHE = "no-cache";
	private final String ARTERISCO = "*";
	private final String GETPOSTOPTIONS = "GET, POST, OPTIONS";
	private final String TEXTHTML_CHARSET_ISO_8859_1	= "text/html; charset=ISO-8859-1";
	private final String CACHE_CONTROL = "Cache-Control";
 
 
	
	public static HeaderTool getInstance() {
		return Util.getInstance(HeaderTool.class);
	}
	
	public static void putNoCacheAndAllConnectOrigin(HttpServletResponse resp) {
		getInstance().privatePutHeader(resp);
	}
	
	private void privatePutHeader(HttpServletResponse resp) {
		resp.setHeader(EXPIRES, CERO);
		resp.setHeader(PRAGMA, NOCACHE);
		resp.setHeader(ACCESS_ORIGIN, ARTERISCO);
		resp.setHeader(ACCESS_METHODS, GETPOSTOPTIONS);
	}

	public boolean setNoCache(HttpServletResponse resp) {
         resp.setHeader(CACHE_CONTROL, NOCACHE);
         resp.setHeader(PRAGMA, NOCACHE);
         resp.setHeader(EXPIRES, CERO);
		return false;
	}
	
	public void setHtml(HttpServletResponse resp) {
		 resp.setContentType(TEXTHTML_CHARSET_ISO_8859_1);
	}

}
