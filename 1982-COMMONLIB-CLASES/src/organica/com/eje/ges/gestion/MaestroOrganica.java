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
import portal.com.eje.tools.ExtrasOrganica;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class MaestroOrganica extends MyHttpServlet
{
	private static final long serialVersionUID = 1L;
	
	public MaestroOrganica() { }

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
            
            SimpleList UnidadList = new SimpleList();
            SimpleHash UnidadIter=new SimpleHash();

            Consulta unidades = new Consulta(Conexion);
            String query = "select u.unid_id,u.unid_desc,j.nodo_padre,u2.unid_desc as unid_descp from (eje_ges_unidades u inner join eje_ges_jerarquia j on u.unid_id=j.nodo_id) inner join eje_ges_unidades u2 on j.nodo_padre=u2.unid_id order by u.unid_desc";
            unidades.exec(query);
            String rutJefe = null,nombreJefe = null;
            for(;unidades.next();UnidadList.add(UnidadIter)) 
            {  
               UnidadIter = new SimpleHash();
               UnidadIter.put("cunid",valida.validarDato(unidades.getString("unid_id")) );
               UnidadIter.put("dunid",valida.validarDato(unidades.getString("unid_desc")) );
               UnidadIter.put("cunidp",valida.validarDato(unidades.getString("nodo_padre")) );
               UnidadIter.put("dunidp",valida.validarDato(unidades.getString("unid_descp")) );
               
               if(ExtrasOrganica.UnidadTieneEncargadov2 (unidades.getString("unid_id"), Conexion))
               { rutJefe = ExtrasOrganica.UnidadEncargadov2 (unidades.getString("unid_id"), Conexion);
                 nombreJefe = ExtrasOrganica.NombreEncargadoUnidad (rutJefe, Conexion);
                 rutJefe+= "-" + ExtrasOrganica.DigVerEncargadoUnidad (rutJefe, Conexion);
               }
               else
               { rutJefe= "No tiene"; nombreJefe="No tiene"; }
               
               UnidadIter.put("rjunid",rutJefe );
               UnidadIter.put("njunid",nombreJefe );
            }
            modelRoot.put("lunidades",UnidadList);
            
            super.retTemplate(resp,"Gestion/MaestroOrganica.htm",modelRoot);
            
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