package cl.ejedigital.consultor;

/**
 * Copia al 2016-03-31
 * Se dejó como default porque el paqueta la necesitaba
 * 
 * @deprecated Ahora que consultor.jar tiene como librería a tool, es preferible usar ese Cronometro
 * @see cl.ejedigital.tool.misc.Cronometro
 * */
class Cronometro {
	private long VAR_TIEMPOINI;
	
	public Cronometro(){
		
	}
	
	public void start() {
		VAR_TIEMPOINI = System.currentTimeMillis(); 
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
	
	public String GetTimeHHMMSS(){
		return(GetHHMMSS(System.currentTimeMillis()-VAR_TIEMPOINI));
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(GetMilliseconds());
	}
	
}
