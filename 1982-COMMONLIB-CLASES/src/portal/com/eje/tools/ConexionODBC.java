package portal.com.eje.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

// Referenced classes of package portal.com.eje.tools:
//            OutMessage

public class ConexionODBC
{

    public ConexionODBC(String Base)
    {
        con = null;
        drivers = "";
        url = "";
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
        drivers = "";
        url = "";
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        proper = ResourceBundle.getBundle("db");
        drivers = proper.getString("odbc.drivers");
        url = proper.getString("odbc.url");
        conexionbd = proper.getString("odbc.name");
        username = proper.getString("database.username");
        passw = proper.getString("database.password");
    }

    public Connection Conecta()
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
            con = DriverManager.getConnection(url, username, passw);
        }
        catch(SQLException sqlEx)
        {
            OutMessage.OutMessagePrint("Se ha producido un error al establecer la conexi\363n con: " + url);
            OutMessage.OutMessagePrint(sqlEx.getMessage());
        }
        OutMessage.OutMessagePrint("Iniciando la conexion con " + conexionbd);
        return con;
    }

    private Connection con;
    private String drivers;
    private String url;
    private String conexionbd;
    private String host;
    private String puerto;
    private String username;
    private String passw;
    private ResourceBundle proper;
}