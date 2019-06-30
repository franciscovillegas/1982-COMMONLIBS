package organica.com.eje.ges.registrovisitas;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class BuscaContacto extends MyHttpServlet
{

    public BuscaContacto()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        doPost(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de Busca Contacto");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            Consulta consul = new Consulta(conexion);
            String rut_visita = req.getParameter("rut_visita");
            String nom_visita = req.getParameter("nom_visita");
            String contacto = req.getParameter("nom_contacto");
            String empresa = req.getParameter("empresa");
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_trabajadores.rut, eje_ges_trabajadores.digito_ver,eje_ges_trabajadores.nombre,eje_ges_trabajadores.anexo,eje_ges_unidades.unid_desc AS unidad,eje_ges_trabajadores.unidad AS unid_id,eje_ges_trabajadores.empresa FROM eje_ges_trabajador INNER JOIN eje_ges_unidades ON eje_ges_trabajadores.empresa = eje_ges_unidades.unid_empresa AND eje_ges_trabajadores.unidad = eje_ges_unidades.unid_id WHERE (eje_ges_trabajadores.nombre LIKE '%")).append(contacto).append("%') AND ")));
            if(empresa == null)
                sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("(eje_ges_trabajadores.empresa = '").append(user.getEmpresa()).append("')")));
            else
                sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("(eje_ges_trabajadores.empresa = '").append(empresa).append("')")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consul.getString("rut"))))).append("-").append(consul.getString("digito_ver")))));
                simplehash1.put("xrut", consul.getString("rut"));
                simplehash1.put("nombre", consul.getString("nombre").trim());
                simplehash1.put("unidad", consul.getString("unidad"));
                simplehash1.put("unid_id", consul.getString("unid_id"));
                simplehash1.put("empresa", consul.getString("empresa"));
                simplehash1.put("anexo", consul.getString("anexo"));
                simplehash1.put("rut_v", rut_visita);
                simplehash1.put("nom_v", nom_visita.trim());
            }

            modelRoot.put("detalle", simplelist);
            modelRoot.put("rut_v", rut_visita);
            modelRoot.put("nom_v", nom_visita.trim());
            consul.close();
            super.retTemplate(resp,"Gestion/RegistroVisitas/resultado.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost Busca Contacto");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/RegistroVisitas/mensaje.htm",modelRoot);
    }
    
    private Usuario user;
    private Mensaje mensaje;
}