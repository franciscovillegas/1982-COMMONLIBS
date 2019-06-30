package portal.com.eje.serhumano.usermanager;

import java.sql.Connection;
import java.sql.PreparedStatement;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleList;

public class AssignAccesToRol
{

    public AssignAccesToRol(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public boolean AddAccessToRol(String rol, String acceso)
    {
        boolean valor = false;
        String sql = "INSERT INTO userjs.eje_js_rol_acceso (rol, acc_id)  VALUES  (?, ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, rol);
            pstmt.setString(2, acceso);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteAllAccessToRol()
    {
        boolean valor = false;
        String sql = "DELETE FROM userjs.eje_js_rol_acceso";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean DeleteAccessToRol()
    {
        boolean valor = false;
        return valor;
    }

    public SimpleList SimpleListAccess()
    {
        String sql = "SELECT acc_id, acc_glosa FROM  userjs.eje_js_acceso ORDER BY acc_glosa ";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public Consulta ConsulAccess()
    {
        String sql = "SELECT acc_id, acc_glosa FROM  userjs.eje_js_acceso ORDER BY acc_glosa ";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public SimpleList SimpleListRol()
    {
        String sql = "SELECT rol, rol_desc FROM userjs.eje_js_roles ORDER BY rol_desc";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul.getSimpleList();
    }

    public Consulta ConsulListaRol()
    {
        String sql = "SELECT rol, rol_desc FROM userjs.eje_js_roles ORDER BY rol_desc";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public String getError()
    {
        return mensajeError;
    }

    public Consulta ConsulAllRolAccess()
    {
        String sql = "SELECT  userjs.eje_js_roles.rol, userjs.eje_js_acceso.acc_id FROM  userjs.eje_js_roles,userjs.eje_js_acceso ";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulRolAccess()
    {
        String sql = "SELECT  userjs.eje_js_rol_acceso.acc_id, userjs.eje_js_roles.rol FROM  userjs.eje_js_acceso, userjs.eje_js_rol_acceso, userjs.eje_js_roles WHERE  (userjs.eje_js_rol_acceso.rol = userjs.eje_js_roles.rol  AND   userjs.eje_js_acceso.acc_id = userjs.eje_js_rol_acceso.acc_id) ";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulRolAccess(String rol)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT  userjs.eje_js_rol_acceso.acc_id, userjs.eje_js_roles.rol FROM  userjs.eje_js_acceso, userjs.eje_js_rol_acceso, userjs.eje_js_roles WHERE  (userjs.eje_js_rol_acceso.rol = userjs.eje_js_roles.rol  AND   userjs.eje_js_acceso.acc_id = userjs.eje_js_rol_acceso.acc_id) and userjs.eje_js_roles.rol='")).append(rol).append("'")));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulRolAccess(String rol, String access)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT  count(*) as cant FROM  userjs.eje_js_acceso, userjs.eje_js_rol_acceso, userjs.eje_js_roles WHERE  (userjs.eje_js_rol_acceso.rol = userjs.eje_js_roles.rol  AND   userjs.eje_js_acceso.acc_id = userjs.eje_js_rol_acceso.acc_id) and userjs.eje_js_roles.rol='")).append(rol).append("' and userjs.eje_js_acceso.acc_id='").append(access).append("'")));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public void ConcultaClose()
    {
        consul.close();
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
}