package cl.intercept.trackingIntranet;

import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class Varios {

	public Varios(){
	}
	
	public static Calendar getInstanceCalendar(){
		TimeZone tz = null; 
		try {
			ResourceBundle proper = ResourceBundle.getBundle("varios");
			tz = TimeZone.getTimeZone(proper.getString("zona_horaria"));
		} 
		catch (Exception e){
			tz = TimeZone.getTimeZone("America/Santiago");
		}
	    return Calendar.getInstance(tz); 
	}

}
