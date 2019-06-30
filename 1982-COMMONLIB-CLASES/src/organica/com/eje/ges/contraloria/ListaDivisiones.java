package organica.com.eje.ges.contraloria;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ListaDivisiones extends MyHttpServlet
{

    public ListaDivisiones()
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de ListaDivisiones");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT div_id, div_desc FROM eje_ges_division WHERE (div_empresa = '")).append(user.getEmpresa()).append("')")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul.getString("div_id"));
                simplehash1.put("division", consul.getString("div_desc"));
            }
            modelRoot.put("detalle", simplelist);
            consul.close();
            super.retTemplate(resp,"Contraloria/makeinforme.html",modelRoot);
        } else
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Contraloria/mensaje.htm",modelRoot);
    }

    private Usuario user;
}