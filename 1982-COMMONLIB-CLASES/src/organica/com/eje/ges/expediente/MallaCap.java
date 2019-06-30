package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class MallaCap extends MyHttpServlet
{

    public MallaCap()
    {
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de Malla de Capacitacion frameset");
        user = Usuario.rescatarUsuario(req);
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Malla", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(Conexion, "df_exp_mall_cap", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Malla", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("rut", req.getParameter("rut"));
            super.retTemplate(resp,"Gestion/Expediente/Mallas/frame_malla.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Mensaje mensaje;
}