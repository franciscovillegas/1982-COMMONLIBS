package organica.com.eje.ges.anexos.visor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.anexos.ManAnexo;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class VisorResul extends MyHttpServlet
{

    public VisorResul()
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de VisorResul");
        user = Usuario.rescatarUsuario(req);

        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Visor de Anexos", "Tiempo de Sesi\363n expirado...");
        } else
        {
            ManAnexo manAnexo = new ManAnexo(conexion);
            SimpleHash modelRoot = new SimpleHash();
            String paramEmpresa = req.getParameter("empresa");
            String paramUnidad = req.getParameter("unidad");
            modelRoot.put("empresa", paramEmpresa);
            modelRoot.put("unidad", paramUnidad);
            if(paramUnidad != null)
            {
                if(req.getParameter("ver_anexo") != null)
                {
                    modelRoot.put("anexos", manAnexo.getAnexosExistentes(paramUnidad));
                    modelRoot.put("ver_anexos", true);
                }
                if(req.getParameter("ver_fax") != null)
                {
                    modelRoot.put("faxes", manAnexo.getFaxExistentes(paramUnidad));
                    modelRoot.put("ver_faxes", true);
                }
                if(req.getParameter("ver_fono") != null)
                {
                    modelRoot.put("fonos", manAnexo.getFonosExistentes(paramUnidad));
                    modelRoot.put("ver_fonos", true);
                }
            }
            manAnexo.close();

            super.retTemplate(resp,"Gestion/Anexos/Visor/VisorResul.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
}