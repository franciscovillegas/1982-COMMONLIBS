package cl.eje.mail.modulos;


import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.tools.ClaseGenerica;

public class RecuperaClave extends AbsClaseWebInsegura {

	public RecuperaClave(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
	}

	@Override
	public void doGet() throws Exception {
		
		
		try {
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(),"login.credenciales","if_correo_not_valid_then_getcorreo", "false");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(),"login.credenciales","if_correo_not_valid_then_getcellphone", "false");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(),"login.credenciales","metodo_de_recuperacion", "cl.eje.mail.modulos.RecuperacionPorDefault");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(),"conf.mail.administrator","rut"	, "99999990");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(),"conf.mail.administrator","mail"	, "patricio.quinteros@peoplemanager.cl");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(),"conf.mail.administrator","nombre"	, "Patricio Quinteros");
		} catch (PPMException e) {
			e.printStackTrace();
		}
		
		ParametroValue pv = ParametroLocator.getInstance().getValor("login.credenciales", "metodo_de_recuperacion");
		
		ClaseGenerica cg = new ClaseGenerica();
		Object[] values = {};
		Class[] defs = {};
		cg.cargaConstructor(pv.getValue(), defs, values);
		
		IRecuperaClave ir = (IRecuperaClave)cg.getObject();
		ir.doGet(getIoClaseWeb());
	}


}

