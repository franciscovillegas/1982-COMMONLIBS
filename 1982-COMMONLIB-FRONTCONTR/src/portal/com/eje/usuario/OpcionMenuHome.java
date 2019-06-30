package portal.com.eje.usuario;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public enum OpcionMenuHome {

	
	//Perfiles
	
	INICIO("index.html","Inicio"),
	AUTOSERVICIO(null,"Autoservicio"),
	MIEQUIPO(null,"Mi Equipo"),
	COMUNIDADPARTICIPA("javascript:;window.open('Tool?htm=participa/index.html');",">Comunidad </br>de participaci&oacute;n"),
	INCIDENCIASSALUDO(/*"javascript:;window.open('Tool?htm=incidencias_de_salud/index.html')"*/null,"Incidencias </br>de Salud"),
	CAMBIOCLAVE(/*"javascript:;window.open('Tool?htm=incidencias_de_salud/index.html')"*/null,"Cambio de clave"),
	DESEMPENO(/*"about:blank"*/null,"Piloto de Desempe&ntilde;o"),	
	
	//Descripciones
	MIESTRUC("miestruc_likeboss","Mi Estructura"),
	PARTICIPA("participa_adm","Participa"),
	TRASLD("prfl_trasld","WF-Traslados"),
	EGRESO("prfl_egreso","WF-Egresos"),	
	FPERSONA("prfl_fpersona","WF-Ficha Personal"),
	AMONESTA("prfl_amonesta","WF-Amonestaciones"),
	GGD("prfl_amonesta","Gestioacute;n del Desempe&ntilde;o");
	
	String contenidoHref,textoEtiqueta;
	
	OpcionMenuHome(String contenidoHref,String textoEtiqueta){
		if (contenidoHref == null)
			contenidoHref="#";
		
		this.contenidoHref = contenidoHref;
		this.textoEtiqueta = textoEtiqueta;
	}
	static Set<OpcionMenuHome> getMenuPorPerfil(List<PerfilUsuario> perfiles) {
		
		Set<OpcionMenuHome> mostrarContenido = new HashSet<OpcionMenuHome>();
		for (PerfilUsuario perfil : perfiles) {
			
			mostrarContenido.addAll(mostrarContenido(perfil.id));
		}
		
		return mostrarContenido;
	}
	
	static Set<OpcionMenuHome> mostrarContenido(int perfil) {
		
		Set<OpcionMenuHome> contenido=  new HashSet<OpcionMenuHome>();
		  
		//contenido.add(INCIDENCIASSALUDO);
        
    	contenido.add(INICIO);
		contenido.add(AUTOSERVICIO);
    	contenido.add(CAMBIOCLAVE);

    	contenido.add(COMUNIDADPARTICIPA);
    	
		switch (perfil) {
         /**** JEFES  ****/
		
        case 1:  //Administrador
        	
        case 2:  //Perfil General Gerencia Felicidad  
        case 3:  //Perfil Gtes Y Sgtes Gerencia Felicidad
		case 5:  //Perfil Administrador
		case 10:  //Perfil Jefatura Gerencia Felicidad
			
        case 14:  //Gerentes
        case 15:  //Perfil Jefe       
        case 16:  //Subgerentes

        	contenido.add(MIEQUIPO); 
        	break;	
        	
        	
        case 6: //Perfil Administrador Estructura 
        	contenido.add(DESEMPENO);  
        	break; 	
        case 12:  //Perfil Jefe De Remuneraciones
        case 7: //Perfil Jefe De Proyecto
        	break;	
        	/*** PERSONAS ***/
       
        	
        
        case 8:  //Perfil Básico Gerencia Felicidad
        	break;	
        case 4:  //Perfil Explotador
        	
       
        
       
        case 9:  //Colaborador
        	break;
        case 11:  //Asistentesgcia felicidad & estructura gyrem
        	break;
        case 13:  //Perfil Zonal
        	break;
       
        case 17:  //Asistente Regional
        	break;
        
		}
		
		return contenido;
	}
	public static Set<OpcionMenuHome> getMenuPorAtributo(Set<String> descSeleccionadas2) {
		
		Set<OpcionMenuHome> mostrarContenido = new HashSet<OpcionMenuHome>();
		
		for (String perfil : descSeleccionadas2) {
			
			if (perfil.equals("miestruc_likeboss")) {
				mostrarContenido.add(OpcionMenuHome.MIESTRUC);
			}
			if (perfil.equals("participa_adm")) {
				mostrarContenido.add(OpcionMenuHome.PARTICIPA);
			}
			if (perfil.equals("prfl_trasld")) {
				mostrarContenido.add(OpcionMenuHome.TRASLD);
			}
			if (perfil.equals("prfl_fpersona")) {
				mostrarContenido.add(OpcionMenuHome.FPERSONA);
			}
			if (perfil.equals("prfl_amonesta")) {
				mostrarContenido.add(OpcionMenuHome.AMONESTA);
			}
			if (perfil.equals("prfl_egreso")) {
				mostrarContenido.add(OpcionMenuHome.EGRESO);
			}
			if (perfil.equals("prfl_ggd")) {
				mostrarContenido.add(OpcionMenuHome.GGD);
			}
			
		}
		
		return mostrarContenido;
	}
}

