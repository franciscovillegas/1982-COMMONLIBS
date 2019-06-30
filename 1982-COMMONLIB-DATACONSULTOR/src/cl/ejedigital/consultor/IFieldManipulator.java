package cl.ejedigital.consultor;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public interface IFieldManipulator {

	public boolean getBoolean(Field f);

	public byte getByte(Field f);

	public short getShort(Field f);

	public int getInt(Field f);

	public long getLong(Field f);

	public float getFloat(Field f);

	public double getDouble(Field f);

	public java.util.Date getDateJava(Field f);

	public Date getDate(Field f);

	public Time getTime(Field f);

	public Timestamp getTimestamp(Field f);

	public String getString(Field f);
	
	public String getForcedString(Field f);

	public Blob getBlob(Field f);
	
	public BigDecimal getBigDecimal(Field f);
	
	public Clob getClob(Field f);

	public byte[] getByteArray(Field f);

	public Object getObject(Field field);

}
