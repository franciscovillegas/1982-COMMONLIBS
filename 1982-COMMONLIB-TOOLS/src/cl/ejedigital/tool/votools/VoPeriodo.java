package cl.ejedigital.tool.votools;

import java.io.Serializable;

import cl.ejedigital.tool.exceptions.ExceptionParametersNotValid;



public class VoPeriodo implements IVoBase, Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8652277446288109594L;
	private int periodo;

	
	public VoPeriodo(int periodo) throws ExceptionParametersNotValid {
		if(String.valueOf(periodo).length() != 6) {
			throw new ExceptionParametersNotValid("El valor \""+periodo+"\" debe tener largo 6 (yyyyMM) ");
		}
		 
		this.periodo = periodo;
	}
	
	public int getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public Double getVoCode() {
		return (double)this.periodo;
	}


	
	
}
