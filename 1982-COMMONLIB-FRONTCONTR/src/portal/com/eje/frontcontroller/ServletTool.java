package portal.com.eje.frontcontroller;

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

import portal.com.eje.frontcontroller.resobjects.IResourceManipulate;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.frontcontroller.resobjects.ResourceImage;
import portal.com.eje.frontcontroller.resobjects.ResourceOther;
import portal.com.eje.frontcontroller.resobjects.ResourceScript;
import portal.com.eje.frontcontroller.resobjects.ResourceStyle;
import portal.com.eje.frontcontroller.resobjects.ResourceTool;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ServletTool extends MyHttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	@Override
	public void destroy() {
	
		super.destroy();		
	}

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        
   
    	IResourceManipulate ir = getResourceHelper(req);
    	ir.init(this,req,resp);
    	ServletOutputStream out = null;
		
    	try	 {
			/* CARRETERA CONOCIDA */
    		IOClaseWeb io = new IOClaseWeb(this, req, resp);
		 	//CorrespondenciaLocator.getInstance().sendCorreosPendientes(io);
		 	//ParametroLocator.getInstance().recognizeIDCliente(io.getServletContext());
		 	//IOClaseWebSchedulerManager.getInstance().tryToExec(io);
		 	//IOClaseWebSchedulerManager.getInstance().trackingBeforeResponse(io);
		 	/* END CARRETERA CONOCIDA*/
    		
			out = ir.getWriter();
		    
		}
    	catch (SocketException e) {
    		System.out.println("Cliente cerró!!!");
			return;
		}
    	catch (FileNotFoundException e ) {
    		System.out.println("FileNotFoundException "+req.getQueryString());
			return;
		}
		catch ( IOException e ) {
			e.printStackTrace();
			
			return;
		}
		catch ( ServletException e ) { 
			e.printStackTrace();
			
			return;
		}
		finally {
			
			if(out != null) {
				out.flush();
			    out.close();
			}
			
		}
		
        
    }   
    
    
    private IResourceManipulate getResourceHelper(HttpServletRequest req) {
    	IResourceManipulate r = null;
    	
		if (req.getParameter("htm") != null) {
			r = new ResourceHtml();
		}
		else {
			 String ext = ResourceTool.getInstance().getExtension(req);
			
			 if(images.indexOf(ext) != -1) {
				r = new ResourceImage();
    			
    		} else if(styles.indexOf(ext) != -1) {
    			r = new ResourceStyle();
    			
    		} else if(javascript.indexOf(ext) != -1) {
    			r = new ResourceScript();
    			
    		} else {
    			r = new ResourceOther();
    			
    		}
		}
		
		return r;
    }
}