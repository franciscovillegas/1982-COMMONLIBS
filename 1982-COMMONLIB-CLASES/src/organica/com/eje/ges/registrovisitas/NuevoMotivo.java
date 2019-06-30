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

public class NuevoMotivo extends MyHttpServlet
{

    public NuevoMotivo()
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
        OutMessage.OutMessagePrint("\nEntro a CargaPagina en NuevoMotivo");
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        String sql = "";
        Consulta consul = new Consulta(conexion);
        Consulta busca = new Consulta(conexion);
        sql = "SELECT grup_id AS id, grup_desc AS grupo FROM eje_ges_visita_motivo_grupo";
        consul.exec(sql);
        for(; consul.next(); OutMessage.OutMessagePrint("****************************"))
        {
            SimpleHash simplehash1 = new SimpleHash();
            simplehash1.put("id", consul.getString("id"));
            simplehash1.put("grupo", consul.getString("grupo"));
            simplelist.add(simplehash1);
        }

        modelRoot.put("detalle", simplelist);
        consul.close();
        busca.close();
        super.retTemplate(resp,"Gestion/RegistroVisitas/nuevo.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost Man Motivos");
    }

    public void GeneraCambios(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro al Insertar en NuevoMotivo");
        user = Usuario.rescatarUsuario(req);
        String sql = "";
        int maximo = 1;
        String desc_grupo = "";
        String desc_motivo = "";
        String id_grupo = "";
        String rut = user.getRutConsultado();
        Consulta consul = new Consulta(conexion);
        Consulta mantiene = new Consulta(conexion);
        String opcion = req.getParameter("radiobutton");
        OutMessage.OutMessagePrint("------------>OPCION: ".concat(String.valueOf(String.valueOf(opcion))));
        if(opcion.equals("G"))
        {
            sql = "SELECT max(convert(int,SUBSTRING(grup_id, 2, 3))) AS num FROM eje_ges_visita_motivo_grupo";
            consul.exec(sql);
            if(consul.next())
                maximo = consul.getInt("num") + 1;
            desc_grupo = req.getParameter("grup_desc");
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_visita_motivo_grupo (grup_id, grup_desc, rut_crea) VALUES ('G0")).append(maximo).append("', '").append(desc_grupo).append("', ").append(rut).append(")")));
            OutMessage.OutMessagePrint("----------->Nuevo Grupo: ".concat(String.valueOf(String.valueOf(sql))));
            mantiene.insert(sql);
        } else
        {
            sql = "SELECT MAX(convert(int,SUBSTRING(mot_id, 3, 2))) as num FROM eje_ges_visita_motivo";
            consul.exec(sql);
            if(consul.next())
                maximo = consul.getInt("num") + 1;
            desc_motivo = req.getParameter("mot_desc");
            id_grupo = req.getParameter("grupo");
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_visita_motivo (mot_id, mot_grupo, mot_desc, rut_crea) VALUES ('M0")).append(maximo).append("', '").append(id_grupo).append("', '").append(desc_motivo).append("', ").append(rut).append(")")));
            OutMessage.OutMessagePrint("----------->Nuevo Motivo: ".concat(String.valueOf(String.valueOf(sql))));
            mantiene.insert(sql);
        }
        consul.close();
        mantiene.close();
        resp.sendRedirect("/organica/servlet/ManMotivos");
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