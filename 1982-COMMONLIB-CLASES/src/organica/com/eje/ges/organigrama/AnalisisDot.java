package organica.com.eje.ges.organigrama;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class AnalisisDot extends MyHttpServlet
{

    public AnalisisDot()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de AnalisisDot");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            Periodo peri = new Periodo(conexion);
            Consulta consul = new Consulta(conexion);
            String unidad = req.getParameter("unidad");
            String paramEmpresa = req.getParameter("empresa");
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ur.uni_rama AS unid_id, u.unid_desc, id.dot_aut, id.planta_indef AS dot_real, id.dot_fem, id.dot_masc, AVG(DATEDIFF(month, tr.fec_unidad, GETDATE())) AS antg_u,  AVG(DATEDIFF(month, tr.fecha_ingreso, GETDATE())) AS antg, AVG(DATEDIFF(month, tr.fecha_nacim, GETDATE())) AS edad FROM eje_ges_unidad_rama ur INNER JOIN eje_ges_unidades u ON ur.empresa = u.unid_empresa AND ur.uni_rama = u.unid_id INNER JOIN eje_ges_indic_bae_dotacion id ON u.unid_empresa = id.empresa AND u.unid_id = id.unidad LEFT OUTER JOIN eje_ges_trabajador tr ON u.unid_empresa = tr.empresa AND u.unid_id = tr.unidad WHERE (id.uni_rama = 'U') AND (id.periodo = ")).append(peri.getPeriodo()).append(") AND").append(" (ur.empresa = '").append(paramEmpresa).append("') AND (ur.unidad = '").append(unidad).append("') AND").append(" (ur.tipo = 'R')").append(" GROUP BY ur.uni_rama, u.unid_desc, id.dot_aut, id.planta_indef,").append(" id.dot_fem, id.dot_masc")));
            consul.exec(sql);
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul.getString("unid_id"));
                simplehash1.put("desc", consul.getString("unid_desc"));
                int dot_real = consul.getInt("dot_real");
                int dot_aut = consul.getInt("dot_aut");
                simplehash1.put("dot_aut", Tools.setFormatNumber(dot_aut));
                simplehash1.put("dot_dif", Tools.setFormatNumber(dot_aut - dot_real));
                int porc_fem = 0;
                int porc_masc = 0;
                if(dot_real > 0)
                {
                    porc_fem = (consul.getInt("dot_fem") * 100) / dot_real;
                    porc_masc = (consul.getInt("dot_masc") * 100) / dot_real;
                }
                simplehash1.put("dot_real", Tools.setFormatNumber(String.valueOf(dot_real)));
                simplehash1.put("dot_fem", String.valueOf(porc_fem));
                simplehash1.put("dot_masc", String.valueOf(porc_masc));
                int ant_meses = consul.getInt("antg");
                int ant_year = ant_meses / 12;
                ant_meses %= 12;
                simplehash1.put("ant_year", String.valueOf(ant_year));
                simplehash1.put("ant_meses", String.valueOf(ant_meses));
                ant_meses = consul.getInt("antg_u");
                ant_year = ant_meses / 12;
                ant_meses %= 12;
                simplehash1.put("ant_u_year", String.valueOf(ant_year));
                simplehash1.put("ant_u_meses", String.valueOf(ant_meses));
                int edad = consul.getInt("edad") / 12;
                simplehash1.put("edad", String.valueOf(edad));
            }

            modelRoot.put("detalle", simplelist);
            consul.close();
            Consulta consul_unid = new Consulta(conexion);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc FROM eje_ges_unidades WHERE (unid_empresa = '")).append(paramEmpresa).append("') AND (unid_id = '").append(unidad).append("')")));
            consul_unid.exec(sql);
            if(consul_unid.next())
                modelRoot.put("unid_desc", consul_unid.getString("unid_desc"));
            consul_unid.close();
            super.retTemplate(resp,"Gestion/Organizacion/AnalisisDot.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}