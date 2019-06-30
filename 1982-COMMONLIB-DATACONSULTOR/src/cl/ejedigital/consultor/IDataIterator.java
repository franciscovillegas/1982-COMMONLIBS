package cl.ejedigital.consultor;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

 
public interface IDataIterator extends IData{

	
	
	public void toStart();
	
	public boolean next();
	
	public boolean existField(String colName);
	
	public Field getField(String colName);
	
	public Field getField(int colPosition) throws IndexOutOfBoundsException;
	
	public boolean getBoolean(String colName);
	
	public boolean getBoolean(int colPosition) throws IndexOutOfBoundsException;

	public byte getByte(String colName);
	
	public byte getByte(int colPosition) throws IndexOutOfBoundsException;
	
	public byte[] getByteArray(String colName);
	
	public byte[] getByteArray(int colPosition) throws IndexOutOfBoundsException;

	public short getShort(String colName);
	
	public short getShort(int colPosition) throws IndexOutOfBoundsException;

	public int getInt(String colName);
	
	public int getInt(int colPosition) throws IndexOutOfBoundsException;

	public long getLong(String colName);
	
	public long getLong(int colPosition) throws IndexOutOfBoundsException;

	public float getFloat(String colName);
	
	public float getFloat(int colPosition) throws IndexOutOfBoundsException;

	public double getDouble(String colName);
	
	public double getDouble(int colPosition) throws IndexOutOfBoundsException;
	
	public BigDecimal getBigDecimal(String colName);
	
	public BigDecimal getBigDecimal(int colPosition) throws IndexOutOfBoundsException;

	public java.util.Date getDateJava(String colName);
	
	public java.util.Date getDateJava(int colPosition) throws IndexOutOfBoundsException;

	public Date getDate(String colName);
	
	public Date getDate(int colPosition) throws IndexOutOfBoundsException;

	public Time getTime(String colName);
	
	public Time getTime(int colPosition) throws IndexOutOfBoundsException;

	public Timestamp getTimestamp(String colName);
	
	public Timestamp getTimestamp(int colPosition) throws IndexOutOfBoundsException;

	public String getString(String colName);
	
	public String getString(int colPosition) throws IndexOutOfBoundsException;

	public Blob getBlob(String colName);
	
	public Blob getBlob(int colPosition) throws IndexOutOfBoundsException;
	
	public Clob getClob(String colName);
	
	public Clob getClob(int colPosition) throws IndexOutOfBoundsException;
	
	public String getClobAsString(String colName);
	
	public String getClobAsString(int colPosition) throws IndexOutOfBoundsException;
		
	public Struct getStruct(String colName);
	
	public Struct getStruct(int colPosition) throws IndexOutOfBoundsException;
	
	public Array getArray(String colName);
	
	public Array getArray(int colPosition) throws IndexOutOfBoundsException;	
	
	public void put(String field, Object objeto);
	
	public int getPosition();
	
	public List<String> getNombreColumnas();
	
	public void setPosition(int pos);
	
}
