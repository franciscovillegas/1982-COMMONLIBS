// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:01:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_EnviaSolicitud.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.DistribucionDias;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.Rut;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.MailOutlook;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class S_EnviaSolicitud extends MyHttpServlet
{

    public S_EnviaSolicitud()
    {
        RAIZ = null;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("Entro al doPost de EnviaSolicitud");
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection ConOrigen = super.connMgr.getConnection("portal");
        java.sql.Connection ConDestino = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        RAIZ = getServletContext().getRealPath("/html/mail_attach");
        Template templateMail = getTemplate("misdatos/sol_vacaciones/mensajecorreo.htm");
        SimpleHash modelRoot = new SimpleHash();
        if(ConDestino == null)
        {
            this.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        } else
        {
            insTracking(req, "Envio de Solicitud".intern(), null);
            String rut_destino = req.getParameter("rut_dest");
            String rut_user = user.getRutId();
            int peri_actual = 0;
            String dias = req.getParameter("dias");
            String tipo_sol = req.getParameter("tipo_sol");
            String diad = req.getParameter("diad");
            String mesd = req.getParameter("mesd");
            String yeard = req.getParameter("yeard");
            String hasta = req.getParameter("hasta");
            Rut datosRut = new Rut(ConDestino, user.getRutId());
            Rut datosDestino = new Rut(ConDestino, rut_destino);
            Consulta consulta = new Consulta(ConDestino);
            int idsolic = 1;
            String sql = "SELECT ISNULL(MAX(sol_id), 0) AS maximo FROM eje_ges_workflow_solicitud";
            consulta.exec(sql);
            consulta.next();
            if(!consulta.getString("maximo").equals("0"))
                idsolic = consulta.getInt("maximo") + 1;
            Consulta consultaOri = new Consulta(ConOrigen);
            sql = "SELECT MAX(periodo) AS periodo FROM eje_ges_vacaciones";
            consultaOri.exec(sql);
            if(consultaOri.next())
                peri_actual = consultaOri.getInt("periodo");
            consultaOri.close();
            DistribucionDias calculadias = new DistribucionDias(ConOrigen, peri_actual, Integer.parseInt(dias), rut_user);
            System.err.println("----->Tipo Sol: ".concat(String.valueOf(String.valueOf(tipo_sol))));
            if(tipo_sol.equals("1"))
            {
                OutMessage.OutMessagePrint("<-----Feriado legal----->");
                sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_workflow_solicitud (sol_id, sol_rut_solic, sol_fecha, sol_tipo,sol_dias, sol_fec_inicio, sol_fec_fin,sol_rut_dest, sol_unidad,sol_status, sol_sub_status) VALUES (")).append(idsolic).append(",").append(rut_user).append(",").append("GETDATE(),").append(tipo_sol).append(", ").append(dias).append(", ").append("CONVERT(DATETIME, '").append(yeard).append("-").append(mesd).append("-").append(diad).append(" 00:00:00', 102),").append(" CONVERT(DATETIME,'").append(hasta).append(" 00:00:00', 103),").append(rut_destino).append(",'").append(datosRut.Unidad_id).append("',0,6)")));
                OutMessage.OutMessagePrint("<-----Nueva Solicitud de Feriado legal\n".concat(String.valueOf(String.valueOf(sql))));
            } else
            {
                OutMessage.OutMessagePrint("<-----Feriado Progresivo----->");
                sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_workflow_solicitud (sol_id, sol_rut_solic, sol_fecha, sol_tipo,sol_dias,sol_rut_dest, sol_unidad,sol_status, sol_sub_status) VALUES (")).append(idsolic).append(",").append(rut_user).append(",").append("GETDATE(),").append(tipo_sol).append(", ").append(dias).append(", ").append(rut_destino).append(",'").append(datosRut.Unidad_id).append("',0,6)")));
            }
            OutMessage.OutMessagePrint("Nueva Solicitud\n ".concat(String.valueOf(String.valueOf(sql))));
            if(consulta.insert(sql))
            {
                if(tipo_sol.equals("1"))
                    sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_workflow_solicitud SET sol_dias_p1 = ")).append(calculadias.getDiasPeriodo1()).append(",").append("sol_periodo1 = ").append(calculadias.getPeriodo1()).append(",").append("sol_dias_p2 = ").append(calculadias.getDiasPeriodo2()).append(",").append("sol_periodo2 = ").append(calculadias.getPeriodo2()).append(",").append("sol_resto_dias = ").append(calculadias.getRestoDias()).append(",").append("sol_resto_peri1 = ").append(calculadias.getRestoPeriodo1()).append(",").append("sol_resto_peri2 = ").append(calculadias.getRestoPeriodo2()).append(" ").append("WHERE (sol_id = ").append(idsolic).append(")")));
                else
                    sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_workflow_solicitud SET sol_resto_dias = ")).append(calculadias.getRestoDias()).append(",").append("sol_resto_peri1 = ").append(calculadias.getRestoPeriodo1()).append(",").append("sol_resto_peri2 = ").append(calculadias.getRestoPeriodo2()).append(" ").append("WHERE (sol_id = ").append(idsolic).append(")")));
                OutMessage.OutMessagePrint("Actualizando Solicitud\n ".concat(String.valueOf(String.valueOf(sql))));
                consulta.insert(sql);
                proper = ResourceBundle.getBundle("db");
                String servidor = proper.getString("server.web");
                String puerto = proper.getString("puerto.web");
                String urlsolic = String.valueOf(String.valueOf((new StringBuilder("http://")).append(servidor).append(":").append(puerto).append("/firstfactors/portalrrhh/servlet/com.eje.serhumano.misdatos.sol_vacaciones.S_MuestraSolicitud?").append("id_sol=").append(idsolic).append("&rut_solic=").append(rut_user)));
                String mensaje = "Solicitud de Vacaciones\nPara aceptar o Rechazar esta Solicitud acceda a la Opci\363n WorkFlow-Solicitudes  Pendientes Aprobaci\363n.";
                String asunto = "Solicitud de Vacaciones de ";
                String maildestino = "";
                maildestino = datosDestino.Mail;
                asunto = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(asunto)))).append(datosRut.Nombres).append(" ").append(datosRut.Ap_paterno).append(" ").append(datosRut.Ap_materno)));
                MailOutlook mail = new MailOutlook();
                mail.enviarMail("Alexander Diaz", asunto, templateMail, modelRoot, RAIZ, "ATT01", servidor, mensaje);
                int numdias = Integer.parseInt(dias);
                if(datosRut.tiene_bono.equals("1") && numdias < 10)
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_workflow_empleados SET emp_tiene_bono = '0' WHERE (emp_rut = ")).append(user.getRutId()).append(")")));
                    consulta.insert(sql);
                    modelRoot.put("derecho", "1");
                }
                consulta.close();
            }
            OutMessage.OutMessagePrint("\n**** FIN doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
            resp.sendRedirect("/firstfactors/portalrrhh/servlet/com.eje.serhumano.misdatos.sol_vacaciones.S_MuestraFormulario?id=".concat(String.valueOf(String.valueOf(idsolic))));
        }
        super.connMgr.freeConnection("portal", ConOrigen);
        super.connMgr.freeConnection("portal", ConDestino);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"WorkFlow/mensajelogin.html",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
    private ResourceBundle proper;
    private String RAIZ;
}