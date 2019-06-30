package organica.com.eje.ges.indicadores;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class RelacionInd extends MyHttpServlet
{

    public RelacionInd()
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
        OutMessage.OutMessagePrint("\nEntro Lista de Indicadores");
        user = Usuario.rescatarUsuario(req);
        String unidad = user.getUnidad();
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("unidad", unidad);
        super.retTemplate(resp,"Gestion/Indicadores/ListaInd.html",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
}