package portal.com.eje.portal.liquidacion;

import java.util.Calendar;
import java.util.Date;

import cl.ejedigital.tool.misc.Formatear;

public class PeriodoMensual implements IPeriodoMensual {
	private int periodo;
	private double uf;
	private double utm;
	
	/**
	 * EL parámetro periodo deberá venir en formato YYYYMM.<br/>
	 * <ul>
	 * 	<li>ej: 201706</li>
	 * </ul>
	 * */
	public PeriodoMensual(int periodo, double uf, double utm) {
		this.periodo = periodo;
		this.uf = uf;
		this.utm = utm;
	}

	public int getPeriodo() {
		return this.periodo;
	}

	public int getMes() {
		return Integer.parseInt(getMesString());
	}
	
	@Override
	public String getMesString() {
		return String.valueOf(this.periodo).substring(4, 6);
	}

	@Override
	public int getYYY() {
		return this.periodo / 100;
	}

	@Override
	public String getLastDateAnsi() {
		return Formatear.getInstance().toDate(getLastDate(), "yyyyMMdd hh:mm:ss");
	}
	
	@Override
	public Date getLastDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(getYYY(), getMes() - 1, 1);
		 
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 59);
	 
		
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
	
	@Override
	public String getFirstDateAnsi() {
		return Formatear.getInstance().toDate(getFirstDate(), "yyyyMMdd hh:mm:ss");
	}

	@Override
	public Date getFirstDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(getYYY(), getMes() -1, 1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 59);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		return cal.getTime();
	}
	
	@Override
	public double getUtm() {
		return this.utm;
	}

	@Override
	public double getUF() {
		return this.uf;
	}

	

}
