package cl.ejedigital.tool.misc;

import java.io.Serializable;

import cl.ejedigital.tool.strings.MyString;

public class Cronometro implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2305924432943510505L;
	private long VAR_TIEMPOINI=-1;
	
	public Cronometro(){
		
	}
	
	public Cronometro(long millisecondsBeforeOrPastCurrentTimeMillis){
		VAR_TIEMPOINI = (System.currentTimeMillis() - millisecondsBeforeOrPastCurrentTimeMillis);
	}
	
	public void start() {
		VAR_TIEMPOINI = System.currentTimeMillis(); 
	}
	
	public boolean isStopped() {
		return VAR_TIEMPOINI == -1;
	}
	
	/**
	 * No puede empezar con mayúscula el método.
	 * 
	 * @deprecated
	 * @since 2016-04-14 
	 * */
	public void Start(){
		this.start();
	}
	
	public String GetTime(){
		return(String.valueOf(System.currentTimeMillis()-VAR_TIEMPOINI)+"ms");
	}
	
	public double GetMilliseconds(){ 
		return System.currentTimeMillis()-VAR_TIEMPOINI ;
	}
	
	/**
	 * @deprecated
	 * */
	public String GetTimeHHMMSS(){
		return(GetHHMMSS(System.currentTimeMillis()-VAR_TIEMPOINI));
	}
	
	public String getTimeHHMMSS(){
		return(GetHHMMSS(System.currentTimeMillis()-VAR_TIEMPOINI));
	}
	
	public void printTimeHHMMSS(){
		System.out.println(GetHHMMSS(System.currentTimeMillis()-VAR_TIEMPOINI));
	}
	
	/**
	 * @deprecated
	 * */
	public String GetTimeHHMMSS_milli(){
		return(GetHHMMSS_milli(System.currentTimeMillis()-VAR_TIEMPOINI));
	}
	
	public String getTimeHHMMSS_milli(){
		return(GetHHMMSS_milli(System.currentTimeMillis()-VAR_TIEMPOINI));
	}
	
	public String getTimeDD_HHMMSS_milli(){
		return(Get_DD_HHMMSS_milli(System.currentTimeMillis()-VAR_TIEMPOINI));
	}
	
	public void printTimeHHMMSS_milli() {
		printTimeHHMMSS_milli("");
	}
	public void printTimeHHMMSS_milli(String msg){
		StringBuilder str = new StringBuilder();
		
		if( msg != null ) {
			str.append("[").append(msg).append("] ");
		}
		
		str.append(GetHHMMSS_milli(System.currentTimeMillis()-VAR_TIEMPOINI));
		
		System.out.println(str.toString());
	}
	
	
	private String GetHHMMSS(long mi){
		long resto = mi;
		long horas    = (long) Math.floor(resto / 3600000);
		if(horas!=0) resto = resto%(3600000*horas);
		long minutos  = (long) Math.floor(resto / 60000);
		if(minutos!=0) resto =  resto%(60000*minutos);
		long segundos = (long) Math.floor(resto / 1000);
		
		MyString my = new MyString();
		
		String hh = my.rellenaCadena(String.valueOf(horas)		, '0', 2);
		String mm = my.rellenaCadena(String.valueOf(minutos)	, '0', 2);
		String ss = my.rellenaCadena(String.valueOf(segundos)	, '0', 2);
		
		return hh + ":" + mm + ":" + ss;
	}
	
	private String GetHHMMSS_milli(long mi){
		long resto = mi;
		long horas    = (long) Math.floor(resto / 3600000);
		if(horas!=0) resto = resto%(3600000*horas);
		long minutos  = (long) Math.floor(resto / 60000);
		if(minutos!=0) resto =  resto%(60000*minutos);
		long segundos = (long) Math.floor(resto / 1000);
		if(segundos!=0)resto =  resto%(1000*segundos);
		
		MyString my = new MyString();
		
		String hh = my.rellenaCadena(String.valueOf(horas)		, '0', 2);
		String mm = my.rellenaCadena(String.valueOf(minutos)	, '0', 2);
		String ss = my.rellenaCadena(String.valueOf(segundos)	, '0', 2);
		
		StringBuilder str = new StringBuilder();
		return str.append(hh).append(":").append(mm).append(":").append(ss).append(".").append(resto).toString();
	}
	
	private String Get_DD_HHMMSS_milli(long mi){
		long resto = mi;
		
		long dias = (long) Math.floor(resto /(1000	* 60 * 60 * 24));
		if(dias!=0) resto = resto%(3600000*dias);
		
		long horas    = (long) Math.floor(resto / 3600000);
		if(horas!=0) resto = resto%(3600000*horas);
		
		long minutos  = (long) Math.floor(resto / 60000);
		if(minutos!=0) resto =  resto%(60000*minutos);
		
		long segundos = (long) Math.floor(resto / 1000);
		if(segundos!=0)resto =  resto%(1000*segundos);
		
		MyString my = new MyString();
		
		String dd  = my.rellenaCadena(String.valueOf(dias)		, '0', 2);
		String hh  = my.rellenaCadena(String.valueOf(horas)		, '0', 2);
		String mm  = my.rellenaCadena(String.valueOf(minutos)	, '0', 2);
		String ss  = my.rellenaCadena(String.valueOf(segundos)	, '0', 2);
		
		StringBuilder str = new StringBuilder();
		return str.append(dd).append("d ").append(hh).append(":").append(mm).append(":").append(ss).append(".").append(resto).toString();
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}  
}
