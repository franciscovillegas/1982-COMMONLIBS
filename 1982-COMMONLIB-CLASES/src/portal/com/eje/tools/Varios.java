package portal.com.eje.tools;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;

import freemarker.template.SimpleHash;

public class Varios {
	public Varios(){
		
	}
	
	public static int getRndInt(){
		return ((int) (Math.random() * 100000000));
	}
	
	
	
	
	public static String getMes(int mes){
		switch(mes){
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Septiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";
		}
		
		return "INVALIDO";
	}
	
	public static File nuevoRandomArchivo(String ruta,String extension){
		File file = new File(ruta + "\\" + getRandomString() + "." + extension);
		
		while(file.exists()){
			file = new File(ruta + "\\" + getRandomString() + "." + extension);
		}
		
		return file;
	}
	
	public static String getRandomString(){
		String array = "abcdefghijklmnopqrstuvwxyz123456789";
		String pal = "";
		int largo=20;
		for(int i=0;i<array.length();i++){
			int rnd = (int) (Math.random() * (array.length() -1)); 
			pal += String.valueOf(array.toCharArray()[rnd]);
		}
		
		return pal;
	}
	
	public static String replaceCadena(String palabra,String busca,String reemplaza){
		String str="";
		int i =0;
		while(i < (palabra.length()-busca.length())){
			if(!palabra.substring(i, i+busca.length()).equals(busca)){
				str += palabra.substring(i, i+1);
				i++;
			}
			else{
				str += reemplaza;
				i += busca.length();
			}
			
		}
		
		str += palabra.substring(i, i+busca.length());
		
		return str;
	}
	
	public static boolean isNumeric(String numero){
		boolean verdad = true;
		
		try{
			int i = Integer.parseInt(numero);
		}catch(Exception e){
			verdad = false;
		}
		
		return verdad;
	}
	
	public static String rellenaCadena(String str,char relleno,int largo){
		String pal=str;
		while(pal.length()<largo){
			pal = relleno + pal;
		}
		return pal;
	}
	
	public static String isNull(String str,String valDef){
		if(str != null)
			return str;
		
		return valDef;
	}
	
	public static String getExtension(String file){
		int last = file.lastIndexOf(".");
		String str = null;
		
		if(last != -1)
			str = file.substring( last , file.length() );
		else
			str = "";
		
		return str;
	}
	
	public static String convertToStringDDDDddMMMMYYYY( Calendar cal) {
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int diaSemanaChileno = 0;
		
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				diaSemanaChileno = 7;
				break;
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				diaSemanaChileno = cal.get(Calendar.DAY_OF_WEEK) -1;
			default:
				break;
		}
		
		String diaSem = Tools.rescataDiaSemana( diaSemanaChileno );
		String mes = Tools.RescataMes( cal.get(Calendar.MONTH) + 1 );
		int aaa = cal.get(Calendar.YEAR);
		
		return diaSem + " " + dia + " de " + mes + " de " + aaa; 
		
	}
	
	public static Calendar getInstanceCalendar(){
		TimeZone tz = null; 
		try {
			ResourceBundle proper = ResourceBundle.getBundle("varios");
			tz = TimeZone.getTimeZone(proper.getString("zona_horaria"));
		} catch (Exception e){
			tz = TimeZone.getTimeZone("America/Santiago");
		}
	    
	    return Calendar.getInstance(tz); 
	}

    public static Calendar getCalendarFromString(String fecha) throws Exception {
		String[] partes = fecha.split("\\/");
		Calendar cal = Varios.getInstanceCalendar();
		
		if(partes.length == 3) {
			int dia = Integer.parseInt(partes[0]);
			int mes = Integer.parseInt(partes[1]) -1;
			int aaa = Integer.parseInt(partes[2]);
			
			cal.set(aaa, mes, dia);
		}
    		
    	return cal;
    }
	
}
