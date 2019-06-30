package cl.ejedigital.tool.misc;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cl.ejedigital.tool.validar.Validar;

public class Formatear {
	public enum DateLargeFormat {

		d_de_MMMM_de_yyyy("d 'de' MMMM 'de' yyyy"),
		FULL("dd-MM-yyyy HH:mm:ss.SSS"),
		ANSI("yyyyMMdd HH:mm:ss:SSS");
		
		private String formato;

		private DateLargeFormat(String formato) {
			this.formato = formato;
		}
		
		@Override
		public String toString() {
			return this.formato;
		}
	}
	
	
	private static Formatear instance;
	public final static DecimalFormat DECIMAL_FORMAT = Formatear.getInstance().getDecimalFormat();
	
 
	private Formatear() {
		
	}
	
	public static Formatear getInstance() {
		if(instance == null) {
			synchronized(Formatear.class) {
				if(instance == null) {
					instance = new Formatear();
				}
			}			
		}
		
		return instance;
	}
	
	public DecimalFormat getDecimalFormat() {
		DecimalFormatSymbols ds = new DecimalFormatSymbols(java.util.Locale.US);
		ds.setDecimalSeparator(',');
		ds.setMonetaryDecimalSeparator('.');
		ds.setGroupingSeparator('.');
		StringBuilder pattern = new StringBuilder("###,###.###");
					
		DecimalFormat decimalFormat = new DecimalFormat( pattern.toString(), ds);
		return decimalFormat;
	}
	

	public String toAnotherDate(String fecha, String patternIn, String patternOut) {
		if(fecha != null && patternIn  != null && patternOut != null) {		
			SimpleDateFormat formatRecibe = new SimpleDateFormat(patternIn);
			SimpleDateFormat formatAnsi = new SimpleDateFormat(patternOut);
	
			if(patternIn == null || patternOut == null || "".equals(patternIn) || "".equals(patternOut)) {
				return fecha;
			}
	
			try {
				Date d = formatRecibe.parse(fecha);
				return fecha = formatAnsi.format(d);
			}
			catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}
	
    public Date toDate(String fecha,String formatoOrigen) {
    	if(fecha != null && formatoOrigen != null) {		
	    	SimpleDateFormat sdf = new SimpleDateFormat(formatoOrigen);
			
			Date fechaDate;
			try {
				fechaDate = sdf.parse(fecha);
			} 
			catch (ParseException e) {
				fechaDate = null;
			} 
	    	
			return fechaDate;
    	}
    	
    	return null;
    }
    
    public String toDate(Date fecha, String formatoDestino) {
    	if(fecha != null && formatoDestino != null) {
	    	SimpleDateFormat fDestino = new SimpleDateFormat(formatoDestino);
			
			String fechaDate;
			fechaDate = fDestino.format(fecha); 
	    	
			return fechaDate;
    	}
    	
    	return null;
    }
    
    public String toDate(Date fecha, DateLargeFormat formato) {
    	return Formatear.getInstance().toDate(fecha, formato.toString() );
    }
	
	public double redondear(double num, int numDigitos) {
		return  (double)((int)(num * Math.pow(10, numDigitos)))/Math.pow(10, numDigitos);
	}
	
	public String trasformaATexto(double num) {
		
		int unidades   = (int) (num % 10);
		int decenas    = (int) ((num % 100)   	 / 10);
		int centenas   = (int) ((num % 1000)  	 / 100);
		int miles      = (int) ((num % 10000) 	 / 1000);
		int decMiles   = (int) ((num % 100000)	 / 10000);
		int centMiles  = (int) ((num % 1000000)   / 100000);
		int millones   = (int) ((num % 10000000)	 / 1000000);
		int decMill    = (int) ((num % 100000000) / 10000000);
		int centMill   = (int) ((num % 1000000000)/ 100000000);
		
		StringBuffer str = new StringBuffer();
		str.append(getText("millon","millones",millones	,decMill	,centMill));
		str.append(" ").append(getText("mil"   ,"mil"	  ,miles	,decMiles	,centMiles));
		str.append(" ").append(getText(""	   ,""		  ,unidades	,decenas	,centenas));
			
		return str.toString();
	}
	
	private String getText(String grupoUno, String grupoPlural ,int u, int d, int c) {
		StringBuffer uni = new StringBuffer();
		StringBuffer dec = new StringBuffer();
		StringBuffer cen = new StringBuffer();
		
		switch(c) {
			case 0:
				cen.append("");
				break;
			case 1:
				cen.append("ciento");
				break;
			case 2:
				cen.append("doscientos");
				break;
			case 3:
				cen.append("trescientos");
				break;
			case 4:
				cen.append("cuatrocientos");
				break;
			case 5:
				cen.append("quinientos");
				break;
			case 6:
				cen.append("seiscientos");
				break;
			case 7:
				cen.append("setecientos");
				break;
			case 8:
				cen.append("ochocientos");
				break;
			case 9:
				cen.append("novecientos");
				break;

			default:
				break;
		}
		
		
		switch(d) {
			case 0:
				break;
			case 1:
				switch(u) {
					case 0:
						dec.append("diez");
						break;
					case 1:
						dec.append("once");
						break;
					case 2:
						dec.append("doce");
						break;
					case 3:
						dec.append("trece");
						break;
					case 4:
						dec.append("catorce");
						break;
					case 5:
						dec.append("quince");
						break;
					case 6:
						dec.append("dieciséis");
						break;
					case 7:
						dec.append("diecisiete");
						break;
					case 8:
						dec.append("dieciocho");
						break;
					case 9:
						dec.append("diecinueve");
						break;

					default:
						break;
				}
				break;
			case 2:
				switch(u) {
					case 0:
						dec.append("veinte");
						break;
					case 1:
						dec.append("veintiun");
						break;
					case 2:
						dec.append("veintidos");
						break;
					case 3:
						dec.append("veintitres");
						break;
					case 4:
						dec.append("veinticuatro");
						break;
					case 5:
						dec.append("veinticinco");
						break;
					case 6:
						dec.append("veintiseis");
						break;
					case 7:
						dec.append("veintisiete");
						break;
					case 8:
						dec.append("veintiocho");
						break;
					case 9:
						dec.append("veintinueve");
						break;

					default:
						break;
				}
				break;
			case 3:
				dec.append("treinta");
				break;
			case 4:
				dec.append("cuarenta");
				break;
			case 5:
				dec.append("cincuenta");
				break;
			case 6:
				dec.append("sesenta");
				break;
			case 7:
				dec.append("setenta");
				break;
			case 8:
				dec.append("ochenta");
				break;
			case 9:
				dec.append("noventa");
				break;

			default:
				break;
		}
		
		if(d > 2 && u != 0 ) {
			uni.append("y ");
		}
		
		if( d == 0 || d >= 3) {
			switch(u) {
				case 0:
					uni.append("");
					break;
				case 1:
					uni.append("un");
					break;
				case 2:
					uni.append("dos");
					break;
				case 3:
					uni.append("tres");
					break;
				case 4:
					uni.append("cuatro");
					break;
				case 5:
					uni.append("cinco");
					break;
				case 6:
					uni.append("seis");
					break;
				case 7:
					uni.append("siete");
					break;
				case 8:
					uni.append("ocho");
					break;
				case 9:
					uni.append("nueve");
					break;

				default:
					break;
			}
		}
		
		String textNums = (cen.toString().trim()  + " " + dec.toString().trim() + " " + uni.toString().trim() + " " );
		
		if(textNums != null && textNums.trim().length() > 0) {
			return textNums + " "+  (u > 1 ? grupoPlural.trim() : grupoUno.trim()).trim();
		}
		else {
			return "";
		}
	}

	/**
	 * Retorna el mes escrito</br>
	 * 
	 * 1 = enero</br>
	 * 2 = Marzo</br>
	 * 3 = Abril</br>
	 * ...</br>
	 * 12 = Diciembre</br>
	 * 
	 * @author Francisco
	 * @since 2015-04-24
	 * */
	public String toMonth(int mes) {
		if(mes < 1 || mes > 12) {
			throw new RuntimeException("El parámetro debe ser mayor a 0 y menor a 13, ahora fue: "+mes+ "¿estás seguro que estás pasando el mes?");
		}
		String[] meses = { null ,
						  "enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto",
						  "septiembre", "octubre", "noviembre", "diciembre"};
		
		return meses[mes];
	}
	
	
	public String toDayWeek(int dia) {
		if(dia < 1 || dia > 7) {
			throw new RuntimeException("El parámetro debe ser mayor a 0 y menor a 8, ahora fue: "+dia+ "¿estás seguro que estás pasando el día?");
		}
		
		String[] dias = {null,"lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"};
		
		return dias[dia];
	}
	
	public String toDayWeek(Object[] dias) {
		StringBuilder str = new StringBuilder();
		
		boolean entro = false;
		for(Object o : dias) {
			if(entro) {str.append(","); };
			str.append(toDayWeek(Validar.getInstance().validarInt(String.valueOf(o), -1)));
			entro = true;
		}
		
		return str.toString();
	}
	
	/**
	 * Retorna una fecha en el formato indicado por el Enum DateLargeFormat<br/>
	 * 
	 * 
	 * @since 2015-04-24
	 * @author Francisco
	 * **/
	public String toLargeDate(Date w, DateLargeFormat d) {
		 return toLargeDate(w);
	}
	
	
	public String toLargeDate(Date w) {
		StringBuffer fec = new StringBuffer();
		
		if( w != null) {
			fec.append(this.toDate(w,"dd"));
			fec.append(" de ");  
			fec.append(toMonth(Validar.getInstance().validarInt(this.toDate(w,"MM"),-1)));
			fec.append(" de "); 
			fec.append(Formatear.getInstance().toDate(w,"yyyy"));
		}
		
		return fec.toString();
	}
	
	public String toLargeDate(String fecha,String formatoOrigen) {
		Date date = toDate(fecha, formatoOrigen);
		return toLargeDate(date);
	}
	
	 public Map<TimeUnit, Long> getTimeDifference(Date d1, Date d2) {
	        long[] result = new long[5];
	        Calendar cal = Calendar.getInstance();
	        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
	        cal.setTime(d1);

	        long t1 = cal.getTimeInMillis();
	        cal.setTime(d2);

	        long diff = Math.abs(cal.getTimeInMillis() - t1);
	        final int ONE_DAY = 1000 * 60 * 60 * 24;
	        final int ONE_HOUR = ONE_DAY / 24;
	        final int ONE_MINUTE = ONE_HOUR / 60;
	        final int ONE_SECOND = ONE_MINUTE / 60;

	        long d = diff / ONE_DAY;
	        diff %= ONE_DAY;

	        long h = diff / ONE_HOUR;
	        diff %= ONE_HOUR;

	        long m = diff / ONE_MINUTE;
	        diff %= ONE_MINUTE;

	        long s = diff / ONE_SECOND;
	        long ms = diff % ONE_SECOND;
	        result[0] = d;
	        result[1] = h;
	        result[2] = m;
	        result[3] = s;
	        result[4] = ms;
	        
	        Map<TimeUnit, Long> map = new HashMap<TimeUnit, Long>();
	        map.put(TimeUnit.DAY, d);
	        map.put(TimeUnit.HOUR, h);
	        map.put(TimeUnit.MINUTE, m);
	        map.put(TimeUnit.SECOND, s);
	        map.put(TimeUnit.MILLISECOND, ms);
	        
	        return map;
	    }

	    public static void printDiffs(long[] diffs) {
	        System.out.printf("Days:         %3d\n", diffs[0]);
	        System.out.printf("Hours:        %3d\n", diffs[1]);
	        System.out.printf("Minutes:      %3d\n", diffs[2]);
	        System.out.printf("Seconds:      %3d\n", diffs[3]);
	        System.out.printf("Milliseconds: %3d\n", diffs[4]);
	    }

	    public static enum TimeUnit {DAY,
	        HOUR,
	        MINUTE,
	        SECOND,
	        MILLISECOND;
	    }
	    
	    /**
	     * @since 06-dic-2016
	     * */
	    public static String numero(int numero) {
	        String ret = "0";
	        try
	        {
	            ret = NumberFormat.getInstance(Locale.GERMAN).format(new Integer(numero));
	        }
	        catch(Exception exception) { }
	        return ret;
	    }

		public String getTimeDifferenceString(Date ini, Date fin) {
			Map<TimeUnit, Long> map = getTimeDifference(ini, fin);
			StringBuilder retorno = new StringBuilder();
	        long day    	 = map.get(TimeUnit.DAY);
	        long hour   	 = map.get(TimeUnit.HOUR);
	        long minute 	 = map.get(TimeUnit.MINUTE);
	        long second 	 = map.get(TimeUnit.SECOND);
	        long millisecond = map.get(TimeUnit.MILLISECOND);
			
			if( day != 0L) {
				if(day != 1) {
					retorno.append(day).append(" días ");	
				}
				else {
					retorno.append(day).append(" día ");	
				}
				
			}
			
			if(  hour != 0L ) {
				retorno.append(hour).append("h ");
			}
			
			if(  minute != 0L ) {
				retorno.append(minute).append("m ");
			}
			
			if(  second != 0L ) {
				retorno.append(second).append("s ");
			}
			
			return retorno.toString();
		}
		
		public String toDecimalNumber(double number, int cantDecimals) {
			
			return DECIMAL_FORMAT.format(number);
		}
		
		public String toDecimalNumber(String number, int cantDecimals) {
			DecimalFormat df = (DecimalFormat) DECIMAL_FORMAT.clone();
			df.setMaximumFractionDigits(cantDecimals);
			return toDecimalNumber(Validar.getInstance().validarDouble(number, 0), cantDecimals);
		}

		public String nowToDate(String formatoDestino) {
			// TODO Auto-generated method stub
			return this.toDate(Calendar.getInstance().getTime(), formatoDestino);
		}
		
		public Date now() {
			// TODO Auto-generated method stub
			return Calendar.getInstance().getTime();
		}
		
		public String tTrim(String cadena) {
			if(cadena != null) {
				cadena = cadena.trim();
				
				while(cadena.contains("  ")) {
					cadena = cadena.replaceAll("  ", " ");
				}
			}
			
			return cadena;
		}
}