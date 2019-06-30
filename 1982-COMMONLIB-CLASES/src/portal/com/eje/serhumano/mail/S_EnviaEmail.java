package portal.com.eje.serhumano.mail;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.enviaCorreo;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class S_EnviaEmail extends MyHttpServlet
{

    public S_EnviaEmail()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection conexion = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        String template = "mail/mail_resp.html";
        SimpleHash modelRoot = new SimpleHash();
        userRut = new datosRut(conexion, user.getRutId());

        if(conexion != null)
        {
            insTracking(req, "Envio de e-mail", null);
            if(user.esValido())
            {
                Template templateMail = getTemplate("mail/info_mail.html");
                proper = ResourceBundle.getBundle("db");
                String dominio = proper.getString("portal.domain");
                String de = proper.getString("portal.mail.default");
                String asunto = req.getParameter("asunto");
                String para;
                para = para = req.getParameter("para");
                Vector cadena = new Vector();
                int x = 0;
                String direccion = "";
                para = para.substring(0, para.length() - 1);
                cadena = Tools.separaLista(para, ";");
                for(x = 0; x < cadena.size(); x++)
                {
                    direccion = (String)cadena.elementAt(x);
                    System.err.println("------>Para Direccion: ".concat(String.valueOf(String.valueOf(direccion))));
                    if(!de.equals("nn"))
                    {
                        modelRoot.put("para", para);
                        modelRoot.put("texto", req.getParameter("texto"));
                        modelRoot.put("nomuser", user.getName());
                        modelRoot.put("asunto", req.getParameter("asunto"));
                        X = new enviaCorreo(direccion, asunto, String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(de)))).append("@").append(dominio))), templateMail, modelRoot, "SerHumanoMail.htm");
                        template = "mail/mail_resp.html";
                    } else
                    {
                        template = "mail/mail_error.html";
                    }
                }

                Validar valida = new Validar();
                de = valida.validarDato(de, "nn");
                System.err.println(String.valueOf(String.valueOf((new StringBuilder("---->DE: ")).append(de).append("  ---->Para: ").append(para))));
            } else
            {
                mensaje.devolverPaginaSinSesion(resp, "E-Mail", "Tiempo de Sesi\363n expirado...");
            }
        } else
        {
            mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.retTemplate(resp,template,modelRoot);
        super.connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("\n**** Fin doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Usuario user;
    private enviaCorreo X;
    private ResourceBundle proper;
    private datosRut userRut;
    private Mensaje mensaje;
}