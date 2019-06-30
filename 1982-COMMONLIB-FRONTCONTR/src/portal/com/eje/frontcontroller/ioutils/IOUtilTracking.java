package portal.com.eje.frontcontroller.ioutils;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.serhumano.tracking.trackingManager;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

public class IOUtilTracking extends IOUtil {
 
	public static IOUtilTracking getIntance() {
		return Weak.getInstance(IOUtilTracking.class);
	}
	
	private void insTracking(HttpServletRequest req, String descripcion, String datos) {
        trackingManager tMgr = new trackingManager();
        
        try {
        	Usuario user = SessionMgr.rescatarUsuario(req);
            tMgr.insertTracking(user.getRutId(), user.getEmpresa(), descripcion, datos, req);
        }
        catch(Exception e) { 
        	
        }
    }
    
    /**
     * Método utilizado solo para aquellas zonas en donde se utilizan FrontController, ya que originalmente dentro del mismo método <b>insTracking</b> se
     * obtenía el servlet que se llamaba, ahora opcionalmente se puede ingresar.
     * 
     * @param direcRel
     * @param req
     * @param descripcion
     * @param datos
     */
    
	private void insTracking(String direcRel, HttpServletRequest req, String descripcion, String datos) {
       
        
        try {
        	Usuario user = SessionMgr.rescatarUsuario(req);
        	insTracking(user.getRutIdInt(), Validar.getInstance().validarInt(user.getEmpresa(), -1), direcRel, req, descripcion, datos);
        }
        catch(Exception e) { 
        	
        }
    }
    
    /**
     * Método utilizado solo para aquellas zonas en donde se utilizan FrontController, ya que originalmente dentro del mismo método <b>insTracking</b> se
     * obtenía el servlet que se llamaba, ahora opcionalmente se puede ingresar.
     * 
     * @param direcRel
     * @param req
     * @param descripcion
     * @param datos
     * @since 2015-09-02
     */
    private void insTracking(int rut, int empresa, String direcRel, HttpServletRequest req, String descripcion, String datos) {
        trackingManager tMgr = new trackingManager();
        
        try {
            tMgr.insertTracking(direcRel,String.valueOf(rut), String.valueOf(empresa), descripcion, datos, req);
        }
        catch(Exception e) { 
        	
        }
    }
	/**
	 * Bajo la nueva filosofía de contrucción: modulo-thing-accion, deberá ocuparse le método insTrackingModulo
	 * @see insTrackingModulo(Class clazz, String accion, String descripcion, String datos)
	 * @deprecated
	 * */
	public void insTracking(IIOClaseWebLight io, String descripcion, String datos) {
		insTracking(io.getReq(), descripcion, datos);
	}
	
	/**
	 * Bajo la nueva filosofía de contrucción: modulo-thing-accion, deberá ocuparse le método insTrackingModulo
	 * @see insTrackingModulo(Class clazz, String accion, String descripcion, String datos)
	 * @deprecated
	 * */
	public void insTracking(IIOClaseWebLight io,  String direcRel, String descripcion, String datos) {
		insTracking(direcRel, io.getReq(), descripcion, datos);
	}
	
	/*
	 * Genera un registro de tracking cuando se llama al módulod e forma insegura
	 * */
	public void insTrackingModulo_Inseguro(IIOClaseWebLight io, int idUsuario, Class clazz, String accion, String descripcion, String datos) {
		String[] object = clazz.toString().split("\\.");
		StringBuilder str = new StringBuilder();
		if(accion == null) {
			accion = "";
		}
		if(object.length >= 2) {
			str.append(object[object.length-2]).append(".").append(object[object.length-1]).append(".").append(accion.toLowerCase());
		}
		
		insTracking(idUsuario, -1,
								  str.toString(),
								  io.getReq(),
								  descripcion, 
								  datos);
	}
}
