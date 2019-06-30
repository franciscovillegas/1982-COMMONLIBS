package cl.ejedigital.consultor;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.output.IEscape;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.reflect.ClaseConstructor;
 

public class ConsultaData implements IMetable, IDataIterator, Collection<DataFields> {

	private List<String>		nombreColumnas;
	protected final DataList	data;
	private int					indice;
	private IFieldManipulator	fieldManipulator;
	private Integer 			printEstatusMillisecond;
	private Cronometro 			printEstatusCro;
	public	double	 			time;
	public  String 				timeStr;
	private IMetaData	metaData;
	private int 				preCountTotal;
	
	public ConsultaData(List<String> nombreColumnas) {
		super();
		
		if(nombreColumnas == null ) {
			throw new ExceptionInInitializerError("Los nombres de columnas no pueden ser null");
		}
		else if(nombreColumnas.size() == 0 ) {
			throw new ExceptionInInitializerError("Debe por lo menos tener una columna");
		}
		this.nombreColumnas = nombreColumnas;
		this.data = new DataList();
		
		this.fieldManipulator = new ManipulatorSqlServer2000();
		setMode(ConsultaDataMode.CONVERSION);
		
		printEstatusCro = new Cronometro();
		printEstatusCro.start();
		
		toStart();
	}
	
	public void setMode(ConsultaDataMode mode) {
		if(mode != null && mode.getImplementation() != fieldManipulator.getClass()) {
			Class<?>[] defs = {};
			Object[] params = {};
			this.fieldManipulator = ClaseConstructor.getNewFromClass(mode.getImplementation(), defs, params);
		}
	}
	
	public void printEstatusEverySecond(int millisecond) {
		printEstatusMillisecond = millisecond;
	}
	
	public int getPosition() {
		return indice;
	}

	public void setPosition(int newPosition) {
		indice = newPosition;
	}
	
	
	public void setFieldManipulator(IFieldManipulator nuevo) {
		if(nuevo != null) {
			this.fieldManipulator = nuevo;
		}
	}

	/**
	 * Método que entrega los nombre de los campos de la data, cada nombre viene
	 * dado en el mismo orden de aparición dentro de la consulta SQL
	 * 
	 * @author Francisco
	 * */
	public List<String> getNombreColumnas() {
		return nombreColumnas;
	}

	public DataList getData() {
		return data;
	}
	
	/**
	 * Este método retorna true si actualmente la posisión del puntero puede retornar información 
	 * a través de los métodos get
	 * (Ej: getString, getDate, getInt, getFloar, ect) esto ocurre solo si anteriormente 
	 * se ejecutó next(). <br/>
	 * */
	public boolean existData() {
		return indice >= 0  && (data.size() - 1) >= indice ;
	}
	
	public DataFields getActualData() {
		if(existData()) {
			return data.get(indice);
		}
		
		return null;
	}

	public void toStart() {
		indice = -1;
	}

	public boolean next() {
		if(printEstatusMillisecond != null) {
			if(indice == -1 || !(data.size() > (indice+1)) || printEstatusCro.GetMilliseconds() >= printEstatusMillisecond) {
				printEstatusPercent();
				
				if(!(data.size() > (indice+1))) {
					System.out.print("\n");
				}
				
				printEstatusCro.start();
			}
		}
		
		
		if(data.size() > (indice+1)) {
			indice++;
			return true;
		}

		return false;
	}

	/**
	 * El parámetro debe exactamente el nombre de la columna, es sensible a
	 * mayusculas </br>
	 * 
	 * 
	 * @return La interfaz de un objeto Field; </br> null si no existen
	 *         registros; </br> null si no existe el nombre del columna </br>
	 * 
	 * 
	 * 
	 * */

	public Field getField(String colName) throws NoSuchFieldError {
		if(data.size() > indice) {
			if(colName != null) {
				if(data.get(indice).get(colName) != null) {
					return data.get(indice).get(colName);
				}
				else if(data.get(indice).get(colName.toLowerCase()) != null) {
					return data.get(indice).get(colName.toLowerCase());
				}
				else {
					throw new NoSuchFieldError("No existe el campo \""+colName+"\"");
				}
			}
			
		}

		return null;
	}
	
	
	
	public boolean existField(String colName) {
		try {
			getField(colName);
			return true;
		}
		catch (NoSuchFieldError e) {
			return false;
		}
	}

	/**
	 * El parámetro debe ser >= que 0. </br> Para un resultado con un solo
	 * campo, este podrá recibir como parámetro válido un "0" </br> Para un
	 * resultado con 3 campos, este podrá recibir como parámetros válidos
	 * valores del 0 al 2 </br>
	 * 
	 * @return La interfaz de un objeto Field; null si no existen registros
	 * 
	 * @throws IndexOutOfBoundsException
	 *             cuando colPosition sobrepasa el limite de columnas - 1.
	 * */

	public Field getField(int colPosition) throws NoSuchFieldError {
		if(data.size() > indice) {
			if(nombreColumnas.size() > colPosition) {
				
				return getField(nombreColumnas.get(colPosition));
			}

			throw new NoSuchFieldError("No existe la posicion \""+colPosition+"\"");

		}

		return null;
	}

	public boolean getBoolean(String colName) {
		return fieldManipulator.getBoolean(getField(colName));
	}

	public boolean getBoolean(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getBoolean(getField(colPosition));
	}

	public byte getByte(String colName) {
		return fieldManipulator.getByte(getField(colName));
	}

	public byte getByte(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getByte(getField(colPosition));
	}

	public short getShort(String colName) {
		return fieldManipulator.getShort(getField(colName));
	}

	public short getShort(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getShort(getField(colPosition));
	}

	public int getInt(String colName) {
		return fieldManipulator.getInt(getField(colName));
	}

	public int getInt(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getInt(getField(colPosition));
	}

	public long getLong(String colName) {
		return fieldManipulator.getLong(getField(colName));
	}

	public long getLong(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getLong(getField(colPosition));
	}

	public float getFloat(String colName) {
		return fieldManipulator.getFloat(getField(colName));
	}

	public float getFloat(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getFloat(getField(colPosition));
	}

	public double getDouble(String colName) {
		return fieldManipulator.getDouble(getField(colName));
	}

	public double getDouble(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getDouble(getField(colPosition));
	}

	public Date getDateJava(String colName) {
		return fieldManipulator.getDateJava(getField(colName));
	}

	public Date getDateJava(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getDateJava(getField(colPosition));
	}

	public java.sql.Date getDate(String colName) {
		return fieldManipulator.getDate(getField(colName));
	}

	public java.sql.Date getDate(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getDate(getField(colPosition));
	}

	public Time getTime(String colName) {
		return fieldManipulator.getTime(getField(colName));
	}

	public Time getTime(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getTime(getField(colPosition));
	}

	public Timestamp getTimestamp(String colName) {
		return fieldManipulator.getTimestamp(getField(colName));
	}

	public Timestamp getTimestamp(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getTimestamp(getField(colPosition));
	}

	public String getString(String colName) {
		return fieldManipulator.getString(getField(colName));
	}

	public String getString(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getString(getField(colPosition));
	}

	public Blob getBlob(String colName) {
		return fieldManipulator.getBlob(getField(colName));
	}

	public Blob getBlob(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getBlob(getField(colPosition));
	}
	
	public byte[] getByteArray(String colName) {
		return fieldManipulator.getByteArray(getField(colName));
	}

	public byte[] getByteArray(int colPosition) throws IndexOutOfBoundsException {
		throw new NotImplementedException();
	}

	public BigDecimal getBigDecimal(String colName) {
		return fieldManipulator.getBigDecimal(getField(colName));
	}

	public BigDecimal getBigDecimal(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getBigDecimal(getField(colPosition));
	}

	public Clob getClob(String colName) {
		return fieldManipulator.getClob(getField(colName));
	}

	public Clob getClob(int colPosition) throws IndexOutOfBoundsException {
		return fieldManipulator.getClob(getField(colPosition));
	}
	
	public String getClobAsString(String colName) {
		return getClobAsString(getClob(colName));
	}

	public String getClobAsString(int colPosition) throws IndexOutOfBoundsException {
		return getClobAsString(getClob(colPosition));
	}
	
	public String getForcedString(String colName) {
		return fieldManipulator.getForcedString(getField(colName));
	}
	
	public String getForcedString(int colPosition) {
		return fieldManipulator.getForcedString(getField(colPosition));
	}
	
	public Object getObject(String colName) {
		return fieldManipulator.getObject(getField(colName));
	}
	
	public Object getObject(int colPosition) {
		return fieldManipulator.getObject(getField(colPosition));
	}
	
	private String getClobAsString(Clob c) {
		
		if( c == null) { 
			return null;
		}
		
		
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
	
	public Struct getStruct(String colName) {
		throw new NotImplementedException();
	}

	public Struct getStruct(int colPosition) throws IndexOutOfBoundsException {
		throw new NotImplementedException();
	}

	public Array getArray(String colName) {
		throw new NotImplementedException();
	}

	public Array getArray(int colPosition) throws IndexOutOfBoundsException {
		throw new NotImplementedException();
	}
	

	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.size() == 0;
	}

	public boolean contains(Object o) {
		return data.contains(o);
	}

	public Iterator<DataFields> iterator() {
		return data.iterator();
	}

	public Object[] toArray() {
		return data.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	public boolean add(DataFields o) {
		return data.add(o);
	}

	public boolean remove(Object o) {
		return data.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return data.containsAll(c);
	}

	public boolean addAll(Collection<? extends DataFields> c) {
		return data.addAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return data.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return data.retainAll(c);
	}

	public void clear() {
		data.clear();
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		
		try {
			if(data.size() > indice) {
				strBuf.append(data.get(indice).toString());
			}
			
			return strBuf.toString();
		} catch(Exception e) {
			
		} finally {
			strBuf.delete(0,   strBuf.length());
			strBuf = null;
		}
		
		return null;
	}
	
	public void printEstatusPercent() {
		System.out.print("["+ Formatear.getInstance().redondear((this.indice+1) * 100D / data.size(),1)  +"%]");
	}
	
	public void printTableOverConsole() {
		printTable(System.out);
	}
	
	
	public void printTable(PrintStream ps) {
		printTable(ps, null);
	}
	
	public void printTime() {
		printTime(System.out);
	}
	
	public void printTime(PrintStream p) {
		if(this.printEstatusCro != null && p != null){
			p.println(this.printEstatusCro.GetTimeHHMMSS());	
		}
	}
	
	public void printTable(PrintStream ps, List<String> colName) {
		synchronized (this) {
			int tmpIndex = this.indice;
			
			toStart();
			
			while(next()) {
				int i = 0;
				
				for(String columna : nombreColumnas) {
					if(!existField(columna)) {
						continue;
					}
					boolean print = false;
					
					if(colName != null) {
						if(colName.indexOf(columna) != -1) {
							print = true;
						}
					}
					else {
						print = true;
					}
					
					
					if(print) {
						try {
							ps.write((getForcedString(columna) != null? getForcedString(columna): "").concat("\t").getBytes());
						} catch (IOException e) {
	
						}
					}
					i++;
				}
				
				try {
					ps.write("\n".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			this.indice = tmpIndex;
		}
	}
	
	public String getJSON() {
		JSarrayDataOut dataArray = null;
		
		JSonDataOut dataJs    = new JSonDataOut(data);
		dataJs.setEscape(new EscapeSencha());
		
		DataList result2 = new DataList();
		DataFields fields2 = new DataFields();
		fields2.put("value", new Field(dataJs));
		result2.add(fields2);
		
		dataArray = new JSarrayDataOut(result2);
		return dataArray.getListData();
	}
	
	
	public void markTimeGetData() {
		this.time = this.printEstatusCro.GetMilliseconds();
		this.timeStr = this.printEstatusCro.getTimeHHMMSS_milli();
	}

	public void markTimeGetData(long milliseconds) {
		this.time = milliseconds;
		this.timeStr = new Cronometro(milliseconds).getTimeHHMMSS_milli();
	}
	
	class EscapeSencha implements IEscape {

		public String escape(String normal) {
			return StringEscapeUtils.escapeJava(normal);
		}
		
	}

	public boolean appendRight(ConsultaData data2) {
		try {
			if(data == null) {
				throw new NullPointerException();
			}
			
			if(this.size() != data2.size()) {
				throw new RuntimeException("para ejecutar el append, ambas ConsultaData deben tener la misma cantidad de registros.");
			}
			
			List<String> cols1 = getNombreColumnas();;
			List<String> cols2 = data2.getNombreColumnas();
			List<String> colsInterseccion = new ArrayList<String>();
			
			for(String s : cols2) {
				if(cols1.indexOf(s) != -1) {
					colsInterseccion.add(s);
				}
			}
			
			cols1.addAll(cols2);
			
			synchronized(this.getClass()) {
				int i = getPosition();
				
				toStart();
				data2.toStart();
				
				while(data2.next()) {
					next();
					
					for(String nombre : cols2) {
						boolean debeSerIgual = false;
						boolean sonIguales = false;
						String valDebeSerIgual1 = null;
						String valDebeSerIgual2 = null;
						
						if(colsInterseccion.indexOf(nombre) != -1) {
							/*CHECK solo si son iguales los valores para aquellas columnas en común, si no son iguales no hará nada*/
							valDebeSerIgual1 = getForcedString(nombre);
							valDebeSerIgual2 = data2.getForcedString(nombre);
							debeSerIgual = true;
							
							if(valDebeSerIgual1 == valDebeSerIgual2) {
								sonIguales = true;
							}
							else if(valDebeSerIgual1 != null && valDebeSerIgual1.equals(valDebeSerIgual2)) {
								sonIguales = true;								
							}							
						}
					 
						if(debeSerIgual && !sonIguales) {
							throw new RuntimeException("La columna \""+nombre+"\" debe tener el mismo valor en ambas ConsultaData pero no es así [Valor Local:"+valDebeSerIgual1+"  Valor Externo:\""+valDebeSerIgual2+"\"]");
						}
						
						Field f = null;
						if(data2.getActualData().get(nombre) != null) {
							f =data2.getField(nombre);
						}
						
						getActualData().put(nombre, f);
					}
				}
				
				setPosition(i);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public ConsultaData buildSubConsultaData(List<String> nombreColumnas) {
		if(nombreColumnas == null) {
			throw new NullPointerException();
		}
		else if(nombreColumnas.size() == 0) {
			throw new NullPointerException("Debe tener al menos una columna.");
		}
		
		synchronized (this.getClass()) {
			ConsultaData retorno = new ConsultaData(nombreColumnas);
			int pos = getPosition();
			
			toStart();
			
			while (next()) {
				DataFields df = new DataFields();
				
				for(String col : nombreColumnas) {
					Field f = null;
					
					if(getNombreColumnas().indexOf(col) != -1) {
						f  = getField(col);
					}
					
					if(f != null) {
						df.put(col,  f.getObject());	
					}
					else {
						df.put(col,  null);
					}
				}
				
				retorno.add(df);
			}
			
			setPosition(pos);
			
			return retorno;
		}
	}

	/**
	 * @@author Pancho
	 * @since 2018-03-13
	 * 
	 * Convierte la primera fila en los nombres de columnas, este es un método pesado dado que recorre todos las filas cambiando las asignaciones
	 * */
	public void transformFirstRowIntoHeader() {
		int indice = getPosition();
		
		List<String> nCols = getNombreColumnas();
		Map<String, String> newMappingCols = new LinkedHashMap<String, String>();
		List<String> newCols = new ArrayList<String>();
		
		setPosition(0);
		
		for(String nCol : nCols) {
			String c = new String(getForcedString(nCol)).toLowerCase();
			
			if(c != null && !c.equals("")) {
				if(newCols.indexOf(c) == -1) {
					newMappingCols.put( nCol , c );
					newCols.add(c);		
				}
				else {
					int i = 1;
					while( newCols.indexOf(c + "_" + i) != -1 ) {
						i++;
					}
					
					c = c + "_" + i;
					newMappingCols.put( nCol , c );
					newCols.add(c);		
				}
				
			}
		}
		
		this.nombreColumnas = newCols;
		data.remove(0);
		
		for(DataFields field : data) {
			for(String nCol : nCols) {
				if(newMappingCols.get(nCol)!= null) {
					Object o = field.get(nCol);
					field.remove(nCol);
					field.put(newMappingCols.get(nCol), ((Field)o).getObject() );
				}
			}
		}
		
		setPosition(indice);
	}

	@Override
	public IMetaData getMetaData() {
		return metaData;
	}

	@Override
	public void setMetaData(IMetaData metaData) {
		this.metaData = metaData;
	}

	public int getPreCountTotal() {
		return preCountTotal;
	}

	public void setPreCountTotal(int preCountTotal) {
		this.preCountTotal = preCountTotal;
	}

	/**
	 * Retorna una consultaData sobre el indice vigente <br/>
	 * Lo entrega en la posición 1, si tiene
	 * 
	 * @author Pancho
	 * @since 01-10-2018
	 * 
	 * */
	public ConsultaData getSubConsultaData() {
		int ultPosicion = (this.data.size());
		
		if(this.getPosition() < 0) {
			throw new IndexOutOfBoundsException(" la fila debe ser mayor 0");
		}
		else if( this.getPosition() < 0 || this.getPosition() > ultPosicion) {
			StringBuilder str = new StringBuilder();
			str.append(" La última posición es la ").append(ultPosicion).append(" y se está seleccionando (").append(this.getPosition()).append(")");
			throw new IndexOutOfBoundsException(str.toString());
		}
		
		return getSubConsultaData(this.getPosition(), this.getPosition()+ 1);
	}
 
	/**
	 * Retorna una consultaData entre los dos indices que se indican  <br/>
	 * Lo entrega en la posición 1, si tiene
	 * 
	 * @author Pancho
	 * @since 01-10-2018
	 * 
	 * */
	public ConsultaData getSubConsultaData(int inicio, int termino) {
		int ultPosicion = (this.data.size() );
		
		if(this.indice < 0 ) {
			throw new IndexOutOfBoundsException(" inicio debe ser mayor 0");
		}
		else if( inicio > ultPosicion  || termino > ultPosicion) {
			StringBuilder str = new StringBuilder();
			str.append(" La última posición es la ").append(ultPosicion).append(" y se está seleccionando (").append(inicio).append(",").append(termino).append(")");
			throw new IndexOutOfBoundsException(str.toString());
		}
		
		ConsultaData newData = new ConsultaData(this.getNombreColumnas());
		
		newData.addAll( this.data.subList(inicio, termino) );
		newData.next();
		
		return newData;
	}

	@Override
	public void put(String field, Object value) {
		getActualData().put(field, value);
		
	}
	
	public void each(IConsultaDataStartable startable) {
		if(startable != null) {
			int pos = getPosition();
			try {
				toStart();
				while(next()) {
					startable.each(this);	
				}
				
			}finally {
				setPosition(pos);
			}
		}
	}

}
