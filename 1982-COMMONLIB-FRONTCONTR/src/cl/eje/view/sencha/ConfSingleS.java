package cl.eje.view.sencha;

import cl.eje.helper.SenchaController2018;
import cl.eje.helper.SenchaTipoPeticion;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;
import portal.com.eje.tools.EnumTool;


public class ConfSingleS extends AbsClaseWeb {
	private final String strmodulo = "modulo";
	private final String blanco = "";
	private final String guion = "_";
	
	public ConfSingleS(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doGet()  {
		
		String modulo = super.getIoClaseWeb().getParamString(strmodulo, blanco).toUpperCase();
		String version = modulo.split(guion)[0];
		
		Version v = EnumTool.getEnum(Version.class, version, Version.PORDEFECTO);
		SenchaController2018.getInstance().doThings(v.getPaquete(),SenchaTipoPeticion.doGet, super.getIoClaseWeb());
	}

	@Override
	public void doPost() throws Exception {
		String modulo = super.getIoClaseWeb().getParamString(strmodulo, blanco).toUpperCase();
		String version = modulo.split(guion)[0];
		
		Version v = EnumTool.getEnum(Version.class, version, Version.PORDEFECTO);
		
		SenchaController2018.getInstance().doThings(v.getPaquete(),SenchaTipoPeticion.doPost, super.getIoClaseWeb());
	}
	
}
