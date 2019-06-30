package cl.eje.view.sencha.modulo;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;

import cl.eje.helper.servletmapping.IServletRegistration;
import cl.eje.view.sencha.EjeI;
import cl.eje.view.sencha.EjeS;
import cl.eje.view.servlets.json.ServletJson;
import cl.eje.view.servlets.page.ServletPage;
import cl.eje.view.servlets.page.ServletPagi;
import cl.eje.view.servlets.tableenc.ServletTableEnc;
import cl.ejedigital.tool.strings.MyString;
import portal.com.eje.applistener.InitContext;
import portal.com.eje.frontcontroller.ServletGenerico;
import portal.com.eje.frontcontroller.ServletGenericoInseguro;
import portal.com.eje.frontcontroller.ServletSecure;
import portal.com.eje.frontcontroller.ServletTool;
import portal.com.eje.pdf.ServletTransformPdf;
import portal.com.eje.serhumano.user.S_CambiarClave;
import portal.com.eje.serhumano.user.S_StartSession;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.fileupload.ServletLoadFile;
import portal.com.eje.tools.security.image.PassImageServlet;

/**
 * @author Pancho
 * @since 07-05-2019
 * */
public class MyServletInitializer implements WebApplicationInitializer {
	private Logger logger = Logger.getLogger(getClass());
	
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	createOldServlet(servletContext, "SLoadFile", ServletLoadFile.class);
    	createOldServlet(servletContext, "StartSession", S_StartSession.class);

    	createOldServlet(servletContext, "Tool", ServletTool.class);
    	createOldServlet(servletContext, "Secure", ServletSecure.class);
    	createOldServlet(servletContext, "EjeCore", ServletGenerico.class);
    	createOldServlet(servletContext, "EjeCoreI", ServletGenericoInseguro.class);
    	createOldServlet(servletContext, "EjeS", EjeS.class);
    	createOldServlet(servletContext, "EjeI", EjeI.class);
    	createOldServlet(servletContext, "Table", ServletTableEnc.class);
    	createOldServlet(servletContext, "Page", ServletPage.class);
    	createOldServlet(servletContext, "Pagi", ServletPagi.class);
    	
    	createOldServlet(servletContext, "Json", ServletJson.class);
    	
    	createOldServlet(servletContext, "ServletTransformPdf", ServletTransformPdf.class);
    	
    	createOldServlet(servletContext, "PassImageServlet", PassImageServlet.class);
    	
    	createOldServlet(servletContext, "CambiarClave", S_CambiarClave.class);
    	
    	/*wf*/
    	createIfExist(servletContext, "StartSessionExterno", "portal.com.eje.serhumano.user.S_StartSessionExterno");
    	createIfExist(servletContext, "EndSession", "portal.com.eje.serhumano.user.S_EndSession");
    	createIfExist(servletContext, "ServletActualizaDatosUsuario", "macventase.com.eje.macgenerica.servlets.usuario.ServletActualizaDatosUsuario");
    	createIfExist(servletContext, "ServletCargaMenuTop", "macgenerica.com.eje.macgenerica.servlets.usuario.ServletCargaMenuTop");
    	
    	/*webmatico*/
    	createIfExist(servletContext, "Team", "portal.com.eje.serhumano.certificados.S_Team");
    	createIfExist(servletContext, "S_DatosPrev", "portal.com.eje.serhumano.menu.S_DatosPrev_webmatico");
    	createIfExist(servletContext, "S_DatosContrato", "portal.com.eje.serhumano.menu.S_DatosContrato_webmatico");
    	createIfExist(servletContext, "S_MuestraInfoRut", "portal.com.eje.serhumano.menu.S_MuestraInfoRut_webmatico");
    	createIfExist(servletContext, "S_GrupoFamiliarPortal", "portal.com.eje.serhumano.menu.S_GrupoFamiliar_webmatico");
    	createIfExist(servletContext, "InitCertif", "portal.com.eje.serhumano.certificados.S_Init_Certif_Webmatico");
    	createIfExist(servletContext, "S_Estudios", "portal.com.eje.serhumano.certificados.S_Estudios");
    	createIfExist(servletContext, "S_Vacaciones", "portal.com.eje.serhumano.misdatos.S_Vacaciones_webmatico");
    	createIfExist(servletContext, "S_LicenciasMedicas", "portal.com.eje.serhumano.menu.S_LicenciasMedicas_webmatico");
    	createIfExist(servletContext, "Estructura", "portal.com.eje.serhumano.certificados.S_Estructura");
    	createIfExist(servletContext, "Certificado", "portal.com.eje.serhumano.certificados.S_certificados_webmatico");
    	createIfExist(servletContext, "CumplePortal", "portal.com.eje.serhumano.efemerides.S_Cumple_webmatico");
    	createIfExist(servletContext, "Santoral", "portal.com.eje.serhumano.efemerides.S_Santos_webmatico");
    	
//    	createOldServlet(servletContext, "Team", S_Team.class);
//    	createOldServlet(servletContext, "S_DatosPrev", S_DatosPrev_webmatico.class);
//    	createOldServlet(servletContext, "S_DatosContrato", S_DatosContrato_webmatico.class);
//    	createOldServlet(servletContext, "S_MuestraInfoRut", S_MuestraInfoRut_webmatico.class);
//    	createOldServlet(servletContext, "S_GrupoFamiliarPortal", S_GrupoFamiliar_webmatico.class);
//    	createOldServlet(servletContext, "InitCertif", S_Init_Certif_Webmatico.class);
//    	createOldServlet(servletContext, "S_Estudios", S_Estudios.class);
//    	createOldServlet(servletContext, "S_Vacaciones", S_Vacaciones_webmatico.class);
//    	createOldServlet(servletContext, "S_LicenciasMedicas", S_LicenciasMedicas_webmatico.class);
//    	createOldServlet(servletContext, "Estructura", S_Estructura.class);
//    	createOldServlet(servletContext, "Certificado", S_certificados_webmatico.class);
//    	createOldServlet(servletContext, "CumplePortal", S_Cumple_webmatico.class);
//    	createOldServlet(servletContext, "Santoral", S_Santos_webmatico.class);
    	
    	servletContext.addListener(InitContext.class);

    	//servletContext.addFilter("corrigeUrlMigracionJDK11", (Class<? extends Filter>) FiltroCorrigeJDK11MalUrlCharacter.class).addMappingForUrlPatterns(null, false, "/*");
    }

	@SuppressWarnings("unchecked")
	private void createIfExist(ServletContext servletContext, String servletName , String clase) {
    	Object newInstance = null;
    	
    	try {
    		Class<?>[] def = {};
        	Object[] params = {};
			newInstance =  ClaseGenerica.getInstance().getNew(clase, def, params);
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException
				| InvocationTargetException e) {
			 
		}
    	
    	if(newInstance != null) {
    		createOldServlet(servletContext, servletName, (Class<? extends HttpServlet>) newInstance.getClass());
    	}
    }
    
    private void createOldServlet(ServletContext servletContext, String name, Class<? extends HttpServlet> clase) {
    	
    	try {
	    	if(name == null) {
	    		throw new NullPointerException("\""+name+"\" no puede traers nulls");
	    	}
	    	else if(!name.equals(MyString.getInstance().quitaCEspeciales(name))) {
	    		throw new ServletException("\""+name+"\" no puede traers nulls");
	    	}
	    	
	    	ServletRegistration.Dynamic myServlet = servletContext.addServlet(name, clase);
	    	
	    	if(IServletRegistration.class.isAssignableFrom(clase)) {
	    		IServletRegistration registra = (IServletRegistration) ClaseGenerica.getInstance().getNewFromClass(clase);
	    		registra.setMapping(myServlet);
	    	}
	    	else {
	    		
				
				if(myServlet != null ) {
					myServlet.addMapping(new StringBuilder().append("/servlet/").append(name).append("/*").toString());
			    	myServlet.addMapping(new StringBuilder().append("/servlet/").append(name).append("/").toString());
			    	myServlet.addMapping(new StringBuilder().append("/servlet/").append(name).toString());
			    	myServlet.addMapping(new StringBuilder().append("/").append(name).append("/*").toString());
			        myServlet.addMapping(new StringBuilder().append("/").append(name).append("/").toString());
			        myServlet.addMapping(new StringBuilder().append("/").append(name).toString());	
			        
			        logger.debug("creado " + name);
				}
				else {
			        logger.debug("maybe already exist " + name);
				}
	    	}
			
			
	    	
	        

	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
    	
    	
    }
}