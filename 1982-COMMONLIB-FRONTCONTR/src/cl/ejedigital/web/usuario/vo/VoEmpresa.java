package cl.ejedigital.web.usuario.vo;

import java.io.Serializable;

public class VoEmpresa implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private final String	empresa;
	private final String	descrip;
	private final String	empreRut;
	private final String	empreDir;
	private final String	empreGiro;
	private final String	rutEepr;
	private final String	nomEep;
	private final String	padreUnidad;
	private final String	padreEmpresa;
	private final String	idActEconom;
	private final int		orden;
	private final String	compania;

	public VoEmpresa() {
		empresa = null;
		descrip = null;
		empreRut = null;
		empreDir = null;
		empreGiro = null;
		rutEepr = null;
		nomEep = null;
		padreUnidad = null;
		padreEmpresa = null;
		idActEconom = null;
		orden = 0;
		compania  = null;
	}
	
	public VoEmpresa(String empresa, String descrip, String empreRut, String empreDir, String empreGiro,
						String rutEepr, String nomEep, String padreUnidad, String padreEmpresa, String idActEconom,
						int orden, String compania) {
		super();
		this.empresa = empresa;
		this.descrip = descrip;
		this.empreRut = empreRut;
		this.empreDir = empreDir;
		this.empreGiro = empreGiro;
		this.rutEepr = rutEepr;
		this.nomEep = nomEep;
		this.padreUnidad = padreUnidad;
		this.padreEmpresa = padreEmpresa;
		this.idActEconom = idActEconom;
		this.orden = orden;
		this.compania = compania;
	}

	public String getEmpresa() {
		return empresa;
	}

	public String getDescrip() {
		return descrip;
	}

	public String getEmpreRut() {
		return empreRut;
	}

	public String getEmpreDir() {
		return empreDir;
	}

	public String getEmpreGiro() {
		return empreGiro;
	}

	public String getRutEepr() {
		return rutEepr;
	}

	public String getNomEep() {
		return nomEep;
	}

	public String getPadreUnidad() {
		return padreUnidad;
	}

	public String getPadreEmpresa() {
		return padreEmpresa;
	}

	public String getIdActEconom() {
		return idActEconom;
	}

	public int getOrden() {
		return orden;
	}

	public String getCompania() {
		return compania;
	}

}
