package portal.com.eje.frontcontroller.util;

import java.lang.reflect.Method;

import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.iface.AbsServicioCaller;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.frontcontroller.iface.IServicioListener;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.VoTool;

/**
 * llama a AbsClaseWeb y AbsClaseWebInsegura
 * 
 * @author Pancho
 * @since 11-03-2019
 */
public class CallerServletGenerico extends AbsServicioCaller {

	public static CallerServletGenerico getInstance() {
		return Util.getInstance(CallerServletGenerico.class);
	}

	/**
	 * Objeto siempre será null
	 * 
	 * @author Pancho
	 * @throws Throwable 
	 */
	public boolean doCall(IOClaseWeb io, IServicioGetter servicioGetter, String tipo, IServicioListener servicioListener, Cronometro cro) throws Exception {
		Object objeto = servicioGetter.getServicio(io);

		boolean retorno = false;
		if (io == null) {
			retNotFound(io, objeto, tipo, "io is null", servicioListener);
		} else if (objeto == null) {
			retNotFound(io, objeto, tipo, "AZoneUtil is null", servicioListener);
		} else if (tipo == null) {
			retNotFound(io, objeto, tipo, "method is null", servicioListener);
		} else {

			printParams(io, objeto);

			Method metodo = null;

			if (objeto instanceof AbsClaseWeb || objeto instanceof AbsClaseWebInsegura) {
				Class<?>[] defs = {};
				Object[] params = {};

				metodo = VoTool.getInstance().getMethodOrSuperMethod(objeto.getClass(), tipo, defs);
				
				if(metodo != null) {
					if (isTransactional(metodo)) {
						io.getTransactionConnection().setRestringeCommit(true);
					}

					try {
						VoTool.getInstance().getReturnFromMethodThrowErrors(objeto, metodo, params);
						
						if(servicioListener != null) {
							servicioListener.onShow(objeto, metodo, params, cro);
						}
					} catch (Throwable e) {
						e.printStackTrace();
						returnErrorSencha(io, metodo, e);
					}
					finally {
						
					}
				}
				
			} else {
				retNotFound(io, objeto, tipo, "Wrong Object is nos AZoneUtil " + objeto.getClass().getCanonicalName(), servicioListener);
			}

			retorno = io.getReturnedSuccess();

			printEndTime(io, objeto, metodo);

		}

		return retorno;
	}

}