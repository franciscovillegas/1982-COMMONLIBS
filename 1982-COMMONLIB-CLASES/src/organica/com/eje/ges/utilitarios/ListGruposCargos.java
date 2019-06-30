package organica.com.eje.ges.utilitarios;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
import freemarker.template.Template;

public class ListGruposCargos extends MyHttpServlet
{

    public ListGruposCargos()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        Connection conexion = connMgr.getConnection("portal");
        int operacion = Integer.parseInt(req.getParameter("operacion"));
        user = Usuario.rescatarUsuario(req);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Agrupadores de Cargos", "Tiempo de Sesi\363n expirado...");
        else
            switch(operacion)
            {
            case 0: // '\0'
                MuestraDatos(req, resp, conexion, user);
                break;

            case 1: // '\001'
                CrearGrupo(req, resp, conexion, user);
                break;

            case 2: // '\002'
                MantenerGrupo(req, resp, conexion, user);
                break;

            case 3: // '\003'
                EliminarGrupo(req, resp, conexion, user);
                break;
            }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        Validar valida = new Validar();
        ControlAcceso control = new ControlAcceso(user);
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        SimpleHash modelRoot = new SimpleHash();
        if(!control.tienePermiso("df_grupo_cargos"))
        {
            mensaje.devolverPaginaMensage(resp, "Agrupadores de Cargos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta grupos = new Consulta(conexion);
            String sql = "SELECT colegas, nom_grupo FROM eje_ges_cargos_def_grupos order by nom_grupo";
            grupos.exec(sql);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; grupos.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("colegas", grupos.getString("colegas"));
                simplehash1.put("nombre", valida.validarDato(grupos.getString("nom_grupo")));
            }

            modelRoot.put("grupos", simplelist);
            grupos.close();
        }
        super.retTemplate(resp,"Gestion/grupo_cargos/selec_grupo.htm",modelRoot);
    }

    public void CrearGrupo(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        Validar valida = new Validar();
        SimpleHash modelRoot = new SimpleHash();
        ControlAcceso control = new ControlAcceso(user);
        String nom_grupo = req.getParameter("nom_grupo");
        Consulta consul = new Consulta(conexion);
        String sql = "";
        if(!control.tienePermiso("df_grupo_cargos"))
        {
            mensaje.devolverPaginaMensage(resp, "Agrupadores de Cargos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            int nuevo_id = 0;
            sql = "SELECT ISNULL(MAX(colegas), 100) AS maximo FROM eje_ges_cargos_def_grupos";
            consul.exec(sql);
            consul.next();
            nuevo_id = consul.getInt("maximo") + 1;
            sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_cargos_def_grupos (colegas, nom_grupo) VALUES (")).append(nuevo_id).append(", '").append(nom_grupo).append("')")));
            OutMessage.OutMessagePrint("Creando Grupo de Cargos\n".concat(String.valueOf(String.valueOf(sql))));
            if(!consul.insert(sql))
                modelRoot.put("error", "1");
            sql = "SELECT colegas, nom_grupo FROM eje_ges_cargos_def_grupos order by nom_grupo";
            consul.exec(sql);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("colegas", consul.getString("colegas"));
                simplehash1.put("nombre", valida.validarDato(consul.getString("nom_grupo")));
            }

            modelRoot.put("grupos", simplelist);
            consul.close();
        }
        super.retTemplate(resp,"Gestion/grupo_cargos/selec_grupo.htm",modelRoot);
    }

    public void MantenerGrupo(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        Validar valida = new Validar();
        SimpleHash modelRoot = new SimpleHash();
        ControlAcceso control = new ControlAcceso(user);
        String nom_grupo = req.getParameter("nom_grupo");
        String id_grupo = req.getParameter("id_grupo");
        Consulta consul = new Consulta(conexion);
        String sql = "";
        if(!control.tienePermiso("df_grupo_cargos"))
        {
            mensaje.devolverPaginaMensage(resp, "Agrupadores de Cargos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_cargos_def_grupos SET nom_grupo = '")).append(nom_grupo).append("'").append(" WHERE (colegas = ").append(id_grupo).append(")")));
            OutMessage.OutMessagePrint("Actualizando Grupo de Cargos\n".concat(String.valueOf(String.valueOf(sql))));
            if(!consul.insert(sql))
                modelRoot.put("error", "1");
            sql = "SELECT colegas, nom_grupo FROM eje_ges_cargos_def_grupos order by nom_grupo";
            consul.exec(sql);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("colegas", consul.getString("colegas"));
                simplehash1.put("nombre", valida.validarDato(consul.getString("nom_grupo")));
            }

            modelRoot.put("grupos", simplelist);
            consul.close();
        }
        super.retTemplate(resp,"Gestion/grupo_cargos/selec_grupo.htm",modelRoot);
    }

    public void EliminarGrupo(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        Validar valida = new Validar();
        SimpleHash modelRoot = new SimpleHash();
        ControlAcceso control = new ControlAcceso(user);
        String id_grupo = req.getParameter("id_grupo");
        Consulta consul = new Consulta(conexion);
        String sql = "";
        if(!control.tienePermiso("df_grupo_cargos"))
        {
            mensaje.devolverPaginaMensage(resp, "Agrupadores de Cargos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_cargos_def_grupos WHERE (colegas = ")).append(id_grupo).append(")")));
            OutMessage.OutMessagePrint("Eliminando Grupo de Cargos\n".concat(String.valueOf(String.valueOf(sql))));
            if(!consul.insert(sql))
                modelRoot.put("error", "1");
            sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_cargos_colegas WHERE (colegas = ")).append(id_grupo).append(")")));
            if(!consul.insert(sql))
                modelRoot.put("error", "1");
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_cargos SET colegas = 101  WHERE (colegas = ")).append(id_grupo).append(")")));
            consul.insert(sql);
            sql = "SELECT colegas, nom_grupo FROM eje_ges_cargos_def_grupos order by nom_grupo";
            consul.exec(sql);
            SimpleList simplelist = new SimpleList();
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("colegas", consul.getString("colegas"));
                simplehash1.put("nombre", valida.validarDato(consul.getString("nom_grupo")));
            }

            modelRoot.put("grupos", simplelist);
            consul.close();
        }
        super.retTemplate(resp,"Gestion/grupo_cargos/selec_grupo.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}