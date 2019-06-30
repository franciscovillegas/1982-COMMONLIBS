package cl.ejedigital.web.frontcontroller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.web.MyHttpServlet;
import cl.ejedigital.web.frontcontroller.resobjects.IResourceManipulate;
import cl.ejedigital.web.frontcontroller.resobjects.ResourceHtml;
import cl.ejedigital.web.frontcontroller.resobjects.ResourceImage;
import cl.ejedigital.web.frontcontroller.resobjects.ResourceScript;
import cl.ejedigital.web.frontcontroller.resobjects.ResourceStyle;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class ServletTool extends MyHttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2469671474765867840L;
	private static List<String> images;
	private static List<String> pages;
	private static List<String> styles;
	private static List<String> javascript;
	
    public ServletTool() {
    	images = new ArrayList<String>();
    	pages = new ArrayList<String>();
    	styles = new ArrayList<String>();
    	javascript = new ArrayList<String>();
    	
    	images.add("png");
    	images.add("jpg");
    	images.add("bmp");
    	images.add("jpeg");
    	images.add("gif");
    	
    	pages.add("html");
    	pages.add("htm");
    	pages.add("jsp");
    	
    	styles.add("css");
    	
    	javascript.add("js");
    	
    	
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
	}
	
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
         
    	
    	IResourceManipulate ir = getResourceHelper(req);
    	
		
    	try {
    		if(ir == null) {
    			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    		}
    		else {
	    		ir.init(req,resp);
	        	ServletOutputStream out = ir.getWriter();
				out.flush();
			    out.close();
    		}
		    
		}
    	catch (SocketException e) {
			System.out.println(e);
			
			return;
		}
    	catch (FileNotFoundException e ) {
    		System.out.println(e);
			
			return;
		}
		catch ( IOException e ) {
			System.out.println(e);
			
			return;
		}
		catch ( ServletException e ) { 
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			
			return;
		}
		catch ( Exception e ) { 
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			
			return;
		}		
		
        
    }   
    
    
    private IResourceManipulate getResourceHelper(HttpServletRequest req) {
    	IResourceManipulate r = null;
    	
		if (req.getParameter("htm") != null) {
			r = new ResourceHtml();
		}
		else {
			String ext = req.getQueryString().substring(req.getQueryString().lastIndexOf(".")+1,req.getQueryString().length()).toLowerCase();
			
			if(images.indexOf(ext) != -1) {
				r = new ResourceImage();
    			
    		} else if(styles.indexOf(ext) != -1) {
    			r = new ResourceStyle();
    			
    		} else if(javascript.indexOf(ext) != -1) {
    			r = new ResourceScript();
    			
    		} 
		}
		
    	return r;
    }
}