package organica.com.eje.ges.Buscar;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.datos.VerComponente;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.enviaCorreo;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class VerRut extends MyHttpServlet
{

    public VerRut()
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
        OutMessage.OutMessagePrint("\n************Entro al doPost de Solicitud");
        Usuario user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        String strRut = req.getParameter("rut");
        String modo = req.getParameter("modo");
        if(!user.esValido())
        {
            devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        } else
        {
            userRut = new Rut(conexion, strRut);
            String html = req.getParameter("htm");
            if(html == null || html == "")
                html = "Gestion/Buscar/ver_rut.htm";
            else
                html = "Gestion/" + html;
            OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
            userRut = new Rut(conexion, strRut);
            if(modo != null)
                modelRoot.put("modo", "1");
            modelRoot.put("ver", verComp.getSimpleHash());
            ControlAcceso control = new ControlAcceso(user);
            modelRoot.put("Control", new ControlAccesoTM(control));
            boolean permisoJerarquico = control.tienePermisoJerarquico(conexion, strRut);
            modelRoot.put("ver_expediente", permisoJerarquico);
            modelRoot.put("hacer_pmp", permisoJerarquico);
            modelRoot.put("rut", strRut);
            modelRoot.put("nombre", userRut.Nombres);
            modelRoot.put("unidad", userRut.Unidad);
            modelRoot.put("cargo", userRut.Cargo);
            modelRoot.put("foto", userRut.Foto);
            modelRoot.put("email", userRut.Email);
            modelRoot.put("mail", userRut.Mail);
            modelRoot.put("anexo", userRut.Anexo);
            modelRoot.put("rutsup", userRut.Sup_Rut);
            modelRoot.put("cargosup", userRut.Sup_Cargo);
            modelRoot.put("nomsup", userRut.Sup_Nombre);
            modelRoot.put("unidadsup", userRut.Sup_Unidad);
            modelRoot.put("emailsup", userRut.Sup_Email);
            modelRoot.put("mailsup", userRut.Sup_Mail);
            modelRoot.put("anexosup", userRut.Sup_Anexo);
            super.retTemplate(resp,html,modelRoot);
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

    private Rut userRut;
    private Tools tool;
    private enviaCorreo X;
    private VerComponente verComp;
    private Mensaje mensaje;
}