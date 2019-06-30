package organica.com.eje.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import organica.tools.OutMessage;

public class JConnect
{

    public JConnect()
    {
        conn = null;
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        proper = ResourceBundle.getBundle("DataFolderSource");
        conexionbd = proper.getString("database.name");
        host = proper.getString("database.host");
        puerto = proper.getString("database.puerto");
        username = proper.getString("database.username");
        passw = proper.getString("database.password");
    }

    public JConnect(String Base)
    {
        conn = null;
        conexionbd = "";
        host = "";
        puerto = "";
        username = "";
        passw = "";
        conexionbd = Base;
    }

    public Connection ConectaSource()
    {
        String url = String.valueOf(String.valueOf((new StringBuilder("jdbc:sybase:Tds:")).append(host).append(":").append(puerto).append("?ServiceName=").append(conexionbd)));
        try
        {
            Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
        }
        catch(ClassNotFoundException e)
        {
            System.err.print("LA CLASE NO FUE ENCONTRADA: ");
            OutMessage.OutMessagePrint(e.getMessage());
        }
        try
        {
            conn = DriverManager.getConnection(url, username, passw);
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Connected successfully to '")).append(conexionbd).append("' database using JConnect"))));
        }
        catch(SQLException sqlEx)
        {
            OutMessage.OutMessagePrint("Se ha producido un error al establecer la conexi\363n con: ".concat(String.valueOf(String.valueOf(url))));
            OutMessage.OutMessagePrint(sqlEx.getMessage());
        }
        return conn;
    }

    private Connection conn;
    private String conexionbd;
    private String host;
    private String puerto;
    private String username;
    private String passw;
    private ResourceBundle proper;
}