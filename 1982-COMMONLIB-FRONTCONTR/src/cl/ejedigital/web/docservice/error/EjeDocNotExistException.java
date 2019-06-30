package cl.ejedigital.web.docservice.error;

import portal.com.eje.portal.vo.errors.PeopleExcepcion;

public class EjeDocNotExistException extends PeopleExcepcion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EjeDocNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EjeDocNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EjeDocNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EjeDocNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
