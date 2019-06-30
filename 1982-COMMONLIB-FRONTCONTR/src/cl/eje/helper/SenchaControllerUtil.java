package cl.eje.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.util.DoExecAZonaUtil;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.parametro.ModuloNotRecognizedException;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.errors.MethodNotFoundException;

/**
 * Se cambia por DoExecAZonaUtil
 * 
 * @deprecated
 * @see DoExecAZonaUtil
 * */

class SenchaControllerUtil {
	private String msgPrintTime = "@@class on @@time";
	private String replaceClass = "@@class";
	private String replaceTime = "@@time";
	private String punto = ".";
	
	public static SenchaControllerUtil getInstance() {
		return Util.getInstance(SenchaControllerUtil.class);
	}
	
	public boolean callAccion(IOClaseWeb io, Object objeto,String accion) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception {
		
		
		if(objeto != null) {
			Logger logger = Logger.getLogger(objeto.getClass());
			logger.debug(io.getUtilParam().getParamsAsString(io));
			
			Method metodo = null;
			Object[] args = null;
			if(AZoneUtil.class.isAssignableFrom(objeto.getClass())) {
				
				Class<?>[] defs = {IOClaseWeb.class};
				args = new Object[]{io};
				
				metodo= VoTool.getInstance().getMethod(objeto.getClass(), accion, defs);
				
				if ( isTransactional(metodo) ) {
					io.getTransactionConnection().setRestringeCommit(true);
				}
				
				 
			}
//			else if(AZoneUtilTransaction.class.isAssignableFrom(objeto.getClass())) {
//				
//				Class<?>[] defs = {IOClaseWeb.class, TransactionConnection.class};
//				args = new Object[]{io, io.getTransactionConnection()};
//				
//				io.getTransactionConnection().setRestringeCommit(true);
//				
//				metodo= VoTool.getInstance().getMethod(objeto.getClass(), accion, defs);
//				
//			}
			else {
				Class<?>[] defs = {IOClaseWeb.class};
				args = new Object[]{io};
				
				metodo= VoTool.getInstance().getMethod(objeto.getClass(), accion, defs);
				
				if ( isTransactional(metodo) ) {
					io.getTransactionConnection().setRestringeCommit(true);
				}
			
			}
			
			if(metodo == null) {
				throw new MethodNotFoundException("No está definido ["+accion+"] en Modulo ["+objeto.getClass().getCanonicalName()+"]");
			}
			
			VoTool.getInstance().getReturnFromMethod(objeto, metodo, args);
			
			logger.debug(msgPrintTime
								.replaceAll(this.replaceClass, new StringBuffer()
																.append(objeto.getClass().getCanonicalName())
																.append(punto)
																.append(metodo.getName()).toString())
								.replaceAll(replaceTime, io.getCronometro().getTimeHHMMSS_milli()) );
		}
		else {
			throw new ModuloNotRecognizedException("param modulo ["+io.getParamString("modulo", "null")+"]");
		}
	
		boolean ok = io.getReturnedSuccess();
		return ok;
	}
	
	 
	
	public boolean isTransactional(Method metodo) {
		if(metodo != null) {
			Annotation ano = metodo.getAnnotation(Transactional.class);
			if(ano instanceof Transactional) {
				return true;
			}
		}
		
		return false;
	}
}
