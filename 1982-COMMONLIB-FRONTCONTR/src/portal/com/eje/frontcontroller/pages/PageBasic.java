package portal.com.eje.frontcontroller.pages;

import portal.com.eje.portal.factory.Util;

public class PageBasic {

	public static PageBasic getInstance() {
		return Util.getInstance(PageBasic.class);
	}
	
	public String getPre(String pre) {
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html>\n");
		html.append("<html>\n");
		html.append(" <head>\n");
		html.append("  <meta charset='ISO-8859-1'> \n");
		html.append("  <meta http-equiv='Content-Type' content='text/html;charset=UTF-8'> \n");
		html.append(" </head>\n");
		html.append(" <body>\n");
		html.append(" <pre>\n");
		html.append(pre);
		html.append(" </pre>\n");
		html.append(" </body>\n");
		html.append("</html>\n");
		
		return html.toString();
	}
	
	public String getBody(String body) {
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html>\n");
		html.append("<html>\n");
		html.append(" <head>\n");
		html.append("  <meta charset='ISO-8859-1'> \n");
		html.append("  <meta http-equiv='Content-Type' content='text/html;charset=UTF-8'> \n");
		html.append(" </head>\n");
		html.append(" <body>\n");
		html.append(body);
		html.append(" </body>\n");
		html.append("</html>\n");
		
		return html.toString();
	}
}
