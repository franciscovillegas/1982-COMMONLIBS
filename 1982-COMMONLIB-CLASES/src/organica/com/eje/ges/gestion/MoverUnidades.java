package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.gestion.mantencion.MoverUnidad;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class MoverUnidades extends MyHttpServlet
{

    public MoverUnidades()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
        {
            String valor = req.getParameter("id_unidad");
            OutMessage.OutMessagePrint("*valor: ".concat(String.valueOf(String.valueOf(valor))));
            if(Conexion != null)
                CargaPagina(req, resp, Conexion);
            else
                devolverPaginaError(resp, "Error de conexi\363n a la BD");
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        ControlAcceso control = new ControlAcceso(user);
        String id_unidad = req.getParameter("unidad");
        String unidad = req.getParameter("desc_unidad");
        String id_empresa = req.getParameter("empresa");
        String id_unidad_padre = req.getParameter("unidad_padre");
        String unidad_padre = req.getParameter("desc_uni_padre");
        String id_empresa_padre = req.getParameter("empresa_padre");
        Consulta emp = new Consulta(Conexion);
        String Query = "";
        if(control.tienePermiso("df_mant_unidad"))
        {
            OutMessage.OutMessagePrint("entro");
            Query = String.valueOf(String.valueOf((new StringBuilder("SELECT descrip FROM eje_ges_empresa where empresa = '")).append(id_empresa).append("' ")));
            emp.exec(Query);
            if(emp.next())
            {
                modelRoot.put("linka", "1");
                modelRoot.put("desc_hijo_emp", emp.getString("descrip"));
                modelRoot.put("id_hijo_emp", id_empresa);
                modelRoot.put("desc_hijo_uni", unidad);
                modelRoot.put("id_hijo_uni", id_unidad);
                Query = String.valueOf(String.valueOf((new StringBuilder("SELECT descrip FROM eje_ges_empresa where empresa = '")).append(id_empresa_padre).append("' ")));
                emp.exec(Query);
                if(emp.next())
                {
                    modelRoot.put("linkp", "1");
                    modelRoot.put("desc_padre_emp", emp.getString("descrip"));
                    modelRoot.put("id_padre_emp", id_empresa_padre);
                    modelRoot.put("desc_padre_uni", unidad_padre);
                    modelRoot.put("id_padre_uni", id_unidad_padre);
                    if("1".equals(req.getParameter("grabar")))
                    {
                        Query = "select count(*) as cant from eje_ges_mover_jerarquia where estado=0";
                        emp.exec(Query);
                        emp.next();
                        if(emp.getInt("cant") >= 1)
                        {
                            modelRoot.put("alerta", "1");
                        } else
                        {
                            Query = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_mover_jerarquia     (fecha, fecha_activacion, unidad, empresa, unidad_padre,      empresa_padre, usuario)  VALUES (getdate(), +getdate() , '")).append(id_unidad).append("', '").append(id_empresa).append("', '").append(id_unidad_padre).append("', '").append(id_empresa_padre).append("', '").append(user.getRutUsuario()).append("') ")));
                            if(emp.insert(Query))
                            {
                                modelRoot.put("mensaje", "Su operacion se efectu\363 con \351xito");
                                modelRoot.put("linka", "0");
                                modelRoot.put("linkp", "0");
                                modelRoot.put("desc_padre_emp", "");
                                modelRoot.put("id_padre_emp", "");
                                modelRoot.put("desc_padre_uni", "");
                                modelRoot.put("id_padre_uni", "");
                                modelRoot.put("desc_hijo_emp", "");
                                modelRoot.put("id_hijo_emp", "");
                                modelRoot.put("desc_hijo_uni", "");
                                modelRoot.put("id_hijo_uni", "");
                                MoverUnidad x = new MoverUnidad(Conexion);
                                MoverUnidades _tmp = this;
                                Usuario.SuperNodo = null;
                                Usuario.SuperNodo = null;
                            } else
                            {
                                modelRoot.put("mensaje", "Su operacion se fall\363");
                            }
                        }
                    }
                }
            }
        }
        emp.close();
        super.retTemplate(resp,"Gestion/MantUnidades/MoverUnidad.htm",modelRoot);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"mensaje.htm",modelRoot);
    }

    private void devolverPaginaError(HttpServletResponse resp, String msg)
    {
        try
        {
            PrintWriter printwriter = resp.getWriter();
            resp.setContentType("text/html");
            printwriter.println("<html>");
            printwriter.println("<head>");
            printwriter.println("<title>Valores recogidos en el formulario</title>");
            printwriter.println("</head>");
            printwriter.println("<body>");
            printwriter.println(msg);
            printwriter.println("</body>");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
        }
        catch(IOException e)
        {
            OutMessage.OutMessagePrint("Error al botar la pagina");
        }
    }

    private Usuario user;
    private Mensaje mensaje;
}