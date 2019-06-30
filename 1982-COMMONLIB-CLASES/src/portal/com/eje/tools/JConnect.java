package portal.com.eje.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

// Referenced classes of package portal.com.eje.tools:
//            OutMessage

public class JConnect
{

    public JConnect()
    {
        conn = null;
        drivers = "";
        url = "";
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        proper = ResourceBundle.getBundle("db");
        drivers = proper.getString("database.drivers");
        url = proper.getString("database.url");
        conexionbd = proper.getString("database.name");
        host = proper.getString("database.host");
        puerto = proper.getString("database.puerto");
        username = proper.getString("database.username");
        passw = proper.getString("database.password");
    }

    public JConnect(String Base)
    {
        conn = null;
        drivers = "";
        url = "";
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        conexionbd = Base;
    }

    public Connection ConectaSource()
    {
        try
        {
            Class.forName(drivers);
        }
        catch(ClassNotFoundException e)
        {
            System.err.print("LA CLASE NO FUE ENCONTRADA: ");
            OutMessage.OutMessagePrint(e.getMessage());
        }
        try
        {
            conn = DriverManager.getConnection(url, username, passw);
            OutMessage.OutMessagePrint("Connected successfully to '" + conexionbd + "' database using JConnect");
        }
        catch(SQLException sqlEx)
        {
            OutMessage.OutMessagePrint("Se ha producido un error al establecer la conexi\363n con: " + url);
            OutMessage.OutMessagePrint(sqlEx.getMessage());
        }
        return conn;
    }

    private Connection conn;
    private String drivers;
    private String url;
    private String conexionbd;
    private String host;
    private String puerto;
    private String username;
    private String passw;
    private ResourceBundle proper;
}