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

import portal.com.eje.portal.vo.ClaseConversor;


public class ManipulatorSqlServerV2018 implements IFieldManipulator {

	
	public boolean getBoolean(Field f) {
		try {
			return (Boolean)ClaseConversor.getInstance().getObject(f.getObject(), Boolean.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public byte getByte(Field f) {
		try {
			return (Byte)ClaseConversor.getInstance().getObject(f.getObject(), Byte.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public short getShort(Field f) {
		try {
			return (Short)ClaseConversor.getInstance().getObject(f.getObject(), Short.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public int getInt(Field f) {
		try {
			return (Integer)ClaseConversor.getInstance().getObject(f.getObject(), Integer.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public long getLong(Field f) {
		try {
			return (Long)ClaseConversor.getInstance().getObject(f.getObject(), Long.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public float getFloat(Field f) {
		try {
			return (Float)ClaseConversor.getInstance().getObject(f.getObject(), Float.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public double getDouble(Field f) {
		try {
			return (Double)ClaseConversor.getInstance().getObject(f.getObject(), Double.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public Date getDateJava(Field f) {
		try {
			return (Date)ClaseConversor.getInstance().getObject(f.getObject(), Date.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public java.sql.Date getDate(Field f) {
		try {
			return (java.sql.Date)ClaseConversor.getInstance().getObject(f.getObject(), java.sql.Date.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Time getTime(Field f) {
		try {
			return (Time)ClaseConversor.getInstance().getObject(f.getObject(), Time.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Timestamp getTimestamp(Field f) {
		try {
			return (Timestamp)ClaseConversor.getInstance().getObject(f.getObject(), Timestamp.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getString(Field f) {
		try {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Blob getBlob(Field f) {
		try {
			return (Blob)ClaseConversor.getInstance().getObject(f.getObject(), Blob.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public BigDecimal getBigDecimal(Field f) {
		try {
			return (BigDecimal)ClaseConversor.getInstance().getObject(f.getObject(), BigDecimal.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return new BigDecimal(0);
	}

	public Clob getClob(Field f) {
		try {
			return (Clob)ClaseConversor.getInstance().getObject(f.getObject(), Clob.class);
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public byte[] getByteArray(Field f) {
		try {
			return (byte[])ClaseConversor.getInstance().getObject(f.getObject(), byte[].class);
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
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Byte) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Short) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Integer) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Long) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Float) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Double) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof java.util.Date) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof java.sql.Time) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof java.sql.Timestamp) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof String) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Blob) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof BigDecimal) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else if(f.getObject() instanceof Clob) {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
		else {
			return (String)ClaseConversor.getInstance().getObject(f.getObject(), String.class);
		}
	}

}
