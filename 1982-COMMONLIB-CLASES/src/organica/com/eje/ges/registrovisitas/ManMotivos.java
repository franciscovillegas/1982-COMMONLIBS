package organica.com.eje.ges.registrovisitas;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ManMotivos extends MyHttpServlet
{

    public ManMotivos()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
            CargaPagina(req, resp, Conexion);
        else
            mensaje.devolverPaginaMensage(resp, "RegistroVisitas/mensaje.htm", "", "Tiempo de Sesi\363n expirado...");
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
            GeneraCambios(req, resp, Conexion);
        else
            mensaje.devolverPaginaMensage(resp, "RegistroVisitas/mensaje.htm", "", "Tiempo de Sesi\363n expirado...");
        connMgr.freeConnection("portal", Conexion);
    }

    public void CargaPagina(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a Mostrar Data para Mantener Motivos");
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        SimpleList datos = new SimpleList();
        String sql = "";
        Consulta consul = new Consulta(conexion);
        Consulta busca = new Consulta(conexion);
        sql = "SELECT grup_id AS id, grup_desc AS grupo FROM eje_ges_visita_motivo_grupo";
        consul.exec(sql);
        String layer = "";
        SimpleHash simplehash1;
        for(; consul.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            layer = consul.getString("id");
            simplehash1.put("id", consul.getString("id"));
            simplehash1.put("grupo", consul.getString("grupo"));
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT mot_id, mot_grupo, mot_desc FROM eje_ges_visita_motivo WHERE (mot_grupo = '")).append(consul.getString("id")).append("')")));
            busca.exec(sql);
            datos = new SimpleList();
            SimpleHash simplehash2;
            for(; busca.next(); datos.add(simplehash2))
            {
                simplehash2 = new SimpleHash();
                simplehash2.put("grup", consul.getString("id"));
                simplehash2.put("m_id", busca.getString("mot_id"));
                simplehash2.put("m_desc", busca.getString("mot_desc"));
            }

            simplehash1.put("datos", datos);
        }

        modelRoot.put("detalle", simplelist);
        modelRoot.put("lay", layer);
        consul.close();
        busca.close();
        super.retTemplate(resp,"Gestion/RegistroVisitas/manmotivos.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost Man Motivos");
    }

    public void GeneraCambios(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro al a insertar RegistroVisita");
        user = Usuario.rescatarUsuario(req);
        String sql = "";
        String desc_grupo = "";
        String desc_motivo = "";
        String rut = user.getRutConsultado();
        Consulta consul = new Consulta(conexion);
        Consulta busca = new Consulta(conexion);
        Consulta mantiene = new Consulta(conexion);
        sql = "SELECT grup_id AS id, grup_desc AS grupo FROM eje_ges_visita_motivo_grupo";
        consul.exec(sql);
        while(consul.next()) 
        {
            desc_grupo = req.getParameter(consul.getString("id"));
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_visita_motivo_grupo SET grup_desc = '")).append(desc_grupo).append("', rut_crea = ").append(rut).append(" ").append("WHERE (grup_id = '").append(consul.getString("id")).append("')")));
            mantiene.insert(sql);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT mot_id, mot_grupo, mot_desc FROM eje_ges_visita_motivo WHERE (mot_grupo = '")).append(consul.getString("id")).append("')")));
            busca.exec(sql);
            for(; busca.next(); mantiene.insert(sql))
            {
                OutMessage.OutMessagePrint("-->motivo: ".concat(String.valueOf(String.valueOf(busca.getString("mot_id")))));
                desc_motivo = req.getParameter(busca.getString("mot_id"));
                sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_visita_motivo SET mot_desc = '")).append(desc_motivo).append("', rut_crea = ").append(rut).append(" ").append("WHERE (mot_id = '").append(busca.getString("mot_id")).append("') ").append("AND (mot_grupo = '").append(consul.getString("id")).append("')")));
            }

        }
        consul.close();
        busca.close();
        mantiene.close();
        CargaPagina(req, resp, conexion);
        OutMessage.OutMessagePrint("Fin de doPost Genera Cambios Manteniendo Motivos");
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