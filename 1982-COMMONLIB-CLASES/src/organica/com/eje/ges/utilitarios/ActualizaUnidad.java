package organica.com.eje.ges.utilitarios;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class ActualizaUnidad extends MyHttpServlet
{

    public ActualizaUnidad()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro al doPost de ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        Connection conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Agrupadores de Cargos", "Tiempo de Sesi\363n expirado...");
        else
            generaUpdate(req, resp, conexion, user);
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void ActualizaUnidad(String query, Connection conn)
    {
        try
        {
            String procedure = "{call " + query + "}";
            CallableStatement cs = conn.prepareCall(procedure);
            cs.executeQuery();
        }
        catch(SQLException e)
        {
            System.out.print("No pudo ejecutar el sp :" + query + " , " + e.toString());
        }
    }

    public void generaUpdate(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        SimpleHash modelRoot = new SimpleHash();
        String sql = "eje_ges_sp_actualiza_ccosto";
        ActualizaUnidad(sql, conexion);
        super.retTemplate(resp,"Gestion/grupo_cargos/relacion_cargos.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}