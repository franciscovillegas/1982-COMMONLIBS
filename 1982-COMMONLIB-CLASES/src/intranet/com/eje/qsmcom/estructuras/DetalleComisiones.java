package intranet.com.eje.qsmcom.estructuras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DetalleComisiones {

	public static final int	CANTCAMPOS			= 33;
	public static final int	CAMPOFECHAPROCESO	= 0;
	public static final int	CAMPORUTZONAL		= 1;
	public static final int	CAMPODIGZONAL		= 2;
	public static final int	CAMPONOMBREZONAL	= 3;
	public static final int	CAMPORUTSUP			= 4;
	public static final int	CAMPODIGSUP			= 5;
	public static final int	CAMPONOMBRESUP		= 6;
	public static final int	CAMPOCODSUC			= 7;
	public static final int	CAMPONOMSUC			= 8;
	public static final int	CAMPORUTEJE			= 9;
	public static final int	CAMPODIGEJE			= 10;
	
	public static final int	CAMPONOMEJE			= 11;
	public static final int	CAMPOGLOSAPERFIL	= 12;
	public static final int	CAMPOZONA			= 13;
	public static final int	CAMPONUMEOPERA		= 14;
	public static final int	CAMPORUTCLI			= 15;
	public static final int	CAMPODIGCLI			= 16;
	public static final int	CAMPOM_MANO			= 17;
	public static final int	CAMPOCLASE			= 18;
	public static final int	CAMPOPRODUCTO		= 19;
	public static final int	CAMPOFECH_FORMA		= 20;
	
	public static final int	CAMPOTIPO			= 21;
	public static final int	CAMPOTIPO_CLIENTE	= 22;
	public static final int	CAMPOTIPO_PRODUCTO	= 23;
	public static final int	CAMPOCONCEPTO		= 24;
	public static final int CAMPOCHEQUERAE		= 25;
	public static final int CAMPOESTADO			= 26;
	public static final int CAMPOSEGURO			= 27;
	public static final int CAMPOTARJETA		= 28;
	public static final int CAMPOPORCCOMISION	= 29;
	public static final int CAMPOCOMISION		= 30;
	
	public static final int CAMPOFACTOR			= 31;
	public static final int CAMPOMONTOBRUTO		= 32;
	
	
	private ArrayList		registros;
	private int 			pos;
	private String 	canales;			
	private String	sucursales;
	private ArrayList conceptos;
	
	public DetalleComisiones(ArrayList registros) {
		this.registros = registros;
		
		toStart();
	}
	
	public void toStart() {
		pos = -1;
	}
	
	public boolean next() {
		pos++;
		
		return hasActiveReg();		
	}
	
	private boolean hasActiveReg() {
		if(registros != null && pos < registros.size()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private HashMap getReg() {
		if(hasActiveReg()) {
			return (HashMap) registros.get(pos) ;
		}
		else {
			return null;
		}
		
	}
	
	public String getCampoValue(int pos) {
		if(hasActiveReg()) {
			return (String) getReg().get(String.valueOf(pos));
		}
		return null;
	}
	
	public boolean isColumnValid(int pos) {
		DetalleComisiones tmp = new DetalleComisiones(registros); 
		String valor = "";
		
		while(tmp.next()) {
			valor = tmp.getCampoValue(pos);
			
			if( valor != null &&  ( !"0".equals(valor.trim()) && !"".equals(valor.trim()) ) ) {
				return true;
			}
		}
		
		return false;
	}
	
	public String getCanales() {
		
		if(canales == null) {
			DetalleComisiones tmp = new DetalleComisiones(registros); 
			String valor = "";
			ArrayList lista = new ArrayList();
			
			while(tmp.next()) {
				valor = tmp.getCampoValue(CAMPOGLOSAPERFIL);
				
				if(valor != null && !"".equals(valor.trim()) && lista.indexOf(valor) == -1 ) {
					lista.add(valor);
				}
			}
			
			valor = "";
			for(int i = 0 ; i < lista.size() ; i++) {
				valor += lista.get(i);
				
				if( (i+1) < lista.size()) {
					valor += ", ";
				}
			}
			
			canales = valor;
		}
		
		
		return canales;
	}
	
	public String getSucursales() {
		
		if(sucursales == null) {
			DetalleComisiones tmp = new DetalleComisiones(registros); 
			String valor = "";
			ArrayList lista = new ArrayList();
			
			while(tmp.next()) {
				valor = tmp.getCampoValue(CAMPONOMSUC);
				
				if(valor != null && !"".equals(valor.trim()) && lista.indexOf(valor) == -1 ) {
					lista.add(valor);
				}
			}
			
			valor = "";
			for(int i = 0 ; i < lista.size() ; i++) {
				valor += lista.get(i);
				
				if( (i+1) < lista.size()) {
					valor += ", ";
				}
			}
			
			sucursales = valor;
		}
		
		
		return sucursales;
	}
	
	
	public ArrayList getArrayDistinctConceptos() {
		if(conceptos == null) {
			DetalleComisiones tmp = new DetalleComisiones(registros); 
			String valor = "";
			ArrayList lista = new ArrayList();
			
			while(tmp.next()) {
				valor = tmp.getCampoValue(CAMPOCONCEPTO);
				
				if(valor != null && !"".equals(valor.trim()) && lista.indexOf(valor) == -1 ) {
					lista.add(valor);
				}
			}
			
			conceptos = lista;
		}
		
		
		return conceptos;
	}
	
	
	public DetalleComisiones getRegistrosPorConcepto(String concepto) {
		DetalleComisiones tmp = new DetalleComisiones(registros); 
		String valor = " ";
		ArrayList lista = new ArrayList();
		
		while(tmp.next()) {
			valor = tmp.getCampoValue(CAMPOCONCEPTO);
			
			if(concepto != null && concepto.equals(valor)) {
				lista.add(tmp.getReg());
			}
		}
			
		return new DetalleComisiones(lista);
	}
	
	public int getCantRegistros() {
		return registros.size();
	}
}
