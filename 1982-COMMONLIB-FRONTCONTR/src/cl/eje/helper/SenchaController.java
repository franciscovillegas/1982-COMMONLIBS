package cl.eje.helper;

import java.lang.reflect.InvocationTargetException;

import cl.eje.view.sencha.Version;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.EnumTool;

public class SenchaController {

	private String paquete;
	private boolean conValidacionDeSession;
	

	public SenchaController(String paquete, boolean conValidacionDeSession) {
		super();
		this.paquete = paquete;
		this.conValidacionDeSession = conValidacionDeSession;
	}
	
	public boolean doThings(SenchaTipoPeticion tipo, IOClaseWeb io) {
		
		io.getResp().setHeader("Expires", "0");
		io.getResp().setHeader("Pragma", "no-cache");
		io.getResp().setHeader("Access-Control-Allow-Origin", "*");
		io.getResp().setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		
		String modulo = io.getParamString("modulo", null);
		String thing = io.getParamString("thing", null);
		String accion = io.getParamString("accion", null);
		
		String retMsg = null;
		boolean ok = false;
		
		try {
			if(modulo == null) {
				String msg = "[Error A001] el nombre del parámetro \"módulo\" no puede ser null (modulo!=null) ";
				io.retTexto(msg);
				throw new RuntimeException(msg);
			}
			else if(thing == null) {
				String msg = "[Error A002] el nombre del parámetro \"thing\" no puede ser null (thing!=null) ";
				io.retTexto(msg);
				throw new RuntimeException(msg);
			}
			else if(accion == null) {
				String msg = "[Error A003] el nombre del parámetro \"accion\" no puede ser null (accion!=null) ";
				io.retTexto(msg);
				throw new RuntimeException(msg);
			}
			else {
				ok = doThings_EtapaRemota(thing, modulo, accion, io);
			}
		}
		catch(RuntimeException e) {
			e.printStackTrace();
		}
		
	
		return ok;
	}	
	
	private boolean doThings_EtapaRemota(String thing, String modulo, String accion, IOClaseWeb io) {
		boolean ok = false;
		
		String version = io.getParamString("veje", "").toUpperCase();
		Version v = EnumTool.getEnum(Version.class, version, Version.PORDEFECTO);
		
		if(thing != null) {
			String pathobjeto = new StringBuilder().append(v.getPaquete()).append(".").append(modulo).append(".").append(thing).toString();
 
			
			
			try {
				
				Object objeto = ClaseGenerica.getInstance().getNew(pathobjeto);
				
				SenchaControllerUtil.getInstance().callAccion(io, objeto, accion);
				
				ok = true;
				 
			} catch (NullPointerException e) {
				System.out.println("@@@@ [Error C001]No se ha definido ningún método ["+accion+"] en el objeto ["+pathobjeto+"]");
			} 
			catch (NoSuchMethodException e) {
				System.out.println("@@@@ [Error C002]No está definido el método ["+accion+"] en el objeto ["+pathobjeto+"]");
			} 
			catch (IllegalAccessException e) {
				throw new RuntimeException("[Error C003] IllegalAccessException",e);
			} 
			catch (InvocationTargetException e) {
				throw new RuntimeException("[Error C004] IllegalAccessException", e);
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		}
		
		return ok;
	}
	


	 
}
