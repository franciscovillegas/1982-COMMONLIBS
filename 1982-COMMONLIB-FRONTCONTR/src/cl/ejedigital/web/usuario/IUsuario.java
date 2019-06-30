package cl.ejedigital.web.usuario;

import java.sql.Connection;
import java.sql.Timestamp;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.usuario.vo.VoEmpresa;
import cl.ejedigital.web.usuario.vo.VoTrabajador;
import cl.ejedigital.web.usuario.vo.VoUnidad;
import cl.ejedigital.web.usuario.vo.VoUsuario;
import cl.ejedigital.web.usuario.vo.VoUsuarioPerfil;


public interface IUsuario {

	
	public void loadData(int rut, String password);
	
	public void refreshData();
	
	public boolean esValido();
	
	/**
	 * Este método indica true cuando el usuario es un usuario bloqueado por no uso
	 * 
	 * */
	public boolean esUsuarioBloqueado();
	
	
	/**
	 * Este método indica true si ha sido bloqueado por no uso
	 * */
	public boolean esUsuarioCaducado();
	
	/**
	 * Este método indica true si dicho trabajador existe en la eje_ges_trabajador
	 * */
	public boolean esUsuarioVigente();
	
	/**
	 * Este método indica true si la clave ingresada coindice
	 * */
	public boolean esClaveCorrecta();
	
	public int getCantIngresos();
	
	public Timestamp getLastIngreso();
	
	public String getPassword();
	
	public VoTrabajador getDatosTrabajador();
	
	public VoEmpresa getDatosEmpresa();
	
	public VoUnidad getDatosUnidad();
	
	public VoUsuarioPerfil getPerfilPortal();
	
	public VoUsuario getDatosUsuario();
	
	public ConsultaData getPrivilegios();
	
	public boolean tieneApp(Object app);
	
}
