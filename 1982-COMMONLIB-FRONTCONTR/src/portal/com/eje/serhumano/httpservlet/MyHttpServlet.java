package portal.com.eje.serhumano.httpservlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import cl.ejedigital.web.frontcontroller.io.IWebOutputText;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWebLight;
import portal.com.eje.frontcontroller.ioutils.IOUtilFreemarker;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.frontcontroller.util.HeaderTool;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.tracking.trackingManager;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

public abstract class MyHttpServlet extends HttpServlet implements WriterDestination, IWebOutputText {

    /**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private PrintWriter writer;
	private ResourceHtml html;
	
	protected MyHttpServlet() {
		html = new ResourceHtml();
    } 
    
    public void init(ServletConfig config) throws ServletException {
    	 
        super.init(config);	        
	    System.out.println("[MyHttpServlet Init Servlet] "+config.getServletName().toString());
	    //MyHttpServlet_onInitServer.getInstance(config.getServletContext());
    	 
    }
    
    public void init() {
       mensaje = new Mensaje(this);
		
    } 
    

    public void destroy() {

	    System.out.println("[MyHttpServlet Destroy Servlet] "+getServletConfig().getServletName().toString());
	    //MyHttpServlet_onInitServer.destroyInstance(getServletConfig());
	    super.destroy();

    }

    /**
     * 
     * @deprecated 2016-03-29
     * 
     * */
    public Template getTemplate(String templateName) {
    	try {
			return getTemplate(null, templateName);
		} catch (ServletException e) {
			e.printStackTrace();
		}
    	
    	return null;
	}

    public Map<String,Object> getObjectTemplate(IOClaseWeb io, String templateName)  throws ServletException {
    	Map<String,Object> map = null;
    	
    	try {
    		map = html.getObjectTemplate(MyTemplateUbication.SrcAndWebContent, io.getReq(), templateName);
			//map = MyHttpServlet_onInitServer.getInstance(getServletContext()).getResourceHtml().getObjectTemplate();
		} catch (IOException e) {
			map = new HashMap<String,Object>();
			e.printStackTrace();
		} catch (SQLException e) {
			map = new HashMap<String,Object>();
			e.printStackTrace();
		}
    	
    	return map;
    }
    
    public Template getTemplate(IOClaseWeb io, String templateName)  throws ServletException {
    	return getTemplate(MyTemplateUbication.SrcAndWebContent, io, templateName);
    }
    
    public Template getTemplate(MyTemplateUbication ubication, IOClaseWeb io, String templateName)  throws ServletException {
            	
		try {
			if(io != null) {
				return html.getTemplate(io.getReq(), templateName);	
			}
			else {
				System.out.println("@@@@ [BAD GetTemplate] "+templateName);
				return html.getTemplate(templateName);
			}
			
		}
		catch (IOException e) {
			throw new ServletException();
		}
		catch (SQLException e) {
			throw new ServletException(e);
		}

    }
    
   
    
    public boolean retJavascript(HttpServletResponse resp, String texto) {
   	 boolean ret = true;
        try {
            PrintWriter out = getWriter(resp);
            resp.setContentType("text/javascript; charset=ISO-8859-1");
            out.write(texto);
            out.flush();
            out.close();
        }
        catch(Exception e) {
            getServletContext().log(" Error Metodo retText en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
            ret = false;
        }
        return ret;
   }
    
    public boolean retTexto(HttpServletResponse resp, String texto) {
    	 boolean ret = true;
         try {
        	 if(texto != null && resp != null) {
        		 HeaderTool.getInstance().setHtml(resp);
        		 HeaderTool.getInstance().setNoCache(resp);
        		 
        		 PrintWriter out = getWriter(resp);
                 
                 out.write(texto);
                 out.flush();
                 out.close();
        	 }
        	
         }
         catch(Exception e) {
             getServletContext().log(" Error Metodo retText en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
             ret = false;
         }
         return ret;
    }
    
    public boolean retJson(HttpServletResponse resp, String texto) {
   	 boolean ret = true;
        try {
       	 PrintWriter out = getWriter(resp);
            resp.setContentType("application/json; charset=ISO-8859-1");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            out.write(texto);
            out.flush();
            out.close();
        }
        catch(Exception e) {
            getServletContext().log(" Error Metodo retText en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
            ret = false;
        }
        return ret;
   }
    
	protected boolean retExcel(HttpServletResponse resp, String texto, String fileName) {
    	boolean ret = true;
        try {
        	PrintWriter out = getWriter(resp);
            resp.setContentType("application/vnd.ms-excel");
        	resp.setHeader("Content-Disposition", "attachment; filename="+fileName);
            out.write(texto);
            out.flush();
            out.close();
        }
        catch(Exception e) {
            getServletContext().log(" Error Metodo retText en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
            ret = false;
        }
        
        return ret;
    }

	/**
	 * 
	 * Se debe usar la fucnion retTemplate(HttpServletRequest req, HttpServletResponse resp, String templatePath, SimpleHash modelRoot)
	 * @deprecated
	 * */
	
	
	public boolean retTemplate(HttpServletResponse resp, String templatePath, SimpleHash modelRoot) {
		 return retTemplate(null, resp, templatePath, modelRoot);
	}
	
	public boolean retExcel(HttpServletRequest req, HttpServletResponse resp, String templatePath, SimpleHash modelRoot, String fileName) {
		resp.setContentType("application/vnd.ms-excel");
    	resp.setHeader("Content-Disposition", "attachment; filename="+fileName);
    	
    	IOClaseWeb io = new IOClaseWeb(this, req, resp);
    	return retTemplatePriv(io, templatePath, modelRoot, true);
	}
	
    public boolean retCSV(HttpServletRequest request, HttpServletResponse response, StringBuilder strOut, String fileName) {

    	boolean ok = false;
    	
    	response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
        try
        {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(strOut.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            
            ok = true;
            
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }

    	return ok;
    }
    
	/**
	 * (4 Marzo 2014) - Existe un problema con este método, cuando las peticiones son doPost no se pueden obtener los parametros por httpServletRequest,
	 * ya que el frontcontroller hace un barrido de los parámetros, es por eso que se creó un método que reciba IOClaseWeb 
	 * 
	 * @deprecated
	 * */
    public boolean retTemplate(HttpServletRequest req, HttpServletResponse resp, String templatePath, SimpleHash modelRoot) {
    	
    	return retTemplatePriv(req, resp, templatePath, modelRoot);
    }
    
    public boolean retTemplate(IOClaseWeb cweb, String templatePath, SimpleHash modelRoot) {
    	return retTemplatePriv(cweb, templatePath, modelRoot, true);
    }

    public boolean retTemplate(IOClaseWeb cweb, String templatePath, SimpleHash modelRoot, boolean startFromTemplatePath) {
    	return retTemplatePriv(cweb, templatePath, modelRoot, startFromTemplatePath);
    }


    private boolean retTemplatePriv(IOClaseWeb cweb, String templatePath, SimpleHash modelRoot, boolean startFromTemplatePath) {
    	cweb.getResp().setContentType("text/html; charset=ISO-8859-1");
    	
    	boolean ok = false;
    	PrintWriter out = null;
    	
    	try {  
    		String flow = getTemplateProcess(cweb, templatePath, modelRoot, startFromTemplatePath);
    		if(flow != null) {
    			out = getWriter(cweb.getResp());
    	    	out.write(flow);
    	    	ok = true;	
    		}
	    	
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		ok = false;
    	}
    	finally {
    		if(out != null) {
    			out.close();
    			out.flush();
    		}
    	}
    	
    	return ok ;
    }

    public String getTemplateProcess(IOClaseWeb cweb, String templatePath, SimpleHash modelRoot, boolean startFromTemplatePath) {
    	return IOUtilFreemarker.getInstance().getTemplateProcces(cweb, templatePath, modelRoot, startFromTemplatePath);
    }
    
    /**
	 * Existe un problema con este método, cuando las peticiones son doPost no se pueden obtener los parametros por httpServletRequest,
	 * ya que el frontcontroller hace un barrido de los parámetros, es por eso que se creó un método que reciba
	 * IOClaseWeb 
	 * 
	 * @deprecated 2015-01-01
	 * @see MyHttpServlet
     * 
     * */
    private boolean retTemplatePriv(HttpServletRequest req, HttpServletResponse resp, String templatePath, SimpleHash modelRoot) {
    	IOClaseWebLight io = new IOClaseWebLight(this, req, resp);
    	String toReturn = io.getUtil(IOUtilFreemarker.class).getTemplateProcces(io, templatePath, modelRoot);
    	
    	retTexto(io.getResp(), toReturn);
    	return true;
    	/*
    	boolean ret = true;
        try {
        	StringWriter stringWriter = new StringWriter();
        	Closeable closeable = null;
        	Flushable flusheable = null;
        	
        	if(modelRoot == null) {
        		modelRoot = new SimpleHash();
        	}
            try {
     	        if(req != null ) {

     	        	Usuario user = SessionMgr.rescatarUsuario(req);
	     	        
     	        	if(user != null && user.esValido()) {
	     		        modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
	     		        modelRoot.put("usuario", user.toHash());
	     		        //modelRoot.put("rut", user.getRutId());	        
	     		        modelRoot.put("GetImagenEmpresa", new GetImagenEmpresa(user));
	     	        }
	     	        
     		        modelRoot.put("context", req.getContextPath());
     		        modelRoot.put("GetParam", new GetParam(req));
     		        modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
	     	        
     	        }
     	        
 		        Calendar cal = Calendar.getInstance();
 		        int yy = cal.get(Calendar.YEAR);
 		        int mm = cal.get(Calendar.MONTH)+1;
 		        int dd = cal.get(Calendar.DAY_OF_MONTH);
 		        modelRoot.put("fechaActual", dd + " de " + Tools.RescataMes(mm) + " del " + yy);
 		        
     	        PropertiesTools properTools = new PropertiesTools();
		        if(properTools.existsBundle("analytics")) {
	            	modelRoot.put("googleAnalytics",properTools.getString(ResourceBundle.getBundle("analytics"),"google.key",""));
	            }
     	        
		        
		        
		        
     	        Template template = getTemplate(templatePath);

            	
               	PrintWriter print = new PrintWriter(stringWriter);     
                template.process(modelRoot, print);
                
                if(!ResourceTool.getInstance().isCacheActive(CachePortalTypes.CACHETEMPLATE)) {
//                  Francisco: 01-08-2017 eliminado el cache por el lado del cliente
//                  resp.setHeader("Expires", "0");
//                  resp.setHeader("Pragma", "no-cache");
//                  resp.setHeader("Cache-Control", "no-cache");
                }
                else {
                	ResourceTool.getInstance().setCacheHeader(resp, CachePortalTypes.CACHETEMPLATE);
                }

                PrintWriter out =  getWriter(resp);
                closeable = out;
                flusheable = out;
                out.write( 
                		"<!-- ".concat(
                						Validar.getInstance().validarDato(templatePath,"sin template"))
                				.concat("-->\t")
                				.concat(stringWriter.toString()));
                

            }
            catch(IllegalStateException e) {
//            	  ya fue llamado por un getWriter 
            	PrintWriter out =  getWriter(resp);
                closeable = out;
                flusheable = out;
            	out.write(stringWriter.toString());
            }
            catch(Exception e) {
                System.err.println(e.getMessage());
                ret = false;
                getServletContext().log(" Error Metodo retTemplate en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
                
            }
            finally {
            	if(flusheable != null) {
            		flusheable.flush();
            	}
            	
                if(closeable != null) {
                	closeable.close();	
                }
            }

        }
        catch(Exception e) {
            getServletContext().log(" Error Metodo retTemplate en : ".concat(String.valueOf(String.valueOf(getClass().getName()))), e);
            ret = false;
        }
        return ret;
        */
    }
    
    public void insTracking(HttpServletRequest req, String descripcion, String datos) {
        trackingManager tMgr = new trackingManager();
        
        try {
        	Usuario user = SessionMgr.rescatarUsuario(req);
            tMgr.insertTracking(user.getRutId(), user.getEmpresa(), descripcion, datos, req);
        }
        catch(Exception e) { 
        	
        }
    }
    
    /**
     * Método utilizado solo para aquellas zonas en donde se utilizan FrontController, ya que originalmente dentro del mismo método <b>insTracking</b> se
     * obtenía el servlet que se llamaba, ahora opcionalmente se puede ingresar.
     * 
     * @param direcRel
     * @param req
     * @param descripcion
     * @param datos
     */
    
    public void insTracking(String direcRel, HttpServletRequest req, String descripcion, String datos) {
       
        
        try {
        	Usuario user = SessionMgr.rescatarUsuario(req);
        	insTracking(user.getRutIdInt(), Validar.getInstance().validarInt(user.getEmpresa(), -1), direcRel, req, descripcion, datos);
        }
        catch(Exception e) { 
        	
        }
    }
    
    /**
     * Método utilizado solo para aquellas zonas en donde se utilizan FrontController, ya que originalmente dentro del mismo método <b>insTracking</b> se
     * obtenía el servlet que se llamaba, ahora opcionalmente se puede ingresar.
     * 
     * @param direcRel
     * @param req
     * @param descripcion
     * @param datos
     * @since 2015-09-02
     */
    public void insTracking(int rut, int empresa, String direcRel, HttpServletRequest req, String descripcion, String datos) {
        trackingManager tMgr = new trackingManager();
        
        try {
            tMgr.insertTracking(direcRel,String.valueOf(rut), String.valueOf(empresa), descripcion, datos, req);
        }
        catch(Exception e) { 
        	
        }
    }
    
    public String getTemplatePath() {
    	 
    	return html.getTemplatePath();
    }

    public Mensaje getMensaje() {
		return mensaje;
	}
    
    public IDBConnectionManager getConnMgr() {
    	return MyHttpServlet_onInitServer.getInstance(getServletContext()).getConnMgr();
	}
    

	protected void setExcel(HttpServletResponse resp, String fileName) {
    	resp.setContentType("application/vnd.ms-excel");
    	resp.setHeader("Content-Disposition", "attachment; filename="+fileName);
    }

	private PrintWriter getWriter(HttpServletResponse resp) {
		if(writer == null) {
			try {
				return resp.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return writer;
		}
	}
	
	/**
	 * No debe establecerse otro Print, ya que el print siempre debe ser el de Server
	 * 
	 * @deprecated
	 * @since 30-04-2018
	 * */
	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}
 
	public void setWriterByDefault() {
		this.writer = null;
	}
	
    protected Mensaje mensaje;
    
 
    public IDBConnectionManager connMgr = DBConnectionManager.getInstance();//getConnMgr(); //MALO MALO MUCHAS REFERENCIAS A ESTA VAR
    public static boolean ActDominio = false;

}