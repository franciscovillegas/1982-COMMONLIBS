package organica.com.eje.ges.Buscar;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.Tools;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class BusEstCivil extends MyHttpServlet
{

    public BusEstCivil()
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
        Connection Conexion = connMgr.getConnection("portal");
        DespResultado(req, resp, Conexion);
        connMgr.freeConnection("portal", Conexion);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        String paramEmpresa = req.getParameter("empresa");
        if(paramEmpresa == null)
            paramEmpresa = user.getEmpresa();
        String paramEst_civ = req.getParameter("est_civ");
        if(paramEst_civ != null)
        {
            Consulta consul = new Consulta(Conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT t.empresa, t.unidad, u.unid_desc, t.estado_civil, t.rut, t.digito_ver FROM eje_ges_trabajador t INNER JOIN eje_ges_unidades u ON t.empresa = u.unid_empresa AND t.unidad = u.unid_id WHERE (LOWER(t.estado_civil) LIKE '")).append(paramEst_civ).append("%') AND (t.empresa = '").append(paramEmpresa).append("')").append(" ORDER BY u.unid_desc, t.unidad, t.rut")));
            consul.exec(sql);
            SimpleList lista = new SimpleList();
            SimpleHash hash;
            for(; consul.next(); lista.add(hash))
            {
                hash = new SimpleHash();
                hash.put("rut", consul.getString("rut"));
                hash.put("rut_f", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul.getString("rut")))))).append("-").append(consul.getString("digito_ver")))));
                hash.put("unid_id", consul.getString("unidad"));
                hash.put("unid_desc", consul.getString("unid_desc"));
            }

            consul.close();
            modelRoot.put("trabajadores", lista);
        }
        modelRoot.put("empresa", paramEmpresa);
        modelRoot.put("est_civ", paramEst_civ);
        super.retTemplate(resp,"Gestion/Buscar/BusEstCivil.htm",modelRoot);
    }

    private Usuario user;
}