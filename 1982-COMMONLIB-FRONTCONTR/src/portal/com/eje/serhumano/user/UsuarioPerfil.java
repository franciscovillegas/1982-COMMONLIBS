package portal.com.eje.serhumano.user;

import java.io.Serializable;

public class UsuarioPerfil implements Serializable  {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2699796329676129200L;
	private String	idPerfil;
	private String	idPerfilCreo;
	private String	nombre;
	private boolean	estado;
	private String	fechaCreacion;

	public UsuarioPerfil(String idPerfil, String idPerfilCreo, String nombre, boolean estado, String fechaCreacion) {
		super();
		this.idPerfil = idPerfil;
		this.idPerfilCreo = idPerfilCreo;
		this.nombre = nombre;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
	}

	public String getIdPerfil() {
		return idPerfil;
	}

	public String getIdPerfilCreo() {
		return idPerfilCreo;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isEstado() {
		return estado;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

}
