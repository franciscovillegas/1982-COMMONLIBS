package intranet.com.eje.qsmcom.estructuras;

import freemarker.template.SimpleList;

public class TicketsEjecutivo {
	private SimpleList lista;
	private int cantRegistrosTotales;
	
	
	public TicketsEjecutivo() {
		super();
	}
	
	public SimpleList getLista() {
		return lista;
	}
	public void setLista(SimpleList lista) {
		this.lista = lista;
	}
	public int getCantRegistrosTotales() {
		return cantRegistrosTotales;
	}
	public void setCantRegistrosTotales(int cantRegistrosTotales) {
		this.cantRegistrosTotales = cantRegistrosTotales;
	}
	
	
	
}
