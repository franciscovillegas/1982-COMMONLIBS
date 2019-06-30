package cl.ejedigital.web.usuario;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.usuario.vo.VoEmpresa;
import cl.ejedigital.web.usuario.vo.VoTrabajador;
import cl.ejedigital.web.usuario.vo.VoUnidad;
import cl.ejedigital.web.usuario.vo.VoUsuario;
import cl.ejedigital.web.usuario.vo.VoUsuarioPerfil;


public class UsuarioBase implements IUsuario, Serializable {
	

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public UsuarioBase() {} 
	
	
	public void loadData(int rut, String password) {
		
	}

	public boolean esValido() {
		return true;
	}

	public int getCantIngresos() {
		return 0;
	}

	public Timestamp getLastIngreso() {
		return null;
	}

	public String getPassword() {
		return null;
	}

	public VoTrabajador getDatosTrabajador() {
		return null;
	}

	public VoUsuarioPerfil getPerfilPortal() {
		return new VoUsuarioPerfil(0,0,null,true,null);
	}

	public ConsultaData getPrivilegios() {
		return null;
	}

	public boolean tieneApp(Object app) {
		return true;
	}


	public VoEmpresa getDatosEmpresa() {
		return new VoEmpresa(null,null,null,null,null,null,null,null,null,null,0,null);
	}


	public VoUnidad getDatosUnidad() {
		return new VoUnidad(null,null,null,null,' ');
	}


	public boolean esUsuarioBloqueado() {
		return false;
	}


	public boolean esUsuarioCaducado() {
		return false;
	}


	public boolean esUsuarioVigente() {
		return false;
	}


	public boolean esClaveCorrecta() {
		// TODO Auto-generated method stub
		return false;
	}


	public VoUsuario getDatosUsuario() {
		// TODO Auto-generated method stub
		return null;
	}


	public void refreshData() {
		// TODO Auto-generated method stub
		
	}
	
	

}
