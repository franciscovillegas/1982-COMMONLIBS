package portal.com.eje.frontcontroller.iface;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import cl.eje.helper.AZonePage;
import cl.eje.helper.AZoneUtil;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.vo.errors.PeopleExcepcion;

public abstract class AbsServicio {
	protected String msgPrintTime = "@@class on @@time";
	protected String replaceClass = "@@class";
	protected String replaceTime = "@@time";
	protected String punto = ".";
	protected String BLANCO = "";
	protected String GET = "GET";
	protected String POST = "POST";

	protected boolean isTransactional(Method metodo) {
		if (metodo != null) {
			Annotation ano = metodo.getAnnotation(Transactional.class);
			if (ano instanceof Transactional) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getPostMappingAnnotation(Method metodo) {

		if (metodo != null) {
			Annotation ano = metodo.getAnnotation(PostMapping.class);
			if (ano != null && ano instanceof PostMapping) {
				return ((T) ano);
			}
		}

		return null;

	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getGetMappingAnnotation(Method metodo) {

		if (metodo != null) {
			Annotation ano = metodo.getAnnotation(GetMapping.class);
			if (ano != null && ano instanceof GetMapping) {
				return ((T) ano);
			}
		}

		return null;

	}
	
	protected boolean canExecMethodo(HttpServletRequest req, Method metodo) {
		boolean isPost = POST.equals(req.getMethod());
		boolean isGet = GET.equals(req.getMethod());
		boolean postPermitido = getPostMappingAnnotation(metodo) != null;
		boolean getPermitido = getGetMappingAnnotation(metodo) != null;
		boolean puede = false;
		if(postPermitido && !getPermitido || !postPermitido && getPermitido || postPermitido && getPermitido) {
			if(isGet && getPermitido) {
				puede = true;
			}
			else if(isPost && postPermitido) {
				puede = true;
			}
		}
		else if(!postPermitido && !getPermitido) {
			puede = true;
		}
		
		return puede;
	}

	protected void printParams(IOClaseWeb io, Object objeto) {
		Logger logger = Logger.getLogger(objeto.getClass());
		logger.debug(io.getUtilParam().getParamsAsString(io));
	}

	protected void printEndTime(IOClaseWeb io, Object objeto, Method metodo) {
		Logger logger = Logger.getLogger(objeto.getClass());

		logger.debug(msgPrintTime.replaceAll(this.replaceClass, new StringBuffer().append(objeto.getClass().getCanonicalName()).append(punto).append(metodo.getName()).toString()).replaceAll(replaceTime,
				io.getCronometro().getTimeHHMMSS_milli()));
	}

	protected void retNotFound(IOClaseWeb io, Object object, String methodName, String msgRespuesta, IServicioListener servicioListener) throws IOException {
		io.getResp().sendError(HttpServletResponse.SC_NOT_FOUND, msgRespuesta);

		if (servicioListener != null) {
			servicioListener.onNotFound(object, methodName, msgRespuesta);
		}
	}
	
	/**
	 * Discrimina si es AZoneUtil o AZonePage, para retornar una error page o un json con error
	 * @author Pancho
	 * @since 16-05-2019
	 * */
	protected void returnError(IOClaseWeb io, Object objeto, Method metodo, Throwable t, int httpErrorTipe ) {
		if(isPage(objeto)) {
			returnErrorPage(io, objeto, t, httpErrorTipe);
		}
		else {
			returnErrorSencha(io, metodo, t);
		}
	}
	
	protected void returnErrorSencha(IOClaseWeb io, Method metodo, Throwable t)  {
		io.retSenchaJson(t);
	}
	
	protected void returnErrorPage(IOClaseWeb io, Object objeto, Throwable t, int httpErrorTipe) {
		HttpServletResponse resp = io.getResp();
		try {
			resp.sendError(httpErrorTipe, t.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void doExecDice(AZoneUtil zoneUtil, String dice, Throwable e) {
		boolean esPeople = (e instanceof PPMException || e instanceof PeopleExcepcion);
		if( !esPeople) {
			StringBuilder str = new StringBuilder();
			if(dice == null || "".equals(dice)) {
				dice = e.toString();
			}
			
			str.append("(").append(this.getClass().getCanonicalName()).append(")  dice: ").append(dice).append("\n");
			System.out.println(str);
			
			Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Verifica si el objeto es una página, hay dos tipos de objetos genericos AzoneUtil y AZonePage,
	 * Uno devuelve JSON y el otro una página, la gran variante está en el error que retornan.
	 * 
	 * @author Pancho
	 * @since
	 * */
	protected boolean isPage(Object objeto) {
		boolean isPage = objeto != null && AZonePage.class.isAssignableFrom(objeto.getClass());
		return isPage;
	}
	 
}
