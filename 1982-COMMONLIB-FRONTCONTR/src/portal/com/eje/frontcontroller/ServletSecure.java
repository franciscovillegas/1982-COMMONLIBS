package portal.com.eje.frontcontroller;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.frontcontroller.resobjects.IResourceManipulate;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.frontcontroller.resobjects.ResourceImage;
import portal.com.eje.frontcontroller.resobjects.ResourceNull;
import portal.com.eje.frontcontroller.resobjects.ResourceOther;
import portal.com.eje.frontcontroller.resobjects.ResourceScript;
import portal.com.eje.frontcontroller.resobjects.ResourceStyle;
import portal.com.eje.frontcontroller.resobjects.ResourceTool;
import portal.com.eje.portal.correspondencia.CorrespondenciaLocator;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

public class ServletSecure extends MyHttpServlet
{

    private static List images;
    private static List pages;
    private static List styles;
    private static List javascript;

    public ServletSecure()
    {
        images = new ArrayList();
        pages = new ArrayList();
        styles = new ArrayList();
        javascript = new ArrayList();
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

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
    }

    public void destroy()
    {
        super.destroy();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        IResourceManipulate ir = getResourceHelper(req);
        ir.init(this,req, resp);
        try
        {
			/* CARRETERA CONOCIDA */
    		IOClaseWeb io = new IOClaseWeb(this, req, resp);
		 	//CorrespondenciaLocator.getInstance().sendCorreosPendientes(io);
		 	//ParametroLocator.getInstance().recognizeIDCliente(io.getServletContext());
		 	//IOClaseWebSchedulerManager.getInstance().tryToExec(io);
		 	/* END CARRETERA CONOCIDA*/
            ServletOutputStream out = ir.getWriter();
            out.flush();
            out.close();
        }
        catch(SocketException e)
        {
            System.out.println("Cliente cerr\363!!!");
            return;
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e);
            return;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }
        catch(ServletException e)
        {
            e.printStackTrace();
            return;
        }
    }

    private IResourceManipulate getResourceHelper(HttpServletRequest req)
    {
        IResourceManipulate r = null;
        if(SessionMgr.rescatarUsuario(req).esValido())
        {
            if(req.getParameter("htm") != null)
            {
                r = new ResourceHtml();
            } else
            {
                String ext = ResourceTool.getInstance().getExtension(req);
                
                if(images.indexOf(ext) != -1)
                {
                    r = new ResourceImage();
                } else
                if(styles.indexOf(ext) != -1)
                {
                    r = new ResourceStyle();
                } else
                if(javascript.indexOf(ext) != -1)
                {
                    r = new ResourceScript();
                } else
                {
                    r = new ResourceOther();
                }
            }
        } else
        {
            r = new ResourceNull();
        }
        return r;
    }
} 
