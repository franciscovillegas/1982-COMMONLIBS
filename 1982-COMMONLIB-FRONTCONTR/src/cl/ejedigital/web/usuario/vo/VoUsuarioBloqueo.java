package cl.ejedigital.web.usuario.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class VoUsuarioBloqueo implements Serializable , Cloneable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1382653914472851884L;
	private final int		rut;
	private final int		intentos_fallidos;
	private final String	bloqueado;
	private final Timestamp	fecha_bloqueo;
	private final Timestamp	fecha_ultimo_fallo;
	private final String	tipo_bloqueo;

	public VoUsuarioBloqueo(int rut, int intentos_fallidos, String bloqueado, Timestamp fecha_bloqueo,
							Timestamp fecha_ultimo_fallo, String tipo_bloqueo) {
		super();
		this.rut = rut;
		this.intentos_fallidos = intentos_fallidos;
		this.bloqueado = bloqueado;
		this.fecha_bloqueo = fecha_bloqueo;
		this.fecha_ultimo_fallo = fecha_ultimo_fallo;
		this.tipo_bloqueo = tipo_bloqueo;
	}

	public int getRut() {
		return rut;
	}

	public int getIntentos_fallidos() {
		return intentos_fallidos;
	}

	public String getBloqueado() {
		return bloqueado;
	}

	public Timestamp getFecha_bloqueo() {
		return fecha_bloqueo;
	}

	public Timestamp getFecha_ultimo_fallo() {
		return fecha_ultimo_fallo;
	}

	public String getTipo_bloqueo() {
		return tipo_bloqueo;
	}

}
