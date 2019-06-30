package portal.com.eje.serhumano.formularios;

import java.io.IOException;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.formularios.beneficios.BenefManager;
import portal.com.eje.serhumano.formularios.beneficios.SolBenefManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import portal.com.eje.tools.enviaCorreo;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class S_Beneficios extends MyHttpServlet
{

    public S_Beneficios()
    {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170111    	
//        Connection ConexDF = super.connMgr.getConnection("portal");
//        Connection ConexSH = super.connMgr.getConnection("portal");
        Connection ConexDF =  getConnMgr().getConnection("portal");
        Connection ConexSH =  getConnMgr().getConnection("portal");
        
        if(ConexDF != null && ConexSH != null)
            generaPagina(req, resp, ConexDF, ConexSH);
        else
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
//    	MMA 20170111 
//        super.connMgr.freeConnection("portal", ConexDF);
//        super.connMgr.freeConnection("portal", ConexSH);
        getConnMgr().freeConnection("porral", ConexDF);
        getConnMgr().freeConnection("porral", ConexSH);
        
    }

    protected void generaPagina(HttpServletRequest req, HttpServletResponse resp, Connection conexDF, Connection conexSH)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        SimpleHash modelRoot = new SimpleHash();
        SimpleHash simplehash1 = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        Validar valida = new Validar();
        BenefManager bMgr = new BenefManager(conexSH);
        SolBenefManager solMgr = new SolBenefManager(conexSH);
        user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Formulario Beneficios", "Tiempo de Sesi\363n expirado...");
        } else
        {
            userRut = new datosRut(conexDF, user.getRutId());
            String template = null;
            Template templateMail = null;
            String pBeneficios = req.getParameter("beneficios");
            int pBenefId = valida.validarNum(req.getParameter("benef_id"), 0);
            String pObserv = req.getParameter("observ");
            template = "formularios/form_beneficios.htm";
            templateMail = getTemplate("formularios/form_beneficios_email.htm");
            GregorianCalendar Fecha = new GregorianCalendar();
            java.util.Date date2 = Fecha.getTime();
            proper = ResourceBundle.getBundle("db");
            modelRoot.put("Servidor", proper.getString("portal.server"));
            modelRoot.put("rut", user.getRutId());
            modelRoot.put("nombre", userRut.Nombres);
            modelRoot.put("unidad", userRut.Unidad);
            modelRoot.put("cargo", userRut.Cargo);
            modelRoot.put("foto", userRut.Foto);
            modelRoot.put("ubicacion", userRut.UbicFisica);
            modelRoot.put("cargosup", userRut.Sup_Cargo);
            modelRoot.put("nomsup", userRut.Sup_Nombre);
            modelRoot.put("rut2", String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(userRut.Rut))))).append("-").append(userRut.Digito_Ver)));
            modelRoot.put("fecha", valida.validarFecha(date2));
            if(req.getParameter("envia") == null)
            {
                Consulta consul = bMgr.getBeneficios();
                modelRoot.put("beneficios", consul.getSimpleList());
                consul.close();
            } else
            {
                modelRoot.put("beneficios", pBeneficios);
                modelRoot.put("observ", pObserv);
                String origen = proper.getString("portal.mail.default");
                String dominio = proper.getString("portal.domain");
                String dest = proper.getString("portal.mail.form.dest");
                String email = dest + "@" + dominio;
                OutMessage.OutMessagePrint("*******>Email destino Form. Beneficios: ".concat(String.valueOf(String.valueOf(email))));
                int solNum = solMgr.addSolBeneficio(valida.validarNum(user.getRut().getRut()), pBenefId, pObserv);
                template = "formularios/form_resp.htm";
                if(solNum > 0)
                {
                    modelRoot.put("sol_num", String.valueOf(solNum));
                    X = new enviaCorreo(email, "Solicitud de Beneficio", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(userRut.Nombres)))).append("<").append(origen).append(">"))), templateMail, modelRoot, "form_beneficios.htm");
                } else
                {
                    modelRoot.put("error", bMgr.getError());
                }
            }
            super.retTemplate(resp,template,modelRoot);
        }
        OutMessage.OutMessagePrint("\n**** Fin generaPagina: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"mensaje.htm",modelRoot);
    }

    private datosRut userRut;
    private enviaCorreo X;
    private ResourceBundle proper;
    private Usuario user;
    private Tools tool;
}