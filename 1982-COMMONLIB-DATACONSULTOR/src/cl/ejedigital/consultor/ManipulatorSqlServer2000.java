package cl.ejedigital.consultor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;


public class ManipulatorSqlServer2000 implements IFieldManipulator {

	
	public boolean getBoolean(Field f) {
		try {
			if(f.getObject() == null) {
				return false;
			}
			else {
				return (Boolean)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public byte getByte(Field f) {
		try {
			if(f.getObject() == null) {
				return 0;
			}
			else {
				return (Byte)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public short getShort(Field f) {
		try {
			if(f.getObject() == null) {
				return 0;
			}
			else {
				return (Short)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public int getInt(Field f) {
		try {
			if(f.getObject() == null) {
				return 0;
			}
			else {
				return (Integer)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public long getLong(Field f) {
		try {
			if(f.getObject() == null) {
				return 0;
			}
			else {
				return (Long)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public float getFloat(Field f) {
		try {
			if(f.getObject() == null) {
				return 0;
			}
			else {
				return (Float)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public double getDouble(Field f) {
		try {
			if(f.getObject() == null || "".equals(f.getObject())) {
				return 0;
			}
			else {
				return (Double)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public Date getDateJava(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				return (java.util.Date)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public java.sql.Date getDate(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				return (java.sql.Date)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Time getTime(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				return (java.sql.Time)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Timestamp getTimestamp(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				return (java.sql.Timestamp)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getString(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				return (String)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Blob getBlob(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				return (Blob)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public BigDecimal getBigDecimal(Field f) {
		try {
			if(f.getObject() == null) {
				return new BigDecimal(0);
			}
			else {
				return (BigDecimal)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(0);
	}

	public Clob getClob(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				return (Clob)f.getObject();
			}
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public byte[] getByteArray(Field f) {
		try {
			if(f.getObject() == null) {
				return null;
			}
			else {
				 java.sql.Clob b = ( java.sql.Clob)f.getObject();
				//byte[] data = b.getBytes(1, (int) b.length());
				//return data;
			}
		} catch(ClassCastException e) {
			e.printStackTrace();
		}  
		
		return null;
	}
	
	@Override
	public Object getObject(Field field) {
		return field.getObject();
	}

	
	public String getForcedString(Field f) {
		
		if(f.getObject() instanceof Boolean) {
			return String.valueOf(getBoolean(f));
		}
		else if(f.getObject() instanceof Byte) {
			return String.valueOf(getByte(f));
		}
		else if(f.getObject() instanceof Short) {
			return String.valueOf(getShort(f));
		}
		else if(f.getObject() instanceof Integer) {
			return String.valueOf(getInt(f));
		}
		else if(f.getObject() instanceof Long) {
			return String.valueOf(getLong(f));
		}
		else if(f.getObject() instanceof Float) {
			return String.valueOf(getFloat(f));
		}
		else if(f.getObject() instanceof Double) {
			return String.valueOf(getDouble(f));
		}
		else if(f.getObject() instanceof java.util.Date) {
			java.util.Date date = getDateJava(f);
			
			return Formatear.getInstance().toDate(date, "yyyy-MM-dd hh:mm:ss");
		}
		else if(f.getObject() instanceof java.sql.Time) {
			return String.valueOf(getTime(f));
		}
		else if(f.getObject() instanceof java.sql.Timestamp) {
			return String.valueOf(getTimestamp(f));
		}
		else if(f.getObject() instanceof String) {
			return getString(f);
		}
		else if(f.getObject() instanceof Blob) {
			return String.valueOf(getBlob(f));
		}
		else if(f.getObject() instanceof BigDecimal) {
			return String.valueOf(getBigDecimal(f));
		}
		else if(f.getObject() instanceof Clob) {
			return getClobAsString(getClob(f));
		}
		else {
			return null;
		}
	}

	private String getClobAsString(Clob c) {
		StringWriter sw = new StringWriter();
		
		try {
			char clobVal[] = new char[(int) c.length()];
			Reader r = c.getCharacterStream();
		    r.read(clobVal);
		    
		    sw.write(clobVal);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return sw.toString();
	}
	
}
