package intranet.com.eje.qsmcom.interfaces;

public interface IMensajeUsuario {
	final static int	ERROR	= 1;
	final static int	OK		= 2;
	
	void addMsg(String linea, int tipoLinea);
	
}
