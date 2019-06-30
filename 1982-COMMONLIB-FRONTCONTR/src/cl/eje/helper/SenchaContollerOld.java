package cl.eje.helper;

import java.lang.reflect.InvocationTargetException;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Mod;
import portal.com.eje.tools.ClaseGenerica;

class SenchaContollerOld {
	private String paquete;
	private boolean conValidacionDeSession;
	

	public SenchaContollerOld(String paquete, boolean conValidacionDeSession) {
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
		 
		if(thing != null) {
			String pathobjeto = this.paquete+"."+modulo+"."+thing;
			
			{
				 
				try {
					Object objeto = Mod.getInstance(pathobjeto); 
					
					SenchaControllerUtil.getInstance().callAccion(io, objeto, accion);
					
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
		}
		
		return ok;
	}
	
	
	private boolean doThings_EtapaLocal(String thing, String modulo, String accion, IOClaseWeb io) {
		boolean ok = false;
		 
		if(thing != null) {
			String objeto = this.paquete+"."+modulo+"."+thing;
			
			ClaseGenerica cg = new ClaseGenerica();
			{
				Class[] defs = {};
				Object[] args = {};
				try {
					cg.cargaConstructor(objeto, defs, args);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("[Error D001] La clase ["+objeto+"] no existe o no está correctamente definida.",e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException("[Error D002] NoSuchMethodException",e);
				} catch (InstantiationException e) {
					throw new RuntimeException("[Error D003] InstantiationException",e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("[Error D004] IllegalAccessException",e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException("[Error D004] InvocationTargetException",e);
				}
			}
			
			{
				Class[] defs = {IOClaseWeb.class};
				Object[] args = {io};
				try {
					cg.ejecutaMetodo(accion, defs, args);
					ok = true;
				} catch (NullPointerException e) {
					throw new RuntimeException("[Error F001] paso de parmatros no válidos ",e);
				} 
				catch (NoSuchMethodException e) {
					throw new RuntimeException("[Error F002]No está definido el método ["+accion+"] en el objeto ["+objeto+"]",e);
				} 
				catch (IllegalAccessException e) {
					throw new RuntimeException("[Error F003] IllegalAccessException",e);
				} 
				catch (InvocationTargetException e) {
					throw new RuntimeException("[Error F004] IllegalAccessException", e);
				}
			}
		}
		
		return ok;
	}
	 
}
