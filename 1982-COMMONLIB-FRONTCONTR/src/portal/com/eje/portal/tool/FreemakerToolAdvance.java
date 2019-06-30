package portal.com.eje.portal.tool;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.frontcontroller.resobjects.ResourceMapping;
import portal.com.eje.portal.factory.Util;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * @since 12-10-2018
 * */
public class FreemakerToolAdvance extends FreemakerTool {

	public static FreemakerToolAdvance getInstance() {
		return Util.getInstance(FreemakerToolAdvance.class);
	}
	/**
	 * @deprecated
	 * */
	public String templateProcess(HttpServletRequest req, String pHtm, SimpleHash modelRoot) {
		 
		try {
			Template t = new Template(ResourceMapping.getInstance().getFile(req, pHtm));
			return templateProcess(t, modelRoot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * retorna el flujo, a partir de un path base de template, hay que poner la ruta completa
	 * */
	public String templateProcessFromBase(HttpServletRequest req, String templatePath, SimpleHash modelRoot) {
		portal.com.eje.frontcontroller.resobjects.ResourceHtml html = new portal.com.eje.frontcontroller.resobjects.ResourceHtml();
		try {
			Template template = html.getTemplateFromBase(req, templatePath);
			return templateProcess(template, modelRoot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public String templateProcess(String templateOnFormatString, SimpleHash modelRoot) {
		String fileName = "templateTemporal_"+MyString.getInstance().getRandomString(20);
		File tmpTemplate = new File(ContextInfo.getInstance().getRealPath("temporal"+File.separator+fileName+".html"));
		
		try {
			FileUtils.write(tmpTemplate, templateOnFormatString, "UTF-8");
			
			return templateProcess(tmpTemplate, modelRoot);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(tmpTemplate.exists()) {
				tmpTemplate.delete();
			}
		}
		
		return null;
	}
	 
}
