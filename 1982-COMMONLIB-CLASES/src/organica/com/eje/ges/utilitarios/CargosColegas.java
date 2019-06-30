package organica.com.eje.ges.utilitarios;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class CargosColegas extends MyHttpServlet
{

    public CargosColegas()
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
                CrearColegas(req, resp, conexion, user);
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
        SimpleList simplelist = new SimpleList();
        String id_empresa = "";
        String sql = "";
        if(!control.tienePermiso("df_grupo_cargos"))
        {
            mensaje.devolverPaginaMensage(resp, "Agrupadores de Cargos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta cargos = new Consulta(conexion);
            String agrupador = req.getParameter("grupo");
            String nom_agrupador = req.getParameter("name_grupo");
            modelRoot.put("id_agrupador", agrupador);
            modelRoot.put("name_agrupador", nom_agrupador);
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT colegas.colegas,colegas.cargo,colegas.empresa,cargos.descrip AS nom_cargo FROM eje_ges_cargos_colegas colegas INNER JOIN eje_ges_cargos cargos ON colegas.cargo = cargos.cargo AND colegas.empresa = cargos.empresa WHERE (colegas.colegas = ")).append(agrupador).append(") and (cargos.vigente='S') ").append("order by nom_cargo")));
            System.err.println("Cargos relacionados\n".concat(String.valueOf(String.valueOf(sql))));
            cargos.exec(sql);
            SimpleHash simplehash1;
            for(; cargos.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                id_empresa = cargos.getString("empresa");
                simplehash1.put("id_cargo", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(cargos.getString("cargo"))))).append("#").append(id_empresa))));
                simplehash1.put("empresa", id_empresa);
                simplehash1.put("nom_cargo", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(valida.validarDato(cargos.getString("nom_cargo")))))).append(" Soc.:").append(id_empresa))));
            }

            modelRoot.put("colegas", simplelist);
            simplelist = new SimpleList();
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT cargos.descrip AS nom_cargo,cargos.cargo,cargos.empresa,colegas.colegas FROM eje_ges_cargos cargos LEFT OUTER JOIN eje_ges_cargos_colegas colegas ON cargos.colegas = colegas.colegas AND cargos.empresa = colegas.empresa WHERE (cargos.vigente='S') and ((colegas.colegas <> ")).append(agrupador).append(") OR ").append("(colegas.colegas IS NULL))").append(" order by nom_cargo,cargos.empresa")));
            System.err.println("Cargos disponibles\n".concat(String.valueOf(String.valueOf(sql))));
            cargos.exec(sql);
            for(; cargos.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                id_empresa = cargos.getString("empresa");
                simplehash1.put("id_cargo", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(cargos.getString("cargo"))))).append("#").append(id_empresa))));
                simplehash1.put("empresa", id_empresa);
                simplehash1.put("nom_cargo", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(valida.validarDato(cargos.getString("nom_cargo")))))).append(" Soc.:").append(id_empresa))));
            }

            modelRoot.put("disponibles", simplelist);
            cargos.close();
        }
        super.retTemplate(resp,"Gestion/grupo_cargos/relacion_cargos.htm",modelRoot);
    }

    public void CrearColegas(HttpServletRequest req, HttpServletResponse resp, Connection conexion, Usuario user)
        throws IOException, ServletException
    {
        ControlAcceso control = new ControlAcceso(user);
        Consulta consul = new Consulta(conexion);
        String agrupador = req.getParameter("grupo");
        String cargos[] = req.getParameterValues("colegas");
        String sql = "";
        String id_cargo = "";
        String id_empresa = "";
        Vector cadena = new Vector();
        int largo = cargos == null ? 0 : cargos.length;
        if(!control.tienePermiso("df_grupo_cargos"))
        {
            mensaje.devolverPaginaMensage(resp, "Agrupadores de Cargos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_cargos SET colegas = 101  WHERE (colegas = ")).append(agrupador).append(")")));
            consul.insert(sql);
            sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_cargos_colegas WHERE (colegas = ")).append(agrupador).append(")")));
            consul.insert(sql);
            for(int x = 0; x < largo; x++)
            {
                cadena = Tools.separaLista(cargos[x], "#");
                id_cargo = (String)cadena.elementAt(0);
                id_empresa = (String)cadena.elementAt(1);
                sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_cargos_colegas (colegas, cargo, empresa) VALUES (")).append(agrupador).append(", '").append(id_cargo).append("', '").append(id_empresa).append("')")));
                OutMessage.OutMessagePrint("Creando Colegas\n".concat(String.valueOf(String.valueOf(sql))));
                if(!consul.insert(sql))
                    System.err.println("--->Error(Insert) al crear relacion");
                sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE eje_ges_cargos SET colegas = ")).append(agrupador).append(" WHERE (cargo = '").append(id_cargo).append("') ").append("AND (empresa = '").append(id_empresa).append("')")));
                OutMessage.OutMessagePrint("Asignando relacion\n".concat(String.valueOf(String.valueOf(sql))));
                if(!consul.insert(sql))
                    System.err.println("--->Error(Update) al asignar relacion en tabla cargos");
            }

            consul.close();
        }
        MuestraDatos(req, resp, conexion, user);
    }

    private Usuario user;
    private Mensaje mensaje;
}