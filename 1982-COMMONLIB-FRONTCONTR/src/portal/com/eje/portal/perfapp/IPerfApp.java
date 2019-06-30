package portal.com.eje.portal.perfapp;

import java.util.List;

import portal.com.eje.portal.trabajador.VoPersona;

public interface IPerfApp {
	
	public boolean getPerfAppActivo();
	
	public List<voZona> getZona();
	public List<voZona> getZona(VoPersona persona);
	public boolean updZona(voZona zona);
	
	public List<voAplicacion> getAplicacion(voZona zona);
	public List<voAplicacion> getAplicacion(voZona zona, VoPersona persona);
	public boolean updAplicacion(voAplicacion aplicacion);

	public List<voGrupo> getGrupo(voZona zona);
	public List<voGrupo> getGrupo(voZona zona, VoPersona persona);
	public boolean updGrupo(voGrupo grupo);

	public List<voModulo> getModulo();
	public List<voModulo> getModulo(voAplicacion aplicacion);
	public List<voModulo> getModulo(voAplicacion aplicacion, VoPersona persona);
	public boolean updModulo(voModulo modulo);

	public List<voObjeto> getObjeto();
	public List<voObjeto> getObjeto(voGrupo grupo);
	public List<voObjeto> getObjeto(voGrupo grupo, VoPersona persona);
	public boolean updObjeto(voObjeto objeto);
	
	public List<voPerfil> getPerfil();
	public List<voPerfil> getPerfil(boolean bolConMatriz);
	public voPerfil getPerfilPorDefecto();
	public List<voPerfil> getPerfilObjeto();
	public List<voPerfil> getPerfilesDePersona(VoPersona persona);
	public boolean updPerfil(voPerfil perfil);
	public boolean updPerfil(List<voPerfil> perfiles);
	
	public List<VoPersona> getPersonasDelPerfil(voPerfil perfil);
	public boolean updPersonasDelPerfil(voPerfil perfil, List<VoPersona> personas);
	
	public List<voUserApp> getUserApp();
	public List<voUserApp> getUserApp(voPerfil perfil);
	public List<voUserApp> getUserApp(voPerfil perfil, Boolean bolActivo);
	public List<voUserApp> getUserAppsDePersona(VoPersona persona);
	public boolean updUserAppsDelPerfil(voPerfil perfil, List<voUserApp> userapps);
	
	public List<voAutoservicio> getAutoservicio();
	public List<voAutoservicio> getAutoservicio(voPerfil perfil);
	public boolean updAutoserviciosDelPerfil(voPerfil perfil, List<voAutoservicio> autoservicio);	
	
	public List<voMatriz> getMatriz();
	public List<voMatriz> getMatriz(VoPersona persona);
	
}
