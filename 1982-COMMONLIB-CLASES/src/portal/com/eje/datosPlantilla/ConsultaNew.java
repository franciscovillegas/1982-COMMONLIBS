package portal.com.eje.datosPlantilla;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaNew
{

    public ConsultaNew(Connection conn)
    {
        try
        {
            Stmt = conn.createStatement();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException Consulta " + e.getMessage());
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en Consulta: " + e.getMessage());
        }
    }

    public void exec(String query)
    {
        try
        {
            Resultado = Stmt.executeQuery(query);
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en exec: " + e.getMessage() + " " + query);
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en exec: " + e.getMessage());
        }
    }

    public void close()
    {
        try
        {
            if(Stmt != null)
                Stmt.close();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en close: " + e.getMessage());
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en close: " + e.getMessage());
        }
    }

    public String obtieneSimpleListColumnDataString()
    {
        String Lista = new String();
        try {
            if(Resultado != null)
            {
                for(int i = 1; i <= Resultado.getMetaData().getColumnCount(); i++)
                {
                    String Column = Resultado.getMetaData().getColumnName(i);
                    Lista = Lista + "," + Column;
                }

                Lista = Lista.substring(1, Lista.length());
            }
        }
        catch(SQLException e) {
            System.out.println("Error SQL en getSimpleListColumnDataString: " + e.getMessage());
            return Lista;
        }
        catch(Exception e) {
            System.out.println("Error SQL en getSimpleListColumnDataString: " + e.getMessage());
            return Lista;
        }
        return Lista;
    }

    private ResultSet Resultado;
    private Statement Stmt;
}