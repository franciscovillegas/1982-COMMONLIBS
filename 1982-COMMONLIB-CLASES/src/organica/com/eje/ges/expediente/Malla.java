package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Malla extends MyHttpServlet
{

    public Malla()
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de Malla");
        user = Usuario.rescatarUsuario(req);
        String strRut = req.getParameter("rut");
        String strMalla = req.getParameter("malla");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Malla", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_mall_cap", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Malla", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ma.observacion, COUNT(mt.estado) AS n_cur, ma.template, mt.malla_id FROM eje_ges_malla_trab mt INNER JOIN     eje_ges_malla ma ON mt.malla_id = ma.malla_id GROUP BY ma.observacion, mt.estado, ma.template, mt.malla_id, mt.rut HAVING (mt.estado = 'A') AND (mt.malla_id = ")).append(strMalla).append(") AND (mt.rut = ").append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Malla", "Este rut no existe...");
                consul.close();
            } else
            {
                String html = "Gestion/Expediente/" + consul.getString("template");
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("observacion", consul.getString("observacion"));
                modelRoot.put("n_cur", consul.getString("n_cur"));
                consul.close();
                Consulta consul2 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT malla.rut, malla.malla_id, malla.curso_id,curso.curso_nom, malla.estado FROM eje_ges_curso curso INNER JOIN eje_ges_malla_trab malla ON      curso.curso_id = malla.curso_id AND curso.malla_id = malla.malla_id WHERE (rut = ")).append(strRut).append(") AND (malla.malla_id = ").append(strMalla).append(")")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("id", consul2.getString("curso_id"));
                    simplehash1.put("curso", consul2.getString("curso_nom"));
                    simplehash1.put("estado", consul2.getString("estado"));
                }

                consul2.close();
                modelRoot.put("cursos", simplelist);
                super.retTemplate(resp,html,modelRoot);
            }
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Mensaje mensaje;
}