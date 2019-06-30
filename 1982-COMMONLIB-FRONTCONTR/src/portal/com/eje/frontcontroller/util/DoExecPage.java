package portal.com.eje.frontcontroller.util;

import java.io.IOException;
 
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.AZonePage;
import cl.eje.helper.AZoneUtil;
import cl.eje.helper.EnumAccion;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.error.ServiceNotExistException;
import portal.com.eje.frontcontroller.iface.AbsServicio;
import portal.com.eje.frontcontroller.iface.IServicioListener;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.transactions.error.ConnectionsAlreadyStartedException;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.tools.EnumTool;
import portal.com.eje.tools.deprecates.AssertWarn;

public class DoExecPage extends AbsServicio {

	public static DoExecPage getInstance() {
		return Util.getInstance(DoExecPage.class);
	}
	
	public boolean doExec(IOClaseWeb io, String pathobjeto, String tipo) {
		
		AZoneUtil objeto = (AZoneUtil) Util.getInstance(pathobjeto);
		
		return doExec(io, (AZonePage) objeto, tipo, null);
	}

	public boolean doExec(IOClaseWeb io, AZoneUtil objeto, String tipo, IServicioListener servicioListener) {
		boolean retorno = false;
		
		AssertWarn.isTrue(getClass(),
						tipo != null
						 && EnumTool.getEnumByName(EnumAccion.class, tipo, null) != null,
						 "param accion debe ser estandar, y no lo es, el metodo solo puede ser uno de los enums de EnumAccion.class y actualmente es \""+tipo+"\"");
		Method metodo = null;
		
		try {
			if(objeto == null) {
				throw new ServiceNotExistException("No existe AZoneUtil null");
			}
			if (objeto != null && AZoneUtil.class.isAssignableFrom(objeto.getClass())) {
				if (io == null && io == null) {
						throw new ServiceNotExistException("io es null");

				} else if (tipo == null && tipo == null) {
						throw new ServiceNotExistException("accion is null");

				} else if (objeto != null) {
					printParams(io, objeto);

				

					Class<?>[] defs = { IOClaseWeb.class };
					Object[] params = { io };

					metodo = VoTool.getInstance().getMethodOrSuperMethod(objeto.getClass(), tipo, defs);
					
					if(metodo == null) {
						throw new NoSuchMethodException(tipo+"no existe");
					}

					boolean puedeExecMethodo = canExecMethodo(io.getReq(), metodo);
					if(!puedeExecMethodo) {
						throw new IllegalAccessError("Metodo (doPost or doGet) no permitido");
					}
					
					if (isTransactional(metodo)) {
						io.getTransactionConnection().setRestringeCommit(true);
					}

					VoTool.getInstance().getReturnFromMethodThrowErrors(objeto, metodo, params);
					if (servicioListener != null) {
						servicioListener.onShow(objeto, metodo, params, io.getCronometro());
					}

					retorno = io.getReturnedSuccess();

					printEndTime(io, objeto, metodo);

				}
			}
		} 
		catch(ServiceNotExistException e) {
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (IOException e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		} catch (ConnectionsAlreadyStartedException e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		} catch (IllegalArgumentException e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		} catch (IllegalAccessException e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		} catch (NullPointerException e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		} catch (NoSuchMethodException e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		}  catch (Exception e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		} catch (Throwable e) {
			 
			returnErrorPage(io, objeto, e, HttpServletResponse.SC_BAD_REQUEST);
			
		} finally {

		}

		return retorno;

	}
 
	
	
}
