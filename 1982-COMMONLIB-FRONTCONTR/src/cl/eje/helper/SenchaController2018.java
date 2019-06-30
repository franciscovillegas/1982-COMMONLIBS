package cl.eje.helper;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.util.DoExecAZonaUtil;
import portal.com.eje.frontcontroller.util.HeaderTool;
import portal.com.eje.portal.factory.Util;

public class SenchaController2018 {

	public static SenchaController2018 getInstance() {
		return Util.getInstance(SenchaController2018.class);
	}

	public boolean doThings(String paquete, SenchaTipoPeticion tipo, IOClaseWeb io) {

		HeaderTool.putNoCacheAndAllConnectOrigin(io.getResp());

		String modulo = io.getParamString("modulo", null);
		String thing = io.getParamString("thing", null);
		String accion = io.getParamString("accion", null);

		boolean ok = false;

		try {
			if (modulo == null) {
				String msg = "[Error A001] el nombre del parámetro \"módulo\" no puede ser null (modulo!=null) ";
				io.retSenchaJson(msg);
				// throw new RuntimeException(msg);
			} else if (thing == null) {
				String msg = "[Error A002] el nombre del parámetro \"thing\" no puede ser null (thing!=null) ";
				io.retTexto(msg);
				// throw new RuntimeException(msg);
			} else if (accion == null) {
				String msg = "[Error A003] el nombre del parámetro \"accion\" no puede ser null (accion!=null) ";
				io.retTexto(msg);
				// throw new RuntimeException(msg);
			} else {
				ok = doThings_EtapaRemota(paquete, thing, modulo, accion, io);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		return ok;
	}

	private boolean doThings_EtapaRemota(String paquete, String thing, String modulo, String accion, IOClaseWeb io) {
		String pathobjeto = new StringBuilder().append(paquete)
				.append(".").append(modulo).append(".").append(thing).toString();
		
		boolean ok = DoExecAZonaUtil.getInstance().doExec(io, pathobjeto, accion);

		return ok;
	}

}
