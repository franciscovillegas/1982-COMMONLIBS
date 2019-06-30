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

public class Dependencias extends MyHttpServlet
{

    public Dependencias()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de Dependencia/Unidad");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            Periodo peri = new Periodo(conexion);
            Consulta consul = new Consulta(conexion);
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ur.empresa, ur.unidad, ur.tipo, ur.uni_rama,u.unid_desc,dot.periodo, dot.planta_indef, dot.dot_aut FROM eje_ges_unidad_rama ur INNER JOIN eje_ges_unidades u ON ur.empresa = u.unid_empresa AND ur.uni_rama = u.unid_id INNER JOIN eje_ges_indic_bae_dotacion dot ON u.unid_id = dot.unidad AND u.unid_empresa = dot.empresa WHERE (ur.empresa = '")).append(empresa).append("') AND (ur.tipo = 'R') AND ").append("(ur.unidad = '").append(unidad).append("') AND (dot.periodo = ").append(peri.getPeriodo()).append(") AND ").append("(dot.uni_rama = 'U')")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul.getString("uni_rama"));
                simplehash1.put("desc", consul.getString("unid_desc"));
                simplehash1.put("periodo", consul.getString("periodo"));
                simplehash1.put("real", Tools.setFormatNumber(consul.getInt("planta_indef")));
                simplehash1.put("auto", Tools.setFormatNumber(consul.getInt("dot_aut")));
                simplehash1.put("dif", Tools.setFormatNumber(consul.getInt("dot_aut") - consul.getInt("planta_indef")));
            }

            modelRoot.put("detalle", simplelist);
            modelRoot.put("empresa", empresa);
            consul.close();
            super.retTemplate(resp,"dependencias.htm",modelRoot);
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
        super.retTemplate(resp,"mensaje.htm",modelRoot);
    }
 
    private Usuario user;
    private Mensaje mensaje;
}