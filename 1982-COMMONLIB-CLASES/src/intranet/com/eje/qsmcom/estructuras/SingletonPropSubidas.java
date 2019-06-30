package intranet.com.eje.qsmcom.estructuras;

public class SingletonPropSubidas {

	private boolean						subiendoArchivo			= false;
	private String						TipoSuperEjecutivo		;
	private String						TipoComisiones			;
	private String						TipoDetComisiones		;
	private String						TipoMantCoordinador		;
	private String						TipoMantEjecutorIng1	;
	private String						TipoMantEjecutorIng2	;
	private String						TipoMantEjecutorIng3	;
	private String						TipoMantEjecutorECRT	;
	private String						TipoMantEjecutorCliente	;
	private String						TipoMantPeriodos		;
	private String						TipoMantUF				;


	private static SingletonPropSubidas	obj;

	private SingletonPropSubidas() {
		TipoSuperEjecutivo		= "mantTrabajadores";
		TipoComisiones			= "mantComisiones";
		TipoDetComisiones		= "mantDetComisiones";
		TipoMantCoordinador		= "mantCoordinador";
		TipoMantEjecutorIng1	= "mantEjecutorIng1";
		TipoMantEjecutorIng2	= "mantEjecutorIng2";
		TipoMantEjecutorIng3	= "mantEjecutorIng3";
		TipoMantEjecutorECRT	= "mantEjecutorECR";
		TipoMantEjecutorCliente	= "mantEjecutorCliente";
		TipoMantPeriodos		= "mantPeriodos";
		TipoMantUF				= "mantUF";
	}

	public static SingletonPropSubidas getInstance() {
		if (obj == null) {
			obj = new SingletonPropSubidas();
		}

		return obj;
	}

	public boolean isSubiendoArchivo() {
		return subiendoArchivo;
	}

	public void setSubiendoArchivo(boolean subiendoArchivo) {
		this.subiendoArchivo = subiendoArchivo;
	}

	
	public String getTipoSuperEjecutivo() {
		return TipoSuperEjecutivo;
	}

	
	public String getTipoComisiones() {
		return TipoComisiones;
	}

	
	public String getTipoDetComisiones() {
		return TipoDetComisiones;
	}

	
	public String getTipoMantCoordinador() {
		return TipoMantCoordinador;
	}

	
	public String getTipoMantEjecutorIng1() {
		return TipoMantEjecutorIng1;
	}

	
	public String getTipoMantEjecutorIng2() {
		return TipoMantEjecutorIng2;
	}

	
	public String getTipoMantEjecutorIng3() {
		return TipoMantEjecutorIng3;
	}

	
	public String getTipoMantEjecutorECRT() {
		return TipoMantEjecutorECRT;
	}

	
	public String getTipoMantEjecutorCliente() {
		return TipoMantEjecutorCliente;
	}

	
	public String getTipoMantPeriodos() {
		return TipoMantPeriodos;
	}

	
	public String getTipoMantUF() {
		return TipoMantUF;
	}
	
	
	
}
