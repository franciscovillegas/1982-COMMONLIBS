package organica.com.eje.ges.gestion.dimension;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.Validar;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class ManUnidadesDimension extends MyHttpServlet
{
    public ManUnidadesDimension() { }

    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {   doGet(req, resp); }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {   DespResultado(req, resp); }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {   java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {   
            valida = new Validar();
            SimpleHash modelRoot = new SimpleHash();
            Dimension_Manager dimension = new Dimension_Manager(Conexion);

            String accion =  valida.validarDato(req.getParameter("accion"),"D");
            String dimension_id =  valida.validarDato(req.getParameter("dimensiones"),"0");
            String dimension_text =  valida.validarDato(req.getParameter("texto"),"0");
            
            if(accion.equals("N")) { Nuevo(req,resp,Conexion); }
            else if(accion.equals("M")) { Modificar(req,resp,Conexion); }
            else if(accion.equals("E")) { Eliminar(req,resp,Conexion); }
            else if(accion.equals("V")) { Valores(req,resp,Conexion); }
            else if(accion.equals("NV")) { NuevoVal(req,resp,Conexion); }
            else if(accion.equals("MV")) { ModificarVal(req,resp,Conexion); }
            else if(accion.equals("EV")) { EliminarVal(req,resp,Conexion); }
            
            modelRoot.put("dimensiones", dimension.GetListDimensiones(dimension_id));
            modelRoot.put("valoresdimensiones", dimension.GetListDimensionesValores(dimension_id));
            
            modelRoot.put("dimid", dimension_id);
            modelRoot.put("dimtxt", dimension_text);
            modelRoot.put("dimtipo", dimension.XmlGetTipoDimension(dimension_id));

            super.retTemplate(resp,"Gestion/Dimension/AdminDimension.htm",modelRoot);
        } 
        else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private void Nuevo(HttpServletRequest req, HttpServletResponse resp,Connection con)
    throws ServletException, IOException
    {
    	System.out.println("---- Inicio Nueva Dimension ----");
    	Dimension_Manager dimension = new Dimension_Manager(con);
    	String valor =  valida.validarDato(req.getParameter("fndimension"));
    	String tipo =  valida.validarDato(req.getParameter("fntipo"));
    	String cardinalidad =  valida.validarDato(req.getParameter("fncardinalidad"));
    	dimension.GetInsertDimensiones(valor,tipo,cardinalidad);
    	System.out.println("---- Fin Nueva Dimension ----");
    }

    private void Modificar(HttpServletRequest req, HttpServletResponse resp,Connection con)
    throws ServletException, IOException
    {
    	System.out.println("---- Inicio Modificacion Dimension ----");
    	Dimension_Manager dimension = new Dimension_Manager(con);
    	int id = Integer.parseInt(valida.validarDato(req.getParameter("iddimension")));
    	String valor =  valida.validarDato(req.getParameter("fmdimension"));
    	dimension.GetUpdateDimensiones(id,valor);
    	System.out.println("---- Fin Modificacion Dimension ----");
    }
    
    private void Eliminar(HttpServletRequest req, HttpServletResponse resp,Connection con)
    throws ServletException, IOException
    {
    	System.out.println("---- Inicio Eliminacion Dimension ----");
    	Dimension_Manager dimension = new Dimension_Manager(con);
    	String dimensiones[] = req.getParameterValues("fedimensiones");
    	for (int i=0; i< dimensiones.length; i++)
           dimension.GetDeleteDimensiones(Integer.parseInt(dimensiones[i].toString()));
    	System.out.println("---- Fin Eliminacion Dimension ----");
    }
    
    private void Valores(HttpServletRequest req, HttpServletResponse resp,Connection con)
    throws ServletException, IOException
    {
    	System.out.println("---- Inicio Valores Dimension ----");
    	String dimension =  valida.validarDato(req.getParameter("fndimension"));
    	System.out.println("Grabamos la dimension : " + dimension);
    	System.out.println("---- Fin Valores Dimension ----");
    }

    private void NuevoVal(HttpServletRequest req, HttpServletResponse resp,Connection con)
    throws ServletException, IOException
    {
    	System.out.println("---- Inicio Nueva Dimension ----");
    	Dimension_Manager dimension = new Dimension_Manager(con);
    	String dim =  valida.validarDato(req.getParameter("dimensiones"));
    	String valor =  valida.validarDato(req.getParameter("fnvaldimension"));
    	dimension.GetInsertValoresDimensiones(dim,valor);
     	System.out.println("---- Fin Nueva Dimension ----");
    }

    private void ModificarVal(HttpServletRequest req, HttpServletResponse resp,Connection con)
    throws ServletException, IOException
    {
    	System.out.println("---- Inicio Modificacion Dimension ----");
    	Dimension_Manager dimension = new Dimension_Manager(con);
    	String idi =  valida.validarDato(req.getParameter("fevalvalores"));
    	String valor =  valida.validarDato(req.getParameter("fmvaldimension"));
    	dimension.GetUpdateValoresDimensiones(idi,valor);
    	System.out.println("---- Fin Modificacion Dimension ----");
    }
    
    private void EliminarVal(HttpServletRequest req, HttpServletResponse resp,Connection con)
    throws ServletException, IOException
    {
    	System.out.println("---- Inicio Eliminacion Dimension ----");
    	Dimension_Manager dimension = new Dimension_Manager(con);
    	String dimensiones[] = req.getParameterValues("fevalvalores");
    	for (int i=0; i< dimensiones.length; i++)
           dimension.GetDeleteValoresDimensiones(Integer.parseInt(dimensiones[i].toString()));
    	System.out.println("---- Fin Eliminacion Dimension ----");
    }

    private Usuario user;
    private Validar valida;
}