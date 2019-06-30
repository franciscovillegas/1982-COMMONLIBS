package portal.com.eje.serhumano.formularios;

import java.io.IOException;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

public class S_Permisos extends MyHttpServlet
{

    public S_Permisos()
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
//    	MMA 20170111
//    	Connection Conexion = connMgr.getConnection("portal");
        Connection Conexion =  getConnMgr().getConnection("portal");
        if(Conexion != null)
            generaPagina(req, resp, Conexion);
        else
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
//    	MMA 20170111
//      super.connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("porral", Conexion);
    }

    protected void generaPagina(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        Validar valida = new Validar();
        user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Formulario Permiso", "Tiempo de Sesi\363n expirado...");
        } else
        {
        insTracking(req, "Formulario de Permisos".intern(), null);
            userRut = new datosRut(conexion, user.getRutId());
            String template = null;
            Template templateMail = null;
            template = "formularios/form_permiso.htm";
            templateMail = getTemplate("formularios/form_permiso_email.htm");
            String sql = "";
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            GregorianCalendar Fecha = new GregorianCalendar();
            java.util.Date date2 = Fecha.getTime();
            proper = ResourceBundle.getBundle("db");
            modelRoot.put("Servidor", proper.getString("portal.server"));
            modelRoot.put("rut", user.getRutId());
            modelRoot.put("nombre", userRut.Nombres);
            modelRoot.put("id_unidad", userRut.Id_Unidad);
            modelRoot.put("unidad", userRut.Unidad);
            modelRoot.put("cargo", userRut.Cargo);
            modelRoot.put("foto", userRut.Foto);
            modelRoot.put("ubicacion", userRut.UbicFisica);
            modelRoot.put("cargosup", userRut.Sup_Cargo);
            modelRoot.put("nomsup", userRut.Sup_Nombre);
            modelRoot.put("rut2", String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(userRut.Rut))))).append("-").append(userRut.Digito_Ver)));
            modelRoot.put("fecha", valida.validarFecha(date2));
            OutMessage.OutMessagePrint("hasta     ".concat(String.valueOf(String.valueOf(req.getParameter("hasta")))));
            OutMessage.OutMessagePrint("reintegro ".concat(String.valueOf(String.valueOf(req.getParameter("reintegro")))));
            if(req.getParameter("envia") != null && req.getParameter("envia").equals("1"))
            {
                if(req.getParameter("radiobutton") != null)
                    modelRoot.put("rad", req.getParameter("radiobutton"));
                if(req.getParameter("desde") != null)
                    modelRoot.put("desde", req.getParameter("desde"));
                if(req.getParameter("hasta") != null)
                    modelRoot.put("hasta", req.getParameter("hasta"));
                if(req.getParameter("reintegro") != null)
                    modelRoot.put("reintegro", req.getParameter("reintegro"));
                if(req.getParameter("calle") != null)
                    modelRoot.put("calle", req.getParameter("calle"));
                if(req.getParameter("numero") != null)
                    modelRoot.put("numero", req.getParameter("numero"));
                if(req.getParameter("fono") != null)
                    modelRoot.put("fono", req.getParameter("fono"));
                if(req.getParameter("villa") != null)
                    modelRoot.put("villa", req.getParameter("villa"));
                if(req.getParameter("region") != null)
                    modelRoot.put("region", req.getParameter("region"));
                if(req.getParameter("ciudad") != null)
                    modelRoot.put("ciudad", req.getParameter("ciudad"));
                if(req.getParameter("comuna") != null)
                    modelRoot.put("comuna", req.getParameter("comuna"));
                if(req.getParameter("conyuge") != null)
                    modelRoot.put("conyuge", req.getParameter("conyuge"));
                if(req.getParameter("hijo") != null)
                    modelRoot.put("hijo", req.getParameter("hijo"));
                if(req.getParameter("fec_nac") != null)
                    modelRoot.put("fec_nac", req.getParameter("fec_nac"));
                if(req.getParameter("nombre") != null)
                    modelRoot.put("nombre_def", req.getParameter("nombre"));
                if(req.getParameter("parentesco") != null)
                    modelRoot.put("parentesco", req.getParameter("parentesco"));
                if(req.getParameter("celular") != null)
                    modelRoot.put("celular", req.getParameter("celular"));
                if(req.getParameter("casa") != null)
                    modelRoot.put("casa", req.getParameter("casa"));
                template = "formularios/form_resp.htm";
            }
            super.retTemplate(req,resp,template,modelRoot);
            if(req.getParameter("envia") != null)
            {
                String origen = proper.getString("portal.mail.default");
                String dominio = proper.getString("portal.domain");
                String dest = proper.getString("portal.mail.form.dest");
                String email = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(dest)))).append("@").append(dominio)));
                OutMessage.OutMessagePrint("*******>Email destino Sol. Permisos: ".concat(String.valueOf(String.valueOf(email))));
                X = new enviaCorreo(email, "Solicitud de Permiso", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(userRut.Nombres)))).append("<").append(origen).append(">"))), templateMail, modelRoot, "form_permiso.htm");
            }
        }
    }

    private datosRut userRut;
    private enviaCorreo X;
    private ResourceBundle proper;
    private Usuario user;
    private Tools tool;
}