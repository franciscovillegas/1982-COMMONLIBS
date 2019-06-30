package portal.com.eje.serhumano;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class Mensaje {

	private static final long serialVersionUID = 5374063560792177417L;
	private MyHttpServlet myHttp;
	
	public Mensaje(MyHttpServlet myHttp) {
		super();
		this.myHttp = myHttp;
	}

	public boolean isEmpty()
    {
        return false;
    }

	
	/**
	 * Este método se deprecó ya que se necesita el parámetro HttpServletRequest para insertar siempre el parámetro.
	 * 
	 * @deprecated 
	 * @since 28 Jun 2013
	 * 
	 * */
    public void devolverPaginaMensage(HttpServletResponse resp, String pagina, String titulo, String msg)
        throws IOException, ServletException
    {
        devolverPaginaMensage(null, resp, pagina, titulo, msg);
    }
    
    private void devolverPaginaMensage(HttpServletRequest req, HttpServletResponse resp, String pagina, String titulo, String msg)
            throws IOException, ServletException
    {
    	
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("titulo", titulo);
		modelRoot.put("mensaje", msg);
		modelRoot.put("context", ContextInfo.getInstance().getServletContextName());

		IOClaseWeb io = new IOClaseWeb(this.myHttp, req, resp);
		
		myHttp.retTemplate(io, pagina, modelRoot);

    }
    
	/**
	 * Este método se deprecó ya que se necesita el parámetro HttpServletRequest para insertar siempre el parámetro.
	 * 
	 * @deprecated 
	 * @since 28 Jun 2013
	 * 
	 * */

    public void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        devolverPaginaMensage(resp, PAGINA_MENSAGE, titulo, msg);
    }

    
	/**
	 * Este método se deprecó ya que se necesita el parámetro HttpServletRequest para insertar siempre el parámetro.
	 * 
	 * @deprecated 
	 * @since 28 Jun 2013
	 * 
	 * */
    public void devolverPaginaSinSesion(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        devolverPaginaMensage(resp, PAGINA_SIN_SESION, titulo, msg);
    }

    public void devolverPaginaMensage(HttpServletRequest req, HttpServletResponse resp, String titulo, String msg)
            throws IOException, ServletException
    {
        devolverPaginaMensage(req, resp, PAGINA_MENSAGE, titulo, msg);
    }

    public void devolverPaginaSinSesion(HttpServletRequest req, HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        devolverPaginaMensage(req, resp, PAGINA_SIN_SESION, titulo, msg);
    }


    private static String PAGINA_MENSAGE 	= "estaticos/mensajes/mensaje.htm";
    private static String PAGINA_SIN_SESION = "estaticos/mensajes/user_sin_sesion.htm";

}