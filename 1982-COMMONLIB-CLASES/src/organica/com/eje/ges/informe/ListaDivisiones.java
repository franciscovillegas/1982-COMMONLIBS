package organica.com.eje.ges.informe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
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
            modelRoot.put("FNum", new FormatoNumero());
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul.getString("div_id"));
                simplehash1.put("divi", consul.getString("div_desc"));
            }

            modelRoot.put("division", simplelist);
            consul.close();
            Consulta consul2 = new Consulta(conexion);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT cargo, descrip FROM eje_ges_cargos where empresa = '")).append(user.getEmpresa()).append("'")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul2.exec(sql);
            simplelist = new SimpleList();
            for(; consul2.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul2.getString("cargo"));
                simplehash1.put("desc", consul2.getString("descrip"));
            }

            modelRoot.put("cargos", simplelist);
            consul2.close();
            Consulta consul3 = new Consulta(conexion);
            sql = "SELECT motivo_id, motivo_desc FROM eje_ges_mot_retiro";
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul3.exec(sql);
            simplelist = new SimpleList();
            for(; consul3.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul3.getString("motivo_id"));
                simplehash1.put("desc", consul3.getString("motivo_desc"));
            }

            modelRoot.put("motivos", simplelist);
            consul3.close();
            modelRoot.put("empresa", user.getEmpresa());
            super.retTemplate(resp,"Gestion/Informes/main_info.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
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
        super.retTemplate(resp,"mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}