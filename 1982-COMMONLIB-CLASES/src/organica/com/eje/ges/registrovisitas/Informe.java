package organica.com.eje.ges.registrovisitas;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class Informe extends MyHttpServlet
{

    public Informe()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        doPost(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        GeneraCambios(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    public void GeneraCambios(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n*****    Entro al a Registrar Salida   *****");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            Consulta consul = new Consulta(conexion);
            int tipo = Integer.parseInt(req.getParameter("tipo"));
            String sql = "";
            switch(tipo)
            {
            case 1: // '\001'
                OutMessage.OutMessagePrint("-------->Informe del dia");
                sql = "SELECT rut, nombre, anexo, unidad, fecha, motivo, unid_id,empresa, CONVERT(varchar, { fn MONTH(fecha) })  + '/' + CONVERT(varchar, day(fecha))  + '/' + CONVERT(varchar, { fn YEAR(fecha) }) AS fec FROM view_visitas WHERE (CONVERT(varchar, { fn MONTH(fecha) })  + '/' + CONVERT(varchar, day(fecha))  + '/' + CONVERT(varchar, { fn YEAR(fecha) })  = CONVERT(varchar, { fn MONTH(GETDATE()) })  + '/' + CONVERT(varchar, day(GETDATE()))  + '/' + CONVERT(varchar, { fn YEAR(GETDATE()) })) order by unid_id";
                break;

            case 2: // '\002'
                OutMessage.OutMessagePrint("-------->Informe del mes");
                sql = "SELECT rut, nombre, anexo, unidad, fecha, motivo, unid_id,empresa, CONVERT(varchar, { fn MONTH(fecha) })  + '/' + CONVERT(varchar, day(fecha))  + '/' + CONVERT(varchar, { fn YEAR(fecha) }) AS fec FROM view_visitas WHERE (CONVERT(varchar, { fn MONTH(fecha) })  + '/' + CONVERT(varchar, day(fecha))  + '/' + CONVERT(varchar, { fn YEAR(fecha) })  = CONVERT(varchar, { fn MONTH(GETDATE()) })  + '/' + CONVERT(varchar, day(GETDATE()))  + '/' + CONVERT(varchar, { fn YEAR(GETDATE()) })) order by unid_id";
                break;
            }
            while(consul.next()) ;
            consul.close();
            super.retTemplate(resp,"Gestion/RegistroVisitas/exito.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de registro salida");
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/RegistroVisitas/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}