package intranet.com.eje.qsmcom.estructuras;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import portal.com.eje.tools.Varios;

public class DefinicionPeriodoTraspasoQsmCom {
	private ArrayList definiciones;
	private static boolean debug = false;
	
	public DefinicionPeriodoTraspasoQsmCom() {
		definiciones = new ArrayList();
	}
	
	public void addDefinicion(int tipo , String tipoDesc , int factorMes, int diaInicio) {
		Definicion def = new Definicion(diaInicio, factorMes , tipo , tipoDesc);
		
		definiciones.add(def);
	}
	
	public Definicion getMinDefinicion() {
		return  (Definicion)definiciones.get(0);
	}
	
	
	public Definicion getMaxDefinicionDia() {
		Definicion def = null;
		Definicion nueva = new Definicion(0,0,0,"");
		for(int i = 0; i <  definiciones.size() ; i++ ) {
			def = ( Definicion)definiciones.get(i);
			if(nueva.getDiaInicio() < def.getDiaInicio() ){
				nueva  = new Definicion(def.getDiaInicio() , def.getFactorMes(), def.getTipo(), def.getTipoDesc());
			}
		}
		
		return nueva;
	}
	
	public Periodo getPeriodoValido() {
		Calendar fechaActual = Varios.getInstanceCalendar();
		int mes = fechaActual.get(Calendar.MONTH);
		int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
		int aaa = fechaActual.get(Calendar.YEAR);
		int nextMes = -1;
		int nextDia = -1;
		int nextAaa = -1;
		
		
		Calendar calItera = Varios.getInstanceCalendar();
		Calendar calAfterItera = Varios.getInstanceCalendar();
		
		Calendar calFechaIni = Varios.getInstanceCalendar();
		Calendar calFechaFin = Varios.getInstanceCalendar();
		
		int periodo = -1; 		
		calItera.add(Calendar.MONTH, -1);
		
		/*DE MAYOR A MENOS DEBE ESTAR ORDENADO*/
		for(int i = -1 ; i <= 1 ;i++ ) {
			mes = calItera.get(Calendar.MONTH);
			dia = calItera.get(Calendar.DAY_OF_MONTH);
			aaa = calItera.get(Calendar.YEAR);
			periodo =(aaa * 100) +  (mes + 1);
			
			calAfterItera.set(aaa, mes, dia);
			calAfterItera.add(Calendar.MONTH, 1);
			nextMes = calAfterItera.get(Calendar.MONTH);
			nextDia = calAfterItera.get(Calendar.DAY_OF_MONTH);
			nextAaa = calAfterItera.get(Calendar.YEAR);
			
			for(int j = 0; j < definiciones.size() ; j++) {
				calFechaIni.set(fechaActual.get(Calendar.YEAR) - 5, 0,1);
				calFechaFin.set(fechaActual.get(Calendar.YEAR) + 5, 0,1);
				
 				Definicion definicion = (Definicion) definiciones.get(j);
 				calFechaIni = definicion.getFechaIni(aaa, mes);
 				
 				if(j + 1 <  definiciones.size()) {
 					definicion = (Definicion) definiciones.get(j+1);
 					calFechaFin = definicion.getFechaIni(aaa, mes);
 					calFechaFin.add(Calendar.DAY_OF_MONTH, -1);
 				} else {
 					definicion = getMinDefinicion();
 					calFechaFin = definicion.getFechaIni(nextAaa , nextMes);
 					calFechaFin.add(Calendar.DAY_OF_MONTH, -1);
 				}
 				
 				definicion = (Definicion) definiciones.get(j);
 				if(calFechaIni.getTimeInMillis() < fechaActual.getTimeInMillis() &&
 				   calFechaFin.getTimeInMillis() > fechaActual.getTimeInMillis()) {
 					imprime("\t SELPERIODO ---> "+definicion.getTipo()+"  Ini:" +getStringCalendar(calFechaIni) + "  fin:" + getStringCalendar(calFechaFin) ) ;
 					return new Periodo( definicion.getTipoDesc(),  calFechaFin, calFechaIni , definicion.getTipo() , periodo );
 				}
			}
			
			calItera.add(Calendar.MONTH, 1);
		}
		
		return null;
	}
	
	private void imprime(String msg) {
		if(debug) {
			System.out.println(msg);
		}
	}
	
	private String getStringCalendar(Calendar cal ) {
		String mes = String.valueOf(cal.get(Calendar.MONTH) + 1 );
		String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String aaa = String.valueOf(cal.get(Calendar.YEAR));
		
		mes = Varios.rellenaCadena(mes, '0', 2);
		dia = Varios.rellenaCadena(dia, '0', 2);
		
		return aaa + "-" + mes + "-" + dia;
	}
	
}

class OrderByDiaInicio implements Comparator {

	public int compare(Object o1, Object o2) {
		if(o1 instanceof Definicion && o2 instanceof Definicion) {
			Definicion d1 = (Definicion) o1;
			Definicion d2 = (Definicion) o2;
			
			if( d1.hashCode() < d2.hashCode()   ) {
				return -1;
			} else if( d1.hashCode() > d2.hashCode()   ) {
				return 1;
			}
			
		}
		
		return 0;
	}
	
}

class Definicion  {
	private int tipo;
	private int factorMes;
	private int diaInicio;
	private String tipoDesc;
	
	public Definicion(int diaInicio, int factorMes, int tipo, String tipoDesc) {
		super();
		this.diaInicio = diaInicio;
		this.factorMes = factorMes;
		this.tipo = tipo;
		this.tipoDesc = tipoDesc;
	}

	public int getTipo() {
		return tipo;
	}

	public int getFactorMes() {
		return factorMes;
	}

	public int getDiaInicio() {
		return diaInicio;
		
	}
	
	public String getTipoDesc() {
		return tipoDesc;
	}

	public int hashCode() {
		Calendar cal =  Varios.getInstanceCalendar();
		cal.add(Calendar.MONTH, factorMes);
		 
		int cantDias = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		
		return  diaInicio ;
	}
	
	public Calendar getFechaIni (int aaa , int mes ) {
		Calendar calTmp = Varios.getInstanceCalendar();
		calTmp.set(aaa, mes, diaInicio);
		calTmp.add(Calendar.MONTH, factorMes);
		
		return calTmp;
	}
	

	
}
