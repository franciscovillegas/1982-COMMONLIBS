package cl.ejedigital.web.usuario.vo;

import java.io.Serializable;

public class VoUnidad implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private final String		unid_empresa;
	private final String		unidId;
	private final String		unidDesc;
	private final String		area;
	private final char			vigente;

	
	public VoUnidad() {
		unid_empresa = null;
		unidId = null;
		unidDesc = null;
		area = null;
		vigente = ' ';
	}
	
	public VoUnidad(String unid_empresa, String unidId, String unidDesc, String area, char vigente) {
		super();
		this.unid_empresa = unid_empresa;
		this.unidId = unidId;
		this.unidDesc = unidDesc;
		this.area = area;
		this.vigente = vigente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUnid_empresa() {
		return unid_empresa;
	}

	public String getUnidId() {
		return unidId;
	}

	public String getUnidDesc() {
		return unidDesc;
	}

	public String getArea() {
		return area;
	}

	public char getVigente() {
		return vigente;
	}

}
