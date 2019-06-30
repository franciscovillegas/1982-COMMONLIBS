package cl.ejedigital.web.frontcontroller.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.frontcontroller.resobjects.ResourceHtml;
import freemarker.template.SimpleHash;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class WebOutputsManager implements IWebOutput {
	private HttpServletResponse resp;
	private ServletContext servletContext;
	
	public WebOutputsManager(HttpServletResponse resp, ServletContext servletContext) {
		this.resp = resp;
		this.servletContext = servletContext;
	}
	
	private boolean ret(String texto) {
		boolean ret = true;
        try {
        	PrintWriter out = resp.getWriter();
			out.write(texto);
	        out.flush();
	        out.close();
        }
        catch(Exception e) {
            ret = false;
        }
        return ret;
	}

	public boolean retTexto(String texto) {
		 resp.setContentType("text/html");
         resp.setHeader("Cache-Control", "no-cache");
         resp.setHeader("Pragma", "no-cache");
         resp.setHeader("Expires", "0");
            
         return ret(texto);
	}

	public boolean retData(IDataOut data) {
		 resp.setContentType("text/html");
         resp.setHeader("Cache-Control", "no-cache");
         resp.setHeader("Pragma", "no-cache");
         resp.setHeader("Expires", "0");
            
         return ret(data.getListData());
	}

	public boolean retData(IDataOut data, String[] sorted) {
		 resp.setContentType("text/html");
         resp.setHeader("Cache-Control", "no-cache");
         resp.setHeader("Pragma", "no-cache");
         resp.setHeader("Expires", "0");
            
         return ret(data.getListData(sorted));
	}
	
	public boolean retHtml(String htmRuta, SimpleHash modelRoot) {
		FreemakerTool free = new FreemakerTool();
		ResourceHtml htm = new ResourceHtml();
		
		resp.setContentType("text/html");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Expires", "0");
        
		try {
			return ret(free.templateProcess(htm.getTemplate(htmRuta),modelRoot));
		}
		catch (IOException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (ServletException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (SQLException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return false;
		
    }
	
	public boolean retXls(String htmRuta, SimpleHash modelRoot, String fileName) {
		FreemakerTool free = new FreemakerTool();
		ResourceHtml htm = new ResourceHtml();
		
		resp.setContentType("application/vnd.ms-excel");
    	resp.setHeader("Content-Disposition", "attachment; filename="+fileName);
        
		try {
			return ret(free.templateProcess(htm.getTemplate(htmRuta),modelRoot));
		}
		catch (IOException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (ServletException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (SQLException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		
		return false;
    }
	
}