package portal.com.eje.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

// Referenced classes of package portal.com.eje.tools:
//            OutMessage

public class ConexionBd
{

    public ConexionBd(String Base)
    {
        con = null;
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        conexionbd = Base;
    }

    public ConexionBd()
    {
        con = null;
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        proper = ResourceBundle.getBundle("db");
        conexionbd = proper.getString("database.name");
        host = proper.getString("database.host");
        puerto = proper.getString("database.puerto");
        username = proper.getString("database.username");
        passw = proper.getString("database.password");
        OutMessage.SetTraza();
    }

    public Connection Conecta()
    {
        String url = "jdbc:freetds:sqlserver://" + host + ":" + puerto + "/" + conexionbd;
        try
        {
            Class.forName("com.internetcds.jdbc.tds.Driver");
        }
        catch(ClassNotFoundException e)
        {
            System.err.print("LA CLASE NO FUE ENCONTRADA: ");
            OutMessage.OutMessagePrint(e.getMessage());
        }
        try
        {
            con = DriverManager.getConnection(url, username, passw);
            OutMessage.OutMessagePrint("Iniciando la conexion con: " + url);
        }
        catch(SQLException sqlEx)
        {
            OutMessage.OutMessagePrint("--->Se ha producido un error al establecer la conexi\363n con: " + url);
            OutMessage.OutMessagePrint(sqlEx.getMessage());
        }
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