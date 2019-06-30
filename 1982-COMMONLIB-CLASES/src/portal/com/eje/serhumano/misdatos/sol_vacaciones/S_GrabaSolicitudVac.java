// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:01:39
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_GrabaSolicitudVac.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

public class S_GrabaSolicitudVac extends MyHttpServlet
{

    public S_GrabaSolicitudVac()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("Entro al doPost de S_GrabaSolicitudVac");
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection ConOrigen = super.connMgr.getConnection("portal");
        java.sql.Connection ConDestino = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        if(ConDestino == null)
        {
            mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        } else
        {
            insTracking(req, "Grabación Solicitud".intern(), null);
            String dias = req.getParameter("dias");
            String id_sol = req.getParameter("id");
            String tipo_sol = "1";
            String diad = req.getParameter("diad");
            String mesd = req.getParameter("mesd");
            String yeard = req.getParameter("yeard");
            String hasta = req.getParameter("hasta");
            Consulta consulta = new Consulta(ConDestino);
            if(tipo_sol.equals("1"))
            {
                OutMessage.OutMessagePrint("<-----solicitud vacaciones update----->");
                String sql_up = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_solicitud_vacaciones SET fecha_desde =")).append("CONVERT(DATETIME, '").append(yeard).append("-").append(mesd).append("-").append(diad).append(" 00:00:00', 102),").append("fecha_hasta =").append(" CONVERT(DATETIME,'").append(hasta).append(" 00:00:00', 103),").append("dias =").append(dias).append(", estado =").append("'L'").append(" WHERE (id_sol = ").append(id_sol).append(")")));
                consulta.insert(sql_up);
                OutMessage.OutMessagePrint("<-----solicitud vacaciones update\n".concat(String.valueOf(String.valueOf(sql_up))));
            }
            consulta.close();
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
}