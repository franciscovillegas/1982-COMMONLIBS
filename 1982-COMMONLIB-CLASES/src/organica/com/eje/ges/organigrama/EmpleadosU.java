package organica.com.eje.ges.organigrama;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class EmpleadosU extends MyHttpServlet
{

    public EmpleadosU()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de Personal/Directo(EmpleadosU)");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            Consulta consul = new Consulta(conexion);
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM view_dotacion_directa WHERE (empresa = '")).append(empresa).append("') ").append("AND (tipo = 'U') ").append("AND (unidad = '").append(unidad).append("') ")));
            consul.exec(sql);
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("nombre", consul.getString("nombre"));
                simplehash1.put("rut", consul.getString("rut"));
                simplehash1.put("cargo", consul.getString("desc_cargo"));
                simplehash1.put("costo", Tools.setFormatNumber(consul.getString("costo")));
            }

            modelRoot.put("unidad", req.getParameter("desc"));
            modelRoot.put("detalle", simplelist);
            super.retTemplate(resp,"Gestion/Organizacion/personal.htm",modelRoot);
            consul.close();
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
        super.retTemplate(resp,"Gestion/Organizacion/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}