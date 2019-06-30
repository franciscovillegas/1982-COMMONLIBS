package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.servlet.FormatoFecha;
import organica.tools.servlet.FormatoNumero;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class Malla2 extends MyHttpServlet
{

    public Malla2()
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
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT m.malla_year, m.malla_nom, m.observacion, COUNT(mte.curso_id) AS n_cur FROM view_malla_trab_estado mte INNER JOIN eje_ges_malla m ON mte.malla_id = m.malla_id GROUP BY mte.rut, mte.malla_id, m.malla_year, m.malla_nom,  m.observacion, mte.resul_texto HAVING (mte.rut = ")).append(strRut).append(") AND (mte.resul_texto LIKE 'APR%') AND").append(" (mte.malla_id = ").append(strMalla).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Malla", "Este rut no existe...");
                consul.close();
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("FFecha", new FormatoFecha());
                modelRoot.put("FNum", new FormatoNumero());
                modelRoot.put("observacion", consul.getString("observacion"));
                modelRoot.put("n_cur", consul.getString("n_cur"));
                modelRoot.put("malla", consul.getString("malla_nom"));
                modelRoot.put("year", consul.getString("malla_year"));
                consul.close();
                Consulta consul2 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT fam_id, fam_nom, fam_img, tipo, curso_id, curso_nom, periodo, fecha_inicio AS fec_ini, fecha_termino AS fec_ter, nota_aprob AS nota, asistencia AS asis, resul_texto AS estado FROM view_malla_trab_estado WHERE (rut = ")).append(strRut).append(") AND (malla_id = ").append(strMalla).append(")")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                modelRoot.put("cursos", consul2.getSimpleList());
                consul2.close();
                super.retTemplate(resp,"Gestion/Expediente/Mallas/MainMallas.htm",modelRoot);
            }
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Mensaje mensaje;
}