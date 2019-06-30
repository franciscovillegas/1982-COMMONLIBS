package organica.com.eje.ges.expediente;

import java.io.IOException;
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

public class ListaMalla extends MyHttpServlet
{

    public ListaMalla()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        doPost(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro al doPost de ListaMalla");
        java.sql.Connection conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Mallas", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_mall_cap", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Mallas", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, nombre, cargo, fecha_ingreso, id_foto FROM view_ges_InfoRut  WHERE (rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            consul.next();
            modelRoot.put("nombre", consul.getString("nombre"));
            modelRoot.put("rut", strRut);
            modelRoot.put("cargo", valida.cortarString(consul.getString("cargo"), 38));
            Consulta consul2 = new Consulta(conexion);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT DISTINCT m.malla_id, m.malla_nom FROM eje_ges_malla_trab as mt INNER JOIN eje_ges_malla as m ON mt.malla_id = m.malla_id WHERE (mt.rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul2.exec(sql);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul2.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul2.getString("malla_id"));
                simplehash1.put("malla", valida.cortarString(consul2.getString("malla_nom"), 45));
            }

            modelRoot.put("mallas", simplelist);
            consul2.close();
            consul.close();
            super.retTemplate(resp,"Gestion/Expediente/Mallas/ListaMalla.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Mensaje mensaje;
}