package portal.com.eje.serhumano.search;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetProp;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.search:
//            FotosPersonalUnidad

public class S_SearchOrganic extends MyHttpServlet
{

    public S_SearchOrganic()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(SessionMgr.rescatarUsuario(req).esValido())
            DespResultado(req, resp, Conexion);
        else
            super.mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        super.connMgr.freeConnection("portal", Conexion);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio DespResultado: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        String unidad = req.getParameter("unidad");
        String empresa = req.getParameter("empresa");
        OutMessage.OutMessagePrint("Unidad : ".concat(String.valueOf(String.valueOf(unidad))));
        OutMessage.OutMessagePrint("Empresa: ".concat(String.valueOf(String.valueOf(empresa))));
        fotos = new FotosPersonalUnidad();
        modelRoot.put("varios", fotos.GetFotosPersonalUnidad(Conexion, unidad, empresa));
        userRut = new datosRut(Conexion, fotos.RutSup);
        modelRoot.put("rut", fotos.RutSup);
        modelRoot.put("foto", userRut.Foto);
        modelRoot.put("cargosup", userRut.Cargo);
        modelRoot.put("nomsup", userRut.Nombres);
        modelRoot.put("unidadsup", userRut.Unidad);
        modelRoot.put("emailsup", userRut.Email);
        modelRoot.put("mailsup", userRut.Mail);
        modelRoot.put("anexosup", userRut.Anexo);
        modelRoot.put("mision", fotos.MisionUnidad);
        modelRoot.put("unid_desc", fotos.DescUnidad);
        modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
        super.retTemplate(resp,"buscar/organica/busq_organica.htm",modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin DespResultado: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private datosRut userRut;
    private FotosPersonalUnidad fotos;
}