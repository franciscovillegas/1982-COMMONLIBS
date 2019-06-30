package organica.com.eje.ges.gestion;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class SinOrganica extends MyHttpServlet
{
    public SinOrganica() { }

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
            insTracking(req, "Usuarios sin Orgánica".intern(),null );
            SimpleList UsuariosList = new SimpleList();
            SimpleHash UsuariosIter=new SimpleHash();

            Consulta unidades = new Consulta(Conexion);
            String query = "select t.rut,t.digito_ver,t.nombre,t.unidad from eje_ges_trabajador t where t.unidad not in (select unid_id from eje_ges_unidades)";
            unidades.exec(query);
            for(;unidades.next();UsuariosList.add(UsuariosIter)) 
            {  
               UsuariosIter = new SimpleHash();
               UsuariosIter.put("rut",valida.validarDato(unidades.getString("rut"))+"-"+valida.validarDato(unidades.getString("digito_ver")) );
               UsuariosIter.put("nombre",valida.validarDato(unidades.getString("nombre")) );
               UsuariosIter.put("unidad",valida.validarDato(unidades.getString("unidad")) );
            }
            modelRoot.put("lusuarios",UsuariosList);
            super.retTemplate(resp,"Gestion/SinOrganica.htm",modelRoot);
            
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Validar valida;
    private Mensaje mensaje;
}