// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:01:39
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_LlenaSolProgresiva.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.DestinatarioSol;
import portal.com.eje.serhumano.misdatos.sol_vacaciones.beans.Rut;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;

public class S_LlenaSolProgresiva extends MyHttpServlet
{

    public S_LlenaSolProgresiva()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("Entro al doPost de LlenaSolProgresiva");
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection ConOrigen = super.connMgr.getConnection("portal");
        java.sql.Connection ConDestino = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        String rut_user = "";
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        String template = "misdatos/sol_vacaciones/sol_feriado_prog.htm";
        SimpleHash modelRoot = new SimpleHash();
        if(ConOrigen == null || ConDestino == null)
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        } else
        {
            insTracking(req, "Llenado de Sol. Progresiva", null);
            rut_user = user.getRutId();
            DestinatarioSol destinatario = new DestinatarioSol(ConOrigen, ConDestino, rut_user);
            System.err.println("--------->Destinatario: ".concat(String.valueOf(String.valueOf(destinatario.getRutdest()))));
            if(!destinatario.getRutdest().equals("SJ"))
                if(destinatario.getRutdest().equals("JND"))
                {
                    template = "misdatos/sol_vacaciones/BuscaJefe.htm";
                    Rut datosJefe = new Rut(ConDestino, destinatario.getJefeNoDisponible());
                    modelRoot.put("nom_jefe", datosJefe.Nombre_completo);
                    modelRoot.put("tipo_sol", "2");
                } else
                {
                    Rut datosRut = new Rut(ConDestino, destinatario.getRutdest());
                    strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
                    modelRoot.put("fecha", strFechaHoy);
                    modelRoot.put("rut_dest", destinatario.getRutdest());
                    modelRoot.put("rut_label", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(destinatario.getRutdest()))))).append("-").append(datosRut.Digito_ver))));
                    modelRoot.put("nom_jefe", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(datosRut.Nombres)))).append(" ").append(datosRut.Ap_paterno).append(" ").append(datosRut.Ap_materno))));
                    modelRoot.put("cargo_jefe", datosRut.Cargo_desc);
                    if(datosRut.Nombres == null)
                        modelRoot.put("error", "1");
                    else
                        modelRoot.put("tipo_sol", "2");
                    Rut datos_user = new Rut(ConDestino, user.getRutId());
                    modelRoot.put("unidad", datos_user.Unidad_desc);
                    Rut datosRutTrab = new Rut(ConDestino, user.getRutId());
                    modelRoot.put("rut_trabajador", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(user.getRutId()))))).append("-").append(datosRutTrab.Digito_ver))));
                    modelRoot.put("nom_trabajador", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(datosRutTrab.Nombres)))).append(" ").append(datosRutTrab.Ap_paterno).append(" ").append(datosRutTrab.Ap_materno))));
                    int idsolic = 1;
                    Consulta consulta = new Consulta(ConDestino);
                    String sql = "SELECT ISNULL(MAX(sol_id), 0) AS maximo FROM eje_ges_workflow_solicitud";
                    consulta.exec(sql);
                    consulta.next();
                    if(!consulta.getString("maximo").equals("0"))
                        idsolic = consulta.getInt("maximo") + 1;
                    modelRoot.put("id", String.valueOf(idsolic));
                    consulta.close();
                }
            super.retTemplate(resp,template,modelRoot);
        }
        OutMessage.OutMessagePrint("\n**** FIN doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        super.connMgr.freeConnection("portal", ConOrigen);
        super.connMgr.freeConnection("portal", ConDestino);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        OutMessage.OutMessagePrint("**********!!!Menu Left!!!***********");
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"WorkFlow/mensajelogin.html",modelRoot);
    }

    private Usuario user;
    private ResourceBundle proper;
}