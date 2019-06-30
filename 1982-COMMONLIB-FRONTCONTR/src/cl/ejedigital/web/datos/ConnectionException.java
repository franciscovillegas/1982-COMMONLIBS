package cl.ejedigital.web.datos;


public class ConnectionException extends RuntimeException {

	public ConnectionException() {
		
	}
	
	public ConnectionException(int i, String name) {
		super(new StringBuilder(i).append(" - ").append(name).toString());
	}

	public ConnectionException(int i) {
		super(new StringBuilder(i).toString());
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ConnectionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ConnectionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
	
}
