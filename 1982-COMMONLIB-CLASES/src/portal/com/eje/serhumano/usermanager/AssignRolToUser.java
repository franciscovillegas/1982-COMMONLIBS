package portal.com.eje.serhumano.usermanager;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;

public class AssignRolToUser
{

    public AssignRolToUser(Connection conex)
    {
        con = conex;
        consul = new Consulta(con);
    }

    public boolean AddRolToUser()
    {
        boolean valor = false;
        return valor;
    }

    public boolean UpdateRolToUser()
    {
        boolean valor = false;
        return valor;
    }

    public Consulta ConsulUser()
    {
        String sql = "SELECT userjs.eje_js_usuario.rut,\tuserjs.eje_js_usuario.rol FROM  userjs.eje_js_usuario";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    private Connection con;
    private Consulta consul;
}