package portal.com.eje.serhumano.misdatos;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.misdatos:
//            Vaca_Manager

public class S_DetalleVaca extends MyHttpServlet
{

    public S_DetalleVaca()
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
        Connection Conexion = super.connMgr.getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        if(Conexion != null)
        {
            generaTab(req, resp, Conexion);
            insTracking(req, "Detalle de Vacaciones".intern(), "");
        } else
        {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutId();
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Detalle Vacaciones", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String agno_periodo = null;
            SimpleHash modelRoot = new SimpleHash();
            Validar valida = new Validar();
            agno_periodo = req.getParameter("periodo");
            Vaca_Manager cartola = new Vaca_Manager(Conexion);
            Consulta detalle = cartola.GetDetallePeriodoWp(rut, agno_periodo);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; detalle.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("mesper", tool.RescataMes(Integer.parseInt(detalle.getString("mes_periodo"))));
                simplehash1.put("dcd", tool.setFormatNumber(valida.validarDato(detalle.getString("dias_con_derecho"), "0")));
                simplehash1.put("dp", tool.setFormatNumber(valida.validarDato(detalle.getString("dias_pendientes"), "0")));
                simplehash1.put("ddp", tool.setFormatNumber(valida.validarDato(detalle.getString("dias_del_periodo"), "0")));
                simplehash1.put("dpp", tool.setFormatNumber(valida.validarDato(detalle.getString("dias_prox_periodo"), "0")));
                simplehash1.put("sap", tool.setFormatNumber(valida.validarDato(detalle.getString("sdo_actual_periodo"), "0")));
                simplehash1.put("dp", tool.setFormatNumber(valida.validarDato(detalle.getString("dias_progresivos"), "0")));
            }

            modelRoot.put("detalle", simplelist);
            detalle.close();
            super.retTemplate(resp,"misdatos/detalle_vaca.htm",modelRoot);
            OutMessage.OutMessagePrint("\n**** Fin generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
    }

    private Usuario user;
    private Tools tool;
}