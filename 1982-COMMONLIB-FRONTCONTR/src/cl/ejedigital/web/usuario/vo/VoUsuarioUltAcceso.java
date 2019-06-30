package cl.ejedigital.web.usuario.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class VoUsuarioUltAcceso implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8487185368312642955L;
	private final String	rut;
	private final Timestamp	fecha_ult_acceso;
	private final String	clave;
	private final String	clave_enc;

	public VoUsuarioUltAcceso(String rut, Timestamp fecha_ult_acceso, String clave, String clave_enc) {
		super();
		this.rut = rut;
		this.fecha_ult_acceso = fecha_ult_acceso;
		this.clave = clave;
		this.clave_enc = clave_enc;
	}

	public String getRut() {
		return rut;
	}

	public Timestamp getFecha_ult_acceso() {
		return fecha_ult_acceso;
	}

	public String getClave() {
		return clave;
	}

	public String getClave_enc() {
		return clave_enc;
	}

}
