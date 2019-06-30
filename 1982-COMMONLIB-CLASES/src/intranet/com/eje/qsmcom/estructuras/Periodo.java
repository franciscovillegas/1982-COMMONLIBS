package intranet.com.eje.qsmcom.estructuras;

import java.util.Calendar;

public class Periodo {
	private int periodo;
	private int idTipo;
	private String tipoDesc;
	private Calendar fecha_ini;
	private Calendar fecha_fin;
	private int delay;
	
	public Periodo(String tipoDesc ,Calendar fecha_fin, Calendar fecha_ini, int idTipo, int periodo) {
		super();
		this.fecha_fin = fecha_fin;
		this.fecha_ini = fecha_ini;
		this.idTipo = idTipo;
		this.periodo = periodo;
		this.tipoDesc = tipoDesc;
	}

	public int getPeriodo() {
		return periodo;
	}
	
	public String getPeriodoStr() {
		return String.valueOf(periodo);
	}
	
	public int getIdTipo() {
		return idTipo;
	}

	public Calendar getFecha_ini() {
		return fecha_ini;
	}

	public Calendar getFecha_fin() {
		return fecha_fin;
	}

	public String getTipoDesc() {
		return tipoDesc;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	
	
}
