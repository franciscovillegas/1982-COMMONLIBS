package portal.com.eje.cache.error;

import portal.com.eje.portal.PPMException;

public class NotConstructorByDefaultException extends PPMException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotConstructorByDefaultException() {
		super();
		// TODO Auto-generated constructor stub
	}

 

	public NotConstructorByDefaultException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotConstructorByDefaultException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotConstructorByDefaultException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
