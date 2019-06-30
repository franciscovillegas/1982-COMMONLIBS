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
import portal.com.eje.serhumano.user.cargo.VerCargo;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.admin.configdf:
//            UserManager

public class AsignaCargosVer extends MyHttpServlet
{

    public AsignaCargosVer()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de AsignaCargosVer");
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "Cargos Ver", "Tiempo de Sesi\363n expirado...");
        } else
        {
            VerCargo cargoRel = null;
            String paramRutTrab = req.getParameter("rut");
            String paramAccion = req.getParameter("accion");
            UserManager userManager = new UserManager(Conexion);
            if("A".equals(paramAccion))
            {
                if(req.getParameter("empresa") != null && req.getParameter("cargo") != null && !"".equals(req.getParameter("empresa")) && !"".equals(req.getParameter("cargo")))
                {
                    cargoRel = new VerCargo(req.getParameter("empresa"), req.getParameter("cargo"));
                    OutMessage.OutMessagePrint("se asigna cargo/VER: ".concat(String.valueOf(String.valueOf(cargoRel.toString()))));
                }
                if(cargoRel != null)
                    userManager.agregarCargo(paramRutTrab, cargoRel);
            } else
            if("E".equals(paramAccion))
            {
                String cargos[] = req.getParameterValues("elim");
                int largo = cargos != null ? cargos.length : 0;
                for(int x = 0; x < largo; x++)
                {
                    AsignaCargosVer _tmp = this;
                    Vector vecPaso = Tools.separaLista(cargos[x], ",");
                    cargoRel = new VerCargo((String)vecPaso.get(0), (String)vecPaso.get(1));
                    userManager.quitarCargo(paramRutTrab, cargoRel);
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
            super.mensaje.devolverPaginaSinSesion(resp, "Cargos Ver", "Tiempo de Sesi\363n expirado...");
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
            html = "admin/configdf/cargos_ver/cargos_ver.htm";
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
        sql = "SELECT empresa, cargo as cargo_id, RTRIM(descrip) as descrip FROM eje_ges_cargos where vigente='S' ORDER BY empresa, descrip";
        consul.exec(sql);
        simplelist = new SimpleList();
        for(; consul.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("empresa", consul.getString("empresa"));
            simplehash1.put("id", consul.getString("cargo_id"));
            simplehash1.put("desc", consul.getString("descrip"));
        }

        modelRoot.put("cargos", simplelist);
        sql = String.valueOf(String.valueOf((new StringBuilder("SELECT usercargo.empresa AS emp_id,usercargo.cargo AS cargo_id,em.descrip AS empresa, cargos.descrip AS cargo FROM eje_ges_empresa em INNER JOIN eje_ges_usuario_cargo usercargo ON em.empresa = usercargo.empresa INNER JOIN eje_ges_cargos cargos ON usercargo.cargo = cargos.cargo AND usercargo.empresa = cargos.empresa WHERE (usercargo.rut_usuario = ")).append(paramRutTrab).append(")")));
        System.err.println("Usuario-Cargos\n".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        modelRoot.put("emple_cargos", consul.getSimpleList());
        modelRoot.put("rut", paramRutTrab);
        super.retTemplate(resp,html,modelRoot);
        consul.close();
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }

    private Tools tools;
}