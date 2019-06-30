package organica.com.eje.ges.gestion;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class HistoriaAfp extends MyHttpServlet
{

    public HistoriaAfp()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Datos Previsionales", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(Conexion, "df_exp_dat_prev", rut))
        {
            mensaje.devolverPaginaMensage(resp, "Datos Previsionales", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String consulta = null;
            SimpleHash modelRoot = new SimpleHash();
            SimpleList ListAfp = new SimpleList();
            OutMessage.OutMessagePrint("Entro a Mostrar datos!!!!");
            Consulta detalle = new Consulta(Conexion);
            Validar valida = new Validar();
            consulta = String.valueOf(String.valueOf((new StringBuilder("select * from eje_ges_historico_afp where rut=")).append(rut).append(" order by fecha_ing desc")));
            OutMessage.OutMessagePrint(consulta);
            detalle.exec(consulta);
            SimpleHash simplehash1;
            for(; detalle.next(); ListAfp.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("NOMBRE", detalle.getString("nombre"));
                simplehash1.put("FECHA_ING", valida.validarFecha(detalle.getValor("fecha_ing")));
            }

            modelRoot.put("AFP", ListAfp);
            detalle.close();
            super.retTemplate(resp,"Gestion/InfoUsuario/historia_afp.htm",modelRoot);
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}