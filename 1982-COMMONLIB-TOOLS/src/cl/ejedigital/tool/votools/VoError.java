package cl.ejedigital.tool.votools;

import java.io.Serializable;

//@XmlRootElement
public class VoError implements IVoBase, Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2710546079883614468L;
	private String	tipoException;
	private String	descripcion;



	VoError() {

	}



	public VoError(Exception tipoException) {
		super();
		this.tipoException = tipoException.getClass().getSimpleName();
		this.descripcion = tipoException.getLocalizedMessage();
	}



	public String getTipoException() {
		return tipoException;
	}



	public void setTipoException(String tipoException) {
		this.tipoException = tipoException;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public Double getVoCode() {
		// TODO Auto-generated method stub
		return 0d;
	}

}
