package organica.datos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Consulta
{

    public Consulta(Connection conn)
    {
        filasSimpleList = 0;
        try
        {
            Stmt = conn.createStatement();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException Consulta ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en Consulta: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    public ResultSet getRs()
    {
        return Resultado;
    }

    public int getColumnCount()
    {
        int x = 0;
        try
        {
            x = Resultado.getMetaData().getColumnCount();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en getColumnCount : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getColumnCount: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return x;
        }
    }

    public boolean insert(String query)
    {
        boolean logrado = true;
        try
        {
            if(Stmt.executeUpdate(query) > 0)
            {
                logrado = true;
            } else
            {
                logrado = false;
                System.out.println("--> Filas no afectadas :\n".concat(String.valueOf(String.valueOf(query))));
            }
        }
        catch(SQLException e)
        {
            System.out.println(String.valueOf(String.valueOf((new StringBuilder("SQLException en insert: ")).append(e.getMessage()).append(" ").append(query))));
            logrado = false;
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en insert: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return logrado;
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
            System.out.println(String.valueOf(String.valueOf((new StringBuilder("SQLException en exec: ")).append(e.getMessage()).append(" ").append(query))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en exec: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
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
            System.out.println("SQLException en close: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en close: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    public boolean next()
    {
        boolean value = false;
        try
        {
            value = Resultado.next();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en next: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en next: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public String getString(int column)
    {
        String value = "";
        try
        {
            value = Resultado.getString(column);
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en getString: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getString(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public String getString(String column)
    {
        String value = "";
        try
        {
            value = Resultado.getString(column);
        }
        catch(SQLException e)
        {
            System.out.println("column en getstring(string): ".concat(String.valueOf(String.valueOf(column))));
            System.out.println("SQLException en getString(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getString(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public float getFloat(String column)
    {
        float value = 0.0F;
        try
        {
            value = Resultado.getFloat(column);
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en GetFloat(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getFloat(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public float getFloat(int column)
    {
        float value = 0.0F;
        try
        {
            value = Resultado.getFloat(column);
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en GetFloat(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getFloat(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public int getInt(String column)
    {
        int value = 0;
        try
        {
            value = Resultado.getInt(column);
        }
        catch(SQLException e)
        {
            System.out.println("Columna Getint: ".concat(String.valueOf(String.valueOf(column))));
            System.out.println("SQLException en getInt(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getInt(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public Object getValor(int column)
    {
        Object value = "";
        try
        {
            switch(Resultado.getMetaData().getColumnType(column))
            {
            case 12: // '\f'
                value = Resultado.getString(column);
                break;

            case 92: // '\\'
                value = Resultado.getTime(column);
                break;

            case 91: // '['
                value = Resultado.getDate(column);
                break;

            case 2003: 
                value = Resultado.getArray(column);
                break;

            case 1: // '\001'
                value = Resultado.getString(column);
                break;

            case 93: // ']'
                value = Resultado.getTimestamp(column);
                break;

            default:
                value = Resultado.getObject(column);
                break;
            }
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en getValor(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getValor(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public Object getValor(String column)
    {
        try
        {
            int col = 0;
            col = Resultado.findColumn(column);
            if(col > 0)
                return getValor(col);
            else
                return null;
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en getValor(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            System.out.println("--->Null Pointer Error en getValor(string): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            return null;
        }
    }

    public int getColumnType(int column)
    {
        int value = -100;
        try
        {
            value = Resultado.getMetaData().getColumnType(column + 1);
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en getColumnType(int):".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getColumnType(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public String getColumnName(int column)
    {
        String name = null;
        try
        {
            name = Resultado.getMetaData().getColumnName(column);
        }
        catch(SQLException e)
        {
            System.out.println("SQLException en getColumnName(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getColumnType(int): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return name;
        }
    }

    public SimpleList getSimpleList()
    {
        SimpleList Lista = new SimpleList();
        filasSimpleList = 0;
        try
        {
            SimpleHash Temp;
            for(; Resultado.next(); Lista.add(Temp))
            {
                Temp = new SimpleHash();
                for(int i = 1; i <= Resultado.getMetaData().getColumnCount(); i++)
                {
                    String Column = Resultado.getMetaData().getColumnName(i);
                    String dato = Resultado.getString(Column);
                    Temp.put(Column, dato);
                }

                Temp.put("FILA_NUM", String.valueOf(++filasSimpleList));
            }

        }
        catch(SQLException e)
        {
            System.out.println("--->SQLException en getSimpleList: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en getSimpleList: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return Lista;
        }
    }

    public SimpleHash getSimpleHash()
    {
        SimpleHash Temp = null;
        if(next())
        {
            Temp = new SimpleHash();
            for(int i = 1; i <= getColumnCount(); i++)
            {
                String Column = getColumnName(i);
                Temp.put(Column, getString(Column));
            }

        }
        return Temp;
    }

    public boolean isBeforeFirst()
    {
        boolean value = false;
        try
        {
            value = Resultado.isBeforeFirst();
        }
        catch(SQLException e)
        {
            System.out.println("--->SQLException en isBeforeFirst: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en isBeforeFirst: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public void beforeFirst()
    {
        boolean value = false;
        try
        {
            Resultado.beforeFirst();
        }
        catch(SQLException e)
        {
            System.out.println("--->SQLException en beforeFirst: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en beforeFirst: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return;
        }
    }

    public boolean last()
    {
        boolean value = false;
        try
        {
            value = Resultado.last();
        }
        catch(SQLException e)
        {
            System.out.println("--->SQLException en last: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en last: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public boolean previous()
    {
        boolean value = false;
        try
        {
            value = Resultado.previous();
        }
        catch(SQLException e)
        {
            System.out.println("--->SQLException en previous: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        catch(NullPointerException e)
        {
            System.out.println("--->Null Pointer Error en previous: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        finally
        {
            return value;
        }
    }

    public int getFilasSimpleList()
    {
        return filasSimpleList;
    }

    private ResultSet Resultado;
    private Statement Stmt;
    private int filasSimpleList;
}