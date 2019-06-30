package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class TablaIpc extends MyHttpServlet
{

    public TablaIpc()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de TablaIpc");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strUltimos = req.getParameter("ultimos");
        strUltimos = valida.validarDato(strUltimos, "12");
        Consulta consul = new Consulta(Conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT top ")).append(strUltimos).append(" *").append(" FROM eje_ges_ipc ORDER BY periodo desc, mes desc ")));
        consul.exec(sql);
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("ultimos", strUltimos);
        SimpleList simplelist = new SimpleList();
        SimpleHash simplehash1;
        for(; consul.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("mes", valida.validarDato(consul.getValor("mes")));
            simplehash1.put("periodo", valida.validarDato(consul.getString("periodo")));
            simplehash1.put("ipc", valida.validarDato(consul.getString("ipc")));
            simplehash1.put("ipc_acum", valida.validarDato(consul.getString("ipc_acum")));
        }

        modelRoot.put("ipc", simplelist);
        consul.close();
        super.retTemplate(resp,"Gestion/Expediente/TablaIpc.htm",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", Conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/Expediente/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}