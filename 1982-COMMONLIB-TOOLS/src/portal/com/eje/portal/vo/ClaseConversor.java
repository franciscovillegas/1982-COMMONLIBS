package portal.com.eje.portal.vo;
 
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;
import net.sourceforge.jtds.jdbc.ClobImpl;
import portal.com.eje.portal.factory.Util;

public class ClaseConversor implements IParamMapping {
	private final Map<Class, Class> buildMapObjectToPrimitive;
	private final Map<Class, Class> buildMapObjectToObject;
	
	public static ClaseConversor getInstance() {
		return Util.getInstance(ClaseConversor.class);
	}
	
	public ClaseConversor() {
		buildMapObjectToPrimitive = new HashMap<Class, Class>();
		buildMapObjectToPrimitive.put(Integer.class, int.class);
		buildMapObjectToPrimitive.put(Long.class, long.class);
		buildMapObjectToPrimitive.put(Double.class, double.class);
		buildMapObjectToPrimitive.put(Float.class, float.class);
		buildMapObjectToPrimitive.put(Boolean.class, boolean.class);
		buildMapObjectToPrimitive.put(Byte.class, byte.class);
		buildMapObjectToPrimitive.put(Void.class, void.class);
		buildMapObjectToPrimitive.put(Short.class, short.class);
		
		
		buildMapObjectToObject = new HashMap<Class, Class>();
		buildMapObjectToObject.put(int.class, Integer.class );
		buildMapObjectToObject.put(long.class, Long.class);
		buildMapObjectToObject.put(double.class, Double.class );
		buildMapObjectToObject.put(float.class , Float.class );
		buildMapObjectToObject.put(boolean.class, Boolean.class);
		buildMapObjectToObject.put(byte.class , Byte.class);
		buildMapObjectToObject.put(void.class, Void.class );
		buildMapObjectToObject.put( short.class, Short.class);
	}
	
	public Map getMappingObjectToPrimitive() {
		return buildMapObjectToPrimitive;
	}
	
	public Map getMappingObjectToObject() {
		return buildMapObjectToObject;
	}
	
	public Class getToPrimitiveConvertionClass(Object o) {
		if(o != null) {
			if(o.getClass().isAssignableFrom(String.class)) {
				return String.class;
			}
			else {
				Class c = buildMapObjectToPrimitive.get(o.getClass());
				if(c != null) {
					return c;
				}
				else {
					return String.class;
				}
			}
		}
		
		return null;
	}
	
	public <T> T getObject(Object o, Class<T> endClass) {
		if(o == null && (endClass == boolean.class || endClass == Boolean.class) ) { // es un boleano
			return (T) new Boolean(false);
		}
		else if(o == null && ( (buildMapObjectToObject.get(endClass)) != null || (buildMapObjectToPrimitive.get(endClass)) != null) ) { //es un numerico
			o = 0;
		}
		else if(o == null) {
			return null;
		}
		
		if(o instanceof String ) {
			return (T) convertString(o, endClass);
		}
		else if(o instanceof Integer ) {
			return (T) convertInteger(o, endClass);
		}
		else if(o instanceof Long ) {
			return (T) convertLong(o, endClass);
		}
		else if(o instanceof Double) {
			return (T) convertDouble(o, endClass);
		}
		else if(o instanceof Float ) {
			return (T) convertFloat(o, endClass);
		}
		else if(o instanceof Boolean ) {
			return (T) convertBoolean(o, endClass);
		}
		else if(o instanceof Byte ) {
			return (T) convertByte(o, endClass);
		}
		else if(o instanceof Void ) {
			throw new NotImplementedException();
		}
		else if(o instanceof Short ) {
			return (T) convertShort(o, endClass);
		}
		else if(o instanceof Timestamp ) {
			return (T) convertTimestamp(o, endClass);
		}
		else if(o instanceof Date ) {
			return (T) convertDate(o, endClass);
		}
		else if(o instanceof BigDecimal ) {
			return (T) convertBigDecimal( o , endClass);
		}
		else if(o instanceof ClobImpl || o instanceof Clob) {
			String str = getClobAsString((Clob) o);
			return (T) convertString(str, endClass);
		}
		else if(o instanceof GregorianCalendar) {
			return (T) convertGregorianCalendar(o, endClass);
		}
		else if(o instanceof Enum) {
			return (T) convertEnum(o, endClass);
		}
		else if(o instanceof Class) {
			return (T) convertClass(o, endClass);
		}
		else if(o instanceof Class) {
			return (T) convertClass(o, endClass);
		}
		else if(o instanceof Connection) {
			return (T) convertConnection(o, endClass);
		}
		else if(o instanceof Object) {
			return (T) convertObject(o, endClass);
		}
		else if(o instanceof File) {
			return (T) convertFile(o, endClass);
		}
		else {
			throw new NotImplementedException(o.getClass());
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	private <T> T convertBigDecimal(Object o, Class<T> endClass) {
		BigDecimal bd = (BigDecimal) o;
		
		if(String.class.equals(endClass)) {
			DecimalFormat df = new DecimalFormat("#.#");  
			String output = df .format(bd);
			
			return (T) output;
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass) ) {
			return (T) new Integer(bd.intValue());
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass) ) {
			return (T) new Boolean(getBooleanFromNumber(bd.intValue()));
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass) ) {
			return (T) new Double(bd.doubleValue());
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass) ) {
			return (T) new Float(bd.floatValue());
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass) ) {
			return (T) new Long(bd.longValue());
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (T) new Short(bd.shortValue());
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return  (T) new Byte(bd.byteValue());
		}
		else if(BigDecimal.class.equals(endClass)  ) {
			return   (T) bd ;
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			Date date = new Date();
			date.setTime(bd.longValue());
			return (T) date;
		} 
		else {
			throw new NotImplementedException(endClass);	
		}
	}

	private Object convertEnum(Object o, Class endClass) {
		Enum e = (Enum) o;
		
		if(String.class.equals(endClass)) {
			return String.valueOf(e);
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass) ) {
			return e.ordinal();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass) ) {
			return e.ordinal();
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass) ) {
			return e.ordinal();
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass) ) {
			return e.ordinal();
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass) ) {
			return e.ordinal();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return e.ordinal();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(char.class.equals(endClass) ) {
			return e.ordinal();	
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		} 
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertGregorianCalendar(Object o, Class endClass) {
		GregorianCalendar gc = (GregorianCalendar) o;
		
		if(String.class.equals(endClass)) {
			return Formatear.getInstance().toDate(gc.getTime(), "dd/MM/yyyy HH:mm:ss.SSS");
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass) ) {
			return gc.getTimeInMillis();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass) ) {
			return gc != null;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass) ) {
			return gc.getTimeInMillis();	
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass) ) {
			return gc.getTimeInMillis();
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass) ) {
			return gc.getTimeInMillis();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return gc.getTimeInMillis();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return gc.getTimeInMillis();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			 return gc.getTime();
		} 
		else {
			throw new NotImplementedException(endClass);	
		}
	}

	private Object convertString(Object o, Class endClass) {
		if(String.class.equals(endClass)) {
			return String.valueOf(o);
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass) ) {
			return (int)Validar.getInstance().validarInt(String.valueOf(o),0);
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass) ) {
			return (boolean)(String.valueOf(o).toLowerCase().equals("true") || Validar.getInstance().validarInt(String.valueOf(o), 0) > 0);
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass) ) {
			return (double)(Validar.getInstance().validarDouble(String.valueOf(o),-1));		
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass) ) {
			return (float)(Validar.getInstance().validarFloat(String.valueOf(o),-1));
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass) ) {
			return (long)(Validar.getInstance().validarLong(String.valueOf(o),-1));
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (short)(Validar.getInstance().validarInt(String.valueOf(o),-1));
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (byte)(Validar.getInstance().validarInt(String.valueOf(o),-1));
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			String s = String.valueOf(o);
			if(s != null && s.indexOf("T") != -1) {
				s = s.replaceAll("T", " ");
				
				return Formatear.getInstance().toDate(s, getFormat(s));
			}
			else {
				return Formatear.getInstance().toDate(s, getFormat(s));
			}
		} 
		else {
			throw new NotImplementedException(endClass);	
		}
 
	}
	
	private String getFormat(String dateOriginal) {
		boolean tieneSlashs = dateOriginal.indexOf("/") != -1;
		boolean tieneGuion =  dateOriginal.indexOf("-") != -1;

		int lastPosition = String.valueOf(dateOriginal).length();
		
		if(tieneGuion && dateOriginal.indexOf("-") == 4) {
			
			String formatYYYY = "yyyy-MM-dd HH:mm:ss.SSS";
			int maxPos = formatYYYY.length();
			
			return formatYYYY.substring(0, lastPosition <= maxPos ? lastPosition : maxPos);
		}
		else if(tieneSlashs && dateOriginal.indexOf("/") == 2) {
			
			String formatDD = "dd/MM/yyyy HH:mm:ss.SSS";
			int maxPos = formatDD.length();
			
			return formatDD.substring(0, lastPosition <= maxPos ? lastPosition : maxPos);
		}
		else {
			return null;
		}
			 
			
	}
	
	private Object convertInteger(Object o, Class endClass) {
		Integer integer = (Integer) o == null ? 0 : (Integer) o;
		
		if(String.class.equals(endClass)) {
			return (String.valueOf(integer));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return (integer).intValue();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (integer).intValue() > 0;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (integer).doubleValue();		
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return  (integer).floatValue();		
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (integer).longValue();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (integer).shortValue();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (byte) integer.intValue();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertLong(Object o, Class endClass) {
		Long longg = (Long) o ==null ? 0 : (Long) o;
		
		if(String.class.equals(endClass)) {
			return (String.valueOf(longg));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass) ) {
			return (longg).intValue();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (longg).intValue() > 0;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (longg).doubleValue();		
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (longg).floatValue();		
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (longg).longValue();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (longg).shortValue();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertDouble(Object o, Class endClass) {
		Double doublee = (Double) o == null ? 0 : (Double) o;
		
		if(String.class.equals(endClass)) {
			return (String.valueOf(o));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return (doublee).intValue();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (doublee).intValue() > 0;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (doublee).doubleValue();		
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (doublee).floatValue();		
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (doublee).longValue();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (doublee).shortValue();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (doublee).byteValue();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertFloat(Object o, Class endClass) {
		Float floatt = (Float) o == null ? 0 : (Float) o;
		
		if(String.class.equals(endClass)) {
			return (String.valueOf(o));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return (floatt).intValue();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (floatt).intValue() > 0;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (floatt).doubleValue();		
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (floatt).floatValue();		
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (floatt).longValue();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (floatt).shortValue();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (floatt).byteValue();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertBoolean(Object o, Class endClass) {
		Boolean booleano = (Boolean) o == null ? false :  (Boolean) o;
		
		if(String.class.equals(endClass)) {
			return (String.valueOf(booleano));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return (booleano).booleanValue()?1:0;
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (booleano).booleanValue();
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (booleano).booleanValue()?1:0;
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (booleano).booleanValue()?1:0;			
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (booleano).booleanValue()?1:0;	
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (booleano).booleanValue()?1:0;	
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (booleano).booleanValue()?1:0;	
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertShort(Object o, Class endClass) {
		Short shortt = (Short) o == null ? 0 : (Short) o ;
		
		if(String.class.equals(endClass)) {
			return (String.valueOf(shortt));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return (shortt).intValue();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (shortt).intValue() > 0;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (shortt).doubleValue();
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (shortt).floatValue();			
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (shortt).longValue();	
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (shortt).shortValue();	
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (shortt).byteValue();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertByte(Object o, Class endClass) {
		Byte bytee = (Byte) o == null ? 0 : (Byte) o;
		
		Byte b = (Byte)o;
		if(String.class.equals(endClass)) {
			return (String.valueOf(o));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return (bytee).intValue();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (bytee).intValue() > 0;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (bytee).doubleValue();
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (bytee).floatValue();			
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (bytee).longValue();	
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (bytee).shortValue();	
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (bytee).byteValue();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertTimestamp(Object o, Class endClass) {
	    Date date = new java.util.Date(((Timestamp) o).getTime());
	    long milliseconds = date.getTime();
	    
		if(String.class.equals(endClass)) {
			return (Formatear.getInstance().toDate(date, "dd/MM/yyyy HH:mm:ss"));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return milliseconds;
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return false;
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return milliseconds;
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return milliseconds;
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return milliseconds;
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (short)milliseconds;
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (byte)milliseconds;
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			return date;
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertDate(Object o, Class endClass ) {
		Date d = (Date) o;
	    
		if(String.class.equals(endClass)) {
			return (Formatear.getInstance().toDate(d, "dd/MM/yyyy HH:mm:ss.SSS"));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			throw new NotImplementedException();	
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			throw new NotImplementedException();	
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (double)d.getTime();
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (float)d.getTime();
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (long)d.getTime();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (short)d.getTime();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			return (byte)d.getTime();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			return d;
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertClass(Object clase, Class<?> endClass ) {
		Class<?> o = (Class<?>) clase;
	    
		if(String.class.equals(endClass)) {
			return (o.getCanonicalName());
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException(endClass);	
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException(endClass);	
		}
		else {
			throw new NotImplementedException(endClass);	
		}
	}
	
	private Object convertFile(Object o, Class endClass) {
		
		if(String.class.equals(endClass)) {
			return (String.valueOf(o));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			return (java.util.Date) o;
		}
		
		return null;
	}
	
	private Object convertDefault(Object o, Class endClass) {
		if(String.class.equals(endClass)) {
			return (String.valueOf(o));
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			return ((Integer)o).intValue();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			return (o.equals(true) || Validar.getInstance().validarInt(String.valueOf(o)) > 0);
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			return (Validar.getInstance().validarDouble(String.valueOf(o),-1));		
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			return (Validar.getInstance().validarFloat(String.valueOf(o),-1));
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			return (Validar.getInstance().validarLong(String.valueOf(o),-1));
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			return (Validar.getInstance().validarInt(String.valueOf(o),-1));
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			if(o instanceof Timestamp) {
				Timestamp timestamp = (Timestamp) o;
			    long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
			    return new java.util.Date(milliseconds);
			}
			else if(o instanceof Date) {
				return (Date) o;
			}
			else if(o instanceof java.sql.Date) {
				java.sql.Date sqlDate = (java.sql.Date) o;
				java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
				return utilDate;
			}
 
		}
		
		
		
		return null;
	}
	
	private Object convertConnection(Object o, Class endClass) {
		Connection conn = (Connection) o;
	
		if(String.class.equals(endClass)) {
			try {
				return new StringBuilder().append(conn.getMetaData().getURL()).append("|")
						.append(conn.getMetaData().getUserName()).toString();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			throw new NotImplementedException();	
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
 
		}
		
		return null;
	}
	
	private Object convertObject(Object o, Class endClass) {
	
		if(String.class.equals(endClass)) {
			return o.toString();
		}
		else if(int.class.equals(endClass) || Integer.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(boolean.class.equals(endClass) || Boolean.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(double.class.equals(endClass) || Double.class.equals(endClass)) {
			throw new NotImplementedException();	
		}
		else if(float.class.equals(endClass) || Float.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(long.class.equals(endClass) || Long.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(short.class.equals(endClass) || Short.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(byte.class.equals(endClass) || Byte.class.equals(endClass)) {
			throw new NotImplementedException();
		}
		else if(char.class.equals(endClass) ) {
			throw new NotImplementedException();		
		}
		else if(java.util.Date.class.equals(endClass)) {
			throw new NotImplementedException();
 
		}
		
		return null;
	}
	
	private boolean getBooleanFromNumber(int number) {
		return number > 0;
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
