package portal.com.eje.portal.liquidacion;

import java.util.Date;

public interface IPeriodoMensual {

	/**
	 * <ul>
	 * 	<li>ej: 201706</li>
	 * </ul>
	 * */
	public int getPeriodo();
	
	/**
	 * Suponiendo que el periodo sea 201706, el valor a entregar será 6
	 * */
	public int getMes();
	
	/**
	 * Suponiendo que el periodo sea 201706, el valor a entregar será "06"
	 * */
	public String getMesString();
	
	/**
	 * Suponiendo que el periodo sea 201706, el valor a entregar será 2017
	 * */
	public int getYYY();
	
	/**
	 * Suponiendo que el periodo sea 201706, el valor a entregar será un Objeto Date con la fecha "30-06-2017 23:59:59"
	 * */
	public Date getLastDate();
	
	public String getLastDateAnsi();
	
	/**
	 * Suponiendo que el periodo sea 201706, el valor a entregar será un Objeto Date con la fecha "01-06-2017 00:00:00"
	 * */
	
	public Date getFirstDate();
	
	public String getFirstDateAnsi();

	public double getUtm();
	
	public double getUF();
}
