package organica.com.eje.ges.gestion;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class DetalleVaca extends MyHttpServlet
{

    public DetalleVaca()
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
        user = Usuario.rescatarUsuario(req);
        generaTab(req, resp);
    }

    public void generaTab(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        String rut = req.getParameter("rut");
        if(rut == null)
            rut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Detalle Vacaciones", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(Conexion, "df_exp_vaca", rut))
        {
            mensaje.devolverPaginaMensage(resp, "Detalle Vacaciones", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String consulta = null;
            String periodo = null;
            SimpleHash modelRoot = new SimpleHash();
            OutMessage.OutMessagePrint("Entro a Mostrar datos!!!!");
            Consulta detalle = new Consulta(Conexion);
            periodo = req.getParameter("periodo");
            consulta = String.valueOf(String.valueOf((new StringBuilder("select desde,hasta,dias_normales,dias_progresivos,periodo from eje_ges_vacaciones_det where rut=")).append(rut).append(" and periodo=").append(periodo)));
            OutMessage.OutMessagePrint("========>".concat(String.valueOf(String.valueOf(consulta))));
            detalle.exec(consulta);
            SimpleList simplelist = new SimpleList();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleHash simplehash1;
            for(; detalle.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("desde", dateFormat.format(detalle.getValor("desde")));
                simplehash1.put("hasta", dateFormat.format(detalle.getValor("hasta")));
                simplehash1.put("normales", String.valueOf(detalle.getInt("dias_normales")));
                simplehash1.put("progresivos", String.valueOf(detalle.getInt("dias_progresivos")));
            }

            modelRoot.put("detalle", simplelist);
            detalle.close();
            super.retTemplate(resp,"Gestion/InfoUsuario/DetalleVaca.htm",modelRoot);
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private void devolverPaginaError(HttpServletResponse resp, String msg)
    {
        try
        {
            PrintWriter printwriter = resp.getWriter();
            resp.setContentType("text/html");
            printwriter.println("<html>");
            printwriter.println("<head>");
            printwriter.println("<title>Valores recogidos en el formulario</title>");
            printwriter.println("</head>");
            printwriter.println("<body>");
            printwriter.println(msg);
            printwriter.println("</body>");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
        }
        catch(IOException e)
        {
            OutMessage.OutMessagePrint("Error al botar la pagina");
        }
    }

    private Usuario user;
    private Mensaje mensaje;
}