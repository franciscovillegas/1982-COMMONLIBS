package organica.com.eje.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import organica.tools.OutMessage;

public class ConexionODBC
{

    public ConexionODBC(String Base)
    {
        con = null;
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        conexionbd = Base;
    }

    public ConexionODBC()
    {
        con = null;
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        proper = ResourceBundle.getBundle("DataFolder");
        conexionbd = proper.getString("odbc.name");
        username = proper.getString("database.username");
        passw = proper.getString("database.password");
    }

    public Connection Conecta()
    {
        String url = "jdbc:odbc:".concat(String.valueOf(String.valueOf(conexionbd)));
        try
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        }
        catch(ClassNotFoundException e)
        {
            System.err.print("LA CLASE NO FUE ENCONTRADA: ");
            OutMessage.OutMessagePrint(e.getMessage());
        }
        try
        {
            con = DriverManager.getConnection(url, username, passw);
        }
        catch(SQLException sqlEx)
        {
            OutMessage.OutMessagePrint("Se ha producido un error al establecer la conexi\363n con: ".concat(String.valueOf(String.valueOf(url))));
            OutMessage.OutMessagePrint(sqlEx.getMessage());
        }
        OutMessage.OutMessagePrint("Iniciando la conexion con ".concat(String.valueOf(String.valueOf(conexionbd))));
        return con;
    }

    private Connection con;
    private String conexionbd;
    private String host;
    private String puerto;
    private String username;
    private String passw;
    private ResourceBundle proper;
}