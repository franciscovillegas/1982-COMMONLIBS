package cl.ejedigital.consultor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Pancho
 * @deprecated
 * @see cl.ejedigital.tool.misc.Formatear
 * @since 17-04-2019
 * */
class Formatear {
	
	/**
	 * @author Pancho
	 * @deprecated
	 * @see cl.ejedigital.tool.misc.Formatear.DateLargeFormat
	 * */
	public enum DateLargeFormat {
		d_de_MMMM_de_yyyy
	}
	
	
	private static Formatear instance;
	
	private Formatear() {}
	
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
	
	/**
	 * Retorna una fecha en el formato indicado por el Enum DateLargeFormat
	 * 
	 * @since 2015-04-24
	 * @author Francisco
	 * @deprecated
	 * **/
	public String toLargeDate(Date w, DateLargeFormat d) {
		 return toLargeDate(w);
	}
	
	public String toLargeDate(Date w) {
		StringBuffer fec = new StringBuffer();
		
		if( w != null) {
			fec.append(this.toDate(w,"dd"));
			fec.append(" de ");  
			//fec.append(toMonth(Validar.getInstance().validarInt(this.toDate(w,"MM"),-1)));
			fec.append(" de "); 
			fec.append(Formatear.getInstance().toDate(w,"yyyy"));
		}
		
		return fec.toString();
	}
}