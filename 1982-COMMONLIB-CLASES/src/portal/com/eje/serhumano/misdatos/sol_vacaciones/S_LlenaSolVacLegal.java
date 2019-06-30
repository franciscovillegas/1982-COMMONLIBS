// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:01:39
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_LlenaSolVacLegal.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;

public class S_LlenaSolVacLegal extends MyHttpServlet
{

    public S_LlenaSolVacLegal()
    {
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("Entro al doPost de LlenaDolLegal");
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection ConOrigen = super.connMgr.getConnection("portal");
        java.sql.Connection ConDestino = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        String rut_user = user.getRutId();
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int ano = Fecha.get(1);
        int mes = Fecha.get(2) + 1;
        SimpleHash modelRoot = new SimpleHash();
        if(ConOrigen == null || ConDestino == null)
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        } else
        {
            insTracking(req, "Llenado de Sol. de Vacaciones".intern(), null);
            modelRoot.put("tipo_sol", "1");
            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Santiago, ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(" de ").append(ano)));
            modelRoot.put("fecha", strFechaHoy);
            datosRut datosRutTrab = new datosRut(ConDestino, user.getRutId());
            modelRoot.put("rut_trabajador", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(user.getRutId()))))).append("-").append(datosRutTrab.Digito_Ver))));
            modelRoot.put("nom_trabajador", datosRutTrab.Nombres);
            modelRoot.put("fecha_ingreso", datosRutTrab.FecIngreso);
            Consulta consulta = new Consulta(ConDestino);
            String sql_s = "SELECT COUNT(*) AS numRegs FROM eje_ges_solicitud_vacaciones WHERE (estado = 'PO')";
            consulta.exec(sql_s);
            consulta.next();
            OutMessage.OutMessagePrint("<-----numero de registros con estado PO: \n".concat(String.valueOf(consulta.getString("numRegs"))));
            if(!consulta.getString("numRegs").equals("0"))
            {
                String sql_s1 = "SELECT ISNULL(MAX(id_sol), 0) AS maximo FROM eje_ges_solicitud_vacaciones where estado='PO'";
                consulta.exec(sql_s1);
                consulta.next();
                int idsolic = 1;
                if(!consulta.getString("maximo").equals("0"))
                {
                    OutMessage.OutMessagePrint("<-----entra con num regs distinto de cero y maxmo diferente de cero: \n".concat(String.valueOf(consulta.getString("numRegs"))));
                    idsolic = consulta.getInt("maximo");
                    OutMessage.OutMessagePrint("<-----solicitud vacaciones ocupando registros PO actualizados ----->");
                    String sql_up = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_solicitud_vacaciones SET rut =")).append(rut_user).append(", ").append("fecha_desde = null").append(", ").append("fecha_hasta = null").append(", ").append("dias = null").append(", ").append("estado = 'O'").append(", ").append("fecha_sol = ").append("GETDATE()").append(" WHERE (id_sol = ").append(idsolic).append(")")));
                    consulta.insert(sql_up);
                    OutMessage.OutMessagePrint("<-----solicitud vacaciones refresh\n".concat(String.valueOf(String.valueOf(sql_up))));
                }
                modelRoot.put("id", String.valueOf(idsolic));
            } else
            {
                String sql_s2 = "SELECT ISNULL(MAX(id_sol), 0) AS maximo FROM eje_ges_solicitud_vacaciones";
                consulta.exec(sql_s2);
                consulta.next();
                int idsolic = 1;
                if(!consulta.getString("maximo").equals("0"))
                {
                    OutMessage.OutMessagePrint("<-----entra con num regs igual a cero y maximo diferente de cero: \n".concat(String.valueOf(consulta.getString("maximo"))));
                    idsolic = Integer.parseInt(consulta.getString("maximo")) + 1;
                    OutMessage.OutMessagePrint("<-----solicitud vacaciones insert----->");
                    String sql_ins = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_solicitud_vacaciones (id_sol, rut, fecha_desde, fecha_hasta, dias, estado, fecha_sol) VALUES (")).append(idsolic).append(", ").append(rut_user).append(", ").append("null").append(", ").append("null").append(", ").append("null").append(", ").append("'O'").append(", ").append("GETDATE()").append(")")));
                    consulta.insert(sql_ins);
                    OutMessage.OutMessagePrint("<-----Nueva solicitud vacaciones\n".concat(String.valueOf(String.valueOf(sql_ins))));
                } else
                {
                    OutMessage.OutMessagePrint("<-----primera solicitud vacaciones insert----->");
                    String sql_ins = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_solicitud_vacaciones (id_sol, rut, fecha_desde, fecha_hasta, dias, estado, fecha_sol) VALUES (")).append(idsolic).append(", ").append(rut_user).append(", ").append("null").append(", ").append("null").append(", ").append("null").append(", ").append("'O'").append(", ").append("GETDATE()").append(")")));
                    consulta.insert(sql_ins);
                    OutMessage.OutMessagePrint("<-----primera Nueva solicitud vacaciones\n".concat(String.valueOf(String.valueOf(sql_ins))));
                }
                modelRoot.put("id", String.valueOf(idsolic));
            }
            consulta.close();
            super.retTemplate(req,resp,"misdatos/sol_vacaciones/sol_feriado_legal.htm",modelRoot);
        }
        super.connMgr.freeConnection("portal", ConOrigen);
        super.connMgr.freeConnection("portal", ConDestino);
        OutMessage.OutMessagePrint("\n**** FIN doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void devolverPaginaMensage(HttpServletRequest req, HttpServletResponse resp, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        OutMessage.OutMessagePrint("**********!!!Menu Left!!!***********");
        modelRoot.put("mensaje", msg);
        super.retTemplate(req,resp,"WorkFlow/mensajelogin.html",modelRoot);
    }

    private void jbInit()
        throws Exception
    {
    }

    private Usuario user;
    private ResourceBundle proper;
}