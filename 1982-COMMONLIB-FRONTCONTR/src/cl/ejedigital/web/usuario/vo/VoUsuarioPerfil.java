package cl.ejedigital.web.usuario.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class VoUsuarioPerfil implements Serializable , Cloneable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private int	idPerfil;
	private int	idPerfilCreo;
	private String	nombre;
	private boolean	estado;
	private Timestamp fechaCreacion;

	public VoUsuarioPerfil() {
		idPerfil = -1;
		idPerfilCreo = -1;
		nombre = null;
		estado = false;
		fechaCreacion = null;

	}
	
	public VoUsuarioPerfil(int idPerfil, int idPerfilCreo, String nombre, boolean estado, Timestamp fechaCreacion) {
		super();
		this.idPerfil = idPerfil;
		this.idPerfilCreo = idPerfilCreo;
		this.nombre = nombre;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
	}

	public int getIdPerfil() {
		return idPerfil;
	}

	public int getIdPerfilCreo() {
		return idPerfilCreo;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isEstado() {
		return estado;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

}
