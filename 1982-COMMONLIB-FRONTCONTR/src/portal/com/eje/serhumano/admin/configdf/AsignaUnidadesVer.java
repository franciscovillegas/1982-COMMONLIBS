package portal.com.eje.serhumano.admin.configdf;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.unidad.VerUnidad;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.admin.configdf:
//            UserManager

public class AsignaUnidadesVer extends MyHttpServlet
{

    public AsignaUnidadesVer()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de AsignaUnidadesVer");
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Asignar Anexos", "Tiempo de Sesi\363n expirado...");
        } else
        {
            VerUnidad unidRel = null;
            String paramRutTrab = req.getParameter("rut");
            String paramAccion = req.getParameter("accion");
            UserManager userManager = new UserManager(Conexion);
            if("A".equals(paramAccion))
            {
                if(req.getParameter("empresa") != null && req.getParameter("unidad") != null && req.getParameter("tipo") != null && !"".equals(req.getParameter("empresa")) && !"".equals(req.getParameter("unidad")) && !"".equals(req.getParameter("tipo")))
                {
                    unidRel = new VerUnidad(req.getParameter("empresa"), req.getParameter("unidad"), req.getParameter("tipo"));
                    OutMessage.OutMessagePrint("se asigna unidad/relativa ".concat(String.valueOf(String.valueOf(unidRel.toString()))));
                }
                if(unidRel != null)
                    userManager.agregarRama(paramRutTrab, unidRel);
            } else
            if("E".equals(paramAccion))
            {
                String unidades[] = req.getParameterValues("elim");
                int largo = unidades != null ? unidades.length : 0;
                for(int x = 0; x < largo; x++)
                {
                    AsignaUnidadesVer _tmp = this;
                    Vector vecPaso = Tools.separaLista(unidades[x], ",");
                    unidRel = new VerUnidad((String)vecPaso.get(0), (String)vecPaso.get(1));
                    userManager.quitarRama(paramRutTrab, unidRel);
                }

            }
            userManager.close();
            generaPagina(req, resp, "", user, Conexion);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de AsignaUnidadesVer");
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
            super.mensaje.devolverPaginaSinSesion(resp, "Asignar Anexos", "Tiempo de Sesi\363n expirado...");
        else
            generaPagina(req, resp, "", user, Conexion);
        OutMessage.OutMessagePrint("Fin de doGet");
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, String msg, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a generaPagina");
        String paramRutTrab = req.getParameter("rut");
        String html = req.getParameter("htm");
        if(html == null)
            html = "admin/configdf/Asignar_UniRel/UnidadesVer.htm";
        OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
        SimpleHash modelRoot = new SimpleHash();
        String sql = "";
        Consulta consul = new Consulta(conexion);
        SimpleHash simplehash1 = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        sql = "SELECT empresa AS id,descrip AS empresa FROM eje_ges_empresa order by id";
        consul.exec(sql);
        for(; consul.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("id", consul.getString("id"));
            simplehash1.put("desc", consul.getString("empresa"));
        }

        modelRoot.put("empresas", simplelist);
        sql = "SELECT unid_empresa AS empresa, unid_id, unid_desc FROM eje_sh_unidades WHERE vigente='S'  ORDER BY unid_empresa, unid_desc";
        consul.exec(sql);
        simplelist = new SimpleList();
        for(; consul.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("empresa", consul.getString("empresa"));
            simplehash1.put("id", consul.getString("unid_id"));
            simplehash1.put("desc", consul.getString("unid_desc"));
        }

        modelRoot.put("unidades", simplelist);
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT uu.empresa AS emp_id, uu.unidad AS uni_id,     e.descrip AS empresa, u.unid_desc AS unidad, uu.tipo FROM eje_ges_empresa e INNER JOIN     eje_ges_usuario_unidad uu ON     e.empresa = uu.empresa INNER JOIN     eje_sh_unidades u ON uu.empresa = u.unid_empresa AND      uu.unidad = u.unid_id WHERE (uu.rut_usuario = ")).append(paramRutTrab).append(")")));
        consul.exec(sql);
        modelRoot.put("unidades_rel", consul.getSimpleList());
        modelRoot.put("rut", paramRutTrab);
        super.retTemplate(resp,html,modelRoot);
        consul.close();
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }

    private Tools tool;
}