package portal.com.eje.util;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringEscapeUtils;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class Utils extends AbsClaseWeb {

	public Utils(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String ret = null;
		
		try {
			HtmlLoader html = new HtmlLoader();
			ResourceBundle proper = ResourceBundle.getBundle("utils");
			String claseweb = proper.getString("claseweb");
			String[] string = claseweb.split(",");
			
			if(string != null) {
				for (String s :  string) {
					html.addClaseweb(s);
				}
			}
			
			ret = html.toIndexHtml();
		}
		catch (java.lang.NullPointerException e) {
			ret = e.getMessage();
		}
		catch (MissingResourceException e) {
			ret = e.getMessage();
		}
		catch (Exception e) {
			ret = e.getMessage();
		}
		
		super.getIoClaseWeb().retTexto( ret );
	}
	
	
	private class HtmlLoader {
		private List cws;
		
		private HtmlLoader() {
			cws = new ArrayList<String>();
		}
		
		public void addClaseweb(String cw) {
			cws.add(cw);
		}
		
		public String toIndexHtml() {
			StringBuilder html = new StringBuilder();
			html.append("<html>");
			html.append("<head>");
			html.append("</head>");
			html.append("<body>");
			html.append(getButtons());
			html.append("</body>");
			html.append("</html>");
			
			return html.toString();
			
		}
		
		private String getButtons() {

			StringBuilder str = new StringBuilder();
			Object[] obs = cws.toArray();
			
			if(obs != null) {
				for(Object o : obs) {
					if( o != null) {
						String cw = o.toString();
						
						String nombre 		= StringEscapeUtils.escapeHtml(getValue(cw, "nombre","Sin nombre"));
						String descripcion  = StringEscapeUtils.escapeHtml(getValue(cw, "descripcion","Sin descripción"));
						str.append("<a href='../servlet/EjeCoreI?claseweb=").append(cw).append("' title='").append(descripcion).append("'>").append(nombre).append("</a><br/>");	
						
					}
				}
			}
			
			return str.toString();
		}
		
	}
	
	private String getValue(String cw, String descri, String defValue) {
		String value = defValue;
		
		try {
			ResourceBundle proper = ResourceBundle.getBundle("utils");
			value = proper.getString(cw + "." + descri);
		}
		catch (Exception e) {
			
		}
		
		return value;
	}
}


