package cl.ejedigital.web.usuario;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.usuario.vo.VoEmpresa;
import cl.ejedigital.web.usuario.vo.VoTrabajador;
import cl.ejedigital.web.usuario.vo.VoUnidad;
import cl.ejedigital.web.usuario.vo.VoUsuario;
import cl.ejedigital.web.usuario.vo.VoUsuarioBloqueo;
import cl.ejedigital.web.usuario.vo.VoUsuarioPerfil;
import cl.ejedigital.web.usuario.vo.VoUsuarioUltAcceso;
import freemarker.template.SimpleHash;



public interface IUsuarioUtils {

	
	public void cambiaPass(IUsuario user, String nuevaClave);
	
	public SimpleHash toHash(IUsuario user);
	
	public boolean updCantIngresosMasUno(IUsuario user);
	
	public boolean updUltAcceso(IUsuario user);
	
	public boolean resetAccesoFallidos(IUsuario user);
	
	public boolean resetAccesoFallidosHaceXMinutos(IUsuario user);
	
	public boolean registraUsuarioCaducado(IUsuario user);
	
	public boolean registraIntentoFallido(IUsuario user);
		
	public VoTrabajador getDatosTrabajador(int user);
	
	public VoTrabajador getDatosTrabajadorExterno(int user);
	
	public VoUsuarioPerfil getPerfilPortal(int rut);
	
	public VoEmpresa getDatosEmpresa(int rut);
	
	public VoUnidad getDatosUnidad(int rut);
	
	public VoUsuario getDatosUsuario(int rut);
	
	public VoUsuarioBloqueo getDatosUsuarioBloqueo(int rut);
	
	public VoUsuarioUltAcceso getDatosUsuarioUltAcceso(int rut);
	
	public ConsultaData getPrivilegios(int rut);
	
}
