package portal.com.eje.datos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

/**
 * @deprecated
 * 
 * */
public class Consulta {

	public SimpleHash create()  {
		ResultSet rs = this.getRs();
		SimpleHash values = new SimpleHash();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			String[] fieldNames = new String[columnCount];

			for (int i = 1; i <= columnCount; i++) {
				String columnName = rs.getMetaData().getColumnName(i);
				fieldNames[i - 1] = columnName;
			}
			
			for (int i = 0; i < fieldNames.length; i++) {
				if (fieldNames[i] != null) {
					String value = this.getString(fieldNames[i]);
					values.put(fieldNames[i],(value));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return values;
	}
	
	public LinkedHashMap createMap() throws Exception {
		ResultSet rs = this.getRs();
		int columnCount = rs.getMetaData().getColumnCount();
		String[] fieldNames = new String[columnCount];

		for (int i = 1; i <= columnCount; i++) {
			String columnName = rs.getMetaData().getColumnName(i);
			fieldNames[i - 1] = columnName;
		}
		LinkedHashMap values = new LinkedHashMap();
		for (int i = 0; i < fieldNames.length; i++) {
			if (fieldNames[i] != null) {
				String value = this.getString(fieldNames[i]);
				values.put(fieldNames[i],(value));
			}
		}
		return values;
	}
	
	public ArrayList createList() throws Exception {
		ArrayList lista = new ArrayList();
		
		while(Resultado.next()){
			lista.add(createMap());
		}
	
		return lista;
	}
	
	public Consulta(Connection conn) {
		filasSimpleList = 0;
		num_filas = 0;
		try {
			Stmt = conn.createStatement();
		} 
		catch (SQLException e) {
			System.out.println("SQLException Consulta " + e.getMessage());
		} 
		catch (NullPointerException e) {
			System.out.println("--->Null Pointer Error en Consulta: " + e.getMessage());
		}
	}
	
    public Consulta(Connection conn, String sp_name) {
        filasSimpleList = 0;
        num_filas = 0;
        try {
            CStmt = conn.prepareCall(String.valueOf(new StringBuilder("{call ").append(sp_name).append("}")));
        }
        catch(SQLException e) {
            System.out.println("SQLException Procedimiento " + e.getMessage());
        }
        catch(NullPointerException e) {
            System.out.println("--->Null Pointer Error en Procedimiento: " + e.getMessage());
        }
    }

	public int getRows() {
		int x = 0;
		try {
			x = Resultado.getFetchSize();
		} 
		catch (SQLException e) {
			System.out.println("Error SQL en GetRows : " + e.getMessage());
			return x;
		} 
		catch (Exception e) {
			System.out.println("--->Error en GetRows: " + e.getMessage());
			return x;
		}
		return x;
	}

	public ResultSet getRs() {
		return Resultado;
	}

	public int getColumnCount() {
		int x = 0;
		try {
			x = Resultado.getMetaData().getColumnCount();
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getColumnCount : " + e.getMessage());
			return x;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getColumnCount: " + e.getMessage());
			return x;
		}
		return x;
	}

	public boolean insert(String query) {
		boolean logrado = true;
		try {
			if (Stmt.executeUpdate(query) > 0) {
				logrado = true;
			} 
			else {
				logrado = false;
				System.out.println("--> Filas no afectadas :\n" + query);
			}
		} 
		catch (SQLException e) {
			System.out.println("SQLException en insert: " + e.getMessage() + " " + query);
			logrado = false;
			return logrado;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en insert: " + e.getMessage());
			return logrado;
		}
		return logrado;
	}

	public void exec(String query) {
		try {
			Resultado = Stmt.executeQuery(query);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en exec : " + e.getMessage() + " " + query);
		} 
		catch (NullPointerException e) {
			System.out.println("--->Null Pointer Error en exec: " + e.getMessage());
		}
	}
	
    public void execSP(String query) {
        try {
            Resultado = CStmt.executeQuery(query);
        }
        catch(SQLException e) {
            System.out.println("SQLException en exec : " + e.getMessage() + " " + query);
        }
        catch(NullPointerException e) {
            System.out.println("--->Null Pointer Error en exec: " + e.getMessage());
        }
    }

	public int delete(String query) {   
    	int logrado = -1;
        try {  
        	logrado = Stmt.executeUpdate(query); 
        }
        catch(SQLException e) {  
        	System.out.println("Error SQL en insert: " + e.getMessage() + " " + query); }
        catch(Exception e) {  
        	System.out.println("--->Error en update: " + e.getMessage()); }
        finally {  
        	return logrado; 
        }
    }
    
   public int update(String query)
    {
        int logrado = -1;
        try {  logrado = Stmt.executeUpdate(query); }
        catch(SQLException e)
        {  System.out.println("Error SQL en insert: " + e.getMessage() + " " + query);
        }
        catch(Exception e)
        {  System.out.println("--->Error en update: " + e.getMessage());
        }
        finally {  return logrado; }
    }

	public void close() {
		try {
			if(Resultado != null) {
				Resultado.close();
			}
			if (Stmt != null) {
				Stmt.close();
			}
			if(CStmt != null) {
				CStmt.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("SQLException en close: " + e.getMessage());
		} 
		catch (NullPointerException e) {
			System.out.println("--->Null Pointer Error en close: " + e.getMessage());
		}
	}

	public boolean next() {
		boolean value = false;
		try {
			value = Resultado.next();
		} 
		catch (SQLException e) {
			System.out.println("SQLException en next: " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en next: " + e.getMessage());
			return value;
		}
		return value;
	}

	public String getString(int column) {
		String value = "";
		try {
			value = Resultado.getString(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getString : " + column + "  " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getString(int): " + column + "  " + e.getMessage());
			return value;
		}
		return value;
	}

	public String getString(String column) {
		String value = "";
		try {
			value = Resultado.getString(column);
		} 
		catch (SQLException e) {
			System.out.println("column en getstring(string): " + column);
			System.out.println("SQLException en getString(string): " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getString(string): " + e.getMessage());
			return value;
		}
		return value;
	}

	public float getFloat(String column) {
		float value = 0.0F;
		try {
			value = Resultado.getFloat(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en GetFloat(string): " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getFloat(string): " + e.getMessage());
			return value;
		}
		return value;
	}

	public float getFloat(int column) {
		float value = 0.0F;
		try {
			value = Resultado.getFloat(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en GetFloat(int): " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getFloat(int): " + e.getMessage());
			return value;
		}
		return value;
	}

	public int getInt(String column) {
		int value = 0;
		try {
			value = Resultado.getInt(column);
		} 
		catch (SQLException e) {
			System.out.println("Columna Getint: " + column);
			System.out.println("SQLException en getInt(string): " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getInt(string): " + e.getMessage());
			return value;
		}
		return value;
	}

    public int getInt(int column) {
        int value = 0;
        try {  
        	value = Resultado.getInt(column); 
        }
        catch(SQLException e) {  
        	System.out.println("Columna " + column);
        	System.out.println("Error SQL en GetInt(int column): " + e.getMessage());
        }
        catch(Exception e) {  
        	System.out.println("--->Error en GetInt(int column): " + e.getMessage()); 
        }
        finally {  
        	return value; 
        }
    }
    
    public long getLong(int column) {
        long value = 0;
        try {
            value = Resultado.getLong(column);
        }
        catch(SQLException e) {
            System.out.println("SQLException en getLong(int): " + e.getMessage());
            return value;
        }
        catch(Exception e) {
            System.out.println("--->Null Pointer Error en getLong(int): " + e.getMessage());
            return value;
        }
        return value;
    }
    
    public long getLong(String column) {
        long value = 0;
        try {
            value = Resultado.getLong(column);
        }
        catch(SQLException e) {
            System.out.println("SQLException en getLong(String): " + e.getMessage());
            return value;
        }
        catch(Exception e) {
            System.out.println("--->Null Pointer Error en getLong(String): " + e.getMessage());
            return value;
        }
        return value;
    }
	
	public Object getValor(int column) {
		Object value = "";
		try {
			switch (Resultado.getMetaData().getColumnType(column)) {
			case 12:
				value = Resultado.getString(column);
				break;
			case 92:
				value = Resultado.getTime(column);
				break;
			case 91:
				value = Resultado.getDate(column);
				break;
			case 2003:
				value = Resultado.getArray(column);
				break;
			case 1:
				value = Resultado.getString(column);
				break;
			case 93:
				value = Resultado.getTimestamp(column);
				break;
			default:
				value = Resultado.getObject(column);
				break;
			}
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getValor(int): " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getValor(int): " + e.getMessage());
			return value;
		}
		return value;
	}

	public Object getDate(int column) {
		Object value = "";
		try {
			value = Resultado.getDate(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getValor(int): " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getValor(int): " + e.getMessage());
			return value;
		}
		return value;
	}

	public Object getValor(String column) {
		int col = 0;
		try {
			col = Resultado.findColumn(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getValor(string): " + e.getMessage());
			return null;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getValor(string): " + e.getMessage());
			return null;
		}
		if (col > 0) {
			return getValor(col);
		}
		else {
			return null;
		}
	}

	public Object getDate(String column) {
		int col = 0;
		try {
			col = Resultado.findColumn(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getValor(string): " + e.getMessage());
			return null;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getValor(string): " + e.getMessage());
			return null;
		}
		if (col > 0) {
			return getDate(col);
		}
		else {
			return null;
		}
	}

	public int getColumnType(int column) {
		int value = -100;
		try {
			value = Resultado.getMetaData().getColumnType(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getColumnType(int):" + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getColumnType(int): " + e.getMessage());
			return value;
		}
		return value;
	}

	public String getColumnName(int column) {
		String name = null;
		try {
			name = Resultado.getMetaData().getColumnName(column);
		} 
		catch (SQLException e) {
			System.out.println("SQLException en getColumnName(int): " + e.getMessage());
			return name;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getColumnType(int): " + e.getMessage());
			return name;
		}
		return name;
	}

	public SimpleList getSimpleList() {
		SimpleList Lista;
		Lista = new SimpleList();
		try {
			filasSimpleList = 0;
			SimpleHash Temp;
			for (; Resultado.next(); Lista.add(Temp)) {
				Temp = new SimpleHash();
				for (int i = 1; i <= Resultado.getMetaData().getColumnCount(); i++) {
					String Column = Resultado.getMetaData().getColumnName(i);
					Temp.put(Column, Resultado.getString(Column));
				}
				Temp.put("FILA_NUM", String.valueOf(++filasSimpleList));
			}
		}
		catch (SQLException e) {
			System.out.println("--->SQLException en getSimpleList: " + e.getMessage());
			return Lista;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getSimpleList: " + e.getMessage());
			return Lista;
		}
		return Lista;
	}

	public SimpleList getSimpleList(IDataTransformer dataTranformer) {
		SimpleList Lista;
		Lista = new SimpleList();
		try {
			filasSimpleList = 0;
			SimpleHash Temp;
			for (; Resultado.next(); Lista.add(Temp)) {
				Temp = new SimpleHash();
				for (int i = 1; i <= Resultado.getMetaData().getColumnCount(); i++) {
					String Column = Resultado.getMetaData().getColumnName(i);
					Temp.put(Column, dataTranformer.transformText(Column, Resultado.getString(Column)));
				}
				Temp.put("FILA_NUM", String.valueOf(++filasSimpleList));
			}
		}
		catch (SQLException e) {
			System.out.println("--->SQLException en getSimpleList: " + e.getMessage());
			return Lista;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en getSimpleList: " + e.getMessage());
			return Lista;
		}
		return Lista;
	}
	
	public SimpleList getSimpleListColumn() {
		SimpleList Temp;
		SimpleList Lista = new SimpleList();
		Temp = new SimpleList();
		try {
			for (int i = 1; i <= Resultado.getMetaData().getColumnCount(); i++) {
				String Column = Resultado.getMetaData().getColumnName(i);
				Temp.add(Column);
			}
		} 
		catch (SQLException e) {
			System.out.println("Error SQL en getSimpleListColumn: " + e.getMessage());
			return Temp;
		} 
		catch (Exception e) {
			System.out.println("--->Error en getSimpleListColumn: " + e.getMessage());
			return Temp;
		}
		return Temp;
	}

	public SimpleList getSimpleListColumnData() {
		SimpleList Lista;
		Lista = new SimpleList();
		try {
			num_filas = 0;
			SimpleList Temp;
			for (; Resultado.next(); Lista.add(Temp)) {
				Temp = new SimpleList();
				for (int i = 1; i <= Resultado.getMetaData().getColumnCount(); i++) {
					String Column = Resultado.getMetaData().getColumnName(i);
					Temp.add(Resultado.getString(Column));
				}
				num_filas++;
			}
		} 
		catch (SQLException e) {
			System.out.println("Error SQL en getSimpleListColumnData: " + e.getMessage());
			return Lista;
		} 
		catch (Exception e) {
			System.out.println("--->Error en getSimpleListColumnData: " + e.getMessage());
			return Lista;
		}
		return Lista;
	}

	public SimpleHash getSimpleHash() {
		SimpleHash Temp = null;
		if (next()) {
			Temp = new SimpleHash();
			for (int i = 1; i <= getColumnCount(); i++) {
				String Column = getColumnName(i);
				Temp.put(Column, getString(Column));
			}
		}
		return Temp;
	}
	
	public SimpleHash getSimpleHashNoNext() {
		SimpleHash Temp = null;
		 
		Temp = new SimpleHash();
		for (int i = 1; i <= getColumnCount(); i++) {
			String Column = getColumnName(i);
			Temp.put(Column, getString(Column));
		}
		 
		return Temp;
	}

	public boolean isBeforeFirst() {
		boolean value = false;
		try {
			value = Resultado.isBeforeFirst();
		} 
		catch (SQLException e) {
			System.out.println("--->SQLException en isBeforeFirst: " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en isBeforeFirst: " + e.getMessage());
			return value;
		}
		return value;
	}

	public void beforeFirst() {
		boolean value = false;
		try {
			Resultado.beforeFirst();
			return;
		} 
		catch (SQLException e) {
			System.out.println("--->SQLException en beforeFirst: " + e.getMessage());
			return;
		} 
		catch (NullPointerException e) {
			System.out.println("--->Null Pointer Error en beforeFirst: " + e.getMessage());
		} 
	}

	public boolean last() {
		boolean value = false;
		try {
			value = Resultado.last();
		} 
		catch (SQLException e) {
			System.out.println("--->SQLException en last: " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en last: " + e.getMessage());
			return value;
		}
		return value;
	}

	public boolean previous() {
		boolean value = false;
		try {
			value = Resultado.previous();
		} 
		catch (SQLException e) {
			System.out.println("--->SQLException en previous: " + e.getMessage());
			return value;
		} 
		catch (Exception e) {
			System.out.println("--->Null Pointer Error en previous: " + e.getMessage());
			return value;
		}
		return value;
	}

	public int getFilasSimpleList() {
		return filasSimpleList;
	}

	public int getFilas() {
		return num_filas;
	}

	private ResultSet Resultado;
	private Statement Stmt;
    private CallableStatement CStmt;
	private int filasSimpleList;
	private int num_filas;
}